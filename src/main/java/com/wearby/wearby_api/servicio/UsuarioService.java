package com.wearby.wearby_api.servicio;

import com.wearby.wearby_api.modelo.Usuario;
import com.wearby.wearby_api.repositorio.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // Registrar un nuevo usuario
    public Usuario registrar(Usuario usuario) {
        // Ciframos la contraseña antes de guardar
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        return usuarioRepository.save(usuario);
    }

    // Login: busca el usuario por email y verifica la contraseña
    public Optional<Usuario> login(String email, String contrasena) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        if (usuario.isPresent() &&
                passwordEncoder.matches(contrasena, usuario.get().getContrasena())) {
            return usuario;
        }
        return Optional.empty();
    }

    // Obtener todos los usuarios (para el admin)
    public List<Usuario> getUsuarios() {
        return usuarioRepository.findAll();
    }

    // Obtener un usuario por id
    public Optional<Usuario> getUsuario(Integer id) {
        return usuarioRepository.findById(id);
    }

    // Activar o desactivar un usuario (para el admin)
    public Usuario toggleActivo(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setActivo(!usuario.getActivo());
        return usuarioRepository.save(usuario);
    }

    // Eliminar un usuario
    public void eliminar(Integer id) {
        usuarioRepository.deleteById(id);
    }

    // Editar perfil
    public Usuario editarPerfil(Integer id, Usuario datos) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setNombre(datos.getNombre());
        usuario.setEmail(datos.getEmail());
        if (datos.getContrasena() != null && !datos.getContrasena().isEmpty()) {
            usuario.setContrasena(passwordEncoder.encode(datos.getContrasena()));
        }
        return usuarioRepository.save(usuario);
    }
}