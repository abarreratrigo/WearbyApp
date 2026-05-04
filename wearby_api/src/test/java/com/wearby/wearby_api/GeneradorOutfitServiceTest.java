package com.wearby.wearby_api;

import com.wearby.wearby_api.dao.PrendaDAO;
import com.wearby.wearby_api.dto.OutfitCarruselDTO;
import com.wearby.wearby_api.dto.OutfitSolicitudDTO;
import com.wearby.wearby_api.dto.PrendaFiltroDTO;
import com.wearby.wearby_api.repositorio.CategoriaRepository;
import com.wearby.wearby_api.servicio.GeneradorOutfitService;
import com.wearby.wearby_api.modelo.Categoria;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias del generador de outfits.
 * Verifica que los carruseles se generan correctamente
 * y que se incluyen las categorías base obligatorias.
 */

@ExtendWith(MockitoExtension.class)
public class GeneradorOutfitServiceTest {

    @Mock private PrendaDAO prendaDAO;
    @Mock private CategoriaRepository categoriaRepository;

    @InjectMocks
    private GeneradorOutfitService generadorOutfitService;

    @Test
    void generar_ConPrendasDisponibles_debeRetornarCarruseles() throws Exception{

        PrendaFiltroDTO prenda = new PrendaFiltroDTO();
        prenda.setId(1);
        prenda.setNombre("Camiseta blanca");
        prenda.setFavorito(false);

        Categoria categoria = new Categoria();
        categoria.setId(1);
        categoria.setNombre("Camiseta");

        when(prendaDAO.llamarProcedimientoOutfit(1, 1, null, null, null))
                .thenReturn(List.of(prenda));
        when(prendaDAO.llamarProcedimientoOutfit(1, 2, null, null, null))
                .thenReturn(List.of(prenda));
        when(categoriaRepository.findById(any()))
                .thenReturn(Optional.of(categoria));

        OutfitSolicitudDTO solicitud = new OutfitSolicitudDTO();
        solicitud.setUsuarioId(1);
        solicitud.setCategoriaIds(List.of());

        List<OutfitCarruselDTO> resultado = generadorOutfitService.generar(solicitud);

        assertFalse(resultado.isEmpty());
        assertEquals(2, resultado.size());
    }

    @Test
    void generar_sinPrendasCoincidentes_debeRetornarListaVacia() throws Exception {
        when(prendaDAO.llamarProcedimientoOutfit(any(), any(), any(), any(), any()))
                .thenReturn(List.of());

        OutfitSolicitudDTO solicitud = new OutfitSolicitudDTO();
        solicitud.setUsuarioId(1);
        solicitud.setCategoriaIds(List.of());

        List<OutfitCarruselDTO> resultado = generadorOutfitService.generar(solicitud);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void generar_conCategoriasOpcionales_debeIncluirlas() throws Exception{

        PrendaFiltroDTO prenda = new PrendaFiltroDTO();
        prenda.setId(1);
        prenda.setNombre("Zapato negro");

        Categoria categoria = new Categoria();
        categoria.setId(5);
        categoria.setNombre("Zapatos");

        when(prendaDAO.llamarProcedimientoOutfit(eq(1), any(), any(), any(), any()))
                .thenReturn(List.of(prenda));
        when(categoriaRepository.findById(any()))
                .thenReturn(Optional.of(categoria));

        OutfitSolicitudDTO solicitud = new OutfitSolicitudDTO();
        solicitud.setUsuarioId(1);
        solicitud.setCategoriaIds(List.of(5));

        List<OutfitCarruselDTO> resultado = generadorOutfitService.generar(solicitud);

        assertEquals(3, resultado.size());
    }
}
