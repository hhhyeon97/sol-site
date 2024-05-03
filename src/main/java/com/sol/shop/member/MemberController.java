package com.sol.shop.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/join")
    String join(){
        return "join.html";
    }

    @PostMapping("/member")
    String addMember(@RequestParam String username,String password, String displayName){
        Member member = new Member();
        member.setUsername(username);
        var hash = passwordEncoder.encode(password);
        member.setPassword(hash);
        member.setDisplayName(displayName);
        memberRepository.save(member);
        return "redirect:/list";
    }

    @GetMapping("/login")
    public String login(){
        return "login.html";
    }



}
