package com.example.demo.base.event;

import com.example.demo.boundedContext.faq.Service.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class FaqEventListener {

    private final FaqService faqService;

    @EventListener
    public void listen(EventAfterCreateComment event) {
        faqService.whenAfterCreateComment(event.getFaq(), event.getComment());
    }

    @EventListener
    public void listen(EventAfterModifyComment event) {
        faqService.whenAfterModifyComment(event.getFaq(), event.getModifiedComment());
    }

    @EventListener
    public void listen(EventBeforeDeleteComment event) {
        faqService.whenBeforeDeleteComment(event.getFaq());
    }

}
