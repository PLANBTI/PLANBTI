package com.example.demo.boundedContext.member.service;

import com.example.demo.base.AppEnv;
import com.example.demo.base.exception.handler.DataNotFoundException;
import com.example.demo.boundedContext.member.entity.MbtiTest;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.repository.MbtiTestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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

    public boolean isTestExist(String memberUsername, String result, String title) {
        return mbtiTestRepository.findByMemberUsernameAndResultAndTitle(memberUsername, result, title).isPresent();
    }

    public boolean isMemberExist(String memberUsername) {
        return mbtiTestRepository.findAllByMemberUsername(memberUsername).isEmpty();
    }

    public List<MbtiTest> findAllTestsByMember(Member member) {
        return mbtiTestRepository.findAllByMemberUsername(member.getUsername());
    }

    public List<MbtiTest> createTestResult(Member member, String mbti, String plantName, String plantDescription) {
        // plantName과 plantDescription 디코드
        try {
            String decodedPlantName = URLDecoder.decode(plantName, "UTF-8");
            String decodedPlantDescription = URLDecoder.decode(plantDescription, "UTF-8");

            // 중복 항목 생성 방지
            if (!isTestExist(member.getUsername(), mbti, decodedPlantName)){
                create(member, mbti, decodedPlantName, decodedPlantDescription);
            }
            // 멤버의 테스트를 가져옴
            return findAllTestsByMember(member);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(); // 예외 발생 시 빈 리스트 반환
    }


    public MbtiTest create(Member member, String result, String title, String content) {
        MbtiTest test = MbtiTest
                .builder()
                .memberUsername(member.getUsername())
                .result(result)
                .title(title)
                .content(content)
                .testImgUrl(AppEnv.imageUrl+result.toLowerCase()+".jpg")
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
