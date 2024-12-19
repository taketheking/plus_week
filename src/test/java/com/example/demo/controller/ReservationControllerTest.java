package com.example.demo.controller;

import com.example.demo.constants.GlobalConstants;
import com.example.demo.dto.Authentication;
import com.example.demo.dto.ReservationRequestDto;
import com.example.demo.dto.ReservationResponseDto;
import com.example.demo.entity.Role;
import com.example.demo.enums.ReservationStatus;
import com.example.demo.service.ReservationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReservationService reservationService;


    @Test
    void createReservationTest() throws Exception {
        //given
        String itemName = "신라호텔";
        String nickname = "kim";
        LocalDateTime startAt = LocalDateTime.of(2024, 12, 18, 18, 0, 0);
        LocalDateTime endAt = LocalDateTime.of(2024, 12, 19, 14, 0, 0);

        String formatStartAt = startAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        String formatEndAt = endAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));

        ReservationRequestDto reservationRequestDto = new ReservationRequestDto(1L, 1L, startAt, endAt);
        ReservationResponseDto reservationResponseDto = new ReservationResponseDto(1L, nickname, itemName, startAt, endAt, ReservationStatus.APPROVED);

        given(reservationService.createReservation(1L, 1L,  startAt, endAt)).willReturn(reservationResponseDto);

        // user 권한 로그인 세션
        MockHttpSession session = new MockHttpSession();
        Authentication authentication = new Authentication(1L, Role.USER);
        session.setAttribute(GlobalConstants.USER_AUTH, authentication);

        // when, then
        mockMvc.perform(post("/reservations")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservationRequestDto))
                        )
                .andExpect(status().isCreated())  //then
                .andExpect(jsonPath("$.nickname").value(nickname))
                .andExpect(jsonPath("$.itemName").value(itemName))
                .andExpect(jsonPath("$.startAt").value(formatStartAt))
                .andExpect(jsonPath("$.endAt").value(formatEndAt))
                .andExpect(jsonPath("$.status").value(ReservationStatus.APPROVED.toString()))
                .andDo(print());
    }

    @Test
    void updateReservationTest() throws Exception {
        //given
        String itemName = "신라호텔";
        String nickname = "kim";
        LocalDateTime startAt = LocalDateTime.of(2024, 12, 18, 18, 0, 0);
        LocalDateTime endAt = LocalDateTime.of(2024, 12, 19, 14, 0, 0);

        ReservationResponseDto reservationResponseDto = new ReservationResponseDto(1L, nickname, itemName, startAt, endAt, ReservationStatus.PENDING);

        given(reservationService.updateReservationStatus(1L, ReservationStatus.PENDING)).willReturn(reservationResponseDto);


        // user 권한 로그인 세션
        MockHttpSession session = new MockHttpSession();
        Authentication authentication = new Authentication(1L, Role.USER);
        session.setAttribute(GlobalConstants.USER_AUTH, authentication);

        // when, then
        mockMvc.perform(patch("/reservations/1/update-status")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("status", ReservationStatus.PENDING.name())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value(nickname))
                .andExpect(jsonPath("$.itemName").value(itemName))
                .andExpect(jsonPath("$.status").value(ReservationStatus.PENDING.toString())
                );

    }

    @Test
    void getReservationsTest() throws Exception {
        //given
        String itemName = "신라호텔";
        String nickname = "kim";
        LocalDateTime startAt = LocalDateTime.of(2024, 12, 18, 18, 0, 0);
        LocalDateTime endAt = LocalDateTime.of(2024, 12, 19, 14, 0, 0);

        String formatStartAt = startAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        String formatEndAt = endAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));

        List<ReservationResponseDto> list = new ArrayList<>();

        for(long i=1; i<10; i++){
            ReservationResponseDto reservationResponseDto = new ReservationResponseDto(
                    i, nickname, itemName, startAt, endAt, ReservationStatus.APPROVED
            );
            list.add(reservationResponseDto);
        }
        given(reservationService.getReservations()).willReturn(list);

        // user 권한 로그인 세션
        MockHttpSession session = new MockHttpSession();
        Authentication authentication = new Authentication(1L, Role.USER);
        session.setAttribute(GlobalConstants.USER_AUTH, authentication);

        // when, then
        mockMvc.perform(get("/reservations")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())  //then
                .andExpect(jsonPath("$.size()").value(9))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nickname").value(nickname))
                .andExpect(jsonPath("$[0].itemName").value(itemName))
                .andExpect(jsonPath("$[0].startAt").value(formatStartAt))
                .andExpect(jsonPath("$[0].endAt").value(formatEndAt))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andDo(print());
    }

    @Test
    void getReservationSearchTest() throws Exception {
        //given
        String itemName = "신라호텔";
        String nickname = "kim";
        LocalDateTime startAt = LocalDateTime.of(2024, 12, 18, 18, 0, 0);
        LocalDateTime endAt = LocalDateTime.of(2024, 12, 19, 14, 0, 0);
        String formatStartAt = startAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        String formatEndAt = endAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));

        List<ReservationResponseDto> list = new ArrayList<>();

        for(long i=1; i<10; i++){
            ReservationResponseDto reservationResponseDto = new ReservationResponseDto(
                    i, nickname, itemName, startAt, endAt, ReservationStatus.APPROVED
            );
            list.add(reservationResponseDto);
        }
        given(reservationService.searchAndConvertReservations(1L, 1L)).willReturn(list);


        // user 권한 로그인 세션
        MockHttpSession session = new MockHttpSession();
        Authentication authentication = new Authentication(1L, Role.USER);
        session.setAttribute(GlobalConstants.USER_AUTH, authentication);

        // when, then
        mockMvc.perform(get("/reservations/search")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("userId", String.valueOf(1L))
                        .param("itemId", String.valueOf(1L))
                )
                .andExpect(status().isOk())  //then
                .andExpect(jsonPath("$.size()").value(9))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nickname").value(nickname))
                .andExpect(jsonPath("$[0].itemName").value(itemName))
                .andExpect(jsonPath("$[0].startAt").value(formatStartAt))
                .andExpect(jsonPath("$[0].endAt").value(formatEndAt))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andDo(print());

    }
}