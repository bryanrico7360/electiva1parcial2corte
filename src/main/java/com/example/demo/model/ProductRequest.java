package com.example.demo.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Digits;

import java.math.BigDecimal;
public record ProductRequest(
    @NotBlank(message = "El nombre es requerido")
    @Size(max = 100, message = "El nombre no puede superar la cantidad de 100 caracteres")
    String name,

    @NotNull
    @Positive
    @Digits(integer = 10, fraction = 2)
    BigDecimal price
) 

{
    public ProductRequest {
        if(name != null) {
            name = name.trim();
        }
    }
}