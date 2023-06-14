package com.example.demo.boundedContext.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ScheduleService {

    private final MbtiCacheService mbtiCacheService;

    @Scheduled(cron = "0 0 2 * * ?")
    @CacheEvict(value = "mbti",allEntries = true)
    public void deleteAllTest() {
    }

    @Async
    @Scheduled(cron = "1 0 2 * * ?")
    public void getAllTest() {

        String[] rawCategories = {"istj", "isfj", "infj", "intj", "istp", "isfp", "infp", "intp", "estp",
                "esfp", "enfp", "entp", "estj", "esfj", "enfj", "entj"};

        for (String rawCategory : rawCategories) {
            mbtiCacheService.getMbtiResult(rawCategory);
        }
    }

}
