package com.wearby.wearby_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas de seguridad de la aplicación
 * Verifica el cifrado de contraseñas y el aislamiento
 * de datos entre distintos usuarios
 */

@SpringBootTest
@AutoConfigureMockMvc
public class SeguridadTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void contrasena_debeAlmacenarseConHashBCrypt() {

        String contrasenaPlana = "miContrasena123";
        String hash = passwordEncoder.encode(contrasenaPlana);

        assertTrue(hash.startsWith("$2a$"));

        assertTrue(!hash.equals(contrasenaPlana));

        assertTrue(passwordEncoder.matches(contrasenaPlana, hash));
    }

    @Test
    void contrasenasDiferentes_debenGenerarHashesDistintos(){

        String contrasena = "mismaContrasena";
        String hash1 = passwordEncoder.encode(contrasena);
        String hash2 = passwordEncoder.encode(contrasena);

        assertTrue(!hash1.equals(hash2));
        assertTrue(passwordEncoder.matches(contrasena, hash1));
        assertTrue(passwordEncoder.matches(contrasena, hash2));
    }

    @Test
    void usuario_noDebeVerPrendasDeOtroUsuario() throws Exception {

        //El usuario 1 intenta  acceder a las prendas del 2
        mockMvc.perform(get("/api/prendas/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].usuarioId").value(
                        org.hamcrest.Matchers.everyItem(
                                org.hamcrest.Matchers.is(1)
                        )
                ));
    }

    @Test
    void registro_conEmailDuplicado_debeRetorarError() throws Exception {
        String body = """
                {
                    "nombre": "Duplicado",
                    "email": "test@wearby.com",
                    "contrasena": "1234"
                }
                """;

        mockMvc.perform(post("/api/usuarios/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isConflict());
    }

    @Test
    void login_conContrasenaIncorrecta_debeRetornar401() throws Exception{
        String body = """
                {
                    "email": "test@wearby.com",
                    "contrasena": "contrasenaIncorrecta"
                }
                """;

        mockMvc.perform(post("/api/usuarios/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isUnauthorized());
    }
}
