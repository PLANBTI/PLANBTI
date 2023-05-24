package com.example.demo.boundedContext.faq.entity;

import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.base.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
public class Faq extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member member;

    private String category;
    private String title;
    private String content;
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    private Comment comment;

    @Column(nullable = false)
    private String name;

}
