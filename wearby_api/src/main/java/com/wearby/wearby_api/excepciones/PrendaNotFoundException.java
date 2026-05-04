package com.wearby.wearby_api.excepciones;

public class PrendaNotFoundException extends RuntimeException {
    public PrendaNotFoundException(Integer id) {
        super("Prenda no encontrada con id: " + id);
    }
}