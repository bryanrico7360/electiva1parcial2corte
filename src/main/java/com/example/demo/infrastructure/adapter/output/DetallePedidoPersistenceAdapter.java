// by from ia
package com.example.demo.infrastructure.adapter.output;
import com.example.demo.domain.port.out.DetallePedidoRepositoryPort;
import com.example.demo.infrastructure.persistence.repository.DetallePedidoRepository;
import org.springframework.stereotype.Component;
@Component
public class DetallePedidoPersistenceAdapter implements DetallePedidoRepositoryPort {
 private final DetallePedidoRepository repository;
 public DetallePedidoPersistenceAdapter(DetallePedidoRepository repository){this.repository=repository;}
 public boolean existsByProductoId(Long id){return repository.existsByProductoId(id);}
}
