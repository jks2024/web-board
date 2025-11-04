package com.human.web_board.dto;

import lombok.*;

@Getter @Setter @ToString @NoArgsConstructor @AllArgsConstructor
public class CommentUpdateReq {
    private Long id;
    private Long postId;
    private Long memberId;
    private String content;
}
