package com.wearby.modelo;

/**
 * Modelo que representa un usuario de la aplicación
 * Mapea la respuesta JSON de la API REST
 */

public class Usuario {
    private Integer id;
    private String nombre;
    private String email;
    private String rol;
    private Boolean activo;

    public Integer getId() {return id;}
    public String getNombre() {return nombre;}
    public String getEmail() {return email;}
    public String getRol() {return rol;}
    public Boolean getActivo() {return activo;}
}
