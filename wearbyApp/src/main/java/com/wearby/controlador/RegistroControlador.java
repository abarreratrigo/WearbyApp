package com.wearby.controlador;

import com.wearby.servicio.UsuarioServicio;
import com.wearby.sesion.SesionUsuario;
import com.wearby.util.Alertas;
import com.wearby.util.Navegador;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * Controlador de la pantalla de registro
 * Valida los datos introducidos por el usuario y llama a
 * la API REST para crear la cuenta
 */
public class RegistroControlador {

    @FXML private TextField nombreField;
    @FXML private TextField emailField;
    @FXML private PasswordField contrasenaField;
    @FXML private PasswordField confirmarField;
    @FXML private Label errorLabel;
    @FXML private Button registroBtn;

    private final UsuarioServicio usuarioServicio = new UsuarioServicio();

    @FXML
    private void onRegistro() {
        String nombre = nombreField.getText().trim();
        String email = emailField.getText().trim();
        String contrasena = contrasenaField.getText();
        String confirmar = confirmarField.getText();

        if (nombre.isEmpty() || email.isEmpty() || contrasena.isEmpty()) {
            errorLabel.setText("Por favor, rellena todos los campos");
            return;
        }

        if (!contrasena.equals(confirmar)){
            errorLabel.setText("Las contraseñas no coinciden");
            return;
        }

        if (contrasena.length() < 6){
            errorLabel.setText("La contraseña tiene que tener más de 6 caracteres");
            return;
        }

        registroBtn.setDisable(true);
        errorLabel.setText("");

        new Thread(()-> {
            try {
                var usuario = usuarioServicio.registrar(nombre, email, contrasena);

                Platform.runLater(() -> {
                    if (usuario != null) {
                        SesionUsuario.getInstancia().iniciar(
                                usuario.getId(),
                                usuario.getNombre(),
                                usuario.getEmail(),
                                usuario.getRol()
                        );
                        try {
                            Navegador.navegar("principal.fxml");
                        } catch (Exception e) {
                            errorLabel.setText("Error al cargar la pantalla.");
                        }
                    } else {
                        errorLabel.setText("El email ya está registrado");
                        registroBtn.setDisable(false);
                    }
                });
            } catch (Exception e){
                Platform.runLater(() -> {
                    errorLabel.setText("No se puede conectar con el servidor");
                    registroBtn.setDisable(false);
                });
            }
        }).start();
    }

    @FXML
    private void onIrALogin() {
        try {
            Navegador.navegar("login.fxml");
        } catch (Exception e) {
            Alertas.error("Error", "No se puede cargar la pantalla de login");
        }
    }
}
