package com.bookstore.core.controller;

import com.bookstore.core.dto.OrderDto;
import com.bookstore.core.dto.OrderItemDto;
import com.bookstore.core.dto.UpdateOrderDto;
import com.bookstore.core.service.OrderItemService;
import com.bookstore.core.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order endpoint", description = "Work with orders")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderItemService orderItemService;

    @Operation(summary = "Get orders",
            description = "Get current user's orders")
    @GetMapping
    public List<OrderDto> getOrders(
            Authentication authentication,
            @ParameterObject Pageable pageable
    ) {
        String email = authentication.getName();
        return orderService.getOrders(email, pageable);
    }

    @Operation(summary = "Add new order",
            description = "Add a new order based on the user's shopping cart")
    @PostMapping
    public void addOrder(Authentication authentication) {
        String email = authentication.getName();
        orderService.addOrder(email);
    }

    @Operation(summary = "Get order", description = "Get order by ID")
    @GetMapping("/{orderId}")
    public OrderDto getOrderById(
            Authentication authentication,
            @PathVariable Long orderId
    ) {
        String email = authentication.getName();
        return orderService.getOrderById(email, orderId);
    }

    @Operation(summary = "Get order items", description = "Get items by order ID")
    @GetMapping("/{orderId}/items")
    public List<OrderItemDto> getOrderItems(
            Authentication authentication,
            @PathVariable Long orderId
    ) {
        String email = authentication.getName();
        return orderItemService.getOrderItems(email, orderId);
    }

    @Operation(summary = "Get order item", description = "Get order item by ID")
    @GetMapping("/{orderId}/items/{itemId}")
    public OrderItemDto getOrderItemById(
            Authentication authentication,
            @PathVariable Long orderId,
            @PathVariable Long itemId
    ) {
        String email = authentication.getName();
        return orderItemService.getOrderItemById(email, orderId, itemId);
    }

    @Operation(summary = "Update order", description = "Update order by ID")
    @PatchMapping("/{orderId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public OrderDto updateOrderById(
            Authentication authentication,
            @PathVariable Long orderId,
            @RequestBody @Valid UpdateOrderDto requestDto
    ) {
        return orderService.updateOrderById(orderId, requestDto);
    }
}
