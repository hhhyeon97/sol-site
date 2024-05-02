package com.sol.shop.notice;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeRepository noticeRepository;

    @GetMapping("/notice")
    String notice(Model model){
        List<Notice> result = noticeRepository.findAll();  //리스트 자료형으로 가져옴
        model.addAttribute("notice",result);
        return "notice.html";
    }
}
