package com.bookstore.core.service;

import com.bookstore.core.dto.ShoppingCartDto;
import com.bookstore.core.model.ShoppingCart;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart(String email);

    ShoppingCart shoppingCartByEmail(String email);
}
