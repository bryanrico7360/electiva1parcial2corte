
package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Representa un producto incluido en un pedido.
 * Conserva la cantidad solicitada, el precio unitario
 * y el subtotal correspondiente.
 *
 * @author
 * @version 1.0
 */
@Entity
@Table(name = "detalle_pedidos")
@Getter
@Setter
@NoArgsConstructor
public class DetallePedido {

    /**
     * Identificador único del detalle del pedido.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Pedido al que pertenece este detalle.
     */
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    /**
     * Producto asociado al detalle.
     */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    /**
     * Cantidad de unidades solicitadas del producto.
     */
    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor que cero")
    @Column(nullable = false)
    private Integer cantidad;

    /**
     * Precio unitario del producto al momento de agregarlo
     * al pedido.
     */
    @NotNull(message = "El precio unitario es obligatorio")
    @PositiveOrZero(
            message = "El precio unitario no puede ser negativo"
    )
    @Column(
            name = "precio_unitario",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal precioUnitario;

    /**
     * Subtotal calculado a partir del precio unitario
     * y la cantidad solicitada.
     */
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    /**
     * Calcula el subtotal multiplicando el precio unitario
     * por la cantidad solicitada.
     *
     * @return subtotal del detalle del pedido
     * @throws IllegalStateException si faltan datos o
     *         la cantidad no es válida
     */
    public BigDecimal calcularSubtotal() {
        if (precioUnitario == null || cantidad == null || cantidad <= 0) {
            throw new IllegalStateException(
                    "El precio unitario y una cantidad positiva "
                            + "son necesarios para calcular el subtotal"
            );
        }

        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }

    /**
     * Actualiza el subtotal antes de insertar o modificar
     * el registro en la base de datos.
     */
    @PrePersist
    @PreUpdate
    public void actualizarSubtotal() {
        subtotal = calcularSubtotal();
    }
}
