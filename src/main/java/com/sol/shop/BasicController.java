package com.sol.shop;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.ZonedDateTime;

@Controller
public class BasicController {
   @GetMapping("/")
    String main(){
    return "index.html";
   }

//    @GetMapping("/login")
//    @ResponseBody
//    String login(){
//        return "로그인입니다 하이";
//    }

//    @GetMapping("/mypage")
//    @ResponseBody
//    String myPage(){
//        return "마이페이지입니다 하이";
//    }

    @GetMapping("/date")
    @ResponseBody
    String date(){
       return ZonedDateTime.now().toString();
    }

    @GetMapping("/test")
    @ResponseBody
    String test(){
        var a = new Test();
        a.setName("홍길동");
        a.setAge(60);
        a.ageSetting(-20);
        a.add1age();
        System.out.println(a.getAge());
        return "TEST";
    }

}
