package com.bookstore.core.service.impl;

import com.bookstore.core.dto.AddCartItemRequestDto;
import com.bookstore.core.dto.UpdateCartItemRequestDto;
import com.bookstore.core.exception.EntityNotFoundException;
import com.bookstore.core.model.Book;
import com.bookstore.core.model.CartItem;
import com.bookstore.core.model.ShoppingCart;
import com.bookstore.core.repository.cart.CartItemRepository;
import com.bookstore.core.service.BookService;
import com.bookstore.core.service.CartItemService;
import com.bookstore.core.service.ShoppingCartService;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartService shoppingCartService;
    private final BookService bookService;

    @Override
    @Transactional
    public void addItem(String email, AddCartItemRequestDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartService.shoppingCartByEmail(email);
        CartItem cartItem = cartItemRepository.findCardItemByBookId(
                shoppingCart.getId(), requestDto.bookId());
        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + requestDto.quantity());
        } else {
            cartItem = new CartItem();
            cartItem.setShoppingCart(shoppingCart);
            Book book = bookService.getById(requestDto.bookId());
            cartItem.setBook(book);
            cartItem.setQuantity(requestDto.quantity());
        }
        cartItemRepository.save(cartItem);
    }

    @Override
    @Transactional
    public void updateItem(String email, Long id, UpdateCartItemRequestDto requestDto) {
        CartItem cartItem = getByIdAndEmail(id, email);
        cartItem.setQuantity(requestDto.quantity());
        cartItemRepository.save(cartItem);
    }

    @Override
    public void deleteItem(String email, Long id) {
        CartItem cartItem = getByIdAndEmail(id, email);
        cartItemRepository.delete(cartItem);
    }

    @Override
    public void delete(CartItem cartItem) {
        cartItemRepository.delete(cartItem);
    }

    private CartItem getByIdAndEmail(Long id, String email) {
        Optional<CartItem> optionalCartItem = cartItemRepository.findById(id);
        if (optionalCartItem.isEmpty() || !Objects.equals(
                optionalCartItem.get().getShoppingCart().getUser().getEmail(), email)
        ) {
            throw new EntityNotFoundException("Can't find cart entity with id " + id);
        }
        return optionalCartItem.get();
    }
}
