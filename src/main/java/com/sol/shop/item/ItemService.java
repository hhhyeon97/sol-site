package com.sol.shop.item;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public void saveItem(String title, Integer price, Long userId, String imageUrl, String descContent){

        Item item = new Item();
        item.setTitle(title);
        item.setPrice(price);
        item.setUserId(userId);
        item.setImageUrl(imageUrl);
        item.setDescContent(descContent);
        itemRepository.save(item);
    }

    public List<Item> findAllItems() {
        return itemRepository.findAll();
    }

    public Optional<Item> findItemById(Long id) {
        return itemRepository.findById(id);
    }

    public void editItem(String title, Integer price, Long id, String descContent){
        if (title.length() > 100) {
            throw new IllegalArgumentException("제목은 100자 이하여야 합니다.");
        }
        if (price < 0) {
            throw new IllegalArgumentException("가격은 음수가 될 수 없습니다.");
        }
        if (descContent.length() > 255){
            throw new IllegalArgumentException("설명은 255자 이하여야 합니다.");
        }
        Optional<Item> optionalItem = itemRepository.findById(id);
        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();
            item.setTitle(title);
            item.setPrice(price);
            item.setDescContent(descContent);
            itemRepository.save(item);
        } else {
            throw new IllegalArgumentException("해당 아이템이 존재하지 않습니다.");
        }
    }
}
