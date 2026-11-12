// by from ia
package com.example.demo.infrastructure.adapter.input.rest.dto;
import jakarta.validation.constraints.Email; import jakarta.validation.constraints.NotBlank; import jakarta.validation.constraints.Size;
public record UsuarioRequest(@NotBlank @Size(max=120) String nombre,@NotBlank @Email @Size(max=160) String email,@Size(max=30) String telefono) {}
