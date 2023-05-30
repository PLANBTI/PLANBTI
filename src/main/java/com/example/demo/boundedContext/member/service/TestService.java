package com.example.demo.boundedContext.member.service;

import com.example.demo.base.exception.DataNotFoundException;
import com.example.demo.boundedContext.member.entity.Test;
import com.example.demo.boundedContext.member.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;

    public Test findById(Long id) {
        Optional<Test> test = testRepository.findById(id);
        if(test.isEmpty()) {
            throw new DataNotFoundException("존재하지 않는 테스트 기록입니다.");
        }
        return test.get();
    }

    public Test create(char result, String content) {
        Test test = Test
                .builder()
                .result(result)
                .content(content)
                .build();

        testRepository.save(test);
        return test;
    }

    // soft-delete
    public void delete(Test test) {
        Test test1 = test
                .toBuilder()
                .deleteDate(LocalDateTime.now())
                .build();
        testRepository.save(test1);
    }
}
