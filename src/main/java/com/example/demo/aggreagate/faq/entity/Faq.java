package com.example.demo.aggreagate.faq.entity;

import com.example.demo.aggreagate.member.entity.Member;
import com.example.demo.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
public class Faq extends BaseEntity {

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
