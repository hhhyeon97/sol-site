package com.sol.shop.member;

import com.sol.shop.comment.Comment;
import com.sol.shop.comment.CommentRepository;
import com.sol.shop.sales.Sales;
import com.sol.shop.sales.SalesRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final SalesRepository salesRepository;
    private final CommentRepository commentRepository;

    @GetMapping("/join")
    String join(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            // 로그인한 사용자인 경우
            return "redirect:/";
        } else {
            // 로그인하지 않은 사용자인 경우
            return "join.html";
        }
    }

    @PostMapping("/member")
    String addMember(@RequestParam String username, String password, String displayName, Model model){

        try {
            Member member = new Member();
            member.setUsername(username);
            var hash = passwordEncoder.encode(password);
            member.setPassword(hash);
            member.setDisplayName(displayName);
            member.setRole("ROLE_USER");
            memberRepository.save(member);
            return "redirect:/login";

        } catch (DataIntegrityViolationException e) {
            // 중복된 아이디 예외 처리
            model.addAttribute("errorMessage", "이미 사용 중인 아이디입니다. 다른 아이디를 선택해주세요.");
            return "join.html";
        }

    }
    @GetMapping("/checkUsername")
    @ResponseBody
    public ResponseEntity<String> checkUsername(@RequestParam String username) {
        // 아이디가 이미 존재하는지 확인하는 로직
        boolean isUsernameExists = memberRepository.existsByUsername(username);

        if (isUsernameExists) {
            // 이미 존재하는 아이디인 경우
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        } else {
            // 존재하지 않는 아이디인 경우
            return ResponseEntity.ok("Username is available");
        }
    }


    @GetMapping("/login")
    public String login(Authentication auth){
        if (auth != null && auth.isAuthenticated()) {
            return "redirect:/";
        } else {
            return "login.html";
        }
    }

    @GetMapping("/my-page")
    public String myPage(Model model, Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            CustomUser user = (CustomUser) auth.getPrincipal();
            Member member = memberRepository.findByUsername(user.getUsername()).orElse(null);
            if (member != null) {
                model.addAttribute("username", member.getUsername());
                model.addAttribute("displayName", member.getDisplayName());
//                // 주문 내역 가져오기
                List<Sales> sales = salesRepository.findByMemberId(member.getId());
                model.addAttribute("sales", sales);
                // 작성한 리뷰 가져오기
                List<Comment> reviews = commentRepository.findByUsername(member.getUsername());
                model.addAttribute("reviews", reviews);
            }
            return "mypage.html";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/user/1")
    @ResponseBody
    public MemberDto getUser(){
        var a = memberRepository.findById(1L);
        var result = a.get();
        var data = new MemberDto(result.getUsername(), result.getDisplayName(), result.getId());
        return data;
    }

    @PostMapping("/withdraw")
    @ResponseBody
    public String withdrawUser(@RequestParam String username, Authentication authentication) {
        // 현재 로그인한 사용자 권환 확인하기
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            // 사용자 탈퇴
            System.out.println("권한 : "+authentication.getAuthorities());
            Optional<Member> memberOptional = memberRepository.findByUsername(username);
            System.out.println("탈퇴시키려는유저 : "+username);
            if (memberOptional.isPresent()) {
                memberRepository.delete(memberOptional.get());
                return "회원 탈퇴가 완료되었습니다.";
            } else {
                return "해당 사용자를 찾을 수 없습니다.";
            }
        } else {
            return "권한이 없습니다.";
        }
    }


    @PostMapping("/update-nickname")
    public String updateNickname(@RequestParam String displayName,Authentication authentication, RedirectAttributes redirectAttributes){
        String username = authentication.getName();
        Optional<Member> optionalMember = memberRepository.findByUsername(username);

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            member.setDisplayName(displayName);
            memberRepository.save(member);
            redirectAttributes.addFlashAttribute("message", "닉네임이 업데이트 되었습니다!");
        }
        else {
            redirectAttributes.addFlashAttribute("message", "닉네임 수정에 실패하였습니다.");
        }
        return "redirect:/my-page";
    }

    @PostMapping("/update-password")
    public String updatePassword(@RequestParam String password,Authentication authentication,
                                 RedirectAttributes redirectAttributes, HttpServletRequest request){
        String username = authentication.getName();
        Optional<Member> optionalMember = memberRepository.findByUsername(username);

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            member.setPassword(passwordEncoder.encode(password));
            memberRepository.save(member);
            // 로그아웃 처리
            LogoutHandler logoutHandler = new SecurityContextLogoutHandler();
            logoutHandler.logout(request, null, SecurityContextHolder.getContext().getAuthentication());

            redirectAttributes.addFlashAttribute("notification", "패스워드가 업데이트 되었습니다! 다시 로그인 해주세요");

            return "redirect:/login";
        }
        else {
            redirectAttributes.addFlashAttribute("message", "패스워드 재설정에 실패하였습니다.");
        }
        return "redirect:/my-page";
    }

    @GetMapping("/get-username")
    public String getUsername(Authentication authentication) {
        String username = authentication.getName();
        Optional<Member> optionalMember = memberRepository.findByUsername(username);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            return member.getDisplayName();
        }
        return "";
    }



}

class MemberDto {
    public String username;
    public String displayName;
    public Long id;
    MemberDto(String a, String b) {
        this.username = a;
        this.displayName = b;
    }
    MemberDto(String a, String b, Long c){
        this.username = a;
        this.displayName = b;
        this.id = c;
    }
}