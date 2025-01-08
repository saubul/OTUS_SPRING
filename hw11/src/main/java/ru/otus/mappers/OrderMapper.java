package ru.otus.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.dtos.OrderDto;
import ru.otus.entities.Order;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderMapper implements Mapper<Order, OrderDto> {

    private final ProductMapper productMapper;

    @Override
    public Order toEntity(OrderDto orderDto) {
        Order order = new Order();
        order.setProductList(orderDto.getProductList().stream()
                .map(productMapper::toEntity).collect(Collectors.toList()));
        return order;
    }

    @Override
    public OrderDto toDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setProductList(order.getProductList().stream()
                .map(productMapper::toDto).collect(Collectors.toList()));
        return orderDto;
    }

}
