package com.wearby.wearby_api.repositorio;

import com.wearby.wearby_api.modelo.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}
