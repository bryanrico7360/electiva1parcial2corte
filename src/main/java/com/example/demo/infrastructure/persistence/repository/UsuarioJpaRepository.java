// by from ia
package com.example.demo.infrastructure.persistence.repository;
import com.example.demo.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface UsuarioJpaRepository extends JpaRepository<Usuario,Long> { Optional<Usuario> findByEmailIgnoreCase(String email); boolean existsByEmailIgnoreCase(String email); }
