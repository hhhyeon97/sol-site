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
                     @RequestParam Integer price,
                     @RequestParam Integer count,
                     Authentication auth) {
        Sales sales = new Sales();
        sales.setCount(count);
        sales.setPrice(price);
        sales.setItemName(title);
        CustomUser user = (CustomUser) auth.getPrincipal();
        System.out.println(user.userId);
        var member = new Member();
        member.setId(user.userId);
        sales.setMember(member);
//        sales.setMemberId(user.userId);
        salesRepository.save(sales);
        return "redirect:/list";
    }

    @GetMapping("/order/all")
    String getOrderAll(Model model) {
//        List<Sales> result = salesRepository.findAll();
    List<Sales> result = salesRepository.customFindAll();
//        System.out.println(result);
//        var salesDto = new SalesDto();
//        salesDto.itemName = result.get(0).getItemName();
//        salesDto.price = result.get(0).getPrice();
//        salesDto.username = result.get(0).getMember().getUsername();
        model.addAttribute("orderList",result);

        var result2 = memberRepository.findById(1L);
        System.out.println(result2.get().getSales());

        return "orderList.html";
    }

}

class SalesDto {
    public String itemName;
    public Integer price;
    public String username;
}
