package com.sol.shop;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;

    @GetMapping("/list")
    String list(Model model){
        List<Item> result = itemRepository.findAll(); //리스트 자료형으로 가져옴
        model.addAttribute("items",result);

        String firstTitle = null;
        if (!result.isEmpty()) {
            firstTitle = result.get(0).getTitle();
        }
        System.out.println("첫 번째 제목: " + firstTitle);

        return "list.html";
    }

    @GetMapping("/write")
    String write(){
        return "write.html";
    }

    @PostMapping("/add")
    String addPost(@RequestParam String title, @RequestParam Integer price){
        Item item = new Item();
        item.setTitle(title);
        item.setPrice(price);
        itemRepository.save(item);
        return "redirect:/list";
    }

    @GetMapping("/detail/{id}")
    String detail(@PathVariable Integer id){
        System.out.println(id);
        Optional<Item> result = itemRepository.findById(1L);
        if(result.isPresent()){
            System.out.println(result.get());
            return "detail.html";
        }else {
            return "404page.html";
        }
    }

}
