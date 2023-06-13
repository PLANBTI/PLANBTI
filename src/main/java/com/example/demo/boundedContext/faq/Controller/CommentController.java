package com.example.demo.boundedContext.faq.Controller;

import com.example.demo.boundedContext.faq.Service.CommentService;
import com.example.demo.boundedContext.faq.Service.FaqService;
import com.example.demo.boundedContext.faq.entity.Comment;
import com.example.demo.boundedContext.faq.entity.Faq;
import com.example.demo.util.rq.Rq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Comment Controller",description = "Comment 생성, 수정, 삭제")
@RequiredArgsConstructor
@Controller
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final FaqService faqService;
    private final Rq rq;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentForm {
        @NotBlank
        private String content;
    }

    @Operation(summary = "comment 신청 페이지")
    @GetMapping("/create/{id}")
    public String create(Model model, @PathVariable Long id) {
        Faq faq = faqService.findByIdAndDeleteDateIsNull(id);
        model.addAttribute("faq", faq);
        return "comment/create";
    }

    @Operation(summary = "comment 생성",description = "comment 생성을 요청합니다.")
    @Parameter(name = "form",description = "추가할 comment 내용")
    @PostMapping("/create/{id}")
    public String create(@PathVariable Long id, @Valid CommentForm form) {
        Faq faq = faqService.findByIdAndDeleteDateIsNull(id);
        commentService.create(faq, form.getContent());
        return rq.redirectWithMsg("/faq/detail/%s".formatted(id), "코멘트를 추가하였습니다.");
    }

    @Operation(summary = "comment 수정 페이지")
    @GetMapping("/modify/{id}")
    public String modify(@PathVariable Long id, Model model) {
        Faq faq = faqService.findByIdAndDeleteDateIsNull(id);
        Comment comment = commentService.findByFaq(faq);
        model.addAttribute("faq", faq);
        model.addAttribute("comment", comment);
        return "comment/modify";
    }

    @Operation(summary = "comment 수정")
    @Parameter(name = "form",description = "변경할 comment 내용")
    @PostMapping("/modify/{id}")
    public String modify(@PathVariable Long id, @Valid CommentForm form) {
        Faq faq = faqService.findByIdAndDeleteDateIsNull(id);
        Comment comment = commentService.findByFaq(faq);
        String newContent = form.getContent();

        if(comment.getContent().equals(newContent)) rq.historyBack("수정된 내용이 없습니다.");

        commentService.modify(faq, comment, newContent);
        return rq.redirectWithMsg("/faq/detail/%s".formatted(id), "코멘트를 수정하였습니다.");
    }

    @Operation(summary = "comment 삭제")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        Comment comment = commentService.findById(id);
        Faq faq = comment.getFaq();
        commentService.delete(faq, comment);
        return rq.redirectWithMsg("/faq/detail/%s".formatted(id), "코멘트를 삭제하였습니다.");
    }

}