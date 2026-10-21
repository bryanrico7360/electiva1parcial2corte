
package com.example.demo.service;

import com.example.demo.dto.ProductoResponse;
import com.example.demo.exception.RecursoNoEncontradoException;
import com.example.demo.model.Producto;
import com.example.demo.repository.DetallePedidoRepository;
import com.example.demo.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

/**
 * Contiene la lógica de negocio para administrar productos.
 * Gestiona las consultas, el registro, la actualización,
 * las validaciones y la eliminación de productos.
 *
 * @author
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productRepository;
    private final DetallePedidoRepository detallePedidoRepository;

    /**
     * Obtiene todos los productos registrados.
     *
     * @return lista de productos convertidos a DTO de respuesta
     */
    @Transactional(readOnly = true)
    public List<ProductoResponse> getProducts() {
        return productRepository.findAll()
                .stream()
                .map(ProductoResponse::from)
                .toList();
    }

    /**
     * Busca un producto por su identificador.
     *
     * @param id identificador del producto
     * @return DTO con los datos del producto encontrado
     * @throws RecursoNoEncontradoException si el producto no existe
     */
    @Transactional(readOnly = true)
    public ProductoResponse getProductById(Long id) {
        Producto producto = buscarProducto(id);
        return ProductoResponse.from(producto);
    }

    /**
     * Registra un nuevo producto después de validar sus datos.
     *
     * @param producto datos del producto que se registrará
     * @return DTO con los datos del producto guardado
     * @throws ResponseStatusException si los datos no son válidos
     */
    @Transactional
    public ProductoResponse createProduct(Producto producto) {
        validarProducto(producto);

        producto.setId(null);

        Producto guardado = productRepository.save(producto);
        return ProductoResponse.from(guardado);
    }

    /**
     * Actualiza los datos de un producto existente.
     *
     * @param id identificador del producto que se actualizará
     * @param datos nuevos datos del producto
     * @return DTO con los datos actualizados
     * @throws RecursoNoEncontradoException si el producto no existe
     * @throws ResponseStatusException si los datos no son válidos
     */
    @Transactional
    public ProductoResponse updateProduct(Long id, Producto datos) {
        validarProducto(datos);

        Producto producto = buscarProducto(id);

        producto.setNombre(datos.getNombre());
        producto.setDescripcion(datos.getDescripcion());
        producto.setPrecio(datos.getPrecio());
        producto.setStock(datos.getStock());

        Producto actualizado = productRepository.save(producto);
        return ProductoResponse.from(actualizado);
    }

    /**
     * Elimina un producto si no está asociado a detalles de pedidos.
     *
     * @param id identificador del producto que se eliminará
     * @throws RecursoNoEncontradoException si el producto no existe
     * @throws ResponseStatusException si el producto está asociado
     *         a un pedido
     */
    @Transactional
    public void deleteProduct(Long id) {
        Producto producto = buscarProducto(id);

        if (detallePedidoRepository.existsByProductoId(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede eliminar el producto porque está asociado a un pedido"
            );
        }

        productRepository.delete(producto);
    }

    /**
     * Recupera un producto por su identificador.
     *
     * @param id identificador del producto
     * @return entidad del producto encontrado
     * @throws RecursoNoEncontradoException si no existe un producto
     *         con el identificador indicado
     */
    private Producto buscarProducto(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un producto con el ID " + id
                ));
    }

    /**
     * Comprueba que los datos del producto cumplan las reglas
     * de validación del negocio.
     *
     * @param producto producto cuyos datos se validarán
     * @throws ResponseStatusException si algún dato es inválido
     */
    private void validarProducto(Producto producto) {
        if (producto == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Los datos del producto son obligatorios"
            );
        }

        if (producto.getNombre() == null
                || producto.getNombre().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El nombre es obligatorio"
            );
        }

        if (producto.getNombre().length() > 120) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El nombre no puede superar los 120 caracteres"
            );
        }

        if (producto.getDescripcion() != null
                && producto.getDescripcion().length() > 225) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La descripción no puede superar los 225 caracteres"
            );
        }

        if (producto.getPrecio() == null
                || producto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El precio debe ser mayor que cero"
            );
        }

        if (producto.getStock() == null || producto.getStock() < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El stock debe ser mayor o igual a cero"
            );
        }
    }
}
