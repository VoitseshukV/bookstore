package com.bookstore.core.mapper;

import com.bookstore.core.config.MapperConfig;
import com.bookstore.core.dto.OrderItemDto;
import com.bookstore.core.model.OrderItem;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface OrderItemMapper {
    @Mapping(target = "bookId", ignore = true)
    @Mapping(target = "bookTitle", ignore = true)
    OrderItemDto toDto(OrderItem orderItem);

    @AfterMapping
    default void setBookIdTitle(@MappingTarget OrderItemDto orderItemDto, OrderItem orderItem) {
        orderItemDto.setBookId(orderItem.getBook().getId());
        orderItemDto.setBookTitle(orderItem.getBook().getTitle());
    }

    @Named("orderItemsToDtos")
    default Set<OrderItemDto> toDtos(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(this::toDto)
                .collect(Collectors.toUnmodifiableSet());
    }
}
