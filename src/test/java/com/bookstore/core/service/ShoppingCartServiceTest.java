package com.bookstore.core.service;

import static com.bookstore.core.util.TestDataFactory.getShoppingCartDtoTemplate;
import static com.bookstore.core.util.TestDataFactory.getShoppingCartTemplate;
import static com.bookstore.core.util.TestDataFactory.getUserTemplate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.bookstore.core.dto.ShoppingCartDto;
import com.bookstore.core.exception.EntityNotFoundException;
import com.bookstore.core.mapper.ShoppingCartMapper;
import com.bookstore.core.model.ShoppingCart;
import com.bookstore.core.model.User;
import com.bookstore.core.repository.cart.ShoppingCartRepository;
import com.bookstore.core.service.impl.ShoppingCartServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @Mock
    private UserService userService;
    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @Test
    @DisplayName("getByEmail: Get shopping cart by valid email")
    public void getByEmail_ValidEmail_ReturnsShoppingCart() {
        // Given
        String email = "user@bookstore.ua";
        ShoppingCartDto expected = getShoppingCartDtoTemplate(1);
        when(userService.findByEmail(email)).thenReturn(getUserTemplate(1));
        when(shoppingCartRepository.getByUser(getUserTemplate(1)))
                .thenReturn(getShoppingCartTemplate(1));
        when(shoppingCartMapper.toDto(getShoppingCartTemplate(1))).thenReturn(expected);

        // When
        ShoppingCartDto actual = shoppingCartService.getByEmail(email);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("getByEmail: Get shopping cart by valid email with creation")
    public void getByEmail_ValidEmailNonExistingShoppingCart_ReturnsNewShoppingCart() {
        // Given
        String email = "user@bookstore.ua";
        ShoppingCartDto expected = getShoppingCartDtoTemplate(1);
        ShoppingCart shoppingCart = getShoppingCartTemplate(1);
        when(userService.findByEmail(email)).thenReturn(getUserTemplate(1));
        when(shoppingCartRepository.getByUser(getUserTemplate(1))).thenReturn(null);
        when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(shoppingCart);
        when(shoppingCartMapper.toDto(getShoppingCartTemplate(1))).thenReturn(expected);

        // When
        ShoppingCartDto actual = shoppingCartService.getByEmail(email);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("getByEmail: Get shopping cart by invalid email")
    public void getByEmail_InvalidEmail_Exception() {
        // Given
        String email = "user@bookstore.ua";
        String expected = "Can't get user with email: " + email;
        when(userService.findByEmail(email)).thenThrow(
                new EntityNotFoundException("Can't get user with email: " + email));

        // When
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.getByEmail(email));
        String actual = exception.getMessage();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("getByUser: Get shopping cart by valid user")
    public void getByUser_ValidUser_ReturnsShoppingCart() {
        // Given
        User user = getUserTemplate(1);
        ShoppingCart expected = getShoppingCartTemplate(1);
        when(shoppingCartRepository.getByUser(user)).thenReturn(expected);

        // When
        ShoppingCart actual = shoppingCartService.getByUser(user);

        // Then
        assertEquals(expected, actual);
    }
}
