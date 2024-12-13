package com.example.demo.controller;

import com.example.demo.dto.ReservationRequestDto;
import com.example.demo.dto.ReservationResponseDto;
import com.example.demo.enums.ReservationStatus;
import com.example.demo.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDto> createReservation(@RequestBody ReservationRequestDto reservationRequestDto) {
        ReservationResponseDto reservationResponseDto = reservationService.createReservation(reservationRequestDto.getItemId(),
                                            reservationRequestDto.getUserId(),
                                            reservationRequestDto.getStartAt(),
                                            reservationRequestDto.getEndAt());

        return ResponseEntity.status(HttpStatus.CREATED).body(reservationResponseDto);
    }

    @PatchMapping("/{id}/update-status")
    public ResponseEntity<ReservationResponseDto> updateReservation(@PathVariable Long id, @RequestParam ReservationStatus status) {
        ReservationResponseDto reservationResponseDto = reservationService.updateReservationStatus(id, status);

        return ResponseEntity.ok().body(reservationResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> findAll() {

        List<ReservationResponseDto> reservationResponseDtoList = reservationService.getReservations();

        return ResponseEntity.status(HttpStatus.OK).body(reservationResponseDtoList);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ReservationResponseDto>> searchAll(@RequestParam(required = false) Long userId,
                                                  @RequestParam(required = false) Long itemId) {

        List<ReservationResponseDto> reservationResponseDtoList = reservationService.searchAndConvertReservations(userId, itemId);

        return ResponseEntity.status(HttpStatus.OK).body(reservationResponseDtoList);
    }
}
