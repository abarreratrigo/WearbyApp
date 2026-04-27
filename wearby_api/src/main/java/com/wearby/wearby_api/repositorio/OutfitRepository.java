package com.wearby.wearby_api.repositorio;

import com.wearby.wearby_api.modelo.Outfit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutfitRepository extends JpaRepository<Outfit, Integer> {
    List<Outfit> findByUsuarioId(Integer usuarioId);
}
