package com.example.demo.base.adm.service;

import com.example.demo.boundedContext.order.entity.OrderDetail;
import com.example.demo.boundedContext.order.entity.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.boundedContext.order.entity.OrderItemStatus.SHIPPING;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
public class AdmOrderDetailServiceTest {

    @Autowired
    private AdmOrderDetailService admOrderDetailService;

    @Test
    @DisplayName("findByStatusIsNotPending()")
    void t001() {
        assertThat(admOrderDetailService.findByStatusIsNotPending().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("getStatusInProgress()")
    void t002() {
        assertThat(admOrderDetailService.getStatusInProgress().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("getPendingStatus() & startDelivery()")
    void t003() {
        List<OrderDetail> orderDetails = admOrderDetailService.getPendingStatus();
        OrderDetail orderDetail = orderDetails.get(0);

        admOrderDetailService.startDelivery(orderDetail, "0123456789");

        assertThat(orderDetail.getStatus()).isEqualTo(SHIPPING);
        assertThat(orderDetail.getInvoiceNumber()).isNotNull();
    }

    @Test
    @DisplayName("getMonthlyCompleted()")
    void t004() {
        List<OrderDetail> orderDetails = admOrderDetailService.getMonthlyCompleted(2023, 6);

        assertThat(orderDetails.size()).isEqualTo(5);

        orderDetails = orderDetails.stream().filter(od -> od.getOrder().getStatus().equals(OrderStatus.COMPLETE)).toList();

        assertThat(orderDetails.size()).isEqualTo(5);

        assertThat(orderDetails.stream()
                .filter(od -> od.getProduct().getCategory().getName().equals("infj")).count()).isEqualTo(4);
    }

}