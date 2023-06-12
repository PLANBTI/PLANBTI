package com.example.demo.base.adm.service;

import com.example.demo.base.exception.handler.DataNotFoundException;
import com.example.demo.boundedContext.order.entity.OrderDetail;
import com.example.demo.boundedContext.order.entity.OrderItemStatus;
import com.example.demo.boundedContext.order.repository.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.demo.boundedContext.order.entity.OrderItemStatus.*;

@Service
@RequiredArgsConstructor
public class AdmOrderService {

    private final OrderDetailRepository orderDetailRepository;

    public List<OrderDetail> findAll() {
        return orderDetailRepository.findAll();
    }

    public List<OrderDetail> findByStatusIsNotPending() {
        List<OrderDetail> list = orderDetailRepository.findAll();
        isDelivered(list);
        isCompleted(list);
        return list.stream().filter(od -> !od.getStatus().equals(PENDING)).toList();
    }

    public List<OrderDetail> getPendingStatus() {
        return orderDetailRepository.findAll().stream()
                .filter(od -> od.getStatus().equals(PENDING)).toList();
    }

    public List<OrderDetail> getStatusInProgress() {
        return orderDetailRepository.findAll().stream()
                .filter(od -> od.getStatus().equals(EXCHANGE) || od.getStatus().equals(RETURN) ||
                        od.getStatus().equals(APPROVED))
                .toList();
    }

    public List<OrderDetail> getCompletedStatus() {
        List<OrderDetail> list = orderDetailRepository.findAll();
        isCompleted(list);
        return list.stream().filter(od -> od.getStatus().equals(COMPLETED)).toList();
    }

    public OrderDetail findById(Long id) {
        return orderDetailRepository.findById(id).orElseThrow(() -> new DataNotFoundException("존재하지 않는 데이터입니다."));
    }

    public void isDelivered(List<OrderDetail> list) {
        // SHIPPING 상태인 동시에 배송 시작된 시점부터 7일이 지났다면 배송 완료 상태로 변경
        list.stream().filter(od -> od.isEqualStatusTo(SHIPPING) && isPassed(od, 7))
                .forEach(od -> updateStatus(od, DELIVERED));
    }

    public void isCompleted(List<OrderDetail> list) {
        // DELIVERED 상태인 동시에 배송 완료된 시점부터 7일이 지났다면 구매 확정 상태로 변경
        list.stream().filter(od -> od.isEqualStatusTo(DELIVERED) && isPassed(od, 7))
                .forEach(od -> updateStatus(od, COMPLETED));
    }

    private boolean isPassed(OrderDetail orderDetail, int day) {
        return LocalDateTime.now().isAfter(orderDetail.getModifyDate().plusDays(day));
    }

    public void updateStatus(OrderDetail orderDetail, OrderItemStatus status) {
        orderDetail.updateStatus(status);
        orderDetailRepository.save(orderDetail);
    }

    public void startDelivery(OrderDetail orderDetail, String invoiceNumber) {
        OrderDetail orderDetail1 = orderDetail.toBuilder().status(SHIPPING).invoiceNumber(invoiceNumber).build();
        orderDetailRepository.save(orderDetail1);
    }
}