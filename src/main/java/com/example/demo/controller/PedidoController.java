
package com.example.demo.controller;

import com.example.demo.dto.PedidoRequest;
import com.example.demo.model.Pedido;
import com.example.demo.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Controlador REST encargado de gestionar las operaciones
 * relacionadas con los pedidos de la tienda.
 *
 * @author
 * @version 1.0
 */
@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    /**
     * Consulta todos los pedidos registrados.
     *
     * @return respuesta HTTP 200 con la lista de pedidos
     */
    @GetMapping
    public ResponseEntity<List<Pedido>> listarPedidos() {
        return ResponseEntity.ok(pedidoService.listarPedidos());
    }

    /**
     * Consulta un pedido mediante su identificador.
     *
     * @param id identificador del pedido
     * @return respuesta HTTP 200 con el pedido encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPedido(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscarPedido(id));
    }

    /**
     * Crea un pedido con los artículos solicitados.
     *
     * @param request datos del cliente y de los artículos del pedido
     * @return respuesta HTTP 201 con el pedido creado
     */
    @PostMapping
    public ResponseEntity<Pedido> crearPedido(
            @Valid @RequestBody PedidoRequest request
    ) {
        Pedido pedido = pedidoService.crearPedido(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }

    /**
     * Actualiza el cliente y los artículos de un pedido
     * que se encuentra en estado carrito.
     *
     * @param id identificador del pedido
     * @param request nuevos datos del pedido
     * @return respuesta HTTP 200 con el pedido actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<Pedido> actualizarPedido(
            @PathVariable Long id,
            @Valid @RequestBody PedidoRequest request
    ) {
        return ResponseEntity.ok(
                pedidoService.actualizarPedido(id, request)
        );
    }

    /**
     * Agrega un producto a un pedido.
     *
     * @param id identificador del pedido
     * @param productoId identificador del producto que se agregará
     * @param cantidad cantidad de unidades solicitadas
     * @return respuesta HTTP 200 con el pedido actualizado
     */
    @PostMapping("/{id}/items")
    public ResponseEntity<Pedido> agregarProducto(
            @PathVariable Long id,
            @RequestParam Long productoId,
            @RequestParam Integer cantidad
    ) {
        return ResponseEntity.ok(
                pedidoService.agregarProducto(id, productoId, cantidad)
        );
    }

    /**
     * Retira un artículo de un pedido.
     *
     * @param id identificador del pedido
     * @param itemId identificador del detalle que se retirará
     * @return respuesta HTTP 200 con el pedido actualizado
     */
    @DeleteMapping("/{id}/items/{itemId}")
    public ResponseEntity<Pedido> retirarProducto(
            @PathVariable Long id,
            @PathVariable Long itemId
    ) {
        return ResponseEntity.ok(
                pedidoService.retirarProducto(id, itemId)
        );
    }

    /**
     * Confirma un pedido y descuenta las unidades correspondientes
     * del inventario.
     *
     * @param id identificador del pedido que se confirmará
     * @return respuesta HTTP 200 con el pedido confirmado
     */
    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<Pedido> confirmarPedido(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.confirmarPedido(id));
    }

    /**
     * Elimina un pedido existente.
     *
     * @param id identificador del pedido que se eliminará
     * @return respuesta HTTP 204 sin contenido
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {
        pedidoService.eliminarPedido(id);
        return ResponseEntity.noContent().build();
    }
}
