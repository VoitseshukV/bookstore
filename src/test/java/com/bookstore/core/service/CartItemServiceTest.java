package com.bookstore.core.service;

import static com.bookstore.core.util.TestDataFactory.getAddCartItemRequestDtoTemplate;
import static com.bookstore.core.util.TestDataFactory.getBookTemplateById;
import static com.bookstore.core.util.TestDataFactory.getCartItemDtoTemplate;
import static com.bookstore.core.util.TestDataFactory.getCartItemTemplate;
import static com.bookstore.core.util.TestDataFactory.getShoppingCartTemplate;
import static com.bookstore.core.util.TestDataFactory.getUpdateCartItemRequestDtoTemplate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bookstore.core.dto.AddCartItemRequestDto;
import com.bookstore.core.dto.CartItemDto;
import com.bookstore.core.dto.UpdateCartItemRequestDto;
import com.bookstore.core.exception.EntityNotFoundException;
import com.bookstore.core.mapper.CartItemMapper;
import com.bookstore.core.model.CartItem;
import com.bookstore.core.model.ShoppingCart;
import com.bookstore.core.repository.cart.CartItemRepository;
import com.bookstore.core.service.impl.CartItemServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@ExtendWith(MockitoExtension.class)
public class CartItemServiceTest {
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private CartItemMapper cartItemMapper;
    @Mock
    private ShoppingCartService shoppingCartService;
    @Mock
    private BookService bookService;
    @InjectMocks
    private CartItemServiceImpl cartItemService;

    @Test
    @DisplayName("addItem: Add new cart item by valid email")
    public void addItem_ValidEmail_ReturnsNewCartItemDto() {
        // Given
        String email = "user@bookstore.ua";
        AddCartItemRequestDto requestDto = getAddCartItemRequestDtoTemplate(0);
        ShoppingCart shoppingCart = getShoppingCartTemplate(1);
        CartItemDto expected = getCartItemDtoTemplate(0);
        when(shoppingCartService.shoppingCartByEmail(email)).thenReturn(shoppingCart);
        when(cartItemRepository.findCardItemByBookId(2L, 1L))
                .thenReturn(null);
        when(bookService.getById(1L)).thenReturn(getBookTemplateById(0));
        when(cartItemRepository.save(any(CartItem.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        when(cartItemMapper.toDto(any(CartItem.class)))
                .thenAnswer(this::cartItemToDtoByInvocationOnMock);

        // When
        CartItemDto actual = cartItemService.addItem(email, requestDto);

        // Then
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @DisplayName("addItem: Modify existing cart item by valid email")
    public void addItem_ValidEmailModify_ReturnsCartItemDto() {
        // Given
        String email = "user@bookstore.ua";
        AddCartItemRequestDto requestDto = getAddCartItemRequestDtoTemplate(0);
        ShoppingCart shoppingCart = getShoppingCartTemplate(1);
        CartItem cartItem = getCartItemTemplate(0);
        CartItemDto expected = getCartItemDtoTemplate(0);
        when(shoppingCartService.shoppingCartByEmail(email)).thenReturn(shoppingCart);
        when(cartItemRepository.findCardItemByBookId(2L, 1L))
                .thenReturn(cartItem);
        when(cartItemRepository.save(cartItem)) .thenReturn(cartItem);
        when(cartItemMapper.toDto(cartItem)).thenReturn(expected);

        // When
        CartItemDto actual = cartItemService.addItem(email, requestDto);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("addItem: Add cart item by invalid email")
    public void addItem_InvalidEmail_Exception() {
        // Given
        String email = "admin@bookstore.ua";
        AddCartItemRequestDto requestDto = getAddCartItemRequestDtoTemplate(0);
        String expected = "Can't get user with email: " + email;
        when(shoppingCartService.shoppingCartByEmail(email))
                .thenThrow(new EntityNotFoundException(expected));

        // When
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> cartItemService.addItem(email, requestDto));
        String actual = exception.getMessage();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("updateItem: Update cart item by valid iD and email")
    public void updateItem_ValidIdAndEmail_ReturnsCartItemDto() {
        // Given
        String email = "user@bookstore.ua";
        UpdateCartItemRequestDto requestDto = getUpdateCartItemRequestDtoTemplate(1);
        CartItem cartItem = getCartItemTemplate(1);
        CartItemDto expected = getCartItemDtoTemplate(1);
        when(cartItemRepository.findById(2L)).thenReturn(Optional.of(cartItem));
        when(cartItemRepository.save(cartItem)).thenReturn(cartItem);
        when(cartItemMapper.toDto(cartItem)).thenReturn(expected);

        // When
        CartItemDto actual = cartItemService.updateItem(email, 2L, requestDto);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("updateItem: Update cart item by invalid ID")
    public void updateItem_InvalidId_Exception() {
        // Given
        String email = "user@bookstore.ua";
        Long id = 3L;
        UpdateCartItemRequestDto requestDto = getUpdateCartItemRequestDtoTemplate(1);
        String expected = "Can't find cart entity with id " + id;
        when(cartItemRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> cartItemService.updateItem(email, id, requestDto));
        String actual = exception.getMessage();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("updateItem: Update cart item by invalid email")
    public void updateItem_InvalidEmail_Exception() {
        // Given
        String email = "admin@bookstore.ua";
        Long id = 1L;
        UpdateCartItemRequestDto requestDto = getUpdateCartItemRequestDtoTemplate(1);
        String expected = "Can't find cart entity with id " + id;
        when(cartItemRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> cartItemService.updateItem(email, id, requestDto));
        String actual = exception.getMessage();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("checkAndDeleteById: Delete by valid email and existing ID")
    public void checkAndDeleteById_ValidEmailExistingId_Success() {
        // Given
        Long id = 1L;
        String email = "user@bookstore.ua";
        CartItem cartItem = getCartItemTemplate(0);
        if (cartItem == null) {
            throw new NullPointerException("Error getting template CartItem");
        }
        when(cartItemRepository.findById(id)).thenReturn(Optional.of(cartItem));

        // When
        cartItemService.checkAndDeleteById(email, id);

        // Then
        verify(cartItemRepository, times(1)).delete(cartItem);
    }

    @Test
    @DisplayName("checkAndDeleteById: Delete by invalid email and existing ID")
    public void checkAndDeleteById_InvalidEmailExistingId_Exception() {
        // Given
        Long id = 1L;
        String email = "user@bookstore.ua";
        String expected = "Can't find cart entity with id " + id;
        when(cartItemRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> cartItemService.checkAndDeleteById(email, id));
        String actual = exception.getMessage();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("delete: Delete cart item")
    public void delete_ValidCartItem_Success() {
        // Given
        CartItem cartItem = getCartItemTemplate(0);

        // When
        cartItemService.delete(cartItem);

        // Then
        verify(cartItemRepository, times(1)).delete(cartItem);
    }

    private CartItemDto cartItemToDtoByInvocationOnMock(InvocationOnMock invocationOnMock) {
        CartItem cartItem = (CartItem) invocationOnMock.getArguments()[0];
        return new CartItemDto()
                .setBookId(cartItem.getId())
                .setBookId(cartItem.getBook().getId())
                .setBookTitle(cartItem.getBook().getTitle())
                .setQuantity(cartItem.getQuantity());
    }
}
