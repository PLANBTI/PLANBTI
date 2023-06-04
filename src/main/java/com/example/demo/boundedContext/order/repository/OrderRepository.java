package com.example.demo.boundedContext.order.repository;

import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {

    @Query("select o from Order o join o.member m where m.id = :id and" +
            " o.status = 'BEFORE' order by o.id desc limit 1")
    Optional<Order> findLastOrder(@Param("id") Long id);

    @Query("select o from Order o join o.member m where m.id = :id and" +
            " o.status = 'COMPLETE' order by o.id desc limit 1")
    Optional<Order> findCompleteLastOrder(@Param("id") Long id);

    List<Order> findByMember(Member member);

    Optional<Order> findByUuid(String uuid);
}
