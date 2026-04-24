package com.wearby.wearby_api.dto;

import lombok.Data;

import java.util.List;

/**
 * DTO que representa una combinación concreta de prendas
 * El campo puntuación indica cuántas prendas favoritas hay,
 * usado para ordenar los resultados
 */
@Data
public class OutfitResultadoDTO {
    private List<PrendaFiltroDTO> prendas;
    private int puntuacion;
}
