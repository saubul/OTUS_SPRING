package ru.otus.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import ru.otus.dtos.OrderDto;
import ru.otus.entities.Order;
import ru.otus.entities.Product;
import ru.otus.repositories.OrderRepository;
import ru.otus.repositories.ProductRepository;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @BeforeEach
    void setup() {
        productRepository.deleteAll().block();
        orderRepository.deleteAll().block();
        productRepository.saveAll(
                List.of(
                        new Product("1", "product_1"),
                        new Product("2", "product_2")
                )
        ).blockLast();

        orderRepository.saveAll(
                List.of(
                        new Order("1", List.of(productRepository.findById("1").block()))
                )
        ).blockLast();
    }

    @Test
    void findAll() {
        WebClient client = WebClient.create(String.format("http://localhost:%d", port));
        List<OrderDto> orderDtoList = client.get().uri("/api/v1/orders")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(OrderDto.class)
                .collectList()
                .block();

        assertThat(orderDtoList).isNotNull().isNotEmpty();
    }

    @Test
    void createOrder() {
        WebClient client = WebClient.create(String.format("http://localhost:%d", port));
        OrderDto orderDto = client.post()
                .uri("/api/v1/orders")
                .body(BodyInserters.fromValue("{\"productNameList\":[\"product_1\"]}"))
                .header("Content-Type", "application/json")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(OrderDto.class)
                .block();

        assertThat(orderDto).isNotNull().matches(dto -> dto.getProductList().size() == 1 && dto.getProductList().get(0).getName().equals("product_1"));
    }

    @Test
    void updateOrder() {
        WebClient client = WebClient.create(String.format("http://localhost:%d", port));
        OrderDto orderDto = client.put()
                .uri("/api/v1/orders/1")
                .body(BodyInserters.fromValue("{\"productNameList\":[\"product_2\"]}"))
                .header("Content-Type", "application/json")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(OrderDto.class)
                .block();

        List<Product> productList = Objects.requireNonNull(orderRepository.findById("1").block()).getProductList();
        assertFalse(productList.isEmpty());
        assertEquals("product_2", productList.get(0).getName());
    }

    @Test
    void deleteOrder() {
        WebClient client = WebClient.create(String.format("http://localhost:%d", port));
        client.delete()
                .uri("/api/v1/orders/1")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Void.class)
                .block();

        Order order = orderRepository.findById("1").block();
        assertNull(order);
    }
}