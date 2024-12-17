package com.example.demo.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordEncoderTest {

    @Test
    @DisplayName("비밀번호 encode 테스트")
    void PasswordEncodeTest(){

        String pw = "1234";

        // password 인코딩
        String encodedPassword = PasswordEncoder.encode(pw);

        // null 체크
        assertNotNull(encodedPassword, "null이면 안됩니다.");

        // encoding 된 password 와 원래 pw 를 비교해서 같이 않아야됨
        assertNotEquals(pw, encodedPassword);

        // encoding 된 password 와 pw가 같은 비밀번호를 가리키는 지 확인
        assertTrue(PasswordEncoder.matches(pw, encodedPassword), "비밀번호가 일치합니다.");

    }

}