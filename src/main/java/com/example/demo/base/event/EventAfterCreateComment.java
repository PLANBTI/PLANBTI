package com.example.demo.base.event;

import com.example.demo.boundedContext.faq.entity.Comment;
import com.example.demo.boundedContext.faq.entity.Faq;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EventAfterCreateComment extends ApplicationEvent {

    private final Faq faq;
    private final Comment comment;


    public EventAfterCreateComment(Object source, Faq faq, Comment comment) {
        super(source);
        this.faq = faq;
        this.comment = comment;
    }
}
