package com.example.demo.base.event;

import com.example.demo.boundedContext.faq.entity.Comment;
import com.example.demo.boundedContext.faq.entity.Faq;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EventAfterModifyComment extends ApplicationEvent {

    private final Faq faq;
    private final Comment modifiedComment;


    public EventAfterModifyComment(Object source, Faq faq, Comment modifiedComment) {
        super(source);
        this.faq = faq;
        this.modifiedComment = modifiedComment;
    }
}
