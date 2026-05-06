package com.wearby.controlador;

import com.wearby.modelo.Prenda;
import com.wearby.servicio.CaracteristicasServicio;
import com.wearby.servicio.PrendaServicio;
import com.wearby.sesion.SesionUsuario;
import com.wearby.util.Alertas;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controlador de la pantalla de armario
 * Carga y muestra las prendas del usuario en una galería
 * de tarjeta y con opciones de filtrado y acciones
 */
public class ArmarioControlador implements Initializable {

    @FXML private FlowPane galeriaPane;
    @FXML private ComboBox<String> filtroCategoriaCombo;
    @FXML private ComboBox<String> filtroEstiloCombo;
    @FXML private ComboBox<String> filtroTemporadaCombo;

    private final PrendaServicio prendaServicio = new PrendaServicio();
    private final CaracteristicasServicio caracteristicaServicio = new CaracteristicasServicio();
    private List<Prenda> todasLasPrendas;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        cargarFiltros();
        cargarPrendas();
    }

    private void cargarFiltros() {
        new Thread(() -> {
            try {
                var categorias = caracteristicaServicio.getCategorias();
                var estilos = caracteristicaServicio.getEstilos();
                var temporadas = caracteristicaServicio.getTemporadas();

                Platform.runLater(() -> {
                    filtroCategoriaCombo.getItems().add("Todas");
                    categorias.forEach(c -> filtroCategoriaCombo.getItems().add(c.getNombre()));
                    filtroCategoriaCombo.setValue("Todas");

                    filtroEstiloCombo.getItems().add("Todos");
                    estilos.forEach(e -> filtroEstiloCombo.getItems().add(e.getNombre()));
                    filtroEstiloCombo.setValue("Todos");

                    filtroTemporadaCombo.getItems().add("Todas");
                    temporadas.forEach(t -> filtroTemporadaCombo.getItems().add(t.getNombre()));
                    filtroTemporadaCombo.setValue("Todas");
                });
            } catch (Exception e) {
                Platform.runLater(() -> Alertas.error("Error", "No se pudieron cargar los filtros"));
            }
        }).start();
    }

    private void cargarPrendas() {
        new Thread(()-> {
            try{
                todasLasPrendas = prendaServicio.getPrendas(
                        SesionUsuario.getInstancia().getId()
                );
                Platform.runLater(() -> mostrarPrendas(todasLasPrendas));
            } catch (Exception e) {
                Platform.runLater(() -> Alertas.error("Error", "No se han podido cargar las prendas"));
            }
        }).start();
    }

    private void mostrarPrendas(List<Prenda> prendas) {
        galeriaPane.getChildren().clear();
        for (Prenda prenda: prendas) {
            galeriaPane.getChildren().add(crearTarjeta(prenda));
        }
    }

    /**
     * Se crea la tarjeta visual de cada prenda con su imagen,
     * nombre y botones de acción
     */

    private VBox crearTarjeta(Prenda prenda) {
        VBox tarjeta = new VBox(8);
        tarjeta.setPrefWidth(180);
        tarjeta.setPadding(new Insets(12));
        tarjeta.setAlignment(Pos.TOP_CENTER);
        tarjeta.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                " -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");
        tarjeta.setFocusTraversable(false);
        tarjeta.setOnMouseClicked(e -> e.consume());

        ImageView imageView = new ImageView();
        imageView.setFitWidth(156);
        imageView.setFitHeight(156);
        imageView.setPreserveRatio(true);
        imageView.setStyle("-fx-background-radius: 8;");

        StackPane imagenContenedor = new javafx.scene.layout.StackPane(imageView);
        imagenContenedor.setPrefSize(156, 156);
        imagenContenedor.setMinSize(156, 156);
        imagenContenedor.setMaxSize(156, 156);
        imagenContenedor.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 8;");


        imageView.setOnMouseClicked(e -> {
            e.consume(); // evita que el evento se propague a la tarjeta
            if (prenda.getImageUrl() != null) {
                String urlCompleta = "http://localhost:8080/" +
                        prenda.getImageUrl().replace(" ", "%20");
                ImageView imgGrande = new ImageView(
                        new Image(urlCompleta, 400, 400, true, true)
                );
                imgGrande.setPreserveRatio(true);

                javafx.scene.layout.StackPane contenedor =
                        new javafx.scene.layout.StackPane(imgGrande);
                contenedor.setStyle("-fx-background-color: white; -fx-padding: 20;");

                Stage ventana = new Stage();
                ventana.setTitle(prenda.getNombre());
                ventana.setScene(new javafx.scene.Scene(contenedor, 440, 440));
                ventana.initOwner(galeriaPane.getScene().getWindow());
                ventana.initModality(Modality.APPLICATION_MODAL);
                ventana.show();
            }
        });
        imageView.setStyle("-fx-cursor: hand;");

        if (prenda.getImageUrl() != null){
            String urlCompleta = "http://localhost:8080/" +
                    prenda.getImageUrl().replace(" ", "%20");
            new Thread(() -> {
                try {
                    Image img = new Image(urlCompleta, 156, 156, true, true);
                    Platform.runLater(() -> imageView.setImage(img));
                } catch (Exception e) {
                    System.out.println("Error cargando imagen: " + e.getMessage());
                }
            }).start();
        }

        Label nombre = new Label(prenda.getNombre());
        nombre.setFont(Font.font(15));
        nombre.setWrapText(true);
        nombre.setMaxWidth(156);
        nombre.setStyle("-fx-text-fill: #333333;");

        Label categoria = new Label(
                prenda.getCategoria() != null ? prenda.getCategoria().getNombre() : ""
        );
        categoria.setStyle("-fx-text-fill: #888; -fx-font-size: 11px");

        HBox acciones = new HBox(6);
        acciones.setAlignment(Pos.CENTER);

        Button btnFavorito = new Button(
                Boolean.TRUE.equals(prenda.getFavorito()) ? "♥" : "♡"
        );
        btnFavorito.setStyle(
                "-fx-background-color: #fff0f0;" +
                        "-fx-text-fill: #e74c3c;" +
                        "-fx-cursor: hand;" +
                        "-fx-font-size: 14px;" +
                        "-fx-background-radius: 6;" +
                        "-fx-padding: 4 8 4 8;"
        );
        btnFavorito.setFocusTraversable(false);
        btnFavorito.setOnAction(e -> onToggleFavorito(prenda, btnFavorito));

        Button btnEliminar = new Button("✕");
        btnEliminar.setStyle(
                "-fx-background-color: #f5f5f5;" +
                        "-fx-text-fill: #888;" +
                        "-fx-cursor: hand;" +
                        "-fx-font-size: 14px;" +
                        "-fx-background-radius: 6;" +
                        "-fx-padding: 4 8 4 8;"
        );
        btnEliminar.setFocusTraversable(false);
        btnEliminar.setOnAction(e -> onEliminar(prenda, tarjeta));

        acciones.getChildren().addAll(btnFavorito, btnEliminar);
        tarjeta.getChildren().addAll(imagenContenedor, nombre, categoria, acciones);

        return tarjeta;
    }

    private void onToggleFavorito(Prenda prenda, Button btn) {
        new Thread(()-> {
            try {
                prendaServicio.toggleFavorito(prenda.getId());
                boolean nuevoEstado = !Boolean.TRUE.equals(prenda.getFavorito());
                prenda.setFavorito(nuevoEstado);
                Platform.runLater(() -> btn.setText(nuevoEstado ? "♥" : "♡"));
            } catch (Exception e) {
                Platform.runLater(() ->
                        Alertas.error("Error", "No se ha podido actualizar el favorito"));
            }
        }).start();
    }

    private void onEliminar(Prenda prenda, VBox tarjeta) {
        boolean confirmar = Alertas.confirmar(
                "Eliminar prenda",
                "¿Estás seguro de que quieres eliminar \"" + prenda.getNombre() + "\"?"
        );

        if (confirmar) {
            new Thread(() -> {
                try {
                    prendaServicio.eliminar(prenda.getId());
                    Platform.runLater(() ->
                            galeriaPane.getChildren().remove(tarjeta));
                }catch (Exception e){
                    Platform.runLater(() ->
                            Alertas.error("Error", "No se ha podido eliminar la prenda."));
                }
            }).start();
        }
    }

    @FXML private void onFiltrar() {
        if(todasLasPrendas == null) return;

        String categoria = filtroCategoriaCombo.getValue();
        String estilo = filtroEstiloCombo.getValue();
        String temporada = filtroTemporadaCombo.getValue();

        List<Prenda> filtradas = todasLasPrendas.stream()
                .filter(p -> categoria == null || categoria.equals("Todas") ||
                        (p.getCategoria() != null && p.getCategoria().getNombre().equals(categoria)))
                .filter(p -> estilo == null || estilo.equals("Todos") ||
                        (p.getEstilo() != null && p.getEstilo().getNombre().equals(estilo)))
                .filter(p -> temporada == null || temporada.equals("Todas") ||
                        p.getTemporada() != null && p.getTemporada().getNombre().equals(temporada))
                .toList();

        mostrarPrendas(filtradas);
    }

    @FXML
    private void onLimpiarFiltros() {
        filtroCategoriaCombo.setValue("Todas");
        filtroEstiloCombo.setValue("Todos");
        filtroTemporadaCombo.setValue("Todas");
        mostrarPrendas(todasLasPrendas);
    }

    @FXML
    private void onAnadirPrenda() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/AnadirPrendaDialogo.fxml")
            );

            VBox contenido = loader.load();
            AnadirPrendaControlador controlador = loader.getController();

            //Cuando se guarde correctamente, recargar la galería
            controlador.setOnGuardadoExitoso(this::cargarPrendas);

            Stage dialogo = new Stage();
            dialogo.setTitle("Añadir prenda");
            dialogo.setScene(new javafx.scene.Scene(contenido));
            dialogo.initOwner(galeriaPane.getScene().getWindow());
            dialogo.initModality(Modality.WINDOW_MODAL);
            dialogo.showAndWait();
        } catch (IOException e) {
            Alertas.error("Error", "No se puede abrir el diálogo.");
        }
    }
}
