package com.quizzqq.proyecto.Auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    private String usuario;  // Cambiado de username
    private String clave;    // Cambiado de password
}
