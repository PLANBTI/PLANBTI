package com.example.demo.boundedContext.order.repository;

import com.example.demo.boundedContext.order.dto.OrderExchangeDto;
import com.example.demo.boundedContext.order.entity.OrderItemStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.demo.boundedContext.order.entity.QOrder.order;
import static com.example.demo.boundedContext.order.entity.QOrderDetail.orderDetail;
import static com.example.demo.boundedContext.product.entity.QProduct.product;

@RequiredArgsConstructor
@Transactional
@Repository
public class CustomOrderDetailRepositoryImpl implements CustomOrderDetailRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<OrderExchangeDto> findByOrderIdAndMemberId(Long orderId, Long orderItemId, Long memberId) {

        OrderExchangeDto orderExchangeDto = jpaQueryFactory.select(Projections.constructor(
                        OrderExchangeDto.class, orderDetail.id, product.name, orderDetail.count, product.price
                ))
                .from(order)
                .join(order.orderDetailList, orderDetail)
                .join(orderDetail.product, product)
                .where(equalOrderId(orderId),
                        equalMember(memberId),
                        equalOrderItemId(orderItemId),
                        orderDetail.status.eq(OrderItemStatus.PLACED))
                .fetchOne();

        return Optional.ofNullable(orderExchangeDto);
    }

    private static BooleanExpression equalOrderId(Long orderId) {
        return orderId != null ? order.id.eq(orderId) : null;
    }

    private BooleanExpression equalOrderItemId(Long orderItemId) {
        return orderItemId != null ? orderDetail.id.eq(orderItemId) : null;
    }

    private BooleanExpression equalMember(Long memberId) {
        return memberId != null ? order.member.id.eq(memberId) : null;
    }
}
