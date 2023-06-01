package com.example.demo.boundedContext.order.service;

import com.example.demo.base.exception.OrderException;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.repository.MemberRepository;
import com.example.demo.boundedContext.order.dto.OrderRequest;
import com.example.demo.boundedContext.order.entity.Order;
import com.example.demo.boundedContext.order.entity.OrderDetail;
import com.example.demo.boundedContext.order.infra.TossPaymentInfra;
import com.example.demo.boundedContext.order.repository.OrderDetailRepository;
import com.example.demo.boundedContext.order.repository.OrderRepository;
import com.example.demo.boundedContext.product.entity.Product;
import com.example.demo.boundedContext.product.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class OrderServiceTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderService orderService;

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ProductRepository productRepository;

    @MockBean
    TossPaymentInfra tossPaymentInfra;

    Member member;
    Order order;
    Product product;
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private PlatformTransactionManager tm;
    @BeforeEach
    void setUp() {

        transaction = new TransactionTemplate(tm);
        transaction.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        member = Member.builder().build();
        memberRepository.save(member);

        product = productRepository.save(Product.builder().count(100)
                .price(1000)
                .build());
        Product product2 = productRepository.save(Product.builder().count(100)
                .price(2000)
                .build());

        order = orderRepository.save(Order.builder()
                .member(member)
                .build());

        OrderDetail orderDetail1 = OrderDetail.builder().count(2).build();
        orderDetail1.addOrder(order, product);
        orderDetailRepository.save(orderDetail1);


        OrderDetail orderDetail2 = OrderDetail.builder().count(3).build();
        orderDetail2.addOrder(order, product2);
        orderDetailRepository.save(orderDetail2);
    }

    @DisplayName("주문 검증 성공 테스트")
    @Test
    void verifyOrderSuccess() {

        OrderRequest orderRequest = new OrderRequest("1__2__3", order.getUuid(), 8000L);
        Mockito.when(tossPaymentInfra.requestPermitToss(orderRequest)).thenReturn(ResponseEntity.ok("good"));

        orderService.verifyAndRequestToss(orderRequest, member.getId());
        assertThat(order.getTotalPrice()).isEqualTo(8000L);

    }

    @DisplayName("총합이 비교 실패 테스트")
    @Test
    void verifyOrderFail() {

        OrderRequest orderRequest = new OrderRequest("1__2__3", order.getUuid(), 1000L);

        Mockito.when(tossPaymentInfra.requestPermitToss(orderRequest)).thenReturn(ResponseEntity.ok("good"));

        Assertions.assertThatThrownBy(() -> orderService.verifyAndRequestToss(orderRequest, member.getId()))
                .isInstanceOf(OrderException.class);
    }

    @DisplayName("이미 계산한 경우 실패")
    @Test
    void verifyOrderStatusFail() {

        OrderRequest orderRequest = new OrderRequest("1__2__3", order.getUuid(), 2000L);

        Mockito.when(tossPaymentInfra.requestPermitToss(orderRequest)).thenReturn(ResponseEntity.ok("good"));

        assertThatThrownBy(() -> orderService.verifyAndRequestToss(orderRequest, 2L))
                .isInstanceOf(OrderException.class);
    }

    @DisplayName("주문시 자기 소유의 order가 아닌 경우 실패")
    @Test
    void noAuthorityForOrder() {


        OrderRequest orderRequest = new OrderRequest("1__2__3", "12123", 1000L);

        Mockito.when(tossPaymentInfra.requestPermitToss(orderRequest)).thenReturn(ResponseEntity.ok("good"));

        assertThatThrownBy(() -> orderService.verifyAndRequestToss(orderRequest, 2L))
                .isInstanceOf(OrderException.class);
    }

    @DisplayName("주문시 재고가 부족한 경우 실패")
    @Test
    void notEnoughProduct() {

    }



    private TransactionTemplate transaction;
    @DisplayName("동시성 테스트")
    @Disabled
    @Test
    void concurrentTest() throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Product product = Product.builder().count(100)
                .price(1000)
                .build();
        transaction.execute(status -> productRepository.save(product));
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Member member1 = Member.builder().build();
            transaction.execute((status) ->  memberRepository.saveAndFlush(member1));
            Order order1 = Order.builder()
                    .member(member1)
                    .build();
            transaction.execute((status) -> orderRepository.saveAndFlush(order1));


           Member findMember = memberRepository.findById(member1.getId()).orElseThrow();

            OrderDetail orderDetail1 = OrderDetail.builder().count(2).build();
            orderDetail1.addOrder(order1, product);
            transaction.execute((status) -> orderDetailRepository.saveAndFlush(orderDetail1));

            list.add(order1.getUuid());
        }

        CountDownLatch latch1 = new CountDownLatch(5);
        for (int i = 0; i < 5; i++) {
            String s1 = list.get(i);
            executorService.execute(() -> {
                transaction.execute(status -> {
                    orderService.orderPayComplete(new OrderRequest("1212", s1, 1000L));
                    latch1.countDown();
                    return null;
                });
            });
        }
        latch1.await(2, TimeUnit.SECONDS);
        Thread.sleep(500);
        transaction.execute(status -> {
            Product product1 = productRepository.findById(product.getId()).get();
            System.out.println("product1.getCount() = " + product.getCount());
            return null;
        });


    }

}