package com.wearby.controlador;

import com.wearby.sesion.SesionUsuario;
import com.wearby.util.Alertas;
import com.wearby.util.Navegador;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador de la pantalla principal.
 * Gestiona la navegación entre módulos cargando
 * cada vista en el contenido central dinámicamente
 */

public class PrincipalControlador implements Initializable {

    @FXML private Label nombreUsuarioLabel;
    @FXML private StackPane contenidoCentral;
    @FXML private Button btnArmario;
    @FXML private Button btnFavoritos;
    @FXML private Button btnOutfits;
    @FXML private Button btnPerfil;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nombreUsuarioLabel.setText(SesionUsuario.getInstancia().getNombre());
        cargarVista("armario.fxml");
    }

    /**
     * Carga una vista en el contenido central si cambiar la ventana completa.
     * La barra lateral permanece siempre visible
     */

    private void cargarVista(String rutaFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/" + rutaFxml)
            );
            Node vista = loader.load();
            contenidoCentral.getChildren().setAll(vista);
        } catch (IOException e) {
            Alertas.error("Error", "No se puede cargar la vista: " + rutaFxml);
        }
    }

    private void desactivarTodos() {
        btnArmario.getStyleClass().setAll("nav-boton");
        btnFavoritos.getStyleClass().setAll("nav-boton");
        btnOutfits.getStyleClass().setAll("nav-boton");
        btnPerfil.getStyleClass().setAll("nav-boton");
    }

    @FXML
    private void onArmario() {
        desactivarTodos();
        btnArmario.getStyleClass().setAll("nav-boton-activo");
        cargarVista("armario.fxml");
    }

    @FXML
    private void onFavoritos(){
        desactivarTodos();
        btnFavoritos.getStyleClass().setAll("nav-boton-activo");
        cargarVista("favoritos.fxml");
    }

    @FXML
    private void onOutfits() {
        desactivarTodos();
        btnOutfits.getStyleClass().setAll("nav-boton-activo");
        cargarVista("outfits.fxml");
    }

    @FXML
    private void onPerfil() {
        desactivarTodos();
        btnPerfil.getStyleClass().setAll("nav-boton-activo");
        cargarVista("perfil.fxml");
    }

    @FXML
    private void onCerrarSesion() {
        boolean confirmar = Alertas.confirmar(
                "Cerrar sesión",
                "¿Estás seguro de que quieres cerrar sesión?"
        );

        if (confirmar) {
            SesionUsuario.getInstancia().cerrar();
            try {
                Navegador.navegar("login.fxml");
            } catch (IOException e) {
                Alertas.error("Error", "No se puede cargar la pantalla de login");
            }
        }
    }
}