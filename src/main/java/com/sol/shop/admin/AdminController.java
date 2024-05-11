package com.sol.shop.admin;


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

    @GetMapping("/admin")
    String admin(Model model) {
        List<Sales> result = salesRepository.customFindAll();

        model.addAttribute("orderList",result);

        return "admin.html";
    }

}
