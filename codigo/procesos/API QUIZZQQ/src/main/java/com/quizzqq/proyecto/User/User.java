package com.quizzqq.proyecto.User;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    private Integer id;
    private String usuario;  // Cambiado de username
    private String clave;    // Cambiado de password
    private String nombres;  // Cambiado de firstname
    private String apellidos; // Cambiado de lastname
    private Role role;
    private String estado;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return usuario;
    }

    @Override
    public String getPassword() {
        return clave;
    }

    @Override
    public boolean isAccountNonExpired() {
        return estado.equals("A");
    }

    @Override
    public boolean isAccountNonLocked() {
        return estado.equals("A");
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return estado.equals("A");
    }

    @Override
    public boolean isEnabled() {
        return estado.equals("A");
    }
}
