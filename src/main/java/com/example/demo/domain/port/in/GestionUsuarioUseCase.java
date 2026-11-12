// by from ia
package com.example.demo.domain.port.in;
import com.example.demo.domain.model.Usuario; import java.util.List;
public interface GestionUsuarioUseCase { Usuario registrar(Usuario usuario); List<Usuario> consultarTodos(); Usuario consultarPorId(Long id); Usuario actualizar(Long id,Usuario usuario); void eliminar(Long id); }
