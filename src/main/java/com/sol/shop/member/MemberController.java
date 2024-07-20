package com.sol.shop.member;

import com.sol.shop.comment.Comment;
import com.sol.shop.comment.CommentRepository;
import com.sol.shop.sales.Sales;
import com.sol.shop.sales.SalesRepository;
import com.sol.shop.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final SalesRepository salesRepository;
    private final CommentRepository commentRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @GetMapping("/join")
    String join(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/";
        } else {
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


//    @GetMapping("/login")
//    public String login(Authentication auth){
//        if (auth != null && auth.isAuthenticated()) {
//            return "redirect:/";
//        } else {
//            return "login.html";
//        }
//    }

    // jwt-login-test
    @GetMapping("/login")
    public String login(){
        return "login.html";
    }

    @GetMapping("/my-page")
    public String myPage(Model model, Authentication auth, RedirectAttributes redirectAttributes) {
        if (auth != null && auth.isAuthenticated()) {
            CustomUser user = (CustomUser) auth.getPrincipal();
            Member member = memberRepository.findByUsername(user.getUsername()).orElse(null);
            if (member != null) {
                model.addAttribute("username", member.getUsername());
                model.addAttribute("displayName", member.getDisplayName());
                // 주문 내역 가져오기
                List<Sales> sales = salesRepository.findByMemberId(member.getId());
                model.addAttribute("sales", sales);
                // 작성한 리뷰 가져오기
                List<Comment> reviews = commentRepository.findByUsername(member.getUsername());
                model.addAttribute("reviews", reviews);
            }
            return "mypage.html";
        } else {
            redirectAttributes.addFlashAttribute("MyPageMessage", "로그인 후 이용가능합니다 !");
            return "redirect:/";
        }
    }


    @PostMapping("/update-nickname")
    public String updateNickname(@RequestParam String displayName, Authentication authentication, HttpSession session, RedirectAttributes redirectAttributes){
        String username = authentication.getName();
        Optional<Member> optionalMember = memberRepository.findByUsername(username);

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            member.setDisplayName(displayName);
            memberRepository.save(member);
            // 세션에 사용자의 닉네임 업데이트
            session.setAttribute("displayName", displayName);
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

    @PostMapping("/login/jwt")
    @ResponseBody
    public String loginJWT(@RequestBody Map<String, String> data, HttpServletResponse response) {
        var authToken = new UsernamePasswordAuthenticationToken(data.get("username"), data.get("password"));
        var auth = authenticationManagerBuilder.getObject().authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);

        var jwt = JwtUtil.createToken(SecurityContextHolder.getContext().getAuthentication());

//        System.out.println("jwt : " + jwt);

        var cookie = new Cookie("jwt",jwt);
        cookie.setMaxAge(10);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);

        return jwt;
    }

    @GetMapping("/mypage/jwt")
    @ResponseBody
    String mypageJWT(HttpServletRequest request){


        return "마이페이지데이터";
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