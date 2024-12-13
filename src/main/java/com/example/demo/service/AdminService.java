package com.example.demo.service;

import com.example.demo.enums.UserStatus;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminService {
    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // TODO: 4. find or save 예제 개선
    @Transactional
    public int reportUsers(List<Long> userIds) {

//        List<User> users = userRepository.findAllById(userIds).stream().toList();
//
//        if(users.isEmpty()) {
//            throw new IllegalArgumentException("해당 ID에 맞는 값이 존재하지 않습니다.");
//        }

        // 위의 user 조회가 없어도 동작함
        // 해당 id의 레코드가 없어도 실행됨
        return userRepository.updateStatusByIds(UserStatus.BLOCKED, userIds);
    }
}
