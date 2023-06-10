package com.example.demo.boundedContext.product.repository;

import com.example.demo.boundedContext.product.dto.Basket;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasketRepository extends CrudRepository<Basket,Long> {
}
