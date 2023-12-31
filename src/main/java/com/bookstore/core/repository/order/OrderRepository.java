package com.bookstore.core.repository.order;

import com.bookstore.core.model.Order;
import com.bookstore.core.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUser(User user, Pageable pageable);

    Optional<Order> findByUserAndId(User user, Long id);
}
