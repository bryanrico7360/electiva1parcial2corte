
package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Representa un artículo solicitado dentro de un pedido.
 *
 * @param productoId identificador del producto que se desea agregar
 * @param cantidad unidades del producto solicitadas
 *
 * @author
 * @version 1.0
 */
public record PedidoItemRequest(

        @NotNull(message = "El producto es obligatorio")
        @Positive(message = "El ID del producto debe ser positivo")
        Long productoId,

        @NotNull(message = "La cantidad es obligatoria")
        @Positive(message = "La cantidad debe ser mayor que cero")
        Integer cantidad

) {
}
