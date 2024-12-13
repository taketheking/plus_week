package com.example.demo.dto;

import com.example.demo.entity.Reservation;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReservationResponseDto {
    private Long id;
    private String nickname;
    private String itemName;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    public ReservationResponseDto(Long id, String nickname, String itemName, LocalDateTime startAt, LocalDateTime endAt) {
        this.id = id;
        this.nickname = nickname;
        this.itemName = itemName;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public static ReservationResponseDto toDto(Reservation reservation) {
        return new ReservationResponseDto(
                reservation.getId(),
                reservation.getUser().getNickname(),
                reservation.getItem().getName(),
                reservation.getStartAt(),
                reservation.getEndAt()
        );
    }
}
