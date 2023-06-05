package com.example.demo.boundedContext.product.entity;

import com.example.demo.base.entity.BaseEntity;
import com.example.demo.base.exception.OrderException;
import com.example.demo.boundedContext.category.entity.Category;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE product SET delete_date = NOW() WHERE id = ?")
@SuperBuilder(toBuilder = true)
@Entity
public class Product extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;
    private String name;

    @Column(length = 1000)
    private String content;
    private int price;
    private int salePrice;
    private int count;

    @Version
    private Long version;

    public void updateProductCount(int purchase) {
        if (!isEnoughCount(purchase))
            throw new OrderException("재고가 부족합니다");
        this.count -= purchase;
    }

    public boolean isEnoughCount(int count) {
        return this.count - count >= 0;
    }

    public void addCount(int count) {
        this.count +=count;
    }
}
