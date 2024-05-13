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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final SalesRepository salesRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    @GetMapping("/admin")
    String admin(Model model) {
        List<Sales> result = salesRepository.customFindAll();
        List<Comment> result2 = commentRepository.findAll();
        List<Member> result3 = memberRepository.findAll();
        model.addAttribute("orderList",result);
        model.addAttribute("reviewList",result2);
        model.addAttribute("memberList",result3);
        return "admin.html";
    }

    @GetMapping("/admin/user-list")
    String userList(Model model){
        List<Member> result = memberRepository.findAll();
        model.addAttribute("memberList",result);
        return "userList.html";
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
    String reviewList(Model model){
        List<Comment> result = commentRepository.findAll();
        model.addAttribute("reviewList",result);
        return "reviewList.html";
    }

}
