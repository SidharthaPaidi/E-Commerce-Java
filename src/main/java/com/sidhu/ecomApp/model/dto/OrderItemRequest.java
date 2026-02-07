package com.sidhu.ecomApp.model.dto;

public record OrderItemRequest(
        Integer productId,
        Integer quantity
) {}
