package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.status = :status where u.id IN :userIds")
    void updateStatusByIds(@Param("status") String status, @Param("userIds") List<Long> userIds);
}
