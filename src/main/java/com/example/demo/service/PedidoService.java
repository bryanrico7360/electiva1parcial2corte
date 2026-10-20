
package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.model.EstadoPedido;
import com.example.demo.model.Pedido;
import com.example.demo.model.PedidoItem;
import com.example.demo.repository.PedidoRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

/**
 * Contiene la lógica de negocio relacionada con los pedidos.
 *
 * Permite crear, consultar, modificar y eliminar pedidos,
 * además de gestionar sus productos y confirmar las compras.
 */
@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductRepository productRepository;

    /**
     * Construye el servicio con los repositorios necesarios.
     *
     * @param pedidoRepository repositorio de pedidos
     * @param productRepository repositorio de productos
     */
    public PedidoService(
            PedidoRepository pedidoRepository,
            ProductRepository productRepository) {
        this.pedidoRepository = pedidoRepository;
        this.productRepository = productRepository;
    }

    /**
     * Consulta todos los pedidos registrados.
     *
     * @return lista de pedidos
     */
    @Transactional(readOnly = true)
    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    /**
     * Busca un pedido por su identificador.
     *
     * @param id identificador del pedido
     * @return pedido encontrado
     * @throws ResponseStatusException si el pedido no existe
     */
    @Transactional(readOnly = true)
    public Pedido buscarPedido(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró el pedido con ID " + id
                ));
    }

    /**
     * Crea un pedido nuevo para un cliente.
     *
     * @param cliente nombre del cliente
     * @return pedido guardado
     */
    @Transactional
    public Pedido crearPedido(String cliente) {
        if (cliente == null || cliente.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El nombre del cliente es obligatorio"
            );
        }

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente.trim());
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setTotal(BigDecimal.ZERO);

        return pedidoRepository.save(pedido);
    }

    /**
     * Actualiza el nombre del cliente de un pedido pendiente.
     *
     * @param id identificador del pedido
     * @param cliente nuevo nombre del cliente
     * @return pedido actualizado
     */
    @Transactional
    public Pedido actualizarCliente(Long id, String cliente) {
        Pedido pedido = buscarPedido(id);
        validarPedidoPendiente(pedido);

        if (cliente == null || cliente.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El nombre del cliente es obligatorio"
            );
        }

        pedido.setCliente(cliente.trim());
        return pedidoRepository.save(pedido);
    }

    /**
     * Agrega un producto existente a un pedido pendiente.
     *
     * @param pedidoId identificador del pedido
     * @param productoId identificador del producto
     * @param cantidad cantidad que se agregará
     * @return pedido actualizado
     */
    @Transactional
    public Pedido agregarProducto(
            Long pedidoId,
            Long productoId,
            Integer cantidad) {

        Pedido pedido = buscarPedido(pedidoId);
        validarPedidoPendiente(pedido);

        if (cantidad == null || cantidad <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La cantidad debe ser mayor que cero"
            );
        }

        Product producto = productRepository.findById(productoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró el producto con ID " + productoId
                ));

        if (producto.getPrice() == null
                || producto.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El producto tiene un precio inválido"
            );
        }

        PedidoItem item = new PedidoItem();
        item.setProducto(producto);
        item.setCantidad(cantidad);
        item.setPrecioUnitario(producto.getPrice());

        pedido.agregarItem(item);
        pedido.actualizarTotal();

        return pedidoRepository.save(pedido);
    }

    /**
     * Retira un ítem de un pedido pendiente.
     *
     * @param pedidoId identificador del pedido
     * @param itemId identificador del ítem
     * @return pedido actualizado
     */
    @Transactional
    public Pedido retirarProducto(Long pedidoId, Long itemId) {
        Pedido pedido = buscarPedido(pedidoId);
        validarPedidoPendiente(pedido);

        PedidoItem item = pedido.getItems().stream()
                .filter(actual -> actual.getId() != null
                        && actual.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "El ítem no pertenece al pedido indicado"
                ));

        pedido.retirarItem(item);
        pedido.actualizarTotal();

        return pedidoRepository.save(pedido);
    }

    /**
     * Confirma un pedido y descuenta las existencias de los productos.
     *
     * @param id identificador del pedido
     * @return pedido confirmado
     */
    @Transactional
    public Pedido confirmarPedido(Long id) {
        Pedido pedido = buscarPedido(id);
        validarPedidoPendiente(pedido);

        if (pedido.getItems().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No se puede confirmar un pedido sin productos"
            );
        }

        for (PedidoItem item : pedido.getItems()) {
            Product producto = productRepository.findById(
                    item.getProducto().getId()
            ).orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No se encontró uno de los productos del pedido"
            ));

            int stockDisponible = producto.getStock() == null
                    ? 0 : producto.getStock();

            if (stockDisponible < item.getCantidad()) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Stock insuficiente para el producto: "
                                + producto.getName()
                );
            }

            producto.setStock(stockDisponible - item.getCantidad());
            productRepository.save(producto);
        }

        pedido.actualizarTotal();
        pedido.setEstado(EstadoPedido.CONFIRMADO);

        return pedidoRepository.save(pedido);
    }

    /**
     * Cancela un pedido que todavía está pendiente.
     *
     * @param id identificador del pedido
     * @return pedido cancelado
     */
    @Transactional
    public Pedido cancelarPedido(Long id) {
        Pedido pedido = buscarPedido(id);
        validarPedidoPendiente(pedido);

        pedido.setEstado(EstadoPedido.CANCELADO);
        return pedidoRepository.save(pedido);
    }

    /**
     * Elimina un pedido que todavía está pendiente.
     *
     * @param id identificador del pedido
     */
    @Transactional
    public void eliminarPedido(Long id) {
        Pedido pedido = buscarPedido(id);
        validarPedidoPendiente(pedido);
        pedidoRepository.delete(pedido);
    }

    /**
     * Verifica que un pedido pueda modificarse.
     *
     * @param pedido pedido que se validará
     */
    private void validarPedidoPendiente(Pedido pedido) {
        if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Solo se pueden modificar pedidos pendientes"
            );
        }
    }
}
