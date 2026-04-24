package com.wearby.wearby_api.modelo;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;

    @Column(unique = true)
    private String email;

    private String contrasena;

    @Enumerated(EnumType.STRING)
    private Rol rol = Rol.usuario;

    private Boolean activo = true;

    public enum Rol { usuario, admin}
}
