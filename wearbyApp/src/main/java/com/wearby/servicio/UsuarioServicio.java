package com.wearby.servicio;

import com.google.gson.reflect.TypeToken;
import com.wearby.modelo.Usuario;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio que gestiona las llamadas a la API REST
 * relacionadas con los usuarios
 */

public class UsuarioServicio extends ApiService{

    public Usuario login(String email, String contrasena) throws IOException {
        Map<String, String> credenciales = new HashMap<>();
        credenciales.put("email", email);
        credenciales.put("contrasena", contrasena);

        String jsonBody = gson.toJson(credenciales);
        Response respuesta = post("/usuarios/login", jsonBody);

        if (respuesta.isSuccessful() && respuesta.body() != null) {
            return gson.fromJson(respuesta.body().string(), Usuario.class);
        }
        return null;
    }

    public Usuario registrar(String nombre, String email, String contrasena) throws IOException {
        Map<String, String> datos = new HashMap<>();
        datos.put("nombre", nombre);
        datos.put("email", email);
        datos.put("contrasena", contrasena);

        String jsonBody = gson.toJson(datos);
        Response respuesta = post("/usuarios/registro", jsonBody);

        if (respuesta.isSuccessful() && respuesta.body() != null){
            return gson.fromJson(respuesta.body().string(), Usuario.class);
        }
        return null;
    }

    public Usuario editarPerfil(Integer id, String nombre,
                                String email, String contrasena) throws IOException {
        Map<String, String> datos = new HashMap<>();
        datos.put("nombre", nombre);
        datos.put("email", email);
        if (contrasena != null) datos.put("contrasena", contrasena);

        Response respuesta = put("/usuarios/" + id, gson.toJson(datos));
        if (respuesta.isSuccessful() && respuesta.body() != null) {
            return gson.fromJson(respuesta.body().string(), Usuario.class);
        }
        return null;
    }

    public List<Usuario> getUsuarios() throws IOException {
        Response r = get("/usuarios/");
        if (r.isSuccessful() && r.body() != null) {
            Type tipo = new TypeToken<List<Usuario>>(){}.getType();
            return gson.fromJson(r.body().string(), tipo);
        }
        return List.of();
    }

    public void toggleActivo(Integer id) throws IOException {
        put("/usuarios/" + id + "/toggle", "");
    }

    public void eliminar(Integer id) throws IOException {
        delete("/usuarios/" + id);
    }
}
