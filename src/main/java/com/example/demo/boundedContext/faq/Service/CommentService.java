package com.example.demo.boundedContext.faq.Service;

import com.example.demo.base.exception.DataNotFoundException;
import com.example.demo.boundedContext.faq.entity.Comment;
import com.example.demo.boundedContext.faq.entity.Faq;
import com.example.demo.boundedContext.faq.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment findById(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new DataNotFoundException("comment not found"));
    }

    public Comment findByIdAndDeleteDateIsNull(Long id) {
        return commentRepository.findByIdAndDeleteDateIsNull(id).orElseThrow(() -> new DataNotFoundException("comment not found"));
    }

    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    public Comment create(Faq faq, String content) {
        Comment comment = Comment.builder()
                .faq(faq)
                .content(content).build();
        commentRepository.save(comment);
        return comment;
    }

    public Comment modify(Comment comment, String content) {
        Comment modifiedComment = comment.toBuilder()
                .content(content).build();
        commentRepository.save(modifiedComment);
        return modifiedComment;
    }

    // hard-delete
    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }

}
