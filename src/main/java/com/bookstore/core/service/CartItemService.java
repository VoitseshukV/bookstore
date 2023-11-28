package com.bookstore.core.service;

import com.bookstore.core.dto.AddCartItemRequestDto;
import com.bookstore.core.dto.UpdateCartItemRequestDto;
import com.bookstore.core.model.CartItem;

public interface CartItemService {
    void addItem(String email, AddCartItemRequestDto requestDto);

    void updateItem(String email, Long id, UpdateCartItemRequestDto requestDto);

    void deleteItem(String email, Long id);

    void delete(CartItem cartItem);
}
