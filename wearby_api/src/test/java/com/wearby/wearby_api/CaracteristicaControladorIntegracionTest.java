package com.wearby.wearby_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de integración de los endpoints de características.
 * Verifica que los desplegables devuelven datos correctamente.
 */

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test@wearby.com", roles = "USUARIO")
public class CaracteristicaControladorIntegracionTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getCategoria_debeRetornarListaNoVacia() throws Exception{
        mockMvc.perform(get("/api/caracteristicas/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(greaterThan(0)));
    }

    @Test
    void getEstilos_debeRetornarListaNoVacia() throws Exception {
        mockMvc.perform(get("/api/caracteristicas/estilos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(greaterThan(0)));
    }

    @Test
    void getTemporadas_debeRetornarListaNoVacia() throws Exception{
        mockMvc.perform(get("/api/caracteristicas/temporadas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}