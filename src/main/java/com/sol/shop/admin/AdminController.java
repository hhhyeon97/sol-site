package com.sol.shop.admin;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class AdminController {

    @GetMapping("/admin/123")
    String admin() {
        return "admin.html";
    }

}
