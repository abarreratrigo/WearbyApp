package com.wearby.wearby_api.dto;

import lombok.Data;

/**
 * DTO para transferir datos de prendas en el proceso
 * de generación de outfits. Evita exponer la entidad
 * completa y reduce el acoplamiento entre capas.
 */

@Data
public class PrendaFiltroDTO {
    private Integer id;
    private String nombre;
    private String imagenUrl;
    private Boolean favorito;
    private String categoria;
    private String estilo;
    private String temporada;
    private String formalidad;
    private String color;
}