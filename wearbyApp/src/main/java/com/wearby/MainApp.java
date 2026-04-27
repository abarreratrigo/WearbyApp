package com.wearby;

import com.wearby.util.Navegador;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Navegador.setStagePrincipal(stage);

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/wearby/fxml/login.fxml")
        );

        Parent root = loader.load();

        Scene scene = new Scene(root, 1200, 800);
        scene.getStylesheets().add(
                getClass().getResource("/com/wearby/css/estilos.css").toExternalForm()
        );

        stage.setTitle("Wearby");
        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
