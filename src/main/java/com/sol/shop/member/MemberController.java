package com.sol.shop.member;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

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
            return "redirect:/list";

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
    public String myPage(Authentication auth){
        if(auth != null && auth.isAuthenticated()){
            return "mypage.html";
        }else {
            return "redirect:/";
        }
//        CustomUser result = (CustomUser)auth.getPrincipal();
//        System.out.println(result.displayName);

    }



}
