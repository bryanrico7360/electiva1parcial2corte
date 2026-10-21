
package com.example.demo.model;

/**
 * Define los estados posibles de un pedido dentro de la tienda.
 *
 * @author
 * @version 1.0
 */
public enum EstadoPedido {

    /**
     * Pedido en preparación que todavía puede modificarse.
     */
    CARRITO,

    /**
     * Pedido confirmado y con el inventario descontado.
     */
    CONFIRMADO,

    /**
     * Pedido cancelado.
     */
    CANCELADO
}
