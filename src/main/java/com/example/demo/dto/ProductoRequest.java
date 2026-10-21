
package com.example.demo.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Representa los datos recibidos para crear o actualizar un producto.
 *
 * <p>Incluye validaciones para el nombre, la descripción, el precio
 * y las existencias del producto.</p>
 *
 * @param nombre nombre del producto
 * @param descripcion descripción del producto
 * @param precio precio unitario del producto
 * @param stock cantidad de unidades disponibles
 *
 * @author
 * @version 1.0
 */
public record ProductoRequest(

        @NotBlank(message = "El nombre es requerido")
        @Size(max = 120, message = "El nombre no puede superar los 120 caracteres")
        String nombre,

        @Size(max = 225, message = "La descripción no puede superar los 225 caracteres")
        String descripcion,

        @NotNull(message = "El precio es requerido")
        @Positive(message = "El precio debe ser mayor que cero")
        @Digits(integer = 10, fraction = 2)
        BigDecimal precio,

        @NotNull(message = "El stock es requerido")
        @PositiveOrZero(message = "El stock debe ser mayor o igual a cero")
        Integer stock

) {

    /**
     * Normaliza los textos recibidos eliminando espacios al inicio y al final.
     *
     * @param nombre nombre del producto recibido
     * @param descripcion descripción del producto recibida
     * @param precio precio unitario recibido
     * @param stock existencias recibidas
     */
    public ProductoRequest {
        if (nombre != null) {
            nombre = nombre.trim();
        }

        if (descripcion != null) {
            descripcion = descripcion.trim();
        }
    }
}
