package com.wearby.wearby_api.servicio;

import com.wearby.wearby_api.modelo.Prenda;
import com.wearby.wearby_api.repositorio.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrendaService {

    private final PrendaRepository prendaRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final ColorRepository colorRepository;
    private final EstiloRepository estiloRepository;
    private final FormalidadRepository formalidadRepository;
    private final TemporadaRepository temporadaRepository;
    private final ImagenService imagenService;

    public List<Prenda> getPrendas(Integer usuarioId){
        return prendaRepository.findByUsuarioId(usuarioId);
    }

    public List<Prenda> getFavoritas(Integer usuarioId){
        return prendaRepository.findByUsuarioIdAndFavoritoTrue(usuarioId);
    }

    public Prenda anadir(
            Integer usuarioId,
            String nombre,
            Integer categoriaId,
            Integer colorId,
            Integer estiloId,
            Integer formalidadId,
            Integer temporadaId,
            String notas,
            MultipartFile imagen) throws IOException {

        Prenda prenda = new Prenda();

        prenda.setUsuario(usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado")));
        prenda.setNombre(nombre);
        prenda.setNotas(notas);
        prenda.setCategoria(categoriaRepository.findById(categoriaId).orElse(null));
        prenda.setColor(colorRepository.findById(colorId).orElse(null));
        prenda.setEstilo(estiloRepository.findById(estiloId).orElse(null));
        prenda.setFormalidad(formalidadRepository.findById(formalidadId).orElse(null));
        prenda.setTemporada(temporadaRepository.findById(temporadaId).orElse(null));

        if (imagen != null && !imagen.isEmpty()) {
            String rutaImagen = imagenService.guardarImagen(imagen);
            prenda.setImagenUrl(rutaImagen);
        }

        return prendaRepository.save(prenda);
    }

    public Prenda editar(
            Integer id,
            String nombre,
            Integer categoriaId,
            Integer colorId,
            Integer estiloId,
            Integer formalidadId,
            Integer temporadaId,
            String notas,
            MultipartFile imagen) throws IOException {

        Prenda prenda = prendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prenda no encontrada"));

        prenda.setNombre(nombre);
        prenda.setNotas(notas);
        prenda.setCategoria(categoriaRepository.findById(categoriaId).orElse(null));
        prenda.setColor(colorRepository.findById(colorId).orElse(null));
        prenda.setEstilo(estiloRepository.findById(estiloId).orElse(null));
        prenda.setFormalidad(formalidadRepository.findById(formalidadId).orElse(null));
        prenda.setTemporada(temporadaRepository.findById(temporadaId).orElse(null));

        if (imagen != null && !imagen.isEmpty()) {
            imagenService.eliminarImagen(prenda.getImagenUrl());
            prenda.setImagenUrl(imagenService.guardarImagen(imagen));
        }

        return prendaRepository.save(prenda);
    }

    public Prenda toggleFavorito(Integer id) {
        Prenda prenda = prendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prenda no encontrada"));
        prenda.setFavorito(!prenda.getFavorito());
        return prendaRepository.save(prenda);
    }

    public void eliminar(Integer id) throws IOException {
        Prenda prenda = prendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prenda no encontrada"));
        imagenService.eliminarImagen(prenda.getImagenUrl());
        prendaRepository.deleteById(id);
    }
}
