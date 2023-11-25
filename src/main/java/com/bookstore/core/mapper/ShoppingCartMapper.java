package com.bookstore.core.mapper;

import com.bookstore.core.config.MapperConfig;
import com.bookstore.core.dto.ShoppingCartDto;
import com.bookstore.core.model.ShoppingCart;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "cartItems", qualifiedByName = "cartItemsToDtos")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    @AfterMapping
    default void setEmail(
            @MappingTarget ShoppingCartDto shoppingCartDto,
            ShoppingCart shoppingCart
    ) {
        shoppingCartDto.setEmail(shoppingCart.getUser().getEmail());
    }
}
