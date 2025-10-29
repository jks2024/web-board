package com.human.web_board.service;

import com.human.web_board.dto.PostCreateReq;
import com.human.web_board.dto.PostRes;

import java.util.List;

public interface PostService {
    // 게시글 등록
    Long write(PostCreateReq req);
    // 게시글 목록
    List<PostRes> list();
    // 개별 게시글 보기 (게시글 ID)
    PostRes get(Long id);
    // 게시글 수정
    boolean edit(Long id, String title, String content);
    // 게시글 삭제
    boolean delete(Long id);

}