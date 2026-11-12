// by from ia
package com.example.demo.domain.port.out;
import com.example.demo.domain.model.Usuario; import java.util.List; import java.util.Optional;
public interface UsuarioRepositoryPort { List<Usuario> findAll(); Optional<Usuario> findById(Long id); Optional<Usuario> findByEmail(String email); Usuario save(Usuario usuario); void delete(Usuario usuario); }
