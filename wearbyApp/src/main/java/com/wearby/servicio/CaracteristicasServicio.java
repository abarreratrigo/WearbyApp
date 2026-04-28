package com.wearby.servicio;

import com.google.gson.reflect.TypeToken;
import com.wearby.modelo.*;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Servicio que gestiona las llamadas a la API REST
 * para obtener los valores de los desplegables
 */

public class CaracteristicasServicio extends ApiService{

    public List<Categoria> getCategorias() throws IOException {
        Response r = get("/caracteristicas/categorias");
        Type tipo = new TypeToken<List<Categoria>>(){}.getType();
        return gson.fromJson(r.body().string(), tipo);
    }

    public List<Estilo> getEstilos() throws IOException{
        Response r = get("/caracteristicas/estilos");
        Type tipo = new TypeToken<List<Estilo>>(){}.getType();
        return gson.fromJson(r.body().string(), tipo);
    }

    public List<Temporada> getTemporadas() throws IOException {
        Response r = get("/caracteristicas/temporadas");
        Type tipo = new TypeToken<List<Temporada>>(){}.getType();
        return gson.fromJson(r.body().string(), tipo);
    }

    public List<Color> getColores() throws IOException {
        Response r = get("/caracteristicas/colores");
        Type tipo = new TypeToken<List<Color>>(){}.getType();
        return gson.fromJson(r.body().string(), tipo);
    }

    public List<Formalidad> getFormalidades() throws IOException {
        Response r = get("/caracteristicas/formalidades");
        Type tipo = new TypeToken<List<Formalidad>>(){}.getType();
        return gson.fromJson(r.body().string(), tipo);
    }
}

