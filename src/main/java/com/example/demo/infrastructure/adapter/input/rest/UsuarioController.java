// by from ia
package com.example.demo.infrastructure.adapter.input.rest;
import com.example.demo.domain.model.Usuario; import com.example.demo.domain.port.in.GestionUsuarioUseCase; import com.example.demo.infrastructure.adapter.input.rest.dto.UsuarioRequest;
import jakarta.validation.Valid; import org.springframework.http.HttpStatus; import org.springframework.http.ResponseEntity; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api/usuarios")
public class UsuarioController {
 private final GestionUsuarioUseCase useCase; public UsuarioController(GestionUsuarioUseCase useCase){this.useCase=useCase;}
 @GetMapping public ResponseEntity<List<Usuario>> listar(){return ResponseEntity.ok(useCase.consultarTodos());}
 @GetMapping("/{id}") public ResponseEntity<Usuario> buscar(@PathVariable Long id){return ResponseEntity.ok(useCase.consultarPorId(id));}
 @PostMapping public ResponseEntity<Usuario> registrar(@Valid @RequestBody UsuarioRequest request){return ResponseEntity.status(HttpStatus.CREATED).body(useCase.registrar(map(request)));}
 @PutMapping("/{id}") public ResponseEntity<Usuario> actualizar(@PathVariable Long id,@Valid @RequestBody UsuarioRequest request){return ResponseEntity.ok(useCase.actualizar(id,map(request)));}
 @DeleteMapping("/{id}") public ResponseEntity<Void> eliminar(@PathVariable Long id){useCase.eliminar(id);return ResponseEntity.noContent().build();}
 private Usuario map(UsuarioRequest r){Usuario u=new Usuario();u.setNombre(r.nombre());u.setEmail(r.email());u.setTelefono(r.telefono());return u;}
}
