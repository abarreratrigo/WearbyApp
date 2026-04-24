package com.wearby.wearby_api.dto;

import lombok.Data;

import java.util.List;

/**
 * DTO que recibe la solicitud de generación de outfit del usuario
 * Contiene los filtros comunes y las categorías seleccionadas
 */

@Data
public class OutfitSolicitudDTO {
    private Integer usuarioId;
    private Integer estiloId;
    private Integer temporadaId;
    private Integer formalidadId;
    private List<Integer> categoriaIds;
}
