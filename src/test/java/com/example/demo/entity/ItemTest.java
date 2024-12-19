package com.example.demo.entity;

import com.example.demo.config.JPAConfig;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.UserRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JPAConfig.class)
class ItemTest {


    @Test
    @DisplayName("item 생성 테스트")
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

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("item Repository 생성시 status 예외 테스트")
    @Transactional
    public void createItemRepositoryTest() {
        // given
        String name = "item";
        String description = "item description";

        User manager = new User(Role.USER, "a@naver.com", "kim", "1234");
        User owner = new User(Role.USER, "b@naver.com", "lee", "1234");

        userRepository.save(manager);
        userRepository.save(owner);

        // when
        Item item = new Item(name, description, manager, owner, null);

        // then
        assertThrows(ConstraintViolationException.class, () -> itemRepository.saveAndFlush(item));

    }
}