package com.wearby.wearby_api.modelo;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table (name= "prenda")
public class Prenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="id_usuario")
    private Usuario usuario;

    private String nombre;

    @Column(name="imagen_url")
    private String imagenUrl;

    @ManyToOne
    @JoinColumn(name="id_categoria")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "id_color")
    private Color color;

    @ManyToOne
    @JoinColumn(name = "id_estilo")
    private Estilo estilo;

    @ManyToOne
    @JoinColumn(name = "id_formalidad")
    private Formalidad formalidad;

    @ManyToOne
    @JoinColumn(name = "id_temporada")
    private Temporada temporada;

    private String notas;

    private Boolean favorito = false;
}
