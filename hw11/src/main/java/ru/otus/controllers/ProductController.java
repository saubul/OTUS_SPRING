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
import ru.otus.dtos.ProductDto;
import ru.otus.mappers.ProductMapper;
import ru.otus.repositories.ProductRepository;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    @GetMapping
    public Flux<ProductDto> findAll() {
        return productRepository.findAll().map(productMapper::toDto);
    }

    @PostMapping
    public Mono<ProductDto> createProduct(@RequestBody Mono<ProductDto> productDto) {
        return productDto.map(productMapper::toEntity).flatMap(productRepository::save).map(productMapper::toDto);
    }

    @PutMapping("/{id}")
    public Mono<ProductDto> updateProduct(@RequestBody Mono<ProductDto> productDto, @PathVariable("id") String id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Product with id %s not found".formatted(id))))
                .flatMap(product ->
                    productDto.map(dto -> {
                        product.setName(dto.getName());
                        return product;
                    })
                )
                .flatMap(productRepository::save)
                .map(productMapper::toDto);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteProduct(@PathVariable("id") String id) {
        return productRepository.deleteById(id);
    }

}
