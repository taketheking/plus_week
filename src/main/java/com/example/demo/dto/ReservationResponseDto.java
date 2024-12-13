package com.example.demo.dto;

import com.example.demo.entity.Reservation;
import com.example.demo.enums.ReservationStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReservationResponseDto {
    private final Long id;
    private final String nickname;
    private final String itemName;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;
    private final ReservationStatus status;

    public ReservationResponseDto(Long id, String nickname, String itemName, LocalDateTime startAt, LocalDateTime endAt, ReservationStatus status) {
        this.id = id;
        this.nickname = nickname;
        this.itemName = itemName;
        this.startAt = startAt;
        this.endAt = endAt;
        this.status = status;
    }

    public static ReservationResponseDto toDto(Reservation reservation) {
        return new ReservationResponseDto(
                reservation.getId(),
                reservation.getUser().getNickname(),
                reservation.getItem().getName(),
                reservation.getStartAt(),
                reservation.getEndAt(),
                reservation.getStatus()
        );
    }
}
