package com.sol.shop.admin;


import com.sol.shop.comment.Comment;
import com.sol.shop.comment.CommentRepository;
import com.sol.shop.member.Member;
import com.sol.shop.member.MemberRepository;
import com.sol.shop.sales.Sales;
import com.sol.shop.sales.SalesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final SalesRepository salesRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    @GetMapping("/admin")
    String admin() {
        return "admin.html";
    }

    @GetMapping("/admin/user-list")
    String userList(Model model){
        List<Member> result = memberRepository.findAll();
        model.addAttribute("memberList",result);
        return "userList.html";
    }

    @PostMapping("/withdraw")
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    public String withdrawUser(@RequestParam String username) {
        // 사용자 탈퇴
        Optional<Member> memberOptional = memberRepository.findByUsername(username);
        if (memberOptional.isPresent()) {
            memberRepository.delete(memberOptional.get());
            return "회원 탈퇴가 완료되었습니다.";
        } else {
            return "해당 사용자를 찾을 수 없습니다.";
        }
    }

    @GetMapping("/admin/order-list")
    String orderList(Model model, @RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "desc") String sort) {
        //Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("id").descending());
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.Direction.fromString(sort), "id");

        Page<Sales> salesPage = salesRepository.findAll(pageable);

        model.addAttribute("orderList", salesPage.getContent());

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", salesPage.getTotalPages());
        model.addAttribute("sort", sort);
        return "orderList.html";
    }

    @GetMapping("/admin/review-list")
    String reviewList(Model model, @RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "desc") String sort){
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.Direction.fromString(sort), "id");

        Page<Comment> reviewPage = commentRepository.findAll(pageable);

        model.addAttribute("reviewList", reviewPage.getContent());

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", reviewPage.getTotalPages());
        model.addAttribute("sort", sort);

        return "reviewList.html";
    }

}
