package com.example.demo.base.event;

import com.example.demo.boundedContext.faq.entity.Faq;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EventBeforeDeleteComment extends ApplicationEvent {

    private final Faq faq;

    public EventBeforeDeleteComment(Object source, Faq faq) {
        super(source);
        this.faq = faq;
    }
}
