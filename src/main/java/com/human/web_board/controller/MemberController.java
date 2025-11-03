package com.human.web_board.controller;

import com.human.web_board.dto.MemberRes;
import com.human.web_board.dto.MemberSignupReq;
import com.human.web_board.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;  // 의존성 주입
    // 회원 가입 폼 페이지
    @GetMapping("/new")
    public String signupForm(Model model) {
        // model은 화면 정보 공유를 위해서 사용, 회원 정보를 담을 빈 객체를 생성 후 전달
        model.addAttribute("memberForm", new MemberSignupReq());
        return "members/new";
    }

    // 회원 가입 처리
    @PostMapping("/new")
    public String signup(MemberSignupReq req, Model model) {
        try {
            memberService.signup(req);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getStackTrace());
            return "members/new";
        }
        return "redirect:/";  // 가입이 성공하면 로그인 페이지로 이동
    }

    // 회원 조회 ==> 실습
    @GetMapping
    public String list(Model model) {
        List<MemberRes> members = memberService.list();
        model.addAttribute("members", members);
        return "members/list";
    }

    // 회원 정보 상세 보기
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        MemberRes member = memberService.getById(id);

        return "/members/detail";
    }


    // 회원 정보 수정 하기
}
