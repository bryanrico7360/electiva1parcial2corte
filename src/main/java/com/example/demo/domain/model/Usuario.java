// by from ia
package com.example.demo.domain.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter; import lombok.Setter; import lombok.NoArgsConstructor;
@Entity @Table(name="usuarios") @Getter @Setter @NoArgsConstructor
public class Usuario {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @NotBlank @Size(max=120) @Column(nullable=false,length=120) private String nombre;
 @Email @NotBlank @Size(max=160) @Column(nullable=false,length=160,unique=true) private String email;
 @Size(max=30) @Column(length=30) private String telefono;
}
