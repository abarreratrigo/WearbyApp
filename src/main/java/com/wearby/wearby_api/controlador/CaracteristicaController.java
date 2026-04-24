package com.wearby.wearby_api.controlador;

import com.wearby.wearby_api.modelo.*;
import com.wearby.wearby_api.repositorio.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/caracteristicas")
@RequiredArgsConstructor
public class CaracteristicaController {

    private final CategoriaRepository categoriaRepository;
    private final ColorRepository colorRepository;
    private final EstiloRepository estiloRepository;
    private final FormalidadRepository formalidadRepository;
    private final TemporadaRepository temporadaRepository;

    //GET /api/caracteristicas/categorias
    @GetMapping("/categorias")
    public ResponseEntity<List<Categoria>> getCategorias() {
        return ResponseEntity.ok(categoriaRepository.findAll());
    }

    //GET /api/caracteristicas/colores
    @GetMapping("/colores")
    public ResponseEntity<List<Color>> getColores() {
        return ResponseEntity.ok(colorRepository.findAll());
    }

    //GET /api/caracteristicas/estilos
    @GetMapping("/estilos")
    public ResponseEntity<List<Estilo>> getEstilos() {
        return ResponseEntity.ok(estiloRepository.findAll());
    }

    //GET /api/caracteristicas/formalidades
    @GetMapping("/formalidades")
    public ResponseEntity<List<Formalidad>> getFormalidades() {
        return ResponseEntity.ok(formalidadRepository.findAll());
    }

    //GET /api/caracteristicas/temporadas
    @GetMapping("/temporadas")
    public ResponseEntity<List<Temporada>> getTemporadas() {
        return ResponseEntity.ok(temporadaRepository.findAll());
    }

    //POST /api/caracteristicas/categorias (solo admin)
    @PostMapping("/categorias")
    public ResponseEntity<Categoria> addCategoria(@RequestBody Categoria categoria) {
        return ResponseEntity.ok(categoriaRepository.save(categoria));
    }

    //POST /api/caracteristicas/colores
    @PostMapping("/colores")
    public ResponseEntity<Color> addColor(@RequestBody Color color) {
        return ResponseEntity.ok(colorRepository.save(color));
    }

    //POST /api/caracteristicas/estilos
    @PostMapping("/estilos")
    public ResponseEntity<Estilo> addEstilo(@RequestBody Estilo estilo) {
        return ResponseEntity.ok(estiloRepository.save(estilo));
    }

    //POST /api/caracteristicas/formalidades
    @PostMapping("/formalidades")
    public ResponseEntity<Formalidad> addFormalidad(@RequestBody Formalidad formalidad) {
        return ResponseEntity.ok(formalidadRepository.save(formalidad));
    }

    // POST /api/caracteristicas/temporadas
    @PostMapping("/temporadas")
    public ResponseEntity<Temporada> addTemporada(@RequestBody Temporada temporada) {
        return ResponseEntity.ok(temporadaRepository.save(temporada));
    }

    // DELETE /api/caracteristicas/categorias/{id}
    @DeleteMapping("/categorias/{id}")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Integer id) {
        categoriaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // DELETE /api/caracteristicas/colores/{id}
    @DeleteMapping("/colores/{id}")
    public ResponseEntity<Void> deleteColor(@PathVariable Integer id) {
        colorRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // DELETE /api/caracteristicas/estilos/{id}
    @DeleteMapping("/estilos/{id}")
    public ResponseEntity<Void> deleteEstilo(@PathVariable Integer id) {
        estiloRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // DELETE /api/caracteristicas/formalidades/{id}
    @DeleteMapping("/formalidades/{id}")
    public ResponseEntity<Void> deleteFormalidad(@PathVariable Integer id) {
        formalidadRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // DELETE /api/caracteristicas/temporadas/{id}
    @DeleteMapping("/temporadas/{id}")
    public ResponseEntity<Void> deleteTemporada(@PathVariable Integer id) {
        temporadaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
