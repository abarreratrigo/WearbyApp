
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias de las validaciones del login.
 * Verifica que los campos vacíos y email inválidos
 * son detectados antes de llamar a la API
 */
public class ValidacionLoginTest {

    private String validarLogin(String email, String contrasena) {
        if (email == null || email.trim().isEmpty()) {
            return "El email no puede estar vacío";
        }
        if (contrasena == null || contrasena.isEmpty()){
            return "La contraseña no puede estar vacía";
        }
        if (!email.contains("@") || email.startsWith("@") ||
                email.endsWith("@") || email.contains(" ")) {
            return "El email no tiene un formato válido";
        }
        return null;
    }

    @Test
    void validar_conEmailVacio_debeRetornarError(){
        String error = validarLogin("", "1234");
        assertNotNull(error);
        assertTrue(error.contains("vacío"));
    }

    @Test
    void validar_conContrasenaVacia_debeRetornarError(){
        String error = validarLogin("alexis@wearby.com", "");
        assertNotNull(error);
        assertTrue(error.contains("vacía"));
    }

    @Test
    void validar_conDatosValidos_debeRetornarNull(){
        String error = validarLogin("alexis@wearby.com", "1234");
        assertNull(error);
    }

    @ParameterizedTest
    @ValueSource(strings = {"noesuncorreo", "sin-arrroba.com", "sindominio@"})
    void validar_conEmailInvalido_debeRetornarError(String emailInvalido){
        String error = validarLogin(emailInvalido, "1234");
        assertNotNull(error);
    }
}
