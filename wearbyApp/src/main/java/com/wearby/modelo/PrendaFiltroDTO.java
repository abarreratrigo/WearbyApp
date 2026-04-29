package com.wearby.modelo;

/**
 * DTO con los datos de una prenda para mostrarla
 * en el carrusel de generación
 */

public class PrendaFiltroDTO {
    private Integer id;
    private String nombre;
    private String imagenUrl;
    private Boolean favorito;
    private String categoria;
    private String estilo;
    private String color;
    private String temporada;
    private String formalidad;

    public Integer getId(){return id;}
    public String getNombre(){return nombre;}
    public String getImagenUrl(){return imagenUrl;}
    public Boolean getFavorito(){return favorito;}
    public String getCategoria() {return categoria;}
    public String getColor() {return color;}
}
