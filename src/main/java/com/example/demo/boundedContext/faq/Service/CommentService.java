package com.example.demo.boundedContext.faq.Service;

import com.example.demo.base.event.EventAfterCreateComment;
import com.example.demo.base.event.EventAfterModifyComment;
import com.example.demo.base.event.EventBeforeDeleteComment;
import com.example.demo.base.exception.handler.DataNotFoundException;
import com.example.demo.boundedContext.faq.entity.Comment;
import com.example.demo.boundedContext.faq.entity.Faq;
import com.example.demo.boundedContext.faq.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ApplicationEventPublisher publisher;

    public Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("존재하지 않는 코멘트입니다."));
    }

    public Comment findByFaq(Faq faq) {
        return commentRepository.findByFaq(faq).orElse(null);
    }

    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    public Comment create(Faq faq, String content) {
        Comment comment = Comment.builder()
                .faq(faq)
                .content(content).build();
        commentRepository.save(comment);

        publisher.publishEvent(new EventAfterCreateComment(this, faq, comment));
        return comment;
    }

    public Comment modify(Faq faq, Comment comment, String content) {
        Comment modifiedComment = comment.toBuilder()
                .content(content).build();
        commentRepository.save(modifiedComment);

        publisher.publishEvent(new EventAfterModifyComment(this, faq, modifiedComment));
        return modifiedComment;
    }

    // hard-delete
    public void delete(Faq faq, Comment comment) {
        publisher.publishEvent(new EventBeforeDeleteComment(this, faq));
        commentRepository.delete(comment);
    }

}
