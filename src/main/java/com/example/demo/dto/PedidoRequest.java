
package com.example.demo.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Representa los datos recibidos para crear o actualizar un pedido.
 *
 * @param cliente nombre del cliente que realiza el pedido
 * @param items lista de artículos que conforman el pedido
 *
 * @author
 * @version 1.0
 */
public record PedidoRequest(

        @NotBlank(message = "El cliente es obligatorio")
        @Size(max = 120, message = "El cliente no puede superar 120 caracteres")
        String cliente,

        @NotEmpty(message = "El pedido debe contener al menos un artículo")
        List<@Valid PedidoItemRequest> items

) {
}
