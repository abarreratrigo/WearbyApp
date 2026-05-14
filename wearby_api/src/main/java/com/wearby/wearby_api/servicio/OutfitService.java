package com.wearby.wearby_api.servicio;

import com.wearby.wearby_api.modelo.Outfit;
import com.wearby.wearby_api.modelo.Usuario;
import com.wearby.wearby_api.repositorio.OutfitRepository;
import com.wearby.wearby_api.repositorio.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OutfitService {

    private final OutfitRepository outfitRepository;
    private final UsuarioRepository usuarioRepository;

    public List<Outfit> getOutfits(Integer usuarioId) {
        return outfitRepository.findByUsuarioId(usuarioId);
    }

    public Outfit guardar(Outfit outfit) {
        if (outfit.getUsuario() == null && outfit.getIdUsuario() != null) {
            Usuario usuario = usuarioRepository.findById(outfit.getIdUsuario())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            outfit.setUsuario(usuario);
        }
        return outfitRepository.save(outfit);
    }
}