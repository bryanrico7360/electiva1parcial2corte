// by from ia
package com.example.demo.application.usecase;
import com.example.demo.domain.model.Usuario; import com.example.demo.domain.port.in.GestionUsuarioUseCase; import com.example.demo.domain.port.out.UsuarioRepositoryPort;
import com.example.demo.infrastructure.exception.RecursoNoEncontradoException;
import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service public class UsuarioService implements GestionUsuarioUseCase {
 private final UsuarioRepositoryPort repository;
 public UsuarioService(UsuarioRepositoryPort repository){this.repository=repository;}
 @Override @Transactional public Usuario registrar(Usuario usuario){
  validar(usuario); if(repository.findByEmail(usuario.getEmail().trim()).isPresent()) throw new IllegalArgumentException("Ya existe un usuario con ese correo electrónico");
  usuario.setId(null); usuario.setNombre(usuario.getNombre().trim()); usuario.setEmail(usuario.getEmail().trim().toLowerCase()); return repository.save(usuario);
 }
 @Override @Transactional(readOnly=true) public List<Usuario> consultarTodos(){return repository.findAll();}
 @Override @Transactional(readOnly=true) public Usuario consultarPorId(Long id){return repository.findById(id).orElseThrow(()->new RecursoNoEncontradoException("No existe un usuario con el ID "+id));}
 @Override @Transactional public Usuario actualizar(Long id,Usuario datos){validar(datos); Usuario actual=consultarPorId(id); repository.findByEmail(datos.getEmail().trim()).filter(u->!u.getId().equals(id)).ifPresent(u->{throw new IllegalArgumentException("El correo electrónico ya está registrado");}); actual.setNombre(datos.getNombre().trim()); actual.setEmail(datos.getEmail().trim().toLowerCase()); actual.setTelefono(datos.getTelefono()); return repository.save(actual);}
 @Override @Transactional public void eliminar(Long id){repository.delete(consultarPorId(id));}
 private void validar(Usuario u){if(u==null||u.getNombre()==null||u.getNombre().isBlank())throw new IllegalArgumentException("El nombre es obligatorio"); if(u.getEmail()==null||(!u.getEmail().contains("@") || !u.getEmail().substring(u.getEmail().indexOf("@") + 1).contains(".")))throw new IllegalArgumentException("El correo electrónico no es válido");}
}
