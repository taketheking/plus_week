package com.example.demo.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {


    @Test
    @DisplayName("item 생성 테스트")
    @Transactional
    public void createItemTest() {
        // given
        String name = "item";
        String description = "item description";
        String status = "PENDING";

        User manager = new User(Role.USER, "a@naver.com", "kim", "1234");
        User owner = new User(Role.USER, "b@naver.com", "lee", "1234");

        // when
        Item item = new Item(name, description, manager, owner);


        // then
        assertNotNull(item);

        assertEquals(name, item.getName());
        assertEquals(description, item.getDescription());
        assertEquals(owner, item.getOwner());
        assertEquals(manager, item.getManager());

        assertNotNull(item.getStatus());

        assertEquals(status, item.getStatus());
    }
}