package com.human.web_board.controller;

import com.human.web_board.dto.CommentCreateReq;
import com.human.web_board.dto.CommentRes;
import com.human.web_board.dto.CommentUpdateReq;
import com.human.web_board.dto.MemberRes;
import com.human.web_board.service.CommentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    // ✅ 댓글 등록: 경로변수 제거, postId는 폼 hidden으로 받음
    @PostMapping
    public String create(CommentCreateReq req, HttpSession session) {
        MemberRes member = (MemberRes) session.getAttribute("loginMember");
        if (member == null) return "redirect:/";
        req.setMemberId(member.getId());   // 작성자 ID
        commentService.write(req);
        return "redirect:/posts/" + req.getPostId();
    }

    // 댓글 수정 폼
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, HttpSession session) {
        if (session.getAttribute("loginMember") == null) return "redirect:/";
        CommentRes comment = commentService.detail(id);
        if (comment == null) return "redirect:/posts";

        CommentUpdateReq form = new CommentUpdateReq();
        form.setId(id);
        form.setContent(comment.getContent());
        form.setPostId(comment.getPostId());     // ✅ 수정 후 리다이렉트용 postId도 담아둠

        model.addAttribute("comment", comment);
        model.addAttribute("commentUpdateReq", form);
        return "comments/edit";
    }

    // 댓글 수정 저장
    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @ModelAttribute CommentUpdateReq commentUpdateReq,
                         HttpSession session) {
        if (session.getAttribute("loginMember") == null) return "redirect:/";
        commentService.update(commentUpdateReq);
        return "redirect:/posts/" + commentUpdateReq.getPostId();
    }

    // ✅ 댓글 삭제 (목록에서 사용 중)
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id,
                         @RequestParam Long postId,
                         HttpSession session) {
        if (session.getAttribute("loginMember") == null) return "redirect:/";
        commentService.delete(id);
        return "redirect:/posts/" + postId;
    }
}