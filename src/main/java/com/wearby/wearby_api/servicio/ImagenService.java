package com.wearby.wearby_api.servicio;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class ImagenService {

    // Carpeta donde se guardarán las imágenes dentro del proyecto
    private final Path carpetaUploads = Paths.get("uploads/prendas");

    public ImagenService() throws IOException {
        // Crea la carpeta si no existe al arrancar
        Files.createDirectories(carpetaUploads);
    }

    // Guarda la imagen y devuelve la ruta pública
    public String guardarImagen(MultipartFile archivo) throws IOException {
        // Generamos un nombre único para evitar colisiones
        String nombreArchivo = UUID.randomUUID() + "_" + archivo.getOriginalFilename();
        Path destino = carpetaUploads.resolve(nombreArchivo);
        Files.copy(archivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);
        return "uploads/prendas/" + nombreArchivo;
    }

    // Elimina la imagen del disco
    public void eliminarImagen(String imagenUrl) throws IOException {
        if (imagenUrl != null && !imagenUrl.isEmpty()) {
            Path archivo = Paths.get(imagenUrl);
            Files.deleteIfExists(archivo);
        }
    }
}