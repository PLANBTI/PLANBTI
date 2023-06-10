package com.example.demo.boundedContext.order.repository;

import com.example.demo.boundedContext.order.entity.Order;
import com.example.demo.boundedContext.order.entity.OrderStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.example.demo.boundedContext.member.entity.QMember.member;
import static com.example.demo.boundedContext.order.entity.OrderStatus.COMPLETE;
import static com.example.demo.boundedContext.order.entity.QOrder.order;

@RequiredArgsConstructor
public class CustomOrderRepositoryImpl implements CustomOrderRepository {

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public Optional<Order> findOrderOneByStatus(Long id, OrderStatus status) {
        Order order1 = jpaQueryFactory.selectFrom(order)
                .where(isEqualMember(id), isEqualStatus(status))
                .orderBy(order.id.desc()).limit(1L).fetchOne();

        return Optional.ofNullable(order1);
    }

    @Override
    public Optional<Order> findOrderByOrderId(Long memberId, Long orderId) {

        Order findOrder = jpaQueryFactory.selectFrom(order)
                .join(order.member, member)
                .where(
                        order.id.eq(orderId)
                                .and(order.status.eq(COMPLETE))
                                .and(member.id.eq(memberId))
                ).fetchFirst();


        return Optional.ofNullable(findOrder);
    }

    private static BooleanExpression isEqualStatus(OrderStatus status) {
        return status != null ? order.status.eq(status) : null;
    }

    private static BooleanExpression isEqualMember(Long memberId) {
        return memberId != null ?order.member.id.eq(memberId) : null;
    }
}
