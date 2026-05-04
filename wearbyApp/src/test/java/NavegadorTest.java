import com.wearby.util.Navegador;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias de la clase Navegador.
 * Verifica que el Stage principal se asigna correctamente
 */

public class NavegadorTest {

    @Test
    void setStagePrincipal_conNull_noDebeLanzarExcepcion() {
        assertDoesNotThrow(() -> Navegador.setStagePrincipal(null));
    }

    @Test
    void getStage_sinConfigurar_debeRetornarNull(){

        Navegador.setStagePrincipal(null);

        assertNull(Navegador.getStage());
    }
}
