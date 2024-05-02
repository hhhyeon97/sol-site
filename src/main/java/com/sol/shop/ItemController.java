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
    private final ItemService itemService;

    @GetMapping("/list")
    String list(Model model){
        List<Item> result = itemService.findAllItems(); // 서비스로부터 아이템 리스트를 가져옴
        model.addAttribute("items", result);
        return "list.html";
    }

    @GetMapping("/write")
    String write(){
        return "write.html";
    }

    @PostMapping("/add")
    String addPost(@RequestParam String title, @RequestParam Integer price) {
        itemService.saveItem(title, price);
        return "redirect:/list";
    }

    @GetMapping("/detail/{id}")
    String detail(@PathVariable Long id, Model model) {
    Optional<Item> result = itemService.findItemById(id);
    if (result.isPresent()) {
        Item item = result.get();
        model.addAttribute("item", item);
        return "detail.html";
    } else {
        return "404page.html";
    }
    }

    @GetMapping("/edit/{id}")
    String edit(Model model, @PathVariable Long id){
        Optional<Item> result = itemRepository.findById(id);
        if(result.isPresent()){
            model.addAttribute("data",result.get());
            return "edit.html";
        }else {
            return "redirect:/list";
        }
    }

    @PostMapping("/edit")
    String editItem(@RequestParam String title, Integer price, Long id){
        Item item = new Item();
        item.setId(id);
        item.setTitle(title);
        item.setPrice(price);
        itemRepository.save(item);
        return "redirect:/list";
    }

}
