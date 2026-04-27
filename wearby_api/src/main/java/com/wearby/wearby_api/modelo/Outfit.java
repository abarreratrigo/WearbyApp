package com.wearby.wearby_api.modelo;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name="outfit")
public class Outfit {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="id_usuario")
    private Usuario usuario;

    private String nombre;

    @Column (name = "fecha_generacion")
    private LocalDateTime fechaGeneracion = LocalDateTime.now();

    @ManyToMany
    @JoinTable(
            name = "outfit_prenda",
            joinColumns = @JoinColumn(name="id_outfit"),
            inverseJoinColumns = @JoinColumn(name="id_prenda")
    )
    private List<Prenda> prendas;
}
