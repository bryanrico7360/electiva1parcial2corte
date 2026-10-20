
package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.model.ProductResponse;
import com.example.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Contiene la lógica de negocio para gestionar productos.
 */
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * Obtiene todos los productos.
     *
     * @return lista de productos
     */
    public List<ProductResponse> getProducts() {
        return this.productRepository.findAll().stream()
                .map(ProductResponse::from)
                .toList();
    }

    /**
     * Busca un producto por su identificador.
     *
     * @param id identificador del producto
     * @return producto encontrado, si existe
     */
    public Optional<ProductResponse> getProductById(Long id) {
        return this.productRepository.findById(id)
                .map(ProductResponse::from);
    }

    /**
     * Registra un producto nuevo.
     *
     * @param name nombre del producto
     * @param price precio del producto
     * @return producto creado
     */
    public ProductResponse createProduct(String name, BigDecimal price) {
        validateProduct(name, price);

        Product productSave = this.productRepository.save(
                new Product(name, price)
        );

        return ProductResponse.from(productSave);
    }

    /**
     * Actualiza los datos de un producto existente.
     *
     * @param id identificador del producto
     * @param name nuevo nombre
     * @param price nuevo precio
     * @return producto actualizado, si existe
     */
    public Optional<ProductResponse> updateProduct(
            Long id, String name, BigDecimal price) {

        validateProduct(name, price);

        return productRepository.findById(id).map(product -> {
            product.setName(name);
            product.setPrice(price);

            Product updatedProduct = productRepository.save(product);
            return ProductResponse.from(updatedProduct);
        });
    }

    /**
     * Elimina un producto existente.
     *
     * @param id identificador del producto
     * @return true si se eliminó; false si no existía
     */
    public boolean deleteProduct(Long id) {
        return productRepository.findById(id).map(product -> {
            productRepository.delete(product);
            return true;
        }).orElse(false);
    }

    /**
     * Comprueba que el nombre y el precio sean válidos.
     *
     * @param name nombre que se validará
     * @param price precio que se validará
     */
    private void validateProduct(String name, BigDecimal price) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre es requerido"
            );
        }

        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "El precio debe ser mayor que cero"
            );
        }
    }
}
