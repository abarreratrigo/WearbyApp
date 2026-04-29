package com.wearby.controlador;

import com.wearby.sesion.SesionUsuario;
import com.wearby.util.Alertas;
import com.wearby.util.Navegador;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador del panel de administrador.
 * Gestiona la navegación entre los módulos de administración
 * cargando la vista en el centro
 */

public class AdminControlador implements Initializable {

    @FXML private StackPane contenidoCentral;
    @FXML private Button btnUsuarios;
    @FXML private Button btnEstadisticas;
    @FXML private Button btnCategorias;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarVista("admin_usuarios.fxml");
    }

    private void cargarVista(String rutaFxml){
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/" + rutaFxml)
            );
            Node vista = loader.load();
            contenidoCentral.getChildren().setAll(vista);
        } catch (IOException e){
            Alertas.error("Error", "No se puede cargar: " + rutaFxml);
        }
    }

    private void desactivarTodos() {
        btnUsuarios.getStyleClass().setAll("nav-boton");
        btnEstadisticas.getStyleClass().setAll("nav-boton");
        btnCategorias.getStyleClass().setAll("nav-boton");
    }

    @FXML
    private void onUsuarios() {
        desactivarTodos();
        btnUsuarios.getStyleClass().setAll("nav-boton-activo");
        cargarVista("admin-usuarios.fxml");
    }

    @FXML
    private void onEstadisticas(){
        desactivarTodos();
        btnEstadisticas.getStyleClass().setAll("nav-boton-activo");
        cargarVista("admin-estadisticas.fxml");
    }

    @FXML
    private void onCategorias(){
        desactivarTodos();
        btnCategorias.getStyleClass().setAll("nav-boton-activo");
        cargarVista("admin-categorias.fxml");
    }

    @FXML
    private void onCerrarSesion(){
        boolean confirmar = Alertas.confirmar(
                "Cerrar sesión", "¿Estás seguro de que quieres cerrar sesión?"
        );
        if (confirmar) {
            SesionUsuario.getInstancia().cerrar();
            try {
                Navegador.navegar("login.fxml");
            } catch (IOException e) {
                Alertas.error("Error", "No se puede cargar el login");
            }
        }
    }
}
