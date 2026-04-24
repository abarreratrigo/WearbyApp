package com.wearby.wearby_api.controlador;

import com.wearby.wearby_api.dto.OutfitCarruselDTO;
import com.wearby.wearby_api.dto.OutfitSolicitudDTO;
import com.wearby.wearby_api.modelo.Outfit;
import com.wearby.wearby_api.servicio.GeneradorOutfitService;
import com.wearby.wearby_api.servicio.OutfitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/outfits")
@RequiredArgsConstructor
public class OutfitController {

    private final GeneradorOutfitService generadorOutfitService;
    private final OutfitService outfitService;

    // POST /api/outfits/generar
    @PostMapping("/generar")
    public ResponseEntity<List<OutfitCarruselDTO>> generar(
            @RequestBody OutfitSolicitudDTO solicitud) throws SQLException {
        return ResponseEntity.ok(generadorOutfitService.generar(solicitud));
    }

    // GET /api/outfits/usuario/{usuarioId}
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Outfit>> getOutfits(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(outfitService.getOutfits(usuarioId));
    }

    // POST /api/outfits/guardar
    @PostMapping("/guardar")
    public ResponseEntity<Outfit> guardar(@RequestBody Outfit outfit) {
        return ResponseEntity.ok(outfitService.guardar(outfit));
    }

    // DELETE /api/outfits/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        outfitService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}