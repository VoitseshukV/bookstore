package com.bookstore.core.repository.cart;

import com.bookstore.core.model.ShoppingCart;
import com.bookstore.core.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    ShoppingCart getByUser(User user);
}
