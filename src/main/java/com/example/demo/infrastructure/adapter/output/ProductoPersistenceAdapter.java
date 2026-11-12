// by from ia
package com.example.demo.infrastructure.adapter.output;
import com.example.demo.domain.model.Producto;
import com.example.demo.domain.port.out.ProductoRepositoryPort;
import com.example.demo.infrastructure.persistence.repository.ProductoRepository;
import org.springframework.stereotype.Component;
import java.util.List; import java.util.Optional;
@Component
public class ProductoPersistenceAdapter implements ProductoRepositoryPort {
 private final ProductoRepository repository;
 public ProductoPersistenceAdapter(ProductoRepository repository){this.repository=repository;}
 public List<Producto> findAll(){return repository.findAll();}
 public Optional<Producto> findById(Long id){return repository.findById(id);}
 public Producto save(Producto producto){return repository.save(producto);}
 public void delete(Producto producto){repository.delete(producto);}
 public Optional<Producto> buscarPorIdConBloqueo(Long id){return repository.buscarPorIdConBloqueo(id);}
}
