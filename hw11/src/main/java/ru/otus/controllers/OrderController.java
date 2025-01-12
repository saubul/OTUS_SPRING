package ru.otus.controllers;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.dtos.OrderSaveDto;
import ru.otus.dtos.OrderDto;
import ru.otus.entities.Order;
import ru.otus.mappers.OrderMapper;
import ru.otus.repositories.OrderRepository;
import ru.otus.repositories.ProductRepository;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    private final OrderMapper orderMapper;

    @GetMapping
    public Flux<OrderDto> findAll() {
        return orderRepository.findAll().map(orderMapper::toDto);
    }

    @PostMapping
    public Mono<OrderDto> createOrder(@RequestBody Mono<OrderSaveDto> orderSaveDtoMono) {
        return orderSaveDtoMono.flatMap(dto ->
                productRepository.findByNameIn(dto.getProductNameList())
                        .collectList()
                        .map(products -> {
                            Order order = new Order();
                            order.setProductList(products);
                            return order;
                        })
                )
                .flatMap(orderRepository::save)
                .map(orderMapper::toDto);
    }

    @PutMapping("/{id}")
    public Mono<OrderDto> updateOrder(@RequestBody OrderSaveDto orderSaveDto, @PathVariable("id") String id) {
        return orderRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Order with id %s not found".formatted(id))))
                .flatMap(order ->
                        productRepository.findByNameIn(orderSaveDto.getProductNameList())
                                .collectList()
                                .map(products -> {
                                    order.setProductList(products);
                                    return order;
                                })
                )
                .flatMap(orderRepository::save)
                .map(orderMapper::toDto);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteOrder(@PathVariable String id) {
        return orderRepository.deleteById(id);
    }

}
