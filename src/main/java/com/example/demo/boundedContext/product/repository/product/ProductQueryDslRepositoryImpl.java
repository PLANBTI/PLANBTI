package com.example.demo.boundedContext.product.repository.product;

import com.example.demo.base.entity.QBaseEntity;
import com.example.demo.boundedContext.product.entity.Product;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.demo.boundedContext.product.entity.QProduct.product;

@Repository
@RequiredArgsConstructor
public class ProductQueryDslRepositoryImpl implements ProductQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Product> findAllByCategoryNameAndKeyword(String categoryName, String keyword, Pageable pageable) {

        JPAQuery<Product> contentQuery = jpaQueryFactory
                .select(product)
                .from(product)
                .where(
                        eqCategoryName(categoryName),
                        eqName(keyword),product.deleteDate.isNull()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable.getSort()));

        List<Product> content = contentQuery.fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(product.count())
                .from(product)
                .where(
                        eqCategoryName(categoryName),
                        eqName(keyword)
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Product> findAllForPaging(Pageable pageable) {
        JPAQuery<Product> contentQuery = jpaQueryFactory
                .select(product)
                .from(product)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable.getSort()));

        List<Product> content = contentQuery.fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(product.count())
                .from(product);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression eqCategoryName(String categoryName) {
        if (categoryName == null) return null;
        return product.category.name.eq(categoryName);
    }


    private OrderSpecifier[] getOrderSpecifiers(Sort sort) {
        OrderSpecifier[] orderSpecifiers = sort.stream()
                .map(o -> {
                    Order order = o.getDirection().equals(Sort.Direction.ASC) ? Order.ASC : Order.DESC;
                    String property = o.getProperty();

                    Expression expression = property == null ? null : switch (property) {
                        case "created" -> product.id;
                        default -> product.id;
                    };

                    return new OrderSpecifier<>(order, expression);
                })
                .toArray(OrderSpecifier[]::new);

        return  orderSpecifiers;
    }


    private BooleanExpression eqName(String keyword) {
        if (keyword == null) return null;
        if (keyword.trim().isBlank()) return null;

        return product.name.contains(keyword);
    }



}
