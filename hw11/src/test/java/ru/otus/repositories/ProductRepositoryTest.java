package ru.otus.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import ru.otus.entities.Product;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Test
    void findByNameIn() {
        productRepository.deleteAll().block();
        productRepository.saveAll(
                List.of(
                        new Product(UUID.randomUUID().toString(), "product_1"),
                        new Product(UUID.randomUUID().toString(), "product_2"),
                        new Product(UUID.randomUUID().toString(), "product_3")
                )
        ).blockLast();

        Flux<Product> productFlux = productRepository.findByNameIn(List.of("product_1", "product_2"));

        StepVerifier.create(productFlux).expectNextCount(2).expectComplete().verify();
    }

}