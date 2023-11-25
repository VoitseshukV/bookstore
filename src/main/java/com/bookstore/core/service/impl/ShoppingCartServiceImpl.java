package com.bookstore.core.service.impl;

import com.bookstore.core.dto.ShoppingCartDto;
import com.bookstore.core.exception.EntityNotFoundException;
import com.bookstore.core.mapper.ShoppingCartMapper;
import com.bookstore.core.model.ShoppingCart;
import com.bookstore.core.model.User;
import com.bookstore.core.repository.cart.ShoppingCartRepository;
import com.bookstore.core.repository.user.UserRepository;
import com.bookstore.core.service.ShoppingCartService;
import java.util.HashSet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ShoppingCartDto getShoppingCart(String email) {
        return shoppingCartMapper.toDto(shoppingCartByEmail(email));
    }

    @Override
    public ShoppingCart shoppingCartByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Can't get user with email: " + email));
        ShoppingCart shoppingCart = shoppingCartRepository.getByUser(user);
        if (shoppingCart == null) {
            // Create new shopping cart
            shoppingCart = new ShoppingCart();
            shoppingCart.setUser(user);
            shoppingCart.setCartItems(new HashSet<>());
            shoppingCart = shoppingCartRepository.save(shoppingCart);
        }
        return shoppingCart;
    }
}
