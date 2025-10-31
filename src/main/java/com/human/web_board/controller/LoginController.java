package com.human.web_board.controller;

import com.human.web_board.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller  // 주로 서버사이드렌더링을 위한 컨트롤러
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    private final MemberService memberService;  // 생성자 의존성 주입

    @GetMapping("/")  // 루트 경로에 대한 페이지 이동, http://localhost:8111
    public String loginPage() {
        return "login/login";   // resoures/template/login/login.html
    }

}
