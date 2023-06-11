package com.example.demo.base.adm.service;

import com.example.demo.boundedContext.order.entity.OrderDetail;
import com.example.demo.boundedContext.order.repository.OrderDetailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.boundedContext.order.entity.OrderItemStatus.*;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
public class AdmOrderServiceTest {

    @Autowired
    private AdmOrderService admOrderService;
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
<<<<<<< HEAD
        assertThat(admOrderService.findByStatusIsNotPending().size()).isEqualTo(7);
=======
        assertThat(admOrderService.findByStatusIsNotPending().size()).isEqualTo(4);
>>>>>>> 24bcc9e (test: 관리자 컨트롤러/서비스 유효성 테스트)
    }

    @Test
    @DisplayName("getStatusInProgress()")
    void t002() {
<<<<<<< HEAD
        assertThat(admOrderService.getStatusInProgress().size()).isEqualTo(5);
    }
}
=======
        assertThat(admOrderService.getStatusInProgress().size()).isEqualTo(3);
    }
}
>>>>>>> 24bcc9e (test: 관리자 컨트롤러/서비스 유효성 테스트)
