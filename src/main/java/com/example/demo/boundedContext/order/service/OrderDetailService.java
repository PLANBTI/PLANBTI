package com.example.demo.boundedContext.order.service;

import com.example.demo.base.exception.DataNotFoundException;
import com.example.demo.boundedContext.order.entity.OrderDetail;
import com.example.demo.boundedContext.order.repository.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Transactional
@Service
public class OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;

    public OrderDetail findOrderDetailById(Long orderId,Long orderItemId, Long memberId) {

        return orderDetailRepository.findByIdWithMemberId(orderId,orderItemId,memberId)
                .orElseThrow(() -> new DataNotFoundException("존재하지 않는 데이터입니다"));
    }
}
