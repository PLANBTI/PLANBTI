package com.example.demo.boundedContext.product.repository;

import com.example.demo.boundedContext.product.dto.ReviewDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.demo.boundedContext.member.entity.QMember.member;
import static com.example.demo.boundedContext.product.entity.QProduct.product;
import static com.example.demo.boundedContext.product.entity.QReview.review;

@Repository
@RequiredArgsConstructor
public class CustomReviewRepositoryImpl implements CustomReviewRepository {

    private final static Long REVIEW_MAX_COUNT = 10L;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ReviewDto> findReviewByProductId(Long productId, Long offset) {


        return jpaQueryFactory.select(Projections.constructor(ReviewDto.class,
                        member.username,
                        review.title,
                        review.content,
                        review.image,
                        review.rate,
                        review.createDate
                ))
                .from(review)
                .leftJoin(review.member, member)
                .where(product.id.eq(productId))
                .offset(offset)
                .limit(REVIEW_MAX_COUNT)
                .fetch();

    }


}
