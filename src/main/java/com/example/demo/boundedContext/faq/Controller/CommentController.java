package com.example.demo.boundedContext.faq.Controller;

import com.example.demo.boundedContext.faq.Service.CommentService;
import com.example.demo.boundedContext.faq.Service.FaqService;
import com.example.demo.boundedContext.faq.entity.Comment;
import com.example.demo.boundedContext.faq.entity.Faq;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@PreAuthorize("hasAuthority(ADMIN)")
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final FaqService faqService;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentForm {
        @NotBlank
        private String content;
    }

    @GetMapping("/create")
    public String create() {
        return "comment/create";
    }

    @PostMapping("/create/{id}")
    public String create(@PathVariable Long faqId, @Valid CommentForm form) {
        Faq faq = faqService.findByIdAndDeleteDateIsNull(faqId);
        commentService.create(faq, form.getContent());
        return "redirect:/faq/faqList";
    }

    @GetMapping("/modify/{id}")
    public String modify(@PathVariable Long id, Model model) {
        Comment comment = commentService.findByIdAndDeleteDateIsNull(id);
        model.addAttribute("comment", comment);
        return "comment/modify";
    }

    @PostMapping("/modify/{id}")
    public String modify(@PathVariable Long id, @Valid CommentForm form) {
        Comment comment = commentService.findByIdAndDeleteDateIsNull(id);
        commentService.modify(comment, form.getContent());
        return "redirect:/faq/faqList";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        Comment comment = commentService.findByIdAndDeleteDateIsNull(id);
        Long faqId = comment.getFaq().getId();
        commentService.delete(comment);
        return "redirect:/faq/detail/%s".formatted(faqId);
    }

}
