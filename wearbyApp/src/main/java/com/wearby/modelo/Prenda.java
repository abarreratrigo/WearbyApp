package com.wearby.modelo;

/**
 * Modelo que representa una prenda del armario.
 * Mapea la respuesta de la API REST
 */
public class Prenda {
    private Integer id;
    private String nombre;
    private String imagenUrl;
    private Boolean favorito;
    private Categoria categoria;
    private Color color;
    private Estilo estilo;
    private Formalidad formalidad;
    private Temporada temporada;
    private String notas;

    public Integer getId() {return id;}
    public String getNombre() {return nombre;}
    public String getImageUrl() {return imagenUrl;}
    public Boolean getFavorito() {return favorito;}
    public Categoria getCategoria() {return categoria;}
    public Color getColor() {return color;}
    public Estilo getEstilo() {return estilo;}
    public Formalidad getFormalidad() {return formalidad;}
    public Temporada getTemporada() {return temporada;}
    public String getNotas() {return notas;}
    public void setId(Integer id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setImageUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public void setColor(com.wearby.modelo.Color color) { this.color = color; }
    public void setEstilo(Estilo estilo) { this.estilo = estilo; }
    public void setFormalidad(Formalidad formalidad) { this.formalidad = formalidad; }
    public void setTemporada(Temporada temporada) { this.temporada = temporada; }
    public void setNotas(String notas) { this.notas = notas; }
    public void setFavorito(Boolean favorito) {this.favorito = favorito;}
}
