package com.example.demo.base.event.listen;

import com.example.demo.boundedContext.product.entity.Basket;
import com.example.demo.boundedContext.product.event.DeleteBasket;
import com.example.demo.boundedContext.product.repository.BasketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class BasketEventListener {

    private final BasketRepository basketRepository;

    @EventListener
    public void deleteBasket(DeleteBasket deleteBasket) {
        Long itemCount = deleteBasket.getItemCount();
        Long memberId = deleteBasket.getMemberId();

        Optional<Basket> basketOptional = basketRepository.findById(memberId);
        if (basketOptional.isEmpty())
            return;

        Basket basket = basketOptional.get();
        if (basket.getCount() == itemCount)
            basketRepository.deleteById(memberId);
    }
}
