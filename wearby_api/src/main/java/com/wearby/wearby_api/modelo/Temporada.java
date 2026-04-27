package com.wearby.wearby_api.modelo;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="temporada")
public class Temporada {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
}
