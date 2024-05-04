package com.sol.shop.item;

import com.sol.shop.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;
    private final ItemService itemService;
    private final MemberRepository memberRepository;
    private final S3Service s3Service;
    private final S3Service2 s3Service2;

//    @GetMapping("/list")
//    String list(Model model){
//        List<Item> result = itemService.findAllItems(); // 서비스로부터 아이템 리스트를 가져옴
//        model.addAttribute("items", result);
//        return "list.html";
//    }

    @GetMapping("/list")
    public String list(Model model, @RequestParam(defaultValue = "1") int page) {
    // 페이지 번호는 0부터 시작하므로 -1 처리
    Pageable pageable = PageRequest.of(page - 1, 5, Sort.by("id").descending());

    // 페이지네이션된 결과 가져오기
    Page<Item> itemsPage = itemRepository.findAll(pageable);
    model.addAttribute("items", itemsPage.getContent());
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", itemsPage.getTotalPages());
    return "list.html";
    }

    @GetMapping("/write")
    String write(Authentication authentication){
        if (authentication != null && authentication.isAuthenticated()) {
            return "write.html";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/add")
    String addPost(@RequestParam String title, @RequestParam Integer price,@RequestParam("imageFile") MultipartFile imageFile, Authentication authentication) {
        // 현재 로그인한 사용자의 아이디 가져오기
        String username = authentication.getName();

        // 해당 아이디로 사용자 정보 조회
        var result = memberRepository.findByUsername(username);
        if(result.isEmpty()){

        }
        // 사용자 정보에서 userId 가져오기
        Long userId = result.get().getId();

        // 이미지 파일을 S3에 업로드하고 URL 가져오기
        String imageUrl = s3Service2.uploadImageToS3(imageFile, "items");

        itemService.saveItem(title, price, userId, imageUrl);
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
      itemService.editItem(title,price,id);
        return "redirect:/list";
    }

    @PostMapping("/test1")
    String test1(@RequestBody Map<String, Object> body){
        System.out.println(body);
        return "redirect:/list";
    }

    @DeleteMapping("/item")
    ResponseEntity<String> deleteItem(@RequestParam Long id){
        itemRepository.deleteById(id);
        return ResponseEntity.status(200).body("삭제완료");
    }

    @GetMapping("/test2")
    String test2(){
        var result = new BCryptPasswordEncoder().encode("문자~");
        System.out.println(result);
        return "redirect:/list";
    }

//    @GetMapping("/list/page/{pageNum}")
//    String getListPage(Model model, @PathVariable Integer pageNum) {
//        Page<Item> result = itemRepository.findPageBy(PageRequest.of(pageNum-1,5));
//        model.addAttribute("items",result);
//        return "list.html";
//    }

    @GetMapping("/presigned-url")
    @ResponseBody
    String getURL(@RequestParam String filename){
//        System.out.println(filename);
        var result = s3Service.createPreSignedUrl("test/"+filename);
        System.out.println(result);
        return result;
    }
}
