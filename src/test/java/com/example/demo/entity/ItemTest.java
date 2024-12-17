package com.example.demo.entity;

import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("item 생성 테스트")
    @Transactional
    public void createItemTest() {
        // given
        String name = "item";
        String description = "item description";
        String status = "PENDING";

        User manager = userRepository.findByIdOrElseThrow(1L);
        User owner = userRepository.findByIdOrElseThrow(1L);

        Item item = new Item(name, description, manager, owner);

        Item savedItem = itemRepository.save(item);


        // then
        assertNotNull(savedItem);

        assertEquals(name, savedItem.getName());
        assertEquals(description, savedItem.getDescription());
        assertEquals(owner, savedItem.getOwner());
        assertEquals(manager, savedItem.getManager());

        assertNotNull(savedItem.getStatus());

        assertEquals(status, savedItem.getStatus());
    }
}