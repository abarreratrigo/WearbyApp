package com.wearby.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Gestiona la navegación entre pantallas de la aplicación
 * Centraliza la carga de archivos FXML para evitar repetición de código
 */
public class Navegador {

    private static Stage stagePrincipal;

    public static void setStagePrincipal(Stage stage){
        stagePrincipal = stage;
    }

    public static void navegar(String rutaFxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                Navegador.class.getResource("/fxml/" + rutaFxml)
        );

        Parent root = loader.load();
        stagePrincipal.getScene().setRoot(root);
    }

    public static <T> T navegarConControlador (String rutaFxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                Navegador.class.getResource("/fxml/" + rutaFxml)
        );

        Parent root = loader.load();
        stagePrincipal.getScene().setRoot(root);
        return loader.getController();
    }

    public static Stage getStage(){
        return stagePrincipal;
    }
}
