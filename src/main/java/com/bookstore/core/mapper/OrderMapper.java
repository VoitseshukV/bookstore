package com.bookstore.core.mapper;

import com.bookstore.core.config.MapperConfig;
import com.bookstore.core.dto.OrderDto;
import com.bookstore.core.model.Order;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "orderItems", qualifiedByName = "orderItemsToDtos")
    OrderDto toDto(Order order);

    @AfterMapping
    default void setEmail(@MappingTarget OrderDto orderDto, Order order) {
        orderDto.setEmail(order.getUser().getEmail());
    }
}
