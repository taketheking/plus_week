package com.example.demo.repository;

import com.example.demo.entity.Reservation;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;

import static com.example.demo.entity.QReservation.reservation;

public class ReservationRepositoryQueryImpl implements ReservationRepositoryQuery {

    private final JPAQueryFactory queryFactory;

    public ReservationRepositoryQueryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }


    @Override
    public List<Reservation> findReservationByUserIdAndItemId(Long userId, Long itemId) {
        return queryFactory.selectFrom(reservation)
                .leftJoin(reservation.user)
                .fetchJoin()
                .leftJoin(reservation.item)
                .fetchJoin()
                .where(userIdEq(userId),
                        itemIdEq(itemId)
                )
                .fetch();
    }


    private BooleanExpression userIdEq(Long userId) {
        return userId != null ? reservation.user.id.eq(userId) : null;
    }

    private BooleanExpression itemIdEq(Long itemId) {
        return itemId != null ? reservation.item.id.eq(itemId) : null;
    }
}
