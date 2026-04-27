package com.wearby.wearby_api.servicio;

import com.wearby.wearby_api.modelo.Outfit;
import com.wearby.wearby_api.repositorio.OutfitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OutfitService {

    private final OutfitRepository outfitRepository;

    public List<Outfit> getOutfits(Integer usuarioId) {
        return outfitRepository.findByUsuarioId(usuarioId);
    }

    public Outfit guardar(Outfit outfit) {
        return outfitRepository.save(outfit);
    }

    public void eliminar(Integer id) {
        outfitRepository.deleteById(id);
    }
}