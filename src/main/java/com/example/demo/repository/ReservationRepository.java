package com.example.demo.repository;

import com.example.demo.dto.ReservationResponseDto;
import com.example.demo.entity.Reservation;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RepositoryDefinition(domainClass = Reservation.class, idClass = Integer.class)
public interface ReservationRepository extends JpaRepository<Reservation, Long>, ReservationRepositoryQuery {
//
//    List<Reservation> findByUserIdAndItemId(Long userId, Long itemId);
//
//    List<Reservation> findByUserId(Long userId);
//
//    List<Reservation> findByItemId(Long itemId);

    @Query("SELECT r FROM Reservation r " +
            "WHERE r.item.id = :id " +
            "AND NOT (r.endAt <= :startAt OR r.startAt >= :endAt) " +
            "AND r.status = 'APPROVED'")
    List<Reservation> findConflictingReservations(
            @Param("id") Long id,
            @Param("startAt") LocalDateTime startAt,
            @Param("endAt") LocalDateTime endAt
    );

    // N+1 문제 - 방법 1
    @Override
    @EntityGraph(attributePaths = {"user", "item"})
    List<Reservation> findAll();

    // N+1 문제 - 방법 2
    // +1 은 여전히 실행됨 -> item 에서 user 를 조회하기 때문에 발생
    // item 엔티티에서 owner 와 manager 에 Lazy Loading 설정해서 해결
    @Query("SELECT r FROM Reservation r JOIN FETCH r.item JOIN FETCH r.user ")
    List<Reservation> findAllWithFetch();


}
