// by from ia
package com.example.demo.domain.port.out;

import com.example.demo.domain.model.Producto;
import java.util.List;
import java.util.Optional;
public interface ProductoRepositoryPort {
 List<Producto> findAll(); Optional<Producto> findById(Long id);
 Producto save(Producto producto); void delete(Producto producto);
 Optional<Producto> buscarPorIdConBloqueo(Long id);
}
