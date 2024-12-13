package com.example.demo.repository;

import com.example.demo.entity.User;
import com.example.demo.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    default User findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException("해당 ID에 맞는 값이 존재하지 않습니다."));
    }

    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.status = :status where u.id IN :userIds")
    int updateStatusByIds(@Param("status") UserStatus status, @Param("userIds") List<Long> userIds);
}
