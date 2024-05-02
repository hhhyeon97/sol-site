package com.sol.shop;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    String addPost(@RequestParam String title, @RequestParam Integer price) {
        Item item = new Item();
        item.setTitle(title);
        item.setPrice(price);
        itemRepository.save(item);
        return "redirect:/list";
    }

    @GetMapping("/detail/{id}")
    String detail(@PathVariable Long id, Model model) throws Exception{
        throw new Exception();
//            Optional<Item> result = itemRepository.findById(id);
//            if(result.isPresent()){
//                Item item = result.get();
//                model.addAttribute("item",item);
//                return "detail.html";
//            }else {
//                return "404page.html";
//            }
    }

}
