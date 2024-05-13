package com.sol.shop.comment;


import com.sol.shop.member.CustomUser;
import com.sol.shop.member.Member;
import com.sol.shop.member.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.http.HttpRequest;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    @PostMapping("/comment")
    String postComment(@RequestParam String content, @RequestParam Long parent,HttpServletRequest request,Authentication auth, RedirectAttributes redirectAttributes, Model model) {

        CustomUser user = (CustomUser) auth.getPrincipal();
        String referer = request.getHeader("Referer");

        var result = memberRepository.findByUsername(user.getUsername());

        // 사용자가 해당 상품을 구매했는지 확인
        boolean hasPurchased = result.get().getSales().stream()
                .anyMatch(sale -> sale.getItemId().equals(parent));

        if (!auth.isAuthenticated()) {
            redirectAttributes.addFlashAttribute("message", "로그인 후 이용 가능합니다.");
            return "redirect:"+ referer;
        } else if (!hasPurchased) {
            redirectAttributes.addFlashAttribute("message", "해당 상품을 구매한 유저만 리뷰를 작성할 수 있습니다.");
            return "redirect:"+ referer;
        } else {
            Comment data = new Comment();
            data.setContent(content);
            data.setUsername(user.getUsername());
            data.setParentId(parent);
            commentRepository.save(data);
            return "redirect:"+ referer;
        }
    }


}
