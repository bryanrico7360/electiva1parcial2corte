
package com.example.demo.controller;

import com.example.demo.dto.ProductoRequest;
import com.example.demo.dto.ProductoResponse;
import com.example.demo.model.Producto;
import com.example.demo.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST encargado de gestionar las operaciones
 * de consulta, creación, actualización y eliminación
 * de productos.
 *
 * @author
 * @version 1.0
 */
@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productService;

    /**
     * Obtiene todos los productos registrados.
     *
     * @return respuesta HTTP 200 con la lista de productos
     */
    @GetMapping
    public ResponseEntity<List<ProductoResponse>> getAll() {
        return ResponseEntity.ok(productService.getProducts());
    }

    /**
     * Busca un producto por su identificador.
     *
     * @param id identificador del producto
     * @return respuesta HTTP 200 con el producto encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> getById(
            @PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    /**
     * Registra un producto nuevo.
     *
     * @param request datos del producto que se registrará
     * @return respuesta HTTP 201 con el producto creado
     */
    @PostMapping
    public ResponseEntity<ProductoResponse> create(
            @Valid @RequestBody ProductoRequest request) {

        Producto producto = convertirAProducto(request);
        ProductoResponse created = productService.createProduct(producto);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Actualiza los datos de un producto existente.
     *
     * @param id identificador del producto que se actualizará
     * @param request nuevos datos del producto
     * @return respuesta HTTP 200 con el producto actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequest request) {

        Producto producto = convertirAProducto(request);

        return ResponseEntity.ok(
                productService.updateProduct(id, producto)
        );
    }

    /**
     * Elimina un producto existente.
     *
     * @param id identificador del producto que se eliminará
     * @return respuesta HTTP 204 si la eliminación se completa
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Convierte los datos de una solicitud en una entidad Producto.
     *
     * @param request solicitud con los datos del producto
     * @return entidad Producto construida a partir de la solicitud
     */
    private Producto convertirAProducto(ProductoRequest request) {
        Producto producto = new Producto();
        producto.setNombre(request.nombre());
        producto.setDescripcion(request.descripcion());
        producto.setPrecio(request.precio());
        producto.setStock(request.stock());
        return producto;
    }
}
