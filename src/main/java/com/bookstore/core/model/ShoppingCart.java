package com.bookstore.core.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.Set;

@Entity
@Data
@Table(name = "shopping_carts")
public class ShoppingCart {
    @Id
    private Long id;
    @NotNull
    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name="shopping_cart_id")
    private Set<CartItem> cartItems;
}
