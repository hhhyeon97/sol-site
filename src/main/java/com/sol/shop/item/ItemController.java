package com.sol.shop.item;

import com.sol.shop.comment.Comment;
import com.sol.shop.comment.CommentRepository;
import com.sol.shop.member.CustomUser;
import com.sol.shop.member.Member;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    private final CommentRepository commentRepository;



    @GetMapping("/")
    public String list(Model model, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "id") String sortBy) {
        Sort sort = Sort.by(sortBy).descending();

        if (sortBy.equals("price")) {
            sort = Sort.by(sortBy).ascending();
        } else if (sortBy.equals("title")) {
            sort = Sort.by(sortBy).ascending();
        }

        Pageable pageable = PageRequest.of(page - 1, 9, sort);

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
    String addPost(@RequestParam String title, @RequestParam Integer price,
                   @RequestParam String descContent, @RequestParam("imageFile") MultipartFile imageFile, Authentication authentication) {
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

        itemService.saveItem(title, price, userId, imageUrl, descContent);
        return "redirect:/";
    }


    @GetMapping("/detail/{id}")
    String detail(@PathVariable Long id, Model model) {

    List<Comment> comment = commentRepository.findAllByParentId(id);
    Optional<Item> result = itemService.findItemById(id);
    if (result.isPresent()) {
        Item item = result.get();
        model.addAttribute("item", item);
        model.addAttribute("comment",comment);
        return "detail.html";
    } else {
        return "404page.html";
    }
    }

@GetMapping("/edit/{id}")
String edit(Model model, @PathVariable Long id, Authentication authentication){
    Optional<Item> result = itemRepository.findById(id);
    if(result.isPresent()){
        Item item = result.get();

        // 현재 로그인한 사용자와 아이템 작성자 비교
        String loggedInUsername = authentication.getName();
        Long loggedInUserId = memberRepository.findByUsername(loggedInUsername)
                .orElseThrow()
                .getId();
        if (loggedInUserId.equals(item.getUserId())) {
            model.addAttribute("data", item);
            return "edit.html";
        } else {
            return "redirect:/?editError=true";
        }
    } else {
        return "redirect:/";
    }
}


    @PostMapping("/edit")
    @ResponseBody
    public ResponseEntity<String> editItem(@RequestParam String title, Integer price, Long id, String descContent){
        try {
            itemService.editItem(title, price, id, descContent);
            return ResponseEntity.ok("수정이 완료되었습니다 !");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/test1")
    String test1(@RequestBody Map<String, Object> body){
        System.out.println(body);
        return "redirect:/";
    }


    @PostMapping("/delete/{id}")
    public String deleteItem(@PathVariable Long id, RedirectAttributes redirectAttributes, Authentication authentication, Model model) {
        // 현재 로그인한 사용자의 아이디 가져오기
        String username = authentication.getName();
        // 해당 아이디로 사용자 정보 조회
        var result = memberRepository.findByUsername(username);
        // 사용자 정보에서 userId 가져오기
        Long loggedInUserId = result.get().getId();
        System.out.println("로그인한 유저 id"+loggedInUserId);
        model.addAttribute("loggedInUserId", loggedInUserId);

        // 상품 정보 조회
        Optional<Item> optionalItem = itemRepository.findById(id);
        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();
            Long itemUserId = item.getUserId();
            System.out.println("상품을 작성한 사용자 id: " + itemUserId);

            // 현재 로그인한 사용자의 아이디와 상품을 작성한 사용자의 아이디 비교
            if (loggedInUserId.equals(itemUserId)) {
                // 일치하는 경우 삭제
                itemRepository.deleteById(id);
                redirectAttributes.addFlashAttribute("successMessage", "상품이 성공적으로 삭제되었습니다.");
            } else {

                redirectAttributes.addFlashAttribute("errorMessage", "해당 상품을 삭제할 수 있는 권한이 없습니다.");
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "상품을 찾을 수 없습니다.");
        }




        return "redirect:/";
    }


//    @PostMapping("/delete/{id}")
//    String deleteItem(@PathVariable Long id){
//        itemRepository.deleteById(id);
//        return "redirect:/list";
//    }

//    @DeleteMapping("/item")
//    ResponseEntity<String> deleteItem(@RequestParam Long id){
//        itemRepository.deleteById(id);
//        return ResponseEntity.status(200).body("삭제완료");
//    }

    @GetMapping("/test2")
    String test2(){
        var result = new BCryptPasswordEncoder().encode("문자~");
        System.out.println(result);
        return "list.html";
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

    @PostMapping("/search")
    String postSearch(@RequestParam String searchText, Model model){

        List<Item> result = itemRepository.rawQuery1(searchText);
        //System.out.println(result);
        model.addAttribute("searchList",result);
        model.addAttribute("searchText", searchText);
        return "searchList.html";
    }


}
