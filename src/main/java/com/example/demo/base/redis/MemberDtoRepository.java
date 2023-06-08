package com.example.demo.base.redis;

import com.example.demo.boundedContext.member.dto.MemberDto;
import org.springframework.data.repository.CrudRepository;

public interface MemberDtoRepository extends CrudRepository<MemberDto,Long> {
    MemberDto findByUsername(String username);
}
