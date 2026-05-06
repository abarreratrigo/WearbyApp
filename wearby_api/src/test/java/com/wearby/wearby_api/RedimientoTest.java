package com.wearby.wearby_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas de rendimiento de la aplicación.
 * Verifica que los endpoints críticos responden
 * dentro de los tiempos definidos en los requisitos
 */

@SpringBootTest
@AutoConfigureMockMvc
public class RedimientoTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getCaracteristicas_debeResponderEnMenosDe500Milisegundos() throws Exception{
        long inicio = System.currentTimeMillis();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/caracteristicas/categorias"))
                .andExpect(status().isOk());

        long fin = System.currentTimeMillis();
        long tiempoRespuesta = fin - inicio;

        System.out.println("Tiempo carga categorías: " + tiempoRespuesta + "ms");
        assertTrue(tiempoRespuesta < 500,
                "Las categorías tardaron " + tiempoRespuesta + "ms");
    }

    @Test
    void getPrendas_debeResponderEnMenosDe1Segundo() throws Exception {
        long inicio = System.currentTimeMillis();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/prendas/usuario/1"))
                .andExpect(status().isOk());

        long fin = System.currentTimeMillis();
        long tiempoRespuesta = fin - inicio;

        System.out.println("Tiempo carga prendas: " + tiempoRespuesta + "ms");
        assertTrue(tiempoRespuesta < 1000,
                "Las prendas tardaron: " + tiempoRespuesta + "ms");
    }

    @Test
    void login_debeResponderEnMenosDe1Srgundo() throws Exception {
        String body = """
                {
                    "email": "test@wearby.com",
                    "contrasena": "admin123"
                }
                """;

        long inicio = System.currentTimeMillis();

        mockMvc.perform(post("/api/usuarios/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk());

        long fin = System.currentTimeMillis();
        long tiempoRespuesta = fin - inicio;

        System.out.println("Tiempo login: " + tiempoRespuesta + "ms");
        assertTrue(tiempoRespuesta < 1000,
                "El login tardó: " + tiempoRespuesta + "ms");

    }
}
