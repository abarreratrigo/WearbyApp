package com.wearby.wearby_api.repositorio;

import com.wearby.wearby_api.modelo.Prenda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrendaRepository extends JpaRepository<Prenda, Integer> {
    List<Prenda> findByUsuarioId(Integer usuarioId);
    List<Prenda> findByUsuarioIdAndFavoritoTrue(Integer usuarioId);
}
