// by from ia
package com.example.demo.infrastructure.adapter.output;
import com.example.demo.domain.model.Usuario; import com.example.demo.domain.port.out.UsuarioRepositoryPort;
import com.example.demo.infrastructure.persistence.repository.UsuarioJpaRepository;
import org.springframework.stereotype.Component; import java.util.List; import java.util.Optional;
@Component public class UsuarioPersistenceAdapter implements UsuarioRepositoryPort {
 private final UsuarioJpaRepository repository;
 public UsuarioPersistenceAdapter(UsuarioJpaRepository repository){this.repository=repository;}
 public List<Usuario> findAll(){return repository.findAll();}
 public Optional<Usuario> findById(Long id){return repository.findById(id);}
 public Optional<Usuario> findByEmail(String email){return repository.findByEmailIgnoreCase(email);}
 public Usuario save(Usuario usuario){return repository.save(usuario);}
 public void delete(Usuario usuario){repository.delete(usuario);}
}
