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
        if (r.isSuccessful() && r.body() != null) {
            String cuerpo = r.body().string();
            Type tipo = new TypeToken<List<Categoria>>(){}.getType();
            return gson.fromJson(cuerpo, tipo);
        }
        return List.of();
    }

    public List<Estilo> getEstilos() throws IOException {
        Response r = get("/caracteristicas/estilos");
        System.out.println("Estilos código: " + r.code());
        if (r.isSuccessful() && r.body() != null) {
            String cuerpo = r.body().string();
            System.out.println("Estilos cuerpo: " + cuerpo);
            Type tipo = new TypeToken<List<Estilo>>(){}.getType();
            return gson.fromJson(cuerpo, tipo);
        }
        return List.of();
    }

    public List<Temporada> getTemporadas() throws IOException {
        Response r = get("/caracteristicas/temporadas");
        if (r.isSuccessful() && r.body() != null) {
            String cuerpo = r.body().string();
            Type tipo = new TypeToken<List<Temporada>>(){}.getType();
            return gson.fromJson(cuerpo, tipo);
        }
        return List.of();
    }

    public List<Color> getColores() throws IOException {
        Response r = get("/caracteristicas/colores");
        if (r.isSuccessful() && r.body() != null) {
            String cuerpo = r.body().string();
            Type tipo = new TypeToken<List<Color>>(){}.getType();
            return gson.fromJson(cuerpo, tipo);
        }
        return List.of();
    }

    public List<Formalidad> getFormalidades() throws IOException {
        Response r = get("/caracteristicas/formalidades");
        if (r.isSuccessful() && r.body() != null) {
            String cuerpo = r.body().string();
            Type tipo = new TypeToken<List<Formalidad>>(){}.getType();
            return gson.fromJson(cuerpo, tipo);
        }
        return List.of();
    }

    public Categoria addCategoria(Categoria categoria) throws IOException {
        Response r = post("/caracteristicas/categorias", gson.toJson(categoria));
        if (r.isSuccessful() && r.body() != null)
            return gson.fromJson(r.body().string(), Categoria.class);
        return null;
    }

    public Estilo addEstilo(Estilo estilo) throws IOException {
        Response r = post("/caracteristicas/estilos", gson.toJson(estilo));
        if (r.isSuccessful() && r.body() != null)
            return gson.fromJson(r.body().string(), Estilo.class);
        return null;
    }

    public Temporada addTemporada(Temporada temporada) throws IOException {
        Response r = post("/caracteristicas/temporadas", gson.toJson(temporada));
        if (r.isSuccessful() && r.body() != null)
            return gson.fromJson(r.body().string(), Temporada.class);
        return null;
    }

    public Color addColor(Color color) throws IOException {
        Response r = post("/caracteristicas/colores", gson.toJson(color));
        if (r.isSuccessful() && r.body() != null)
            return gson.fromJson(r.body().string(), Color.class);
        return null;
    }

    public Formalidad addFormalidad(Formalidad formalidad) throws IOException {
        Response r = post("/caracteristicas/formalidades", gson.toJson(formalidad));
        if (r.isSuccessful() && r.body() != null)
            return gson.fromJson(r.body().string(), Formalidad.class);
        return null;
    }
}

