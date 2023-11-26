package com.bookstore.core.repository.order;

import com.bookstore.core.model.Order;
import com.bookstore.core.model.OrderItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<OrderItem> findFirstByOrderAndId(Order order, Long itemId);
}
