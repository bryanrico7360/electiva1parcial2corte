
package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un pedido realizado por un cliente.
 * Administra su estado, sus detalles y el total de la compra.
 *
 * @author
 * @version 1.0
 */
@Entity
@Table(name = "pedidos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String cliente;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private EstadoPedido estado = EstadoPedido.CARRITO;

    @Column(nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal total = BigDecimal.ZERO;

    @JsonManagedReference
    @OneToMany(
            mappedBy = "pedido",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<DetallePedido> items = new ArrayList<>();

    /**
     * Inicializa la fecha del pedido antes de persistirlo,
     * siempre que todavía no tenga una fecha asignada.
     */
    @PrePersist
    public void inicializarFecha() {
        if (fecha == null) {
            fecha = LocalDateTime.now();
        }
    }

    /**
     * Agrega un detalle al pedido, establece la relación
     * entre ambas entidades y actualiza el total.
     *
     * @param item detalle que se agregará al pedido
     * @throws IllegalArgumentException si el detalle es nulo
     */
    public void agregarItem(DetallePedido item) {
        if (item == null) {
            throw new IllegalArgumentException(
                    "El ítem no puede ser nulo"
            );
        }

        items.add(item);
        item.setPedido(this);
        actualizarTotal();
    }

    /**
     * Retira un detalle del pedido y actualiza el total.
     *
     * @param item detalle que se desea retirar
     * @return true si se retiró el detalle; false si era nulo
     *         o no pertenecía a la lista
     */
    public boolean retirarItem(DetallePedido item) {
        if (item == null || !items.remove(item)) {
            return false;
        }

        item.setPedido(null);
        actualizarTotal();
        return true;
    }

    /**
     * Calcula el total del pedido sumando los subtotales
     * de todos sus detalles.
     *
     * @return suma de los subtotales del pedido
     */
    public BigDecimal calcularTotal() {
        return items.stream()
                .map(DetallePedido::calcularSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Actualiza el atributo total con el valor calculado
     * a partir de los detalles actuales del pedido.
     */
    public void actualizarTotal() {
        this.total = calcularTotal();
    }
}
