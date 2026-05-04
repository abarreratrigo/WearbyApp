package com.wearby.wearby_api;

import com.wearby.wearby_api.modelo.Usuario;
import com.wearby.wearby_api.repositorio.UsuarioRepository;
import com.wearby.wearby_api.servicio.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias del servicio de usuarios.
 * Verifica el registro, login y cifrado de contraseñas
 */

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void registrar_debeCifrarContrasena(){

        Usuario usuario = new Usuario();
        usuario.setNombre("Manolo");
        usuario.setEmail("manolo@wearby.com");
        usuario.setContrasena("1234");

        when(passwordEncoder.encode("1234")).thenReturn("$2a$hash");
        when(usuarioRepository.save(any())).thenReturn(usuario);

        Usuario resultado = usuarioService.registrar(usuario);

        verify(passwordEncoder, times(1)).encode("1234");
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void login_conCredencialesCorrectas_debeRetornarUsuario(){

        Usuario usuario = new Usuario();
        usuario.setEmail("paco@wearby.com");
        usuario.setContrasena("$2a$hash");

        when(usuarioRepository.findByEmail("paco@wearby.com"))
                .thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("1234", "$2a$hash")).thenReturn(true);

        Optional<Usuario> resultado = usuarioService.login("paco@wearby.com", "1234");

        assertTrue(resultado.isPresent());
    }

    @Test
    void login_conContrasenaIncorrecta_debeRetornarVacio(){
        Usuario usuario = new Usuario();
        usuario.setEmail("alexis@wearby.com");
        usuario.setContrasena("$2a$hash");

        when(usuarioRepository.findByEmail("alexis@wearby.com"))
                .thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("incorrecta", "$2a$hash")).thenReturn(false);

        Optional<Usuario> resultado = usuarioService.login("alexis@wearby.com", "incorrecta");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void login_conEmailInexistente_debeRetornarVacio(){
        when(usuarioRepository.findByEmail("noexiste@wearby.com"))
                .thenReturn(Optional.empty());

        Optional<Usuario> resultado = usuarioService.login("noexiste@wearby.com", "1234");

        assertTrue(resultado.isEmpty());
    }
}
