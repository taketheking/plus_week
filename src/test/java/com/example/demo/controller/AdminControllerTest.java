package com.example.demo.controller;

import com.example.demo.constants.GlobalConstants;
import com.example.demo.dto.Authentication;
import com.example.demo.dto.ReportRequestDto;
import com.example.demo.entity.Role;
import com.example.demo.service.AdminService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = { AdminController.class })
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AdminService adminService;

    @Test
    void reportUsersTest() throws Exception {
        //given
        List<Long> list = new ArrayList<>();
        for(long i = 0; i < 10; i++){
            list.add(i);
        }
        given(adminService.reportUsers(list)).willReturn(10);

        ReportRequestDto reportRequestDto = new ReportRequestDto(list);

        // admin 권한 로그인 세션
        MockHttpSession session = new MockHttpSession();
        Authentication authentication = new Authentication(1L, Role.ADMIN);
        session.setAttribute(GlobalConstants.USER_AUTH, authentication);

        //when
        mockMvc.perform(post("/admins/report-users")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reportRequestDto))
                )
                .andExpect(status().isOk())  //then
                .andExpect(content().string("10"))
        .andDo(print());

    }

}