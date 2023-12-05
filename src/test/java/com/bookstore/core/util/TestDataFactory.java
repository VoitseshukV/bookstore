package com.bookstore.core.util;

import com.bookstore.core.dto.AddCartItemRequestDto;
import com.bookstore.core.dto.BookDto;
import com.bookstore.core.dto.BookDtoWithoutCategoryIds;
import com.bookstore.core.dto.CartItemDto;
import com.bookstore.core.dto.CategoryDto;
import com.bookstore.core.dto.CreateBookRequestDto;
import com.bookstore.core.dto.CreateCategoryRequestDto;
import com.bookstore.core.dto.OrderDto;
import com.bookstore.core.dto.OrderItemDto;
import com.bookstore.core.dto.ShoppingCartDto;
import com.bookstore.core.dto.UpdateCartItemRequestDto;
import com.bookstore.core.dto.UpdateOrderDto;
import com.bookstore.core.dto.UserLoginRequestDto;
import com.bookstore.core.dto.UserRegistrationRequestDto;
import com.bookstore.core.dto.UserResponseDto;
import com.bookstore.core.model.Book;
import com.bookstore.core.model.CartItem;
import com.bookstore.core.model.Category;
import com.bookstore.core.model.Order;
import com.bookstore.core.model.OrderItem;
import com.bookstore.core.model.Role;
import com.bookstore.core.model.ShoppingCart;
import com.bookstore.core.model.User;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TestDataFactory {
    private static List<Category> categories;
    private static List<CategoryDto> categoryDtos;
    private static List<CreateCategoryRequestDto> categoryRequestDtos;
    private static List<Book> books;
    private static List<BookDto> bookDtos;
    private static List<CreateBookRequestDto> bookRequestDtos;
    private static List<BookDtoWithoutCategoryIds> bookDtoWithoutCategoryIds;
    private static List<ShoppingCart> shoppingCarts;
    private static List<ShoppingCartDto> shoppingCartDtos;
    private static List<CartItem> cartItems;
    private static List<CartItemDto> cartItemDtos;
    private static List<AddCartItemRequestDto> addCartItemRequestDtos;
    private static List<UpdateCartItemRequestDto> updateCartItemRequestDtos;
    private static List<User> users;
    private static List<UserLoginRequestDto> loginRequestDtos;
    private static List<UserRegistrationRequestDto> registrationRequestDtos;
    private static List<UserResponseDto> userResponseDtos;
    private static List<Role> roles;
    private static List<Order> orders;
    private static List<OrderDto> orderDtos;
    private static List<UpdateOrderDto> updateOrderDtos;
    private static List<OrderItem> orderItems;
    private static List<OrderItemDto> orderItemDtos;

    static {
        createCategories();
        createBooks();
        createRoles();
        createUsers();
        createShoppingCarts();
        createOrders();
    }

    public static Category getCategoryTemplateById(int id) {
        return categories.get(id);
    }

    public static CategoryDto getCategoryDtoTemplateById(int id) {
        return categoryDtos.get(id);
    }

    public static CreateCategoryRequestDto getCreateCategoryRequestDtoTemplateById(int id) {
        return categoryRequestDtos.get(id);
    }

    public static Book getBookTemplateById(int id) {
        return books.get(id);
    }

    public static BookDto getBookDtoTemplateById(int id) {
        return bookDtos.get(id);
    }

    public static CreateBookRequestDto getCreateBookRequestDtoTemplateById(int id) {
        return bookRequestDtos.get(id);
    }

    public static BookDtoWithoutCategoryIds getBookDtoWithoutCategoryIdsTemplateById(int id) {
        return bookDtoWithoutCategoryIds.get(id);
    }

    public static Role getRoleTemplate(int id) {
        return roles.get(id);
    }

    public static User getUserTemplate(int id) {
        return users.get(id);
    }

    public static UserResponseDto getUserResponseDtoTemplate(int id) {
        return userResponseDtos.get(id);
    }

    public static UserLoginRequestDto getUserLoginRequestDtoTemplate(int id) {
        return loginRequestDtos.get(id);
    }

    public static UserRegistrationRequestDto getUserRegistrationRequestDtoTemplate(int id) {
        return registrationRequestDtos.get(id);
    }

    public static ShoppingCart getShoppingCartTemplate(int id) {
        return shoppingCarts.get(id);
    }

    public static ShoppingCartDto getShoppingCartDtoTemplate(int id) {
        return shoppingCartDtos.get(id);
    }

    public static CartItem getCartItemTemplate(int id) {
        return cartItems.get(id);
    }

    public static CartItemDto getCartItemDtoTemplate(int id) {
        return cartItemDtos.get(id);
    }

    public static AddCartItemRequestDto getAddCartItemRequestDtoTemplate(int id) {
        return addCartItemRequestDtos.get(id);
    }

    public static UpdateCartItemRequestDto getUpdateCartItemRequestDtoTemplate(int id) {
        return updateCartItemRequestDtos.get(id);
    }

    public static Order getOrderTemplate(int id) {
        return orders.get(id);
    }

    public static OrderDto getOrderDtoTemplate(int id) {
        return orderDtos.get(id);
    }

    public static UpdateOrderDto getUpdateOrderDtoTemplate(int id) {
        return updateOrderDtos.get(id);
    }

    public static OrderItem getOrderItemTemplate(int id) {
        return orderItems.get(id);
    }

    public static OrderItemDto getOrderItemDtoTemplate(int id) {
        return orderItemDtos.get(id);
    }

    private static void createCategories() {
        categories = new ArrayList<>();
        categories.add(new Category()
                .setId(1L)
                .setName("Sci-Fi")
                .setDescription(""));
        categories.add(new Category()
                .setId(2L)
                .setName("Fantasy")
                .setDescription(""));
        categoryDtos = new ArrayList<>();
        categories.forEach(category -> categoryDtos.add(
                new CategoryDto(
                        category.getId(),
                        category.getName(),
                        category.getDescription()
                )
        ));
        categoryRequestDtos = new ArrayList<>();
        categories.forEach(category -> categoryRequestDtos.add(
                new CreateCategoryRequestDto(
                        category.getName(),
                        category.getDescription()
                )
        ));
    }

    private static void createBooks() {
        books = new ArrayList<>();
        books.add(new Book()
                .setId(1L)
                .setTitle("Anansi Boys")
                .setAuthor("Gaiman Neil")
                .setIsbn("9780060515195")
                .setPrice(BigDecimal.valueOf(500))
                .setDescription("")
                .setCoverImage("")
                .setCategories(Set.of(categories.get(1))));
        books.add(new Book()
                .setId(2L)
                .setTitle("American Gods")
                .setAuthor("Gaiman Neil")
                .setIsbn("9780062896261")
                .setPrice(BigDecimal.valueOf(450))
                .setDescription("")
                .setCoverImage("")
                .setCategories(Set.of(categories.get(1))));
        books.add(new Book()
                .setId(3L)
                .setTitle("Elantris")
                .setAuthor("Brandon Sanderson")
                .setIsbn("9780765350374")
                .setPrice(BigDecimal.valueOf(420))
                .setDescription("")
                .setCoverImage("")
                .setCategories(Set.of(categories.get(0), categories.get(1))));
        bookDtos = new ArrayList<>();
        books.forEach(book -> bookDtos.add(
                new BookDto().setId(book
                        .getId())
                        .setTitle(book.getTitle())
                        .setAuthor(book.getAuthor())
                        .setIsbn(book.getIsbn())
                        .setPrice(book.getPrice())
                        .setDescription(book.getDescription())
                        .setCoverImage(book.getCoverImage())
                        .setCategoryIds(book.getCategories().stream()
                                .map(Category::getId)
                                .collect(Collectors.toUnmodifiableSet())
                )
        ));
        bookRequestDtos = new ArrayList<>();
        books.forEach(book -> bookRequestDtos.add(
                new CreateBookRequestDto(
                        book.getTitle(),
                        book.getAuthor(),
                        book.getIsbn(),
                        book.getPrice(),
                        book.getDescription(),
                        book.getCoverImage(),
                        book.getCategories().stream()
                                .map(Category::getId)
                                .collect(Collectors.toUnmodifiableSet())
                )
        ));
        bookDtoWithoutCategoryIds = new ArrayList<>();
        books.forEach(book -> bookDtoWithoutCategoryIds.add(
                        new BookDtoWithoutCategoryIds(
                                book.getId(),
                                book.getTitle(),
                                book.getAuthor(),
                                book.getIsbn(),
                                book.getPrice(),
                                book.getDescription(),
                                book.getCoverImage()
                )
        ));
    }

    private static void createRoles() {
        roles = new ArrayList<>();
        roles.add(new Role()
                .setId(1L)
                .setName(Role.RoleName.ADMIN));
        roles.add(new Role()
                .setId(2L)
                .setName(Role.RoleName.USER));
    }

    private static void createUsers() {
        users = new ArrayList<>();
        User user1 = new User()
                .setId(1L)
                .setEmail("admin@bookstore.ua")
                .setPassword("$2a$10$bDpYvJC1c/ZQjPnk2TucHOydv18Jo.81poZo/5bM4V1zavBrfL7Y2")
                .setRoles(Set.of(getRoleTemplate(0)))
                .setFirstName("Bob")
                .setLastName("Bobson")
                .setShippingAddress("");
        users.add(user1);
        User user2 = new User()
                .setId(2L)
                .setEmail("user@bookstore.ua")
                .setPassword("$2a$10$I5KuzQSeQy3nmLep3plrz.M.fF3g1x.6pcj0SHUzWjACZVvhrIQrO")
                .setRoles(Set.of(getRoleTemplate(1)))
                .setFirstName("Alice")
                .setLastName("Anderson")
                .setShippingAddress("");
        users.add(user2);
        userResponseDtos = new ArrayList<>();
        users.forEach(user -> userResponseDtos.add(new UserResponseDto(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getShippingAddress())
        ));
        registrationRequestDtos = new ArrayList<>();
        registrationRequestDtos.add(new UserRegistrationRequestDto()
                .setEmail(user1.getEmail())
                .setPassword("12345678")
                .setRepeatPassword("12345678")
                .setFirstName(user1.getFirstName())
                .setLastName(user1.getLastName())
                .setShippingAddress(user1.getShippingAddress()));
        registrationRequestDtos.add(new UserRegistrationRequestDto()
                .setEmail(user2.getEmail())
                .setPassword("12345678")
                .setRepeatPassword("12345678")
                .setFirstName(user2.getFirstName())
                .setLastName(user2.getLastName())
                .setShippingAddress(user2.getShippingAddress()));
        loginRequestDtos = new ArrayList<>();
        registrationRequestDtos.forEach(user -> loginRequestDtos.add(new UserLoginRequestDto(
                user.getEmail(),
                user.getPassword()
        )));
    }

    private static void createShoppingCarts() {
        shoppingCarts = new ArrayList<>();
        ShoppingCart shoppingCart1 = new ShoppingCart()
                .setId(1L)
                .setUser(getUserTemplate(0))
                .setCartItems(new HashSet<>());
        shoppingCarts.add(shoppingCart1);
        ShoppingCart shoppingCart2 = new ShoppingCart()
                .setId(2L)
                .setUser(getUserTemplate(1))
                .setCartItems(new HashSet<>());
        shoppingCarts.add(shoppingCart2);
        shoppingCartDtos = new ArrayList<>();
        ShoppingCartDto shoppingCartDto1 = new ShoppingCartDto()
                .setId(shoppingCart1.getId())
                .setEmail(shoppingCart1.getUser().getEmail())
                .setCartItems(new HashSet<>());
        shoppingCartDtos.add(shoppingCartDto1);
        ShoppingCartDto shoppingCartDto2 = new ShoppingCartDto()
                .setId(shoppingCart2.getId())
                .setEmail(shoppingCart2.getUser().getEmail())
                .setCartItems(new HashSet<>());
        shoppingCartDtos.add(shoppingCartDto2);
        cartItems = new ArrayList<>();
        cartItems.add(new CartItem()
                .setId(1L)
                .setShoppingCart(shoppingCart2)
                .setBook(getBookTemplateById(0))
                .setQuantity(1));
        cartItems.add(new CartItem()
                .setId(2L)
                .setShoppingCart(shoppingCart2)
                .setBook(getBookTemplateById(1))
                .setQuantity(2));
        shoppingCart2.getCartItems().addAll(cartItems);
        cartItemDtos = new ArrayList<>();
        cartItems.forEach(cartItem -> cartItemDtos.add(new CartItemDto()
                .setId(cartItem.getId())
                .setBookId(cartItem.getBook().getId())
                .setBookTitle(cartItem.getBook().getTitle())
                .setQuantity(cartItem.getQuantity())));
        shoppingCartDto2.getCartItems().addAll(cartItemDtos);
        addCartItemRequestDtos = new ArrayList<>();
        cartItems.forEach(cartItem -> addCartItemRequestDtos.add(new AddCartItemRequestDto(
                cartItem.getBook().getId(),
                cartItem.getQuantity()
        )));
        updateCartItemRequestDtos = new ArrayList<>();
        cartItems.forEach(cartItem -> updateCartItemRequestDtos.add(new UpdateCartItemRequestDto(
                cartItem.getQuantity()
        )));
    }

    private static void createOrders() {
        orders = new ArrayList<>();
        Order order1 = new Order()
                .setId(1L)
                .setUser(getUserTemplate(1))
                .setStatus(Order.OrderStatus.CREATED)
                .setOrderDate(LocalDateTime.of(2023, 12, 3, 0, 0))
                .setShippingAddress("")
                .setTotal(BigDecimal.valueOf(1820))
                .setOrderItems(new HashSet<>());
        orders.add(order1);
        Order order2 = new Order()
                .setId(2L)
                .setUser(getUserTemplate(1))
                .setStatus(Order.OrderStatus.CREATED)
                .setOrderDate(LocalDateTime.of(2023, 12, 3, 0, 0))
                .setShippingAddress("")
                .setTotal(BigDecimal.valueOf(450))
                .setOrderItems(new HashSet<>());
        orders.add(order2);
        Order order3 = new Order()
                .setId(3L)
                .setUser(getUserTemplate(1))
                .setStatus(Order.OrderStatus.CREATED)
                .setOrderDate(LocalDateTime.of(2023, 12, 3, 0, 0))
                .setShippingAddress("")
                .setTotal(BigDecimal.valueOf(950))
                .setOrderItems(new HashSet<>());
        orders.add(order2);        orderDtos = new ArrayList<>();
        OrderDto orderDto1 = new OrderDto()
                .setId(order1.getId())
                .setEmail(order1.getUser().getEmail())
                .setStatus(order1.getStatus().toString())
                .setOrderDate(order1.getOrderDate())
                .setShippingAddress(order1.getShippingAddress())
                .setTotal(order1.getTotal())
                .setOrderItems(new HashSet<>());
        orderDtos.add(orderDto1);
        OrderDto orderDto2 = new OrderDto()
                .setId(order2.getId())
                .setEmail(order2.getUser().getEmail())
                .setStatus(order2.getStatus().toString())
                .setOrderDate(order2.getOrderDate())
                .setShippingAddress(order2.getShippingAddress())
                .setTotal(order2.getTotal())
                .setOrderItems(new HashSet<>());
        orderDtos.add(orderDto2);
        OrderDto orderDto3 = new OrderDto()
                .setId(order3.getId())
                .setEmail(order3.getUser().getEmail())
                .setStatus(order3.getStatus().toString())
                .setOrderDate(order3.getOrderDate())
                .setShippingAddress(order3.getShippingAddress())
                .setTotal(order3.getTotal())
                .setOrderItems(new HashSet<>());
        orderDtos.add(orderDto3);
        updateOrderDtos = new ArrayList<>();
        orderDtos.forEach(orderDto -> updateOrderDtos.add(new UpdateOrderDto(
                orderDto.getStatus()
        )));
        orderItems = new ArrayList<>();
        orderItems.add(new OrderItem()
                .setId(1L)
                .setOrder(order1)
                .setBook(getBookTemplateById(0))
                .setQuantity(1)
                .setPrice(BigDecimal.valueOf(500)));
        orderItems.add(new OrderItem()
                .setId(2L)
                .setOrder(order1)
                .setBook(getBookTemplateById(2))
                .setQuantity(1)
                .setPrice(BigDecimal.valueOf(420)));
        orderItems.add(new OrderItem()
                .setId(3L)
                .setOrder(order1)
                .setBook(getBookTemplateById(1))
                .setQuantity(2)
                .setPrice(BigDecimal.valueOf(450)));
        orderItems.add(new OrderItem()
                .setId(4L)
                .setOrder(order2)
                .setBook(getBookTemplateById(1))
                .setQuantity(1)
                .setPrice(BigDecimal.valueOf(450)));
        orderItems.add(new OrderItem()
                .setId(5L)
                .setOrder(order3)
                .setBook(getBookTemplateById(0))
                .setQuantity(1)
                .setPrice(BigDecimal.valueOf(500)));
        orderItems.add(new OrderItem()
                .setId(6L)
                .setOrder(order3)
                .setBook(getBookTemplateById(1))
                .setQuantity(1)
                .setPrice(BigDecimal.valueOf(450)));
        orderItems.forEach(orderItem -> orderItem.getOrder().getOrderItems().add(orderItem));
        orderItemDtos = new ArrayList<>();
        orderItems.forEach(orderItem -> orderItemDtos.add(new OrderItemDto()
                .setId(orderItem.getId())
                .setBookId(orderItem.getBook().getId())
                .setBookTitle(orderItem.getBook().getTitle())
                .setPrice(orderItem.getPrice())
                .setQuantity(orderItem.getQuantity())));
        orderDto1.getOrderItems().add(orderItemDtos.get(0));
        orderDto1.getOrderItems().add(orderItemDtos.get(1));
        orderDto1.getOrderItems().add(orderItemDtos.get(2));
        orderDto2.getOrderItems().add(orderItemDtos.get(3));
        orderDto3.getOrderItems().add(orderItemDtos.get(4));
        orderDto3.getOrderItems().add(orderItemDtos.get(5));
    }
}
