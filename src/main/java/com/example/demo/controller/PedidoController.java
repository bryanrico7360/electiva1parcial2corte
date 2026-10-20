
package com.example.demo.controller;

import com.example.demo.model.Pedido;
import com.example.demo.service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Expone los endpoints REST para administrar pedidos.
 */
@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    /**
     * Construye el controlador con el servicio de pedidos.
     *
     * @param pedidoService servicio encargado de la lógica de pedidos
     */
    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    /**
     * Consulta todos los pedidos registrados.
     *
     * @return lista de pedidos
     */
    @GetMapping
    public ResponseEntity<List<Pedido>> listarPedidos() {
        return ResponseEntity.ok(pedidoService.listarPedidos());
    }

    /**
     * Consulta un pedido por su identificador.
     *
     * @param id identificador del pedido
     * @return pedido encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPedido(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscarPedido(id));
    }

    /**
     * Crea un pedido nuevo.
     *
     * @param cliente nombre del cliente
     * @return pedido creado
     */
    @PostMapping
    public ResponseEntity<Pedido> crearPedido(
            @RequestParam String cliente) {

        Pedido pedido = pedidoService.crearPedido(cliente);

        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }

    /**
     * Actualiza el nombre del cliente de un pedido pendiente.
     *
     * @param id identificador del pedido
     * @param cliente nuevo nombre del cliente
     * @return pedido actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<Pedido> actualizarCliente(
            @PathVariable Long id,
            @RequestParam String cliente) {

        return ResponseEntity.ok(
                pedidoService.actualizarCliente(id, cliente)
        );
    }

    /**
     * Agrega un producto a un pedido pendiente.
     *
     * @param id identificador del pedido
     * @param productoId identificador del producto
     * @param cantidad cantidad del producto
     * @return pedido actualizado
     */
    @PostMapping("/{id}/items")
    public ResponseEntity<Pedido> agregarProducto(
            @PathVariable Long id,
            @RequestParam Long productoId,
            @RequestParam Integer cantidad) {

        return ResponseEntity.ok(
                pedidoService.agregarProducto(id, productoId, cantidad)
        );
    }

    /**
     * Retira un producto de un pedido pendiente.
     *
     * @param id identificador del pedido
     * @param itemId identificador del ítem
     * @return pedido actualizado
     */
    @DeleteMapping("/{id}/items/{itemId}")
    public ResponseEntity<Pedido> retirarProducto(
            @PathVariable Long id,
            @PathVariable Long itemId) {

        return ResponseEntity.ok(
                pedidoService.retirarProducto(id, itemId)
        );
    }

    /**
     * Confirma un pedido y descuenta el inventario correspondiente.
     *
     * @param id identificador del pedido
     * @return pedido confirmado
     */
    @PostMapping("/{id}/confirmar")
    public ResponseEntity<Pedido> confirmarPedido(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.confirmarPedido(id));
    }

    /**
     * Cancela un pedido pendiente.
     *
     * @param id identificador del pedido
     * @return pedido cancelado
     */
    @PostMapping("/{id}/cancelar")
    public ResponseEntity<Pedido> cancelarPedido(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.cancelarPedido(id));
    }

    /**
     * Elimina un pedido pendiente.
     *
     * @param id identificador del pedido
     * @return respuesta sin contenido
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {
        pedidoService.eliminarPedido(id);
        return ResponseEntity.noContent().build();
    }
}
