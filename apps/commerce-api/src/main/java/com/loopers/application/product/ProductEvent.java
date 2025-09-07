package com.loopers.application.product;

public class ProductEvent {

    public record ViewEvent(Long productId, String loginId) {
    }
}
