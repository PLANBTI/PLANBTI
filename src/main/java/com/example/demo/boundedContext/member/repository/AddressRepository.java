package com.example.demo.boundedContext.member.repository;

import com.example.demo.boundedContext.member.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByIdAndDeleteDateIsNull(Long id);
}
