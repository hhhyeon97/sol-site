package com.sol.shop.sales;


import com.sol.shop.member.CustomUser;
import com.sol.shop.member.Member;
import com.sol.shop.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SalesController {

    private final SalesRepository salesRepository;
    private final MemberRepository memberRepository;

    @PostMapping("/order")
    String postOrder(@RequestParam String title,
                     @RequestParam Integer count,
                     @RequestParam Integer price,
                     Authentication auth,
                     @RequestParam Long itemId
                     ) {
        Sales sales = new Sales();
        sales.setCount(count);
        sales.setPrice(price*count);
        sales.setItemName(title);
        sales.setItemId(itemId);
        CustomUser user = (CustomUser) auth.getPrincipal();
        System.out.println(user.userId);
        var member = new Member();
        member.setId(user.userId);
        sales.setMember(member);
//        sales.setMemberId(user.userId);
        salesRepository.save(sales);
        return "redirect:/";
    }



}

class SalesDto {
    public String itemName;
    public Integer price;
    public String username;
}
