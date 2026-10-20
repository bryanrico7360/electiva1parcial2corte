
package com.example.demo.controller;

import com.example.demo.model.ProductResponse;
import com.example.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controlador REST para gestionar los productos.
 */
@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * Consulta todos los productos.
     *
     * @return lista de productos
     */
    @GetMapping
    public List<ProductResponse> getAll() {
        return productService.getProducts();
    }

    /**
     * Consulta un producto por su ID.
     *
     * @param id identificador del producto
     * @return producto encontrado o estado 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea un producto.
     *
     * @param name nombre del producto
     * @param price precio del producto
     * @return producto creado
     */
    @PostMapping
    public ResponseEntity<ProductResponse> create(
            @RequestParam String name,
            @RequestParam BigDecimal price) {

        ProductResponse product = productService.createProduct(name, price);

        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    /**
     * Actualiza un producto existente.
     *
     * @param id identificador del producto
     * @param name nuevo nombre
     * @param price nuevo precio
     * @return producto actualizado o estado 404
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam BigDecimal price) {

        return productService.updateProduct(id, name, price)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina un producto existente.
     *
     * @param id identificador del producto
     * @return estado 204 si se eliminó o 404 si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (productService.deleteProduct(id)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
