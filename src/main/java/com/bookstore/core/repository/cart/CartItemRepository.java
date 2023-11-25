package com.bookstore.core.repository.cart;

import com.bookstore.core.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query(value = "SELECT ci FROM CartItem ci "
            + "WHERE ci.shoppingCart.id = :shoppingCartId AND ci.book.id = :bookId")
    CartItem findCardItemByBookId(Long shoppingCartId, Long bookId);
}
