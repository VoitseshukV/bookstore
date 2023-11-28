package com.bookstore.core.controller;

import com.bookstore.core.dto.AddCartItemRequestDto;
import com.bookstore.core.dto.ShoppingCartDto;
import com.bookstore.core.dto.UpdateCartItemRequestDto;
import com.bookstore.core.service.CartItemService;
import com.bookstore.core.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart endpoint", description = "Work with shopping cart")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final CartItemService cartItemService;

    @Operation(summary = "Get shopping cart",
            description = "Get shopping cart contents")
    @GetMapping
    public ShoppingCartDto getShoppingCart(Authentication authentication) {
        String email = authentication.getName();
        return shoppingCartService.getByEmail(email);
    }

    @Operation(summary = "Add book to shopping cart",
            description = "Add book to shopping cart")
    @PostMapping
    public void addCartItem(
            Authentication authentication,
            @RequestBody @Valid AddCartItemRequestDto requestDto
    ) {
        String email = authentication.getName();
        cartItemService.addItem(email, requestDto);
    }

    @Operation(summary = "Update cart item",
            description = "Update books quantity in shopping cart")
    @PutMapping("/items/{id}")
    public void updateCartItemById(
            Authentication authentication,
            @PathVariable Long id,
            @RequestBody @Valid UpdateCartItemRequestDto requestDto
    ) {
        String email = authentication.getName();
        cartItemService.updateItem(email, id, requestDto);
    }

    @Operation(summary = "Delete cart item",
            description = "Delete book from shopping cart")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/items/{id}")
    public void deleteCartItemById(Authentication authentication, @PathVariable Long id) {
        String email = authentication.getName();
        cartItemService.deleteItem(email, id);
    }
}
