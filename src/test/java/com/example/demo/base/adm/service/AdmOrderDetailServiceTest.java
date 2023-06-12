package com.example.demo.base.adm.service;

import com.example.demo.boundedContext.order.entity.OrderDetail;

import com.example.demo.boundedContext.order.repository.orderdetail.OrderDetailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.boundedContext.order.entity.OrderItemStatus.*;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
public class AdmOrderDetailServiceTest {

    @Autowired
    private AdmOrderDetailService admOrderDetailService;
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @BeforeEach
    void beforeEach() {
        OrderDetail orderDetail1 = OrderDetail.builder()
                .status(PENDING).build();
        orderDetailRepository.save(orderDetail1);

        OrderDetail orderDetail2 = OrderDetail.builder()
                .status(EXCHANGE).build();
        orderDetailRepository.save(orderDetail2);

        OrderDetail orderDetail3 = OrderDetail.builder()
                .status(RETURN).build();
        orderDetailRepository.save(orderDetail3);

        OrderDetail orderDetail4 = OrderDetail.builder()
                .status(APPROVED).build();
        orderDetailRepository.save(orderDetail4);

        OrderDetail orderDetail5 = OrderDetail.builder()
                .status(COMPLETED).build();
        orderDetailRepository.save(orderDetail5);
    }

    @Test
    @DisplayName("findByStatusIsNotPending()")
    void t001() {
        assertThat(admOrderDetailService.findByStatusIsNotPending().size()).isEqualTo(7);
    }

    @Test
    @DisplayName("getStatusInProgress()")
    void t002() {
        assertThat(admOrderDetailService.getStatusInProgress().size()).isEqualTo(5);
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
}
