package com.human.web_board.dao;

import com.human.web_board.dto.MemberSignupReq;
import com.human.web_board.dto.MemberRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

// DB와 정보를 주고 받기 위한 SQL 작성 구간
@Repository     //  Spring Container 에 Bean 객체 등록, 싱글톤 객체가 됨
@RequiredArgsConstructor  // 생성자를 통한 의존성 주입을 하기 위해서 사용
@Slf4j  // log 메세지 출력을 지원하기 위한  lombok 기능
public class MemberDao {
    private final JdbcTemplate jdbc;  // JdbcTemplate을 의존성 주입 받음

    // 회원 가입
    public Long save(MemberSignupReq m) {
        @Language("SQL")
        String sql = """
            INSERT INTO member (id, email, pwd, name, reg_date, profile_img)
            VALUES (seq_member.NEXTVAL, ?, ?, ?, SYSTIMESTAMP, NULL)
            """;
        jdbc.update(sql, m.getEmail(), m.getPwd(), m.getName());
        return jdbc.queryForObject("SELECT seq_member.CURRVAL FROM dual", Long.class);  // Long 타입의 id를 반환
    }

    // 이메일로 회원 조회
    public MemberRes findByEmail(String email) {
        @Language("SQL")
        String sql = """
            SELECT id, email, pwd, name, reg_date, profile_img
              FROM member
             WHERE email = ?
            """;
        List<MemberRes> list = jdbc.query(sql, new MemberRowMapper(), email);
        return list.isEmpty() ? null : list.get(0); // 조회 시 결과가 없는 경우 null을 넣기 위해서
    }

    // ID로 회원 조회
    public MemberRes findById(Long id) {
        @Language("SQL")
        String sql = """
            SELECT id, email, pwd, name, reg_date, profile_img
              FROM member
             WHERE id = ?
            """;
        List<MemberRes> list = jdbc.query(sql, new MemberRowMapper(), id);
        return list.isEmpty() ? null : list.get(0); // 조회 시 결과가 없는 경우 null을 넣기 위해서
    }

    // 회원 정보 수정
    public boolean updateMember(Long id, String name, String pwd, String profileImg) {
        StringBuilder sql = new StringBuilder("UPDATE member SET name = ?");
        List<Object> args = new ArrayList<>();
        args.add(name);

        if (pwd != null && !pwd.isBlank()) {
            sql.append(", pwd = ?");
            args.add(pwd); // ⚠ 현재 요구사항: 평문 저장
        }
        if (profileImg != null && !profileImg.isBlank()) {
            sql.append(", profile_img = ?");
            args.add(profileImg);
        }
        sql.append(" WHERE id = ?");
        args.add(id);

        return jdbc.update(sql.toString(), args.toArray()) > 0;
    }

    // 전체 회원 조회
    public List<MemberRes> findAll() {
        @Language("SQL")
        String sql = """
            SELECT id, email, pwd, name, reg_date, profile_img
              FROM member
             ORDER BY id DESC
            """;
        return jdbc.query(sql, new MemberRowMapper());
    }

    // Mapper 메서드  DB -> Member
    static class MemberRowMapper implements RowMapper<MemberRes> {
        @Override
        public MemberRes mapRow(ResultSet rs, int rowNum) throws SQLException {
            MemberRes m = new MemberRes();
            m.setId(rs.getLong("id"));
            m.setEmail(rs.getString("email"));
            m.setPwd(rs.getString("pwd"));
            m.setName(rs.getString("name"));

            Timestamp ts = rs.getTimestamp("reg_date");
            if (ts != null) {
                m.setRegDate(ts.toLocalDateTime()); // DTO 필드명이 regDate라면 여기에 맞춰 세터 사용
            }

            m.setProfileImg(rs.getString("profile_img")); // ✅ 추가 매핑
            return m;
        }
    }


}
