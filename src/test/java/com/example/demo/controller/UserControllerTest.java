package com.example.demo.controller;

import com.example.demo.constants.GlobalConstants;
import com.example.demo.dto.Authentication;
import com.example.demo.dto.LoginRequestDto;
import com.example.demo.dto.UserRequestDto;
import com.example.demo.entity.Role;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    @DisplayName("사용자 생성 test")
    void createUserTest() throws Exception {
        // given
        String nickname = "kim";
        String email = "kim@gmail.com";
        String password = "1234";

        UserRequestDto userRequestDto = new UserRequestDto(Role.USER, email, nickname, password);

        // when
        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)
                        )
                )
                .andExpect(status().isCreated()) // then
                .andExpect(content().string("회원가입 되었습니다."))
                .andDo(print());

    }

    @Test
    @DisplayName("로그인 test")
    void loginTest() throws Exception {
        // given
        String email = "kim@gmail.com";
        String password = "1234";

        LoginRequestDto loginRequestDto = new LoginRequestDto(email, password);

        Authentication authentication = new Authentication(1L, Role.USER);

        Mockito.when(userService.loginUser(any(LoginRequestDto.class))).thenReturn(authentication);

        MockHttpSession session = new MockHttpSession();

        // when
        mvc.perform(post("/users/login")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto)
                        )
                )
                .andExpect(status().isOk()) // then
                .andExpect(content().string("로그인 되었습니다."))
                .andDo(print());

        assertNotNull(session.getAttribute(GlobalConstants.USER_AUTH));
        assertEquals(authentication, session.getAttribute(GlobalConstants.USER_AUTH));
    }

    @Test
    @DisplayName("로그아웃 test")
    void logoutTest() throws Exception {
        // given
        Authentication authentication = new Authentication(1L, Role.USER);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(GlobalConstants.USER_AUTH, authentication);

        // when
        mvc.perform(post("/users/logout")
                        .session(session)
                )
                .andExpect(status().isOk()) // then
                .andExpect(content().string("로그아웃되었습니다."))
                .andDo(print());

        assertTrue(session.isInvalid());
    }


}