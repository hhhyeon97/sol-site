package com.sol.shop;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;

    @GetMapping("/list")
    String list(Model model){
        List<Item> result = itemRepository.findAll(); //리스트 자료형으로 가져옴
        System.out.println(result.get(0).title);
        System.out.println(result.get(0).price);

        List<Object> a = new ArrayList<>();
        a.add(30);
        a.add(40);
        System.out.println(a.get(0));
        System.out.println(a.get(1));

        model.addAttribute("name","홍길동");
        return "list.html";
    }
}
