package com.example.demo.boundedContext.member.service;

import com.example.demo.base.exception.handler.DataNotFoundException;
import com.example.demo.boundedContext.member.entity.MbtiTest;
import com.example.demo.boundedContext.member.repository.MbtiTestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestService {

    private final MbtiTestRepository mbtiTestRepository;

    public MbtiTest findById(Long id) {
        Optional<MbtiTest> test = mbtiTestRepository.findById(id);
        if(test.isEmpty()) {
            throw new DataNotFoundException("존재하지 않는 테스트 기록입니다.");
        }
        return test.get();
    }

    public MbtiTest findByIdAndDeleteDateIsNull(Long id) {
        Optional<MbtiTest> test = mbtiTestRepository.findByIdAndDeleteDateIsNull(id);
        if(test.isEmpty()) {
            throw new DataNotFoundException("존재하지 않는 테스트 기록입니다.");
        }
        return test.get();
    }

    public MbtiTest create(char result, String content) {
        MbtiTest test = MbtiTest
                .builder()
                .result(result)
                .content(content)
                .build();

        mbtiTestRepository.save(test);
        return test;
    }

    // soft-delete
    public void delete(MbtiTest test) {
        MbtiTest test1 = test
                .toBuilder()
                .deleteDate(LocalDateTime.now())
                .build();
        mbtiTestRepository.save(test1);
    }
}
