package com.bookstore.core.service;

import com.bookstore.core.dto.ShoppingCartDto;
import com.bookstore.core.model.ShoppingCart;
import com.bookstore.core.model.User;

public interface ShoppingCartService {
    ShoppingCartDto getByEmail(String email);

    ShoppingCart getByUser(User user);

    ShoppingCart shoppingCartByEmail(String email);
}
