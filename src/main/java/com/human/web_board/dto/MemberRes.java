package com.human.web_board.dto;
import lombok.*;
import java.time.LocalDateTime;

// DB와 동일한 형태의 클래스 (Entity 역할)

@Getter // 게터 메서드 자동 생성
@Setter // 세터 메서드 자동 생성
@NoArgsConstructor // 매개 변수가 없는 기본 생성자 생성
@AllArgsConstructor // 매개 변수가 전부 있는 생성자 생성
@ToString  // toString() 메서드를 오버라이딩 해줌
public class MemberRes {
    private Long id;        // 자동 증가 값, PK
    private String email;   // 이메일
    private String pwd;     // 비밀번호
    private String name;    // 이름
    private LocalDateTime regDate;  // 가입일
    private String profileImg;
}
