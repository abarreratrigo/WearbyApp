package com.wearby.servicio;

import com.google.gson.reflect.TypeToken;
import com.wearby.modelo.Prenda;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Servicio que gestiona las llamadas a la API REST
 * relacionadas con las prendas del armario
 */

public class PrendaServicio extends ApiService{

    public List<Prenda> getPrendas(Integer usuarioId) throws IOException {
        Response respuesta = get("/prendas/usuario/" + usuarioId);
        if (respuesta.isSuccessful() && respuesta.body() != null) {
            Type tipo = new TypeToken<List<Prenda>>(){}.getType();
            return gson.fromJson(respuesta.body().string(), tipo);
        }
        return List.of();
    }

    public List<Prenda> getFavoritas(Integer usuarioId) throws IOException {
        Response respuesta = get("/prendas/usuario/" + usuarioId + "/favoritas");
        if (respuesta.isSuccessful() && respuesta.body() != null) {
            Type tipo = new TypeToken<List<Prenda>>(){}.getType();
            return gson.fromJson(respuesta.body().string(), tipo);
        }
        return List.of();
    }

    public Prenda toggleFavorito(Integer prendaId) throws IOException{
        Response respuesta = put("/prendas/" + prendaId + "/favorito", "");
        if (respuesta.isSuccessful() && respuesta.body() != null){
            return gson.fromJson(respuesta.body().string(), Prenda.class);
        }
        return null;
    }

    public boolean eliminar(Integer prendaId) throws IOException {
        Response respuesta = delete("/prendas/" + prendaId);
        return respuesta.isSuccessful();
    }
}
