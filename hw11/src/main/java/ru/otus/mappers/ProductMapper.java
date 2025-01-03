package ru.otus.mappers;

import org.springframework.stereotype.Component;
import ru.otus.dtos.ProductDto;
import ru.otus.entities.Product;

import java.util.UUID;

@Component
public class ProductMapper implements Mapper<Product, ProductDto> {

    @Override
    public Product toEntity(ProductDto productDto) {
        return new Product(UUID.randomUUID().toString(), productDto.getName());
    }

    @Override
    public ProductDto toDto(Product product) {
        return new ProductDto(product.getId(), product.getName());
    }

}
