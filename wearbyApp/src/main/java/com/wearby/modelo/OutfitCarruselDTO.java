package com.wearby.modelo;

import java.util.List;

/**
 * Representa un carrusel de prendas de una categoría
 * en la pantalla de generación
 */

public class OutfitCarruselDTO {

    private String categoria;
    private Integer categoriaId;
    private List<PrendaFiltroDTO> prendas;

    public String getCategoria(){return categoria;}
    public Integer getCategoriaId(){return categoriaId;}
    public List<PrendaFiltroDTO> getPrendas(){return prendas;}
}
