package com.wearby.controlador;

import com.wearby.servicio.UsuarioServicio;
import com.wearby.sesion.SesionUsuario;
import com.wearby.util.Alertas;
import com.wearby.util.Navegador;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.application.Platform;
import javafx.scene.control.TextField;


/**
 * Controlador de la pantalla de login.
 * Gestiona la autenticación del usuario contra la API REST
 * y redirige según el rol
 */

public class LoginControlador {

    @FXML private TextField emailField;
    @FXML private PasswordField contrasenaField;
    @FXML private Label errorLabel;
    @FXML private Button loginBtn;

    private final UsuarioServicio usuarioServicio = new UsuarioServicio();

    @FXML
    private void onLogin() {
        String email = emailField.getText().trim();
        String contrasena = contrasenaField.getText();

        if(email.isEmpty() || contrasena.isEmpty()){
            errorLabel.setText("No puedes dejar ningún campo vacío");
            return;
        }

        loginBtn.setDisable(true);
        errorLabel.setText("");

        //Llamada a la API separada para no bloquear la UI
        new Thread(() -> {
            try {
                var usuario = usuarioServicio.login(email,contrasena);

                Platform.runLater(() -> {
                    if (usuario != null) {
                        SesionUsuario.getInstancia().iniciar(
                                usuario.getId(),
                                usuario.getNombre(),
                                usuario.getEmail(),
                                usuario.getRol()
                        );

                        try{
                            if (SesionUsuario.getInstancia().isAdmin()) {
                                Navegador.navegar("admin.fxml");
                            } else {
                                Navegador.navegar("principal.fxml");
                            }
                        } catch (Exception e){
                            errorLabel.setText("Error al cargar la pantalla");
                        }
                    } else {
                        errorLabel.setText("Email o contraseña incorrectos");
                        loginBtn.setDisable(false);
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    errorLabel.setText("No se puede conectar con el servidor.");
                    loginBtn.setDisable(false);
                });
            }
        }).start();
    }

    @FXML
    private void onIrARegistro(){
        try {
            Navegador.navegar("registro.fxml");
        } catch (Exception e) {
            Alertas.error("Error", "No se puede cargar la pantalla de registro");
        }
    }
}
