package com.sol.shop.comment;


import com.sol.shop.member.CustomUser;
import com.sol.shop.member.Member;
import com.sol.shop.member.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.http.HttpRequest;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    @PostMapping("/comment")
    String postComment(@RequestParam String content, @RequestParam Long parent, Authentication auth, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        CustomUser user = (CustomUser) auth.getPrincipal();

        var result = memberRepository.findByUsername(user.getUsername());

        // 사용자가 해당 상품을 구매했는지 확인
        boolean hasPurchased = result.get().getSales().stream()
                .anyMatch(sale -> sale.getItemId().equals(parent));

        System.out.println(hasPurchased);

        if (hasPurchased) {
            Comment data = new Comment();
            data.setContent(content);
            data.setUsername(user.getUsername());
            data.setParentId(parent);
            commentRepository.save(data);
            return "redirect:"+ referer;
        } else {
            return "redirect:/error";
        }
    }


}
