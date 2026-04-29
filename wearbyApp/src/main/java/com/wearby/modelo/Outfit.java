package com.wearby.modelo;

import java.util.List;

/**
 * Modelo que representa un outfit guardado.
 * Mapea la respuesta JSON de la API REST.
 */

public class Outfit {
    private Integer id;
    private String nombre;
    private String fechaGeneracion;
    private List<PrendaFiltroDTO> prendas;

    public Integer getId(){return id;}
    public String getNombre(){return nombre;}
    public String getFechaGeneracion(){return fechaGeneracion;}
    public List<PrendaFiltroDTO> getPrendas() {return prendas;}
}

