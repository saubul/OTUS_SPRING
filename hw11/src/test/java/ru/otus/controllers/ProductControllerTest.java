package ru.otus.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import ru.otus.dtos.ProductDto;
import ru.otus.entities.Product;
import ru.otus.repositories.ProductRepository;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    void setup() {
        productRepository.deleteAll().block();
        productRepository.saveAll(
                List.of(
                        new Product("1", "product_1"),
                        new Product("2", "product_2"),
                        new Product("3", "product_3")
                )
        ).blockLast();
    }

    @Test
    void findAll() {
        WebClient client = WebClient.create(String.format("http://localhost:%d", port));

        List<ProductDto> productDtoList = client.get()
                .uri("/api/v1/products")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(ProductDto.class)
                .collectList()
                .block();

        assertThat(productDtoList).isNotNull().isNotEmpty();
    }

    @Test
    void createProduct() {
        WebClient client = WebClient.create(String.format("http://localhost:%d", port));
        ProductDto orderDto = client.post()
                .uri("/api/v1/products")
                .body(BodyInserters.fromValue("{\"name\":\"product_10\"}"))
                .header("Content-Type", "application/json")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ProductDto.class)
                .block();

        assertNotNull(orderDto);
        assertEquals("product_10", orderDto.getName());
    }

    @Test
    void updateProduct() {
        WebClient client = WebClient.create(String.format("http://localhost:%d", port));
        ProductDto productDto = client.put()
                .uri("/api/v1/products/1")
                .body(BodyInserters.fromValue("{\"name\":\"product_8\"}"))
                .header("Content-Type", "application/json")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ProductDto.class)
                .block();

        assertEquals("product_8", Objects.requireNonNull(productRepository.findById("1").block()).getName());
    }

    @Test
    void deleteProduct() {
        WebClient client = WebClient.create(String.format("http://localhost:%d", port));
        client.delete()
                .uri("/api/v1/products/1")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Void.class)
                .block();

        Product product = productRepository.findById("1").block();
        assertNull(product);
    }
}