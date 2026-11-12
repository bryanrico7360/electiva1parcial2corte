// by from ia
package com.example.demo.infrastructure.adapter.output;
import com.example.demo.domain.model.Pedido;
import com.example.demo.domain.port.out.PedidoRepositoryPort;
import com.example.demo.infrastructure.persistence.repository.PedidoRepository;
import org.springframework.stereotype.Component;
import java.util.List; import java.util.Optional;
@Component
public class PedidoPersistenceAdapter implements PedidoRepositoryPort {
 private final PedidoRepository repository;
 public PedidoPersistenceAdapter(PedidoRepository repository){this.repository=repository;}
 public List<Pedido> findAll(){return repository.findAll();}
 public Optional<Pedido> findById(Long id){return repository.findById(id);}
 public Pedido save(Pedido pedido){return repository.save(pedido);}
 public void delete(Pedido pedido){repository.delete(pedido);}
}
