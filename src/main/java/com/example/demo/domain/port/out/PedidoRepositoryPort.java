// by from ia
package com.example.demo.domain.port.out;

import com.example.demo.domain.model.Pedido;
import java.util.List;
import java.util.Optional;
public interface PedidoRepositoryPort {
 List<Pedido> findAll(); Optional<Pedido> findById(Long id);
 Pedido save(Pedido pedido); void delete(Pedido pedido);
}
