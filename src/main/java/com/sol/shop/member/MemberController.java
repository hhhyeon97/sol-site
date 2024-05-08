package com.sol.shop.member;

import com.sol.shop.comment.Comment;
import com.sol.shop.comment.CommentRepository;
import com.sol.shop.sales.Sales;
import com.sol.shop.sales.SalesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

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
            memberRepository.save(member);
            return "redirect:/";

        } catch (DataIntegrityViolationException e) {
            // 중복된 아이디 예외 처리
            model.addAttribute("errorMessage", "이미 사용 중인 아이디입니다. 다른 아이디를 선택해주세요.");
            return "join.html";
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