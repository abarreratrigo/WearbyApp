package com.wearby.wearby_api;

import com.wearby.wearby_api.modelo.Prenda;
import com.wearby.wearby_api.repositorio.PrendaRepository;
import com.wearby.wearby_api.servicio.PrendaService;
import com.wearby.wearby_api.servicio.ImagenService;
import com.wearby.wearby_api.repositorio.*;
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
 * Pruebas unitarias del servicio de prendas.
 * Verifica la obtención, favoritos y eliminación de prendas
 */

@ExtendWith(MockitoExtension.class)
public class PrendaServiceTest {

    @Mock private PrendaRepository prendaRepository;
    @Mock private UsuarioRepository usuarioRepository;
    @Mock private CategoriaRepository categoriaRepository;
    @Mock private ColorRepository colorRepository;
    @Mock private EstiloRepository estiloRepository;
    @Mock private FormalidadRepository formalidadRepository;
    @Mock private TemporadaRepository temporadaRepository;
    @Mock private ImagenService imagenService;

    @InjectMocks
    private PrendaService prendaService;

    @Test
    void getPrendas_debeRetornarListaDePrendas(){

        Prenda p1 = new Prenda();
        Prenda p2 = new Prenda();
        when(prendaRepository.findByUsuarioId(1)).thenReturn(List.of(p1, p2));

        List<Prenda> resultado = prendaService.getPrendas(1);

        assertEquals(2, resultado.size());
        verify(prendaRepository, times(1)).findByUsuarioId(1);
    }

    @Test
    void getFavoritas_debeRetornarSoloPrendasFavoritas(){
        Prenda favorita = new Prenda();
        favorita.setFavorito(true);
        when(prendaRepository.findByUsuarioIdAndFavoritoTrue(1))
                .thenReturn(List.of(favorita));

        List<Prenda> resultado = prendaService.getFavoritas(1);

        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getFavorito());
    }

    @Test
    void toggleFavorito_debeInvertirEstado(){
        Prenda prenda = new Prenda();
        prenda.setId(1);
        prenda.setFavorito(false);

        when(prendaRepository.findById(1)).thenReturn(Optional.of(prenda));
        when(prendaRepository.save(any())).thenReturn(prenda);

        Prenda resultado = prendaService.toggleFavorito(1);

        assertTrue(resultado.getFavorito());
    }

    @Test
    void eliminar_debeBorrarPrendaYSuImagen() throws Exception{
        Prenda prenda = new Prenda();
        prenda.setId(2);
        prenda.setImagenUrl("uploads/prendas/test.jpg");

        when(prendaRepository.findById(2)).thenReturn(Optional.of(prenda));

        prendaService.eliminar(2);

        verify(imagenService, times(1)).eliminarImagen("uploads/prendas/test.jpg");
        verify(prendaRepository, times(1)).deleteById(2);
    }
}
