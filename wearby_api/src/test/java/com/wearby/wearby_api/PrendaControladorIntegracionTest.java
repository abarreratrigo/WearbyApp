package com.wearby.wearby_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de integración del controlador de prendas.
 * Verifica que los endpoints responden con los códigos HTTP correctos.
 */
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test@wearby.com", roles = "usuario")
public class PrendaControladorIntegracionTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getPrendas_deberiaRetornarPrendasDelUsuario() throws Exception {
        mockMvc.perform(get("/api/prendas/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getFavoritas_deberiaRetornarSoloPrendasFavoritas() throws Exception {
        mockMvc.perform(get("/api/prendas/usuario/1/favoritas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void eliminarPrendaInexistente_debeRetornar404() throws Exception {
        mockMvc.perform(delete("/api/prendas/9999999"))
                .andExpect(status().isNotFound()); // 404 ✅
    }
}
