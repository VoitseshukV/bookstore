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
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@Table(name = "shopping_carts")
@Accessors(chain = true)
public class ShoppingCart {
    @Id
    private Long id;
    @NotNull
    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "shoppingCart")
    private Set<CartItem> cartItems;
}
