package com.example.demo.boundedContext.member.repository;

import com.example.demo.boundedContext.member.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
