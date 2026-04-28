package com.wearby.controlador;

import com.wearby.modelo.Prenda;
import com.wearby.servicio.PrendaServicio;
import com.wearby.sesion.SesionUsuario;
import com.wearby.util.Alertas;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controlador de la pantalla de favoritos.
 * Reutiliza la misma lógica de galería del armario
 * mostrando únicamente las prendas marcadas como favoritas
 */

public class FavoritosControlador implements Initializable {

    @FXML private FlowPane galeriaPane;

    private final PrendaServicio prendaServicio = new PrendaServicio();

    @Override
    public void initialize(URL url, ResourceBundle rb){
        cargarFavoritos();
    }

    private void cargarFavoritos(){
        new Thread(() -> {
            try {
                List<Prenda> favoritas = prendaServicio.getFavoritas(
                        SesionUsuario.getInstancia().getId()
                );
                Platform.runLater(() -> mostrarPrendas(favoritas));
            } catch (Exception e){
                Platform.runLater(() ->
                        Alertas.error("Error", "No se han podido cargar las favoritas"));
            }
        }).start();
    }

    private void mostrarPrendas(List<Prenda> prendas){
        galeriaPane.getChildren().clear();

        if (prendas.isEmpty()){
            Label vacio = new Label("No tienes prendas favoritas todavía.");
            vacio.setStyle("-fx-text-fill: #888; -fx-font-size: 16px;");
            galeriaPane.getChildren().add(vacio);
            return;
        }

        for (Prenda prenda: prendas) {
            galeriaPane.getChildren().add(crearTarjeta(prenda));
        }
    }

    private VBox crearTarjeta(Prenda prenda) {
        VBox tarjeta = new VBox(8);
        tarjeta.setPrefWidth(180);
        tarjeta.setPadding(new Insets(12));
        tarjeta.setAlignment(Pos.TOP_CENTER);
        tarjeta.setStyle("-fx-background-color: white; -fx-background-radius: 10;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");

        ImageView imageView = new ImageView();
        imageView.setFitWidth(156);
        imageView.setFitHeight(156);
        imageView.setPreserveRatio(true);

        if (prenda.getImageUrl() != null){
            new Thread(() -> {
                try {
                    Image img = new Image(
                            "http://localhost:8080/" + prenda.getImageUrl(),
                            156, 156, true, true
                    );
                    Platform.runLater(() -> imageView.setImage(img));
                } catch (Exception e) {
                    //Si no carga la imagen se queda en blanco
                }
            }).start();
        }

        Label nombre = new Label(prenda.getNombre());
        nombre.setFont(Font.font(15));
        nombre.setWrapText(true);
        nombre.setMaxWidth(156);

        Label categoria = new Label(
                prenda.getCategoria() != null ? prenda.getCategoria().getNombre() : ""
        );
        categoria.setStyle("-fx-text-fill: #888; -fx-font-size: 12px;");

        Button btnDesmarcar = new Button("❤️ Quitar de favoritos");
        btnDesmarcar.setStyle("-fx-background-color: transparent; -fx-text-fill: #e74c3c; " +
                "-fx-cursor: hand; -fx-font-size: 12px");

        btnDesmarcar.setOnAction(e -> onDesmarcarFavorito(prenda, tarjeta));

        tarjeta.getChildren().addAll(imageView, nombre, categoria, btnDesmarcar);
        return tarjeta;
    }

    private void onDesmarcarFavorito(Prenda prenda, VBox tarjeta) {
        new Thread(() -> {
            try {
                prendaServicio.toggleFavorito(prenda.getId());
                Platform.runLater(() ->
                        galeriaPane.getChildren().remove(tarjeta));
            } catch (Exception e){
                Platform.runLater(() ->
                        Alertas.error("Error", "No se pudo actualizar el favorito"));
            }
        }).start();
    }
}
