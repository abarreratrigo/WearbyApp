package com.wearby.wearby_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de integración del controlador de usuarios.
 * Verifica que los endpoints REST responden correctamente.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class UsuarioControladorIntegracionTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void generarHash() {
        System.out.println(new BCryptPasswordEncoder().encode("admin123"));
    }

    @Test
    void registroConDatosValidos_debeRetornar200() throws Exception{
        String body = """
                {
                    "nombre": "Nuevo Usuario",
                    "email": "nuevo_usuario_test@wearby.com",
                    "contrasena": "1234"
                }
                """;

        mockMvc.perform(post("/api/usuarios/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("nuevo_usuario_test@wearby.com"));
    }

    @Test
    void login_conCredencialesIncorrectas_debeRetornar401() throws Exception{
        String body = """
                {
                    "email": "noexiste@wearby.com",
                    "contrasena": "incorrecta"
                }
                """;

        mockMvc.perform(post("/api/usuarios/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_conCredencialesCorrectas_debeRetornar200() throws Exception {
        String body = """
        {
            "email": "test@wearby.com",
            "contrasena": "admin123"
        }
        """;

        mockMvc.perform(post("/api/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@wearby.com"));
    }
}
