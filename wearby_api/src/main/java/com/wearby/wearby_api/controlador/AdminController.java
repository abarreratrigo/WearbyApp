package com.wearby.wearby_api.controlador;

import com.wearby.wearby_api.dao.PrendaDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final PrendaDAO prendaDAO;

    //GET /api/admin/estadisticas
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Integer>> getEstadisticas() throws SQLException {
        int [] stats = prendaDAO.obtenerEstadisticas();
        return ResponseEntity.ok(Map.of(
                "totalUsuarios", stats[0],
                "totalPrendas", stats[1],
                "totalOutfits", stats[2]
        ));
    }
}
