package com.bookstore.core.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @NotNull
    private OrderStatus status;
    @NotNull
    private BigDecimal total;
    @NotNull
    private LocalDateTime orderDate;
    @NotNull
    private String shippingAddress;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "order")
    private Set<OrderItem> orderItems;
    @Column(name = "is_deleted")
    @NotNull
    private boolean isDeleted = false;

    public enum OrderStatus {
        CREATED,
        PAID,
        SENT,
        DELIVERED,
        COMPLETED
    }
}
