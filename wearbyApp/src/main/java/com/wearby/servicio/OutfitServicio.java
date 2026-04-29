package com.wearby.servicio;

import com.google.gson.reflect.TypeToken;
import com.wearby.modelo.OutfitCarruselDTO;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio que gestiona las llamadas a la API REST
 * relacionadas con la generación y guardado de outfits
 */

public class OutfitServicio extends ApiService {

    public List<OutfitCarruselDTO> generar(
            Integer usuarioId,
            Integer estiloId,
            Integer temporadaId,
            Integer formalidadId,
            List<Integer> categoriaIds) throws IOException {

        Map<String, Object> solicitud = new HashMap<>();
        solicitud.put("usuarioId", usuarioId);
        solicitud.put("estiloId", estiloId);
        solicitud.put("temporadaId", temporadaId);
        solicitud.put("formalidadId", formalidadId);
        solicitud.put("categoriaIds", categoriaIds);

        Response respuesta = post("/outfits/generar", gson.toJson(solicitud));

        if (respuesta.isSuccessful() && respuesta.body() != null) {
            Type tipo = new TypeToken<List<OutfitCarruselDTO>>(){}.getType();
            return gson.fromJson(respuesta.body().string(), tipo);
        }
        return List.of();
    }
}

