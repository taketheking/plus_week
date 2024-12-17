package com.example.demo.entity;

import com.example.demo.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@DynamicUpdate
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String nickname;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserStatus status; // NORMAL, BLOCKED

    @Enumerated(value = EnumType.STRING)
    private Role role = Role.USER;

    public User(Role role, String email, String nickname, String password) {
        this.role = role;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.status = UserStatus.NORMAL;
    }

    public User() {}

    public void updateStatusToBlocked() {
        this.status = UserStatus.BLOCKED;
    }
}
