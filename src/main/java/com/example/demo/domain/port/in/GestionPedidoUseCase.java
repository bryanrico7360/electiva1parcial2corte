// by from ia
package com.example.demo.domain.port.in;
import com.example.demo.domain.model.Pedido;
import com.example.demo.infrastructure.adapter.input.rest.dto.PedidoRequest;
import java.util.List;
public interface GestionPedidoUseCase {
 List<Pedido> listarPedidos(); Pedido buscarPedido(Long id); Pedido crearPedido(PedidoRequest request);
 Pedido actualizarPedido(Long id,PedidoRequest request); Pedido agregarProducto(Long pedidoId,Long productoId,Integer cantidad);
 Pedido retirarProducto(Long pedidoId,Long itemId); Pedido confirmarPedido(Long id); Pedido cancelarPedido(Long id); void eliminarPedido(Long id);
}
