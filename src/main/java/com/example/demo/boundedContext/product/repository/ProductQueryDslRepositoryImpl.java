package com.example.demo.boundedContext.product.repository;

import com.example.demo.boundedContext.product.entity.Product;
import com.example.demo.boundedContext.product.entity.QProduct;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
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
    public Page<Product> findAllByCategoryName(String categoryName, String keyword, Pageable pageable) {

        OrderSpecifier[] orderBy = pageable.getSort().stream()
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

        JPAQuery<Product> contentQuery = jpaQueryFactory
                .select(product)
                .from(product)
                .where(
                        product.category.name.eq(categoryName),
                        eqName(keyword)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderBy);

        List<Product> content = contentQuery.fetch();

        JPAQuery<Integer> countQuery = jpaQueryFactory
                .select(product.count)
                .from(product)
                .where(product.category.name.eq(categoryName));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression eqName(String keyword) {
        if (keyword == null) return null;

        return product.name.contains(keyword);
    }
}
