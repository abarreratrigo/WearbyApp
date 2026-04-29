package com.wearby.controlador;

import com.wearby.servicio.UsuarioServicio;
import com.wearby.sesion.SesionUsuario;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador de la pantalla de perfil
 * Permite al usuario ver y modificar su información personal
 * y cambiar su contraseña
 */

public class PerfilControlador implements Initializable {

    @FXML private TextField nombreField;
    @FXML private TextField emailField;
    @FXML private PasswordField contrasenaActualField;
    @FXML private PasswordField nuevaContrasenaField;
    @FXML private PasswordField confirmarContrasenaField;
    @FXML private Label mensajeLabel;

    private final UsuarioServicio usuarioServicio = new UsuarioServicio();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SesionUsuario sesion = SesionUsuario.getInstancia();
        nombreField.setText(sesion.getNombre());
        emailField.setText(sesion.getEmail());
    }

    @FXML
    private void onGuardarPerfil(){
        String nombre = nombreField.getText().trim();
        String email = emailField.getText().trim();

        if (nombre.isEmpty() || email.isEmpty()) {
            mostrarMensaje("Rellena todos los campos", false);
            return;
        }

        new Thread(() -> {
            try {
                var usuario = usuarioServicio.editarPerfil(
                        SesionUsuario.getInstancia().getId(),
                        nombre, email, null
                );
                Platform.runLater(() -> {
                    if (usuario != null) {
                        SesionUsuario.getInstancia().iniciar(
                                usuario.getId(),
                                usuario.getNombre(),
                                usuario.getEmail(),
                                usuario.getRol()
                        );
                        mostrarMensaje("Perfil actualizado correctamente", true);
                    } else {
                        mostrarMensaje("Error al actualizar el perfil", false);
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() ->
                        mostrarMensaje("No se puede conectar con el servidor", false));
            }
        }).start();
    }

    @FXML
    private void onCambiarContrasena() {
        String nueva = nuevaContrasenaField.getText();
        String confirmar = confirmarContrasenaField.getText();

        if (nueva.isEmpty() || confirmar.isEmpty()) {
            mostrarMensaje("Todos los campos deben estar rellenados", false);
            return;
        }

        if (!nueva.equals(confirmar)) {
            mostrarMensaje("Las contraseñas no coinciden", false);
            return;
        }

        if (nueva.length() < 6) {
            mostrarMensaje("La contraseña tiene que tener al menos 6 caracteres", false);
            return;
        }

        new Thread(() -> {
            try {
                var usuario = usuarioServicio.editarPerfil(
                        SesionUsuario.getInstancia().getId(),
                        nombreField.getText().trim(),
                        emailField.getText().trim(),
                        nueva
                );
                Platform.runLater(() -> {
                    if (usuario != null) {
                        mostrarMensaje("Contraseña cambiada correctamente", true);
                        contrasenaActualField.clear();
                        nuevaContrasenaField.clear();
                        confirmarContrasenaField.clear();
                    } else {
                        mostrarMensaje("Error al cambiar la contraseña", false);
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() ->
                        mostrarMensaje("No se puede conectar con el servidor", false));
            }
        }).start();
    }

    private void mostrarMensaje(String texto, boolean exito) {
        mensajeLabel.setText(texto);
        mensajeLabel.setStyle(exito ?
                "-fx-text-fill: #26ae60; -fx-font-size: 14px;" :
                "-fx-text-fill: #e74c3c; -fx-font-size: 14px");
    }
}