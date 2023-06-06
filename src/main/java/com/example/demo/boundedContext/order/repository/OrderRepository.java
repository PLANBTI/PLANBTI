package com.example.demo.boundedContext.order.repository;

import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long>,CustomOrderRepository {

    List<Order> findByMember(Member member);

    Optional<Order> findByUuid(String uuid);
}
