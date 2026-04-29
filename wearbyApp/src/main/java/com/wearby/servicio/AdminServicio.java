package com.wearby.servicio;

import com.google.gson.reflect.TypeToken;
import com.wearby.modelo.PrendaFiltroDTO;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Servicio que gestiona las llamadas a la API REST
 * del panel de administrador
 */

public class AdminServicio extends ApiService {

    public Map<String, Integer> getEstadisticas() throws IOException {
        Response r = get("/admin/estadisticas");
        if (r.isSuccessful() && r.body() != null) {
            Type tipo = new TypeToken<Map<String, Integer>>(){}.getType();
            return gson.fromJson(r.body().string(), tipo);
        }
        return Map.of();
    }

    public List<PrendaFiltroDTO> getPrendasPorUsuario() throws IOException{
        Response r = get("/admin/prendas-por-usuario");
        if (r.isSuccessful() && r.body() != null){
            Type tipo = new TypeToken<List<PrendaFiltroDTO>>(){}.getType();
            return gson.fromJson(r.body().string(), tipo);
        }
        return List.of();
    }
}
