package com.human.web_board.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
// 로그인 화면에서 입력되는 정보를 컨트롤러가 받기 위한 DTO
public class LoginReq {
    private String email;
    private String pwd;
}
