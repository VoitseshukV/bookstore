package com.bookstore.core.service;

import com.bookstore.core.dto.AddCartItemRequestDto;
import com.bookstore.core.dto.CartItemDto;
import com.bookstore.core.dto.UpdateCartItemRequestDto;
import com.bookstore.core.model.CartItem;

public interface CartItemService {
    CartItemDto addItem(String email, AddCartItemRequestDto requestDto);

    CartItemDto updateItem(String email, Long id, UpdateCartItemRequestDto requestDto);

    void checkAndDeleteById(String email, Long id);

    void delete(CartItem cartItem);
}
