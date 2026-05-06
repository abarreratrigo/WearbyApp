package com.wearby.wearby_api.controlador;

import com.wearby.wearby_api.modelo.Usuario;
import com.wearby.wearby_api.servicio.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // POST /api/usuarios/registro
    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        try {
            Usuario nuevo = usuarioService.registrar(usuario);
            return ResponseEntity.ok(nuevo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("El email ya está registrado");
        }
    }

    // POST /api/usuarios/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        return usuarioService.login(
                        credenciales.get("email"),
                        credenciales.get("contrasena")
                )
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(401).build());
    }

    // GET /api/usuarios (solo admin)
    @GetMapping
    public ResponseEntity<List<Usuario>> getUsuarios() {
        return ResponseEntity.ok(usuarioService.getUsuarios());
    }

    // GET /api/usuarios/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuario(@PathVariable Integer id) {
        return usuarioService.getUsuario(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /api/usuarios/{id}/toggle (activar/desactivar)
    @PutMapping("/{id}/toggle")
    public ResponseEntity<Usuario> toggleActivo(@PathVariable Integer id) {
        return ResponseEntity.ok(usuarioService.toggleActivo(id));
    }

    // PUT /api/usuarios/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> editarPerfil(
            @PathVariable Integer id,
            @RequestBody Usuario datos) {
        return ResponseEntity.ok(usuarioService.editarPerfil(id, datos));
    }

    // DELETE /api/usuarios/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}