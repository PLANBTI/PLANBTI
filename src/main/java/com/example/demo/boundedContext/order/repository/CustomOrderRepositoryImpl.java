package com.example.demo.boundedContext.order.repository;

import com.example.demo.boundedContext.order.entity.Order;
import com.example.demo.boundedContext.order.entity.OrderStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.example.demo.boundedContext.order.entity.QOrder.order;

@RequiredArgsConstructor
public class CustomOrderRepositoryImpl implements CustomOrderRepository {

    private final JPAQueryFactory jpaQueryFactory;
    public Optional<Order> findCompleteOrderOneByStatus(Long id, OrderStatus status) {
        Order order1 = jpaQueryFactory.selectFrom(order)
                .where(order.member.id.eq(id))
                .where(order.status.eq(status))
                .orderBy(order.id.desc()).limit(1L).fetchOne();

        return Optional.ofNullable(order1);
    }
}
