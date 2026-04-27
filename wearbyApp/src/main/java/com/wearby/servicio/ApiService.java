package com.wearby.servicio;

import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;

/**
 * Servicio base para las llamadas HTTP a la API REST.
 * Centraliza la configuración del cliente HTTP y la
 * serialización/deserialización JSON con Gson.
 */

public class ApiService {

    protected static final String BASE_URL = "http://localhost:8080/api";
    protected static final OkHttpClient cliente = new OkHttpClient();
    protected static final Gson gson = new Gson();
    protected static final MediaType JSON = MediaType.get("application/json");

    protected Response post(String endpoint, String json) throws IOException {
        RequestBody body = RequestBody.create(json,JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + endpoint)
                .post(body)
                .build();

        return cliente.newCall(request).execute();
    }

    protected Response delete (String endpoint) throws IOException{
        Request request = new Request.Builder()
                .url(BASE_URL + endpoint)
                .delete()
                .build();
        return cliente.newCall(request).execute();
    }

    protected Response get (String endpoint) throws IOException{
        Request request = new Request.Builder()
                .url(BASE_URL + endpoint)
                .get()
                .build();
        return cliente.newCall(request).execute();
    }

    protected Response put(String endpoint, String json) throws IOException{
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + endpoint)
                .put(body)
                .build();
        return cliente.newCall(request).execute();
    }
}
