package com.sol.shop.sales;


import com.sol.shop.member.CustomUser;
import com.sol.shop.member.Member;
import com.sol.shop.member.MemberRepository;
import com.sol.shop.util.Util;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SalesController {

    private final SalesRepository salesRepository;

//    private static final Logger logger = LoggerFactory.getLogger(SalesController.class);


    @PostMapping("/order")
    String postOrder(@RequestParam String title,
                     @RequestParam Integer count,
                     @RequestParam Integer price,
                     Authentication auth,
                     @RequestParam Long itemId,
                     HttpServletRequest request,
                     Model model,
                     RedirectAttributes redirectAttributes
    ) {
        String referer = request.getHeader("Referer");

        if (auth != null && auth.isAuthenticated()) {
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
            salesRepository.save(sales);
            //        model.addAttribute("orderSuccessMessage", "주문이 성공적으로 완료되었습니다.");
            redirectAttributes.addFlashAttribute("orderSuccessMessage", "주문이 성공적으로 완료되었습니다.");
            return "redirect:"+ referer;
        }else {
            redirectAttributes.addFlashAttribute("loginRequiredMessage", "로그인 후에 구매할 수 있습니다.");
            return "redirect:/login";
        }

    }

    // findAll vs customFindAll
    @GetMapping("/order-all")
    public String getOrderAll(Model model) {
        long start, end;

        // jpa 제공 메서드 findAll
        start = System.currentTimeMillis();
        List<Sales> result = salesRepository.findAll();  // 기존 코드
        end = System.currentTimeMillis();
        log.info("Execution time for findAll: {} ms", (end - start));

        model.addAttribute("orderListFindAll", result);

        // 커스텀메서드
        start = System.currentTimeMillis();
        result = salesRepository.customFindAll();  // 보완한 커스텀메서드
        end = System.currentTimeMillis();
        log.info("Execution time for customFindAll: {} ms", (end - start));

        model.addAttribute("orderListCustomFindAll", result);

        return "orderListTest.html";
    }

}



class SalesDto {
    public String itemName;
    public Integer price;
    public String username;
}
