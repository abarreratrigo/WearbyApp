package com.wearby.wearby_api.controlador;

import com.wearby.wearby_api.modelo.Prenda;
import com.wearby.wearby_api.servicio.PrendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/prendas")
@RequiredArgsConstructor
public class PrendaController {

    private final PrendaService prendaService;

    //GET /api/prendas/usuario/{usuarioId}
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Prenda>> getPrendas(@PathVariable Integer usuarioId){
        return ResponseEntity.ok(prendaService.getPrendas(usuarioId));
    }

    //GET api/prendas/usuario/{usuarioId}/favoritas
    @GetMapping("usuario/{usuarioId}/favoritas")
    public ResponseEntity<List<Prenda>> getFavoritas(@PathVariable Integer usuarioId){
        return ResponseEntity.ok(prendaService.getFavoritas(usuarioId));
    }

    //POST /api/prendas
    @PostMapping
    public ResponseEntity<Prenda> anadir (
            @RequestParam Integer usuarioId,
            @RequestParam String nombre,
            @RequestParam Integer categoriaId,
            @RequestParam Integer colorId,
            @RequestParam Integer estiloId,
            @RequestParam Integer formalidadId,
            @RequestParam Integer temporadaId,
            @RequestParam(required = false) String notas,
            @RequestParam(required = false)MultipartFile imagen) throws IOException {

        return ResponseEntity.ok(prendaService.anadir(
                usuarioId, nombre, categoriaId, colorId,
                estiloId, formalidadId, temporadaId, notas, imagen));
    }

    //PUT /api/prendas/{id}/favorito
    @PutMapping("/{id}/favorito")
    public ResponseEntity<Prenda> toggleFavorito(@PathVariable Integer id){
        return ResponseEntity.ok(prendaService.toggleFavorito(id));
    }

    //DELETE /api/prendas/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) throws IOException {
        prendaService.eliminar(id);
        return ResponseEntity.ok().build();
    }
}
