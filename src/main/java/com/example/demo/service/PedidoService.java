
package com.example.demo.service;

import com.example.demo.dto.PedidoItemRequest;
import com.example.demo.dto.PedidoRequest;
import com.example.demo.model.DetallePedido;
import com.example.demo.model.EstadoPedido;
import com.example.demo.model.Pedido;
import com.example.demo.model.Producto;
import com.example.demo.repository.PedidoRepository;
import com.example.demo.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Servicio encargado de gestionar los pedidos de la tienda.
 *
 * <p>Implementa la creación, actualización, consulta, eliminación,
 * modificación de artículos y confirmación de pedidos, incluyendo
 * el control transaccional del inventario.</p>
 */
@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;

    /**
     * Lista todos los pedidos registrados.
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
     * Crea un pedido con el cliente y los artículos recibidos.
     *
     * @param request datos del cliente y artículos solicitados
     * @return pedido creado
     */
    @Transactional
    public Pedido crearPedido(PedidoRequest request) {
        validarSolicitud(request);
        validarCliente(request.cliente());

        Pedido pedido = new Pedido();
        pedido.setCliente(request.cliente().trim());
        pedido.setEstado(EstadoPedido.CARRITO);
        pedido.setTotal(BigDecimal.ZERO);

        for (PedidoItemRequest item : request.items()) {
            pedido.agregarItem(crearDetalle(item));
        }

        pedido.actualizarTotal();
        return pedidoRepository.save(pedido);
    }

    /**
     * Actualiza el cliente y reemplaza los artículos de un carrito.
     *
     * @param id identificador del pedido
     * @param request nuevos datos del cliente y artículos
     * @return pedido actualizado
     */
    @Transactional
    public Pedido actualizarPedido(Long id, PedidoRequest request) {
        Pedido pedido = buscarPedido(id);
        validarPedidoCarrito(pedido);
        validarSolicitud(request);
        validarCliente(request.cliente());

        pedido.setCliente(request.cliente().trim());

        // Retirar los detalles anteriores manteniendo la colección existente.
        for (DetallePedido item : new ArrayList<>(pedido.getItems())) {
            pedido.retirarItem(item);
        }

        // Incorporar artículos con precios obtenidos de la base de datos.
        for (PedidoItemRequest item : request.items()) {
            pedido.agregarItem(crearDetalle(item));
        }

        pedido.actualizarTotal();
        return pedidoRepository.save(pedido);
    }

    /**
     * Agrega un artículo a un pedido que se encuentra en estado CARRITO.
     *
     * @param pedidoId identificador del pedido
     * @param productoId identificador del producto
     * @param cantidad cantidad solicitada
     * @return pedido actualizado
     */
    @Transactional
    public Pedido agregarProducto(
            Long pedidoId,
            Long productoId,
            Integer cantidad
    ) {
        Pedido pedido = buscarPedido(pedidoId);
        validarPedidoCarrito(pedido);

        if (productoId == null || cantidad == null || cantidad <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El producto es obligatorio y la cantidad debe ser mayor que cero"
            );
        }

        PedidoItemRequest request =
                new PedidoItemRequest(productoId, cantidad);

        pedido.agregarItem(crearDetalle(request));
        pedido.actualizarTotal();

        return pedidoRepository.save(pedido);
    }

    /**
     * Retira un artículo de un pedido que se encuentra en estado CARRITO.
     *
     * @param pedidoId identificador del pedido
     * @param itemId identificador del detalle
     * @return pedido actualizado
     */
    @Transactional
    public Pedido retirarProducto(Long pedidoId, Long itemId) {
        Pedido pedido = buscarPedido(pedidoId);
        validarPedidoCarrito(pedido);

        DetallePedido item = pedido.getItems().stream()
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
     * Confirma un pedido y descuenta el inventario correspondiente.
     *
     * <p>Primero valida todas las cantidades y comprueba que exista
     * stock suficiente para cada producto. Solo después de superar
     * todas las validaciones descuenta el inventario. La transacción
     * permite revertir los cambios si ocurre un error.</p>
     *
     * @param id identificador del pedido
     * @return pedido confirmado
     * @throws ResponseStatusException si el pedido no existe,
     *         no está en estado CARRITO o no hay stock suficiente
     */
    @Transactional
    public Pedido confirmarPedido(Long id) {
        Pedido pedido = buscarPedido(id);
        validarPedidoCarrito(pedido);

        if (pedido.getItems().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No se puede confirmar un pedido sin productos"
            );
        }

        // Agrupar cantidades y procesar los productos por ID.
        Map<Long, Integer> cantidadesPorProducto = new TreeMap<>();

        for (DetallePedido item : pedido.getItems()) {
            if (item.getProducto() == null
                    || item.getProducto().getId() == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Todos los ítems deben tener un producto válido"
                );
            }

            if (item.getCantidad() == null || item.getCantidad() <= 0) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "La cantidad de cada producto debe ser mayor que cero"
                );
            }

            Long productoId = item.getProducto().getId();

            try {
                cantidadesPorProducto.merge(
                        productoId,
                        item.getCantidad(),
                        Math::addExact
                );
            } catch (ArithmeticException ex) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "La cantidad total de un producto es demasiado grande"
                );
            }
        }

        // Bloquear y validar todos los productos antes de descontar inventario.
        Map<Long, Producto> productosBloqueados = new TreeMap<>();

        for (Map.Entry<Long, Integer> entrada
                : cantidadesPorProducto.entrySet()) {

            Long productoId = entrada.getKey();
            int cantidadNecesaria = entrada.getValue();

            Producto producto = productoRepository
                    .buscarPorIdConBloqueo(productoId)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "No se encontró el producto con ID " + productoId
                    ));

            int stockDisponible = producto.getStock() == null
                    ? 0
                    : producto.getStock();

            // CORRECCIÓN: el parcial exige 400 si el stock es insuficiente.
            if (stockDisponible < cantidadNecesaria) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Stock insuficiente para el producto: "
                                + producto.getNombre()
                );
            }

            productosBloqueados.put(productoId, producto);
        }

        // Descontar solo cuando todo el pedido tenga stock suficiente.
        for (Map.Entry<Long, Integer> entrada
                : cantidadesPorProducto.entrySet()) {

            Producto producto = productosBloqueados.get(entrada.getKey());

            int stockActual = producto.getStock() == null
                    ? 0
                    : producto.getStock();

            producto.setStock(stockActual - entrada.getValue());
        }

        pedido.actualizarTotal();
        pedido.setEstado(EstadoPedido.CONFIRMADO);

        return pedidoRepository.save(pedido);
    }

    /**
     * Cancela un pedido que todavía está en estado CARRITO.
     *
     * @param id identificador del pedido
     * @return pedido cancelado
     */
    @Transactional
    public Pedido cancelarPedido(Long id) {
        Pedido pedido = buscarPedido(id);
        validarPedidoCarrito(pedido);

        pedido.setEstado(EstadoPedido.CANCELADO);
        return pedidoRepository.save(pedido);
    }

    /**
     * Elimina un pedido existente.
     *
     * @param id identificador del pedido que se eliminará
     * @throws ResponseStatusException si el pedido no existe
     */
    @Transactional
    public void eliminarPedido(Long id) {
        Pedido pedido = buscarPedido(id);
        pedidoRepository.delete(pedido);
    }

    /**
     * Construye un detalle usando el producto y su precio actual.
     *
     * @param request datos del artículo solicitado
     * @return detalle de pedido
     */
    private DetallePedido crearDetalle(PedidoItemRequest request) {
        if (request == null
                || request.productoId() == null
                || request.cantidad() == null
                || request.cantidad() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cada artículo debe tener un producto válido "
                            + "y una cantidad mayor que cero"
            );
        }

        Producto producto = productoRepository.findById(request.productoId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró el producto con ID "
                                + request.productoId()
                ));

        if (producto.getPrecio() == null
                || producto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El precio del producto debe ser mayor que cero"
            );
        }

        DetallePedido detalle = new DetallePedido();
        detalle.setProducto(producto);
        detalle.setCantidad(request.cantidad());
        detalle.setPrecioUnitario(producto.getPrecio());

        return detalle;
    }

    /**
     * Valida que la solicitud de pedido tenga los datos obligatorios.
     *
     * @param request solicitud recibida
     */
    private void validarSolicitud(PedidoRequest request) {
        if (request == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Los datos del pedido son obligatorios"
            );
        }

        if (request.items() == null || request.items().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El pedido debe contener al menos un artículo"
            );
        }
    }

    /**
     * Valida el nombre del cliente.
     *
     * @param cliente nombre del cliente
     */
    private void validarCliente(String cliente) {
        if (cliente == null || cliente.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El nombre del cliente es obligatorio"
            );
        }

        if (cliente.trim().length() > 120) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El nombre del cliente no puede superar los 120 caracteres"
            );
        }
    }

    /**
     * Comprueba que el pedido pueda modificarse.
     *
     * @param pedido pedido que se va a validar
     * @throws ResponseStatusException si no está en estado CARRITO
     */
    private void validarPedidoCarrito(Pedido pedido) {
        if (pedido.getEstado() != EstadoPedido.CARRITO) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Solo se pueden modificar pedidos en estado CARRITO"
            );
        }
    }
}
