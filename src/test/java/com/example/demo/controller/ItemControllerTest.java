package com.example.demo.controller;

import com.example.demo.dto.ItemRequestDto;
import com.example.demo.dto.ItemResponseDto;
import com.example.demo.dto.UserRequestDto;
import com.example.demo.entity.Item;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ItemService itemService;

    @Test
    void createItemTest() throws Exception {
        //given
        String itemName = "신라호텔";
        String description = "깨끗하고 고급진 호텔";

        String nickname = "kim";
        String email = "kim@gmail.com";
        String password = "1234";

        User owner = new User(Role.USER, email, nickname, password);
        User manager = new User(Role.USER, email, nickname, password);

        Item item = new Item(itemName, description, owner, manager);

        ItemRequestDto itemRequestDto = new ItemRequestDto(itemName, description, owner.getId(), manager.getId());

        ItemResponseDto itemResponseDto = ItemResponseDto.toDto(item);

        given(itemService.createItem(itemName, description, owner.getId(), manager.getId())).willReturn(itemResponseDto);

        MockHttpSession session = new MockHttpSession();

        //when
        mockMvc.perform(post("/items")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequestDto))
                )
                .andExpect(status().isCreated())  //then
                .andExpect(jsonPath("$.itemName").value(item.getName()))
                .andExpect(jsonPath("$.description").value(item.getDescription()))
                .andExpect(jsonPath("$.ownerNickName").value(item.getOwner().getNickname()))
                .andExpect(jsonPath("$.managerNickName").value(item.getManager().getNickname()));

    }

}