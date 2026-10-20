
package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.model.ProductResponse;
import com.example.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse> getProducts() {
        return productRepository.findAll()
                .stream()
                .map(ProductResponse::from)
                .toList();
    }

    public Optional<ProductResponse> getProductById(Long id) {
        return productRepository.findById(id)
                .map(ProductResponse::from);
    }

    public ProductResponse createProduct(Product product) {
        validateProduct(
                product.getName(),
                product.getPrice(),
                product.getStock()
        );

        // El ID se genera automáticamente al crear el producto.
        product.setId(null);

        Product savedProduct = productRepository.save(product);

        return ProductResponse.from(savedProduct);
    }

    public Optional<ProductResponse> updateProduct(
            Long id,
            Product datos) {

        validateProduct(
                datos.getName(),
                datos.getPrice(),
                datos.getStock()
        );

        return productRepository.findById(id).map(product -> {
            product.setName(datos.getName());
            product.setPrice(datos.getPrice());
            product.setDescription(datos.getDescription());
            product.setStock(datos.getStock());

            Product updatedProduct = productRepository.save(product);

            return ProductResponse.from(updatedProduct);
        });
    }

    public boolean deleteProduct(Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    productRepository.delete(product);
                    return true;
                })
                .orElse(false);
    }

    private void validateProduct(
            String name,
            BigDecimal price,
            Integer stock) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre es requerido"
            );
        }

        if (price == null ||
                price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "El precio debe ser mayor que cero"
            );
        }

        if (stock == null || stock < 0) {
            throw new IllegalArgumentException(
                    "El stock debe ser mayor o igual a cero"
            );
        }
    }
}
