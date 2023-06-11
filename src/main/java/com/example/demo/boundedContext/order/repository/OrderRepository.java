package com.example.demo.boundedContext.order.repository;

import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.order.entity.Order;
import com.example.demo.boundedContext.order.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long>,CustomOrderRepository {

    List<Order> findByMember(Member member);

    Optional<Order> findByUuid(String uuid);

    @Query("select o from Order o where o.member.id = :id and o.status = 'BEFORE'")
    List<Order> findByMemberWhereStatusBefore(@Param("id") Long id);

}