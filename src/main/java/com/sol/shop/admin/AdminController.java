package com.sol.shop.admin;


import com.sol.shop.comment.Comment;
import com.sol.shop.comment.CommentRepository;
import com.sol.shop.sales.Sales;
import com.sol.shop.sales.SalesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final SalesRepository salesRepository;
    private final CommentRepository commentRepository;

    @GetMapping("/admin")
    String admin(Model model) {
        List<Sales> result = salesRepository.customFindAll();
        List<Comment> result2 = commentRepository.findAll();
        model.addAttribute("orderList",result);
        model.addAttribute("reviewList",result2);
        return "admin.html";
    }

}
