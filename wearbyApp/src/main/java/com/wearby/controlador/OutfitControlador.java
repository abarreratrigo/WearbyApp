package com.wearby.controlador;

import com.wearby.modelo.*;
import com.wearby.servicio.CaracteristicasServicio;
import com.wearby.servicio.OutfitServicio;
import com.wearby.sesion.SesionUsuario;
import com.wearby.util.Alertas;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controlador de la pantalla de generación de outfits.
 * Gestiona filtros, categorías opcionales y
 * construcción de los carruseles de resultados
 */

public class OutfitControlador implements Initializable {

    @FXML private ComboBox<Estilo> estiloCombo;
    @FXML private ComboBox<Temporada> temporadaCombo;
    @FXML private ComboBox<Formalidad> formalidadCombo;
    @FXML private ComboBox<Categoria> categoriaOpcionalCombo;
    @FXML private HBox categoriasSeleccionadasBox;
    @FXML private VBox resultadosBox;

    private final CaracteristicasServicio caracteristicasServicio = new CaracteristicasServicio();
    private final OutfitServicio outfitServicio = new OutfitServicio();
    private final List<Integer> categoriaIdsOpcionales = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle rb){
        cargarFiltros();
    }

    private void cargarFiltros(){
        new Thread(() -> {
            try {
                List<Estilo> estilos = caracteristicasServicio.getEstilos();
                List<Temporada> temporadas = caracteristicasServicio.getTemporadas();
                List<Formalidad> formalidades = caracteristicasServicio.getFormalidades();
                List<Categoria> categorias = caracteristicasServicio.getCategorias();

                Platform.runLater(() -> {
                    configurarCombo(estiloCombo, estilos);
                    configurarCombo(temporadaCombo, temporadas);
                    configurarCombo(formalidadCombo, formalidades);
                    configurarCombo(categoriaOpcionalCombo, categorias);
                });
            } catch (Exception e) {
                Platform.runLater(() ->
                        Alertas.error("Error", "No se pudieron cargar los filtros"));
            }
        }).start();
    }

    private <T> void configurarCombo(ComboBox<T> combo, List<T> items){
        combo.getItems().setAll(items);
        combo.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(T item, boolean empty){
                super.updateItem(item, empty);
                if (empty || item == null) {setText(null); return;}
                if (item instanceof Estilo e) setText(e.getNombre());
                else if (item instanceof Temporada t) setText(t.getNombre());
                else if (item instanceof Formalidad f) setText(f.getNombre());
                else if (item instanceof Categoria c) setText(c.getNombre());
        }});
        combo.setButtonCell(new ListCell<>() {
            @Override protected void updateItem(T item, boolean empty){
                super.updateItem(item, empty);
                if(empty || item == null) {setText(combo.getPromptText()); return;}
                if (item instanceof Estilo e) setText(e.getNombre());
                else if (item instanceof Temporada t) setText(t.getNombre());
                else if (item instanceof Formalidad f) setText(f.getNombre());
                else if (item instanceof Categoria c) setText(c.getNombre());
            }
        });
    }

    @FXML
    private void onAnadirCategoria(){
        Categoria seleccionada = categoriaOpcionalCombo.getValue();
        if(seleccionada == null) return;
        if(categoriaIdsOpcionales.contains(seleccionada.getId())) return;

        categoriaIdsOpcionales.add(seleccionada.getId());

        HBox etiqueta = new HBox(6);
        etiqueta.setAlignment(Pos.CENTER);
        etiqueta.setStyle("-fx-background-color: #444; -fx-padding: 4 10 4 10; " +
                "-fx-background-radius: 20;");

        Label texto = new Label(seleccionada.getNombre());
        texto.setStyle("-fx-text-fill: white; -fx-font-size: 12px");

        Button eliminar = new Button(" X ");
        eliminar.setStyle("-fx-background-color: transparent; -fx-text-fill: white; " +
                "-fx-cursor-hand; -fx-font-size: 10px; -fx-padding:0;");
        eliminar.setOnAction(e -> {
            categoriaIdsOpcionales.remove(seleccionada.getId());
            categoriasSeleccionadasBox.getChildren().remove(etiqueta);
        });

        etiqueta.getChildren().addAll(texto, eliminar);
        categoriasSeleccionadasBox.getChildren().add(etiqueta);
    }

    @FXML
    private void onGenerar(){
        Integer estiloId = estiloCombo.getValue() != null ?
                estiloCombo.getValue().getId() : null;
        Integer temporadaId = temporadaCombo.getValue() != null ?
                temporadaCombo.getValue().getId() : null;
        Integer formalidad = formalidadCombo.getValue() != null ?
                formalidadCombo.getValue().getId() : null;

        resultadosBox.getChildren().clear();
        Label cargando = new Label("Generando outfits...");
        cargando.setStyle("-fx-font-size:16px; -fx-text-fill: #666;");
        resultadosBox.getChildren().add(cargando);

        new Thread(() -> {
            try {
                List<OutfitCarruselDTO> carruseles = outfitServicio.generar(
                        SesionUsuario.getInstancia().getId(),
                        estiloId, temporadaId, formalidad,
                        categoriaIdsOpcionales
                );

                Platform.runLater(() -> {
                    resultadosBox.getChildren().clear();
                    if(carruseles.isEmpty()) {
                        Label vacio = new Label(
                                "No se encontraron prendas con esas características");
                        vacio.setStyle("-fx-font-size: 15px; -fx-text-fill: #888;");
                        resultadosBox.getChildren().add(vacio);
                        return;
                    }
                    for (OutfitCarruselDTO carrusel : carruseles) {
                        resultadosBox.getChildren().add(crearCarrusel(carrusel));
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    resultadosBox.getChildren().clear();
                    Alertas.error("Error", "No se pudo conectar con el servidor");
                });
            }
        }).start();
    }

    /**
     * Crea un bloque visual de carrusel para una categoría.
     * Incluye título, flechas de navegación e indicadores de posición.
     */
    private VBox crearCarrusel(OutfitCarruselDTO carrusel){
        VBox bloque = new VBox(12);
        bloque.setStyle("-fx-background-color: white; -fx-background-radius:10; " +
                "-fx-padding: 16; -fx-effect; dropshadow(gaussian," +
                "rgba(0,0,0,0.1), 8, 0, 0, 2);");

        Label titulo = new Label(carrusel.getCategoria());
        titulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        List<PrendaFiltroDTO> prendas = carrusel.getPrendas();
        int [] indice = {0};

        ImageView imageView = new ImageView();
        imageView.setFitHeight(200);
        imageView.setFitWidth(200);
        imageView.setPreserveRatio(true);

        Label nombreLabel = new Label();
        nombreLabel.setStyle("-fx-font-size: 13px;");

        Label colorLabel = new Label();
        colorLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 12px;");

        //Indicadores de posición
        HBox indicadores = new HBox(6);
        indicadores.setAlignment(Pos.CENTER);
        for (int i = 0; i < prendas.size(); i++) {
            Label punto = new Label("●");
            punto.setStyle(i == 0 ?
                    "-fx-text-fill: #2c2c2c; -fx-font-size: 12px" :
                    "-fx-text-fill: #ccc; -fx-font-size: 10px;");
            indicadores.getChildren().add(punto);
        }

        Runnable actualizar = () -> {
            PrendaFiltroDTO prenda = prendas.get(indice[0]);
            nombreLabel.setText(prenda.getNombre());
            colorLabel.setText(prenda.getColor() != null ? prenda.getColor() : "");

            if (prenda.getImagenUrl() != null) {
                new Thread(() -> {
                    try {
                        Image img = new Image(
                                "http://localhost:8080/" + prenda.getImagenUrl(),
                                200,200, true, true
                        );
                        Platform.runLater(() -> imageView.setImage(img));
                    } catch (Exception e) {
                        Platform.runLater(() -> imageView.setImage(null));
                    }
                }).start();
            } else {
                imageView.setImage(null);
            }

            for (int i = 0; i<indicadores.getChildren().size(); i++) {
                indicadores.getChildren().get(i).setStyle(
                        i == indice[0] ?
                                "-fx-text-fill: #2c2c2c; -fx-font-size: 12px;" :
                                "-fx-text-fill: #ccc; -fx-font-size: 12px;"
                );
            }
        };

        actualizar.run();

        //Flechas de navegación
        Button izquierda = new Button("◀");
        izquierda.setStyle("-fx-background-color: transparent; -fx-font-size: 20px;" +
                "-fx-cursor: hand; -fx-text-fill: #2c2c2c;");
        izquierda.setOnAction( e -> {
            if (indice[0] > 0) {
                indice[0] --; actualizar.run();
            }
        });

        Button derecha = new Button("▶");
        derecha.setStyle("-fx-background-color: transparent; -fx-font-size: 20px;" +
                "-fx-cursor: hand; -fx-text-fill: #2c2c2c;");
        derecha.setOnAction(e -> {
            if (indice[0] < prendas.size() - 1) {
                indice[0] ++;
                actualizar.run();
            }
        });

        HBox navegacion = new HBox(16);
        navegacion.setAlignment(Pos.CENTER);
        navegacion.getChildren().addAll(izquierda, imageView, derecha);

        VBox info = new VBox(4);
        info.setAlignment(Pos.CENTER);
        info.getChildren().addAll(nombreLabel,colorLabel);

        bloque.getChildren().addAll(titulo, navegacion, info, indicadores);
        return bloque;
    }
}
