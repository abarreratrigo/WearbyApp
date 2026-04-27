package com.wearby.wearby_api.servicio;

import com.wearby.wearby_api.dao.PrendaDAO;
import com.wearby.wearby_api.dto.OutfitCarruselDTO;
import com.wearby.wearby_api.dto.OutfitSolicitudDTO;
import com.wearby.wearby_api.dto.PrendaFiltroDTO;
import com.wearby.wearby_api.repositorio.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Genera los carruseles de prendas por categoría para la pantalla
 * de creación de outfits. Cada categoría devuelve sus prendas
 * ordenadas con favoritos primero (RF13).
 */
@Service
@RequiredArgsConstructor
public class GeneradorOutfitService {

    private final PrendaDAO prendaDAO;
    private final CategoriaRepository categoriaRepository;

    public List<OutfitCarruselDTO> generar(OutfitSolicitudDTO solicitud) throws SQLException {

        List<OutfitCarruselDTO> resultado = new ArrayList<>();

        List<Integer> categorias = new ArrayList<>(List.of(1, 2));

        if (solicitud.getCategoriaIds() != null) {
            for (Integer id : solicitud.getCategoriaIds()) {
                if (!categorias.contains(id)) {
                    categorias.add(id);
                }
            }
        }

        for (Integer categoriaId : categorias) {
            List<PrendaFiltroDTO> prendas = prendaDAO.llamarProcedimientoOutfit(
                    solicitud.getUsuarioId(),
                    categoriaId,
                    solicitud.getEstiloId(),
                    solicitud.getTemporadaId(),
                    solicitud.getFormalidadId()
            );

            if (!prendas.isEmpty()) {
                OutfitCarruselDTO carrusel = new OutfitCarruselDTO();
                carrusel.setCategoriaId(categoriaId);
                carrusel.setCategoria(
                        categoriaRepository.findById(categoriaId)
                                .map(c -> c.getNombre())
                                .orElse("Categoría")
                );
                carrusel.setPrendas(prendas);
                resultado.add(carrusel);
            }
        }

        return resultado;
    }
}