package com.human.web_board.controller;

import com.human.web_board.dto.MemberRes;
import com.human.web_board.dto.MemberSignupReq;
import com.human.web_board.dto.MemberUpdateReq;
import com.human.web_board.service.FileStorageService;
import com.human.web_board.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;  // 의존성 주입
    private final FileStorageService fileStorageService;
    // 회원 가입 폼 페이지
    @GetMapping("/new")
    public String signupForm(Model model) {
        // model은 화면 정보 공유를 위해서 사용, 회원 정보를 담을 빈 객체를 생성 후 전달
        model.addAttribute("memberForm", new MemberSignupReq());
        return "members/new";
    }

    // 회원 가입 처리
    @PostMapping("/new")
    public String signup(@ModelAttribute MemberSignupReq req, Model model) {
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
        if (member == null) {
            model.addAttribute("error", "존재하지 않는 회원 입니다.");
            return "members/list";
        }
        model.addAttribute("member", member);
        return "members/detail";
    }

    // 수정 폼
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        MemberRes member = memberService.getById(id);
        if (member == null) {
            model.addAttribute("error", "존재하지 않는 회원입니다.");
            return "members/list";
        }
        model.addAttribute("member", member);  // 현재 회원 정보 표시
        MemberUpdateReq form = new MemberUpdateReq();  // 수정용 데이터
        form.setName(member.getName());  // 이름값은 기본으로 채워 둠
        form.setPassword("");  // 비워 둠
        model.addAttribute("memberForm", form);
        return "members/edit";
    }


    // 회원 정보 수정 하기
    @PostMapping("/{id}/edit")
    public String updateMember (@PathVariable Long id,
                                @ModelAttribute ("memberForm")MemberUpdateReq req,
                                @RequestParam(value = "profileImage", required = false)MultipartFile profileImage,
                                Model model
                                ) {
        // 현재 회원 정보 조회
        MemberRes member = memberService.getById(id);
        if (member == null) {
            model.addAttribute("error", "존재하지 않는 회원 입니다.");
            return "members/list";
        }
        //업로드(선택) : 이미지가 있으면 저장 후 상대 경로 확보
        String newImagePath = null;
        if (profileImage != null && !profileImage.isEmpty()) {
            newImagePath = fileStorageService.saveImage(profileImage, "members");
        }
        memberService.updateMember(id, req.getName(), req.getPassword(), newImagePath);
        if (newImagePath != null) {
            return "redirect:/members/" + id;
        } else {
            return "redirect:/members";
        }
    }

}
