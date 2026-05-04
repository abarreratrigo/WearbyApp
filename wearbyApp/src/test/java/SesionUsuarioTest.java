import com.wearby.sesion.SesionUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias de la clase SesionUsuario.
 * Verifica el inicio, cierre de sesión y el rol de administrador
 */

public class SesionUsuarioTest {

    @BeforeEach
    void setUp(){
        SesionUsuario.getInstancia().cerrar();
    }

    @Test
    void iniciarSesion_debeAlmacenarDatos(){

        SesionUsuario.getInstancia().iniciar(1, "Alexis", "alexis@wearby.com", "usuario");

        assertEquals(1, SesionUsuario.getInstancia().getId());
        assertEquals("Alexis", SesionUsuario.getInstancia().getNombre());
        assertEquals("alexis@wearby.com", SesionUsuario.getInstancia().getEmail());
        assertEquals("usuario", SesionUsuario.getInstancia().getRol());
    }

    @Test
    void cerrarSesion_debeLimpiarDatos() {

        SesionUsuario.getInstancia().iniciar(1, "Alexis", "alexis@wearby.com", "usuario");

        SesionUsuario.getInstancia().cerrar();

        assertNull(SesionUsuario.getInstancia().getId());
        assertNull(SesionUsuario.getInstancia().getNombre());
        assertNull(SesionUsuario.getInstancia().getEmail());
    }

    @Test
    void isAdmin_conRolAdmin_debeRetornarTrue() {

        SesionUsuario.getInstancia().iniciar(1, "Admin", "admin@wearby.com", "admin");

        assertTrue(SesionUsuario.getInstancia().isAdmin());
    }

    @Test
    void isAdmin_conRolUsuario_debeRetornarFalse(){

        SesionUsuario.getInstancia().iniciar(1, "Alexis", "alexis@wearby.com", "usuario");

        assertFalse(SesionUsuario.getInstancia().isAdmin());
    }

    @Test
    void singleton_debeRetornarSiempreLaMismaInstancia(){
        SesionUsuario instancia1 = SesionUsuario.getInstancia();
        SesionUsuario instancia2 = SesionUsuario.getInstancia();

        assertSame(instancia1, instancia2);
    }
}
