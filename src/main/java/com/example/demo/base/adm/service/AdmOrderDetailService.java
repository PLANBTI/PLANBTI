package com.example.demo.base.adm.service;

import com.example.demo.base.exception.handler.DataNotFoundException;
import com.example.demo.boundedContext.order.entity.OrderDetail;
import com.example.demo.boundedContext.order.entity.OrderItemStatus;
import com.example.demo.boundedContext.order.repository.orderdetail.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.demo.boundedContext.order.entity.OrderItemStatus.*;

@Service
@RequiredArgsConstructor
public class AdmOrderDetailService {

    private final OrderDetailRepository orderDetailRepository;

    public OrderDetail findById(Long id) {
        return orderDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("존재하지 않는 데이터입니다."));
    }

    public List<OrderDetail> findAll() {
        return orderDetailRepository.findAll();
    }

    public List<OrderDetail> getStatusDelivering() {
        List<OrderDetail> list = orderDetailRepository.findAll();
        isDelivered();
        isDone();
        return list.stream()
                .filter(od -> od.getStatus().equals(PLACED) || od.getStatus().equals(SHIPPING)
                    || od.getStatus().equals(APPROVED) || od.getStatus().equals(RETURN))
                .toList();
    }

    public List<OrderDetail> getStatusIsCompleted() {
        return orderDetailRepository.findByStatus(COMPLETED);
    }

    public List<OrderDetail> getStatusIsInProgress() {
        return orderDetailRepository.findAll().stream()
                .filter(od -> od.getStatus().equals(EXCHANGE) || od.getStatus().equals(RETURN) ||
                        od.getStatus().equals(APPROVED))
                .toList();
    }

    public List<OrderDetail> getStatusIsDone() {
        isDone();
        return orderDetailRepository.findByStatus(DONE);
    }

    private void isDelivered() {
        // SHIPPING 상태인 동시에 배송 시작된 시점부터 7일이 지났다면 배송 완료 상태로 변경
        List<OrderDetail> orderDetails = orderDetailRepository.findByStatus(SHIPPING);
        orderDetails.stream().filter(od -> isPassed(od, 7)).forEach(od -> updateStatus(od, DELIVERED));
    }

    private void isDone() {
        // DELIVERED 상태인 동시에 배송 완료된 시점부터 7일이 지났다면 구매 확정 상태로 변경
        List<OrderDetail> orderDetails = orderDetailRepository.findByStatus(DELIVERED);
        orderDetails.stream().filter(od -> isPassed(od, 7)).forEach(od -> updateStatus(od, DONE));
    }

    private boolean isPassed(OrderDetail orderDetail, int day) {
        return LocalDateTime.now().isAfter(orderDetail.getModifyDate().plusDays(day));
    }

    public void updateStatus(OrderDetail orderDetail, OrderItemStatus status) {
        orderDetail.updateStatus(status);
        orderDetailRepository.save(orderDetail);
    }

    public void startDelivery(OrderDetail orderDetail, String invoiceNumber) {
        orderDetailRepository.save(orderDetail.toBuilder()
                .status(SHIPPING).invoiceNumber(invoiceNumber).build());
    }

    public List<OrderDetail> getMonthlyDone(int year, int month) {
        LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endDate = startDate.plusMonths(1);

        return orderDetailRepository.findByCreateDateBetween(startDate, endDate);
    }
}