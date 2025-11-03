package com.human.web_board.controller;

import com.human.web_board.dto.MemberRes;
import com.human.web_board.dto.PostCreateReq;
import com.human.web_board.dto.PostRes;
import com.human.web_board.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
@Slf4j
public class PostController {
    private final PostService postService;
    @GetMapping  // 게시글 목록 가져 오기
    public String list(HttpSession session, Model model) {
        // 로그인 여부 확인 var은 타입 추론을 해서 자동으로 형을 찾아 줌
        var loginMember = session.getAttribute("loginMember");
        if (loginMember == null) return "redirect:/";  // 세션이 없으면 로그인 페이지로 이동
        List<PostRes> list = postService.list();
        model.addAttribute("posts", list);
        return "post/list";
    }

    // 게시글 쓰기 폼
    @GetMapping("/new")
    public String postWriteForm(HttpSession session, Model model) {
        if (session.getAttribute("loginMember") == null) {
            return "redirect:/";
        }
        model.addAttribute(new PostCreateReq()); // 이름을 생략하면 postCreateReq로 모델에 등록
        return "post/new";
    }


    // 게시글 쓰기 DB 처리
    @PostMapping("/new")
    public String create(PostCreateReq req, HttpSession session, Model model) {
        MemberRes member = (MemberRes) session.getAttribute("loginMember");

        log.error("게시글 쓰기 {}", req);

        if (member == null) {
            return "redirect:/";
        }
        try {
            req.setMemberId(member.getId()); // 화면에서 정보를 입려 받을 수 없기 때문에 세션 정보에서 추출해서 넣어 줌
            Long postId = postService.write(req);
            return "redirect:/posts";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "posts/new";
        }
    }



    // 게시글 상세 조회
    @GetMapping("/{postId}")
    public String detail(@PathVariable Long postId, Model model, HttpSession session) {
        return "posts/detail";
    }

}
