
package com.example.demo.dto;

import com.example.demo.model.Producto;

import java.math.BigDecimal;

/**
 * Representa los datos de un producto que se devuelven en las respuestas
 * de la API REST.
 *
 * @param id identificador del producto
 * @param nombre nombre del producto
 * @param descripcion descripción del producto
 * @param precio precio unitario del producto
 * @param stock cantidad de unidades disponibles
 *
 * @author
 * @version 1.0
 */
public record ProductoResponse(
        Long id,
        String nombre,
        String descripcion,
        BigDecimal precio,
        Integer stock
) {

    /**
     * Convierte una entidad Producto en un objeto de respuesta.
     *
     * @param producto entidad del producto que se convertirá
     * @return representación del producto para la respuesta de la API
     */
    public static ProductoResponse from(Producto producto) {
        return new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock()
        );
    }
}
