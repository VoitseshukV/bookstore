package com.bookstore.core.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Data
@Table(name = "shopping_cart_items")
@SQLDelete(sql = "UPDATE shopping_cart_items SET is_deleted = TRUE WHERE id=?")
@Where(clause = "is_deleted = FALSE")
@Accessors(chain = true)
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "shopping_cart_id")
    private ShoppingCart shoppingCart;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
    @NotNull
    private Integer quantity;
    @Column(name = "is_deleted")
    @NotNull
    private boolean isDeleted = false;
}
