package com.wearby.controlador;

import com.google.gson.Gson;
import com.wearby.modelo.*;
import com.wearby.modelo.Color;
import com.wearby.servicio.CaracteristicasServicio;
import com.wearby.servicio.PrendaServicio;
import com.wearby.util.Alertas;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import java.util.List;

/**
 * Controlador del diálogo de edición de prenda.
 * Recibe la prenda a editar, precarga sus datos
 * y envía los cambios
 */
public class EditarPrendaControlador implements Initializable {

    @FXML private TextField nombreField;
    @FXML private ComboBox<Categoria> categoriaCombo;
    @FXML private ComboBox<com.wearby.modelo.Color> colorCombo;
    @FXML private ComboBox<Estilo> estiloCombo;
    @FXML private ComboBox<Formalidad> formalidadCombo;
    @FXML private ComboBox<Temporada> temporadaCombo;
    @FXML private CheckBox favoritoCheck;
    @FXML private TextArea notasArea;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    private final PrendaServicio prendaServicio = new PrendaServicio();
    private final CaracteristicasServicio caracteristicasServicio = new CaracteristicasServicio();
    private final Gson  gson = new Gson();

    private Prenda prenda;
    private Runnable onGuardadoExitoso;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        cargarCaracteristicas();
    }

    /**
     * Recibe la prenda para editar y precarga todos los datos en el formulario
     */

    public void setPrenda(Prenda prenda) {
        this.prenda = prenda;
        Platform.runLater(this::precargarDatos);
    }

    public void setOnGuardadoExitoso (Runnable callback){
        this.onGuardadoExitoso = callback;
    }

    private void cargarCaracteristicas() {
        new Thread(() -> {
            try {
                List<Categoria> categorias = caracteristicasServicio.getCategorias();
                List<Color> colores = caracteristicasServicio.getColores();
                List<Estilo> estilos = caracteristicasServicio.getEstilos();
                List<Formalidad> formalidades = caracteristicasServicio.getFormalidades();
                List<Temporada> temporadas = caracteristicasServicio.getTemporadas();

                Platform.runLater(() -> {
                    categoriaCombo.getItems().addAll(categorias);
                    colorCombo.getItems().addAll(colores);
                    estiloCombo.getItems().addAll(estilos);
                    formalidadCombo.getItems().addAll(formalidades);
                    temporadaCombo.getItems().addAll(temporadas);

                    // Configurar cómo se muestra cada elemento en el combo
                    categoriaCombo.setConverter(new javafx.util.StringConverter<Categoria>() {
                        public String toString(Categoria c) { return c != null ? c.getNombre() : ""; }
                        public Categoria fromString(String s) { return null; }
                    });
                    colorCombo.setConverter(new javafx.util.StringConverter<Color>() {
                        public String toString(Color c) { return c != null ? c.getNombre() : ""; }
                        public Color fromString(String s) { return null; }
                    });
                    estiloCombo.setConverter(new javafx.util.StringConverter<Estilo>() {
                        public String toString(Estilo e) { return e != null ? e.getNombre() : ""; }
                        public Estilo fromString(String s) { return null; }
                    });
                    formalidadCombo.setConverter(new javafx.util.StringConverter<Formalidad>() {
                        public String toString(Formalidad f) { return f != null ? f.getNombre() : ""; }
                        public Formalidad fromString(String s) { return null; }
                    });
                    temporadaCombo.setConverter(new javafx.util.StringConverter<Temporada>() {
                        public String toString(Temporada t) { return t != null ? t.getNombre() : ""; }
                        public Temporada fromString(String s) { return null; }
                    });

                    // Precargar datos si la prenda ya fue asignada
                    if (prenda != null) precargarDatos();
                });
            } catch (Exception e) {
                Platform.runLater(() ->
                        Alertas.error("Error", "No se pudieron cargar las características."));
            }
        }).start();
    }

    private void precargarDatos() {
        if (prenda == null) return;

        nombreField.setText(prenda.getNombre());
        notasArea.setText(prenda.getNotas() != null ? prenda.getNotas() : "");
        favoritoCheck.setSelected(Boolean.TRUE.equals(prenda.getFavorito()));

        if (prenda.getCategoria() != null) {
            categoriaCombo.getItems().stream()
                    .filter(c -> c.getId().equals(prenda.getCategoria().getId()))
                    .findFirst().ifPresent(categoriaCombo::setValue);
        }
        if (prenda.getColor() != null) {
            colorCombo.getItems().stream()
                    .filter(c -> c.getId().equals(prenda.getColor().getId()))
                    .findFirst().ifPresent(colorCombo::setValue);
        }
        if (prenda.getEstilo() != null) {
            estiloCombo.getItems().stream()
                    .filter(e -> e.getId().equals(prenda.getEstilo().getId()))
                    .findFirst().ifPresent(estiloCombo::setValue);
        }
        if (prenda.getFormalidad() != null) {
            formalidadCombo.getItems().stream()
                    .filter(f -> f.getId().equals(prenda.getFormalidad().getId()))
                    .findFirst().ifPresent(formalidadCombo::setValue);
        }
        if (prenda.getTemporada() != null) {
            temporadaCombo.getItems().stream()
                    .filter(t -> t.getId().equals(prenda.getTemporada().getId()))
                    .findFirst().ifPresent(temporadaCombo::setValue);
        }
    }

    @FXML
    private void onGuardar() {
        if (nombreField.getText().trim().isEmpty()) {
            Alertas.error("Error", "El nombre de la prenda es obligatorio.");
            return;
        }
        if (categoriaCombo.getValue() == null){
            Alertas.error("Error", "Debes seleccionar una categoría.");
            return;
        }
        Prenda prendaActualizada = new Prenda();
        prendaActualizada.setId(prenda.getId());
        prendaActualizada.setNombre(nombreField.getText().trim());
        prendaActualizada.setCategoria(categoriaCombo.getValue());
        prendaActualizada.setColor(colorCombo.getValue());
        prendaActualizada.setEstilo(estiloCombo.getValue());
        prendaActualizada.setFormalidad(formalidadCombo.getValue());
        prendaActualizada.setTemporada(temporadaCombo.getValue());
        prendaActualizada.setFavorito(favoritoCheck.isSelected());
        prendaActualizada.setNotas(notasArea.getText().trim());
        prendaActualizada.setImageUrl(prenda.getImageUrl());

        btnGuardar.setDisable(true);
        btnGuardar.setText("Guardando...");

        new Thread(() -> {
            try {
                String json = gson.toJson(prendaActualizada);
                prendaServicio.editar(prenda.getId(), json);

                Platform.runLater(() -> {
                    if (onGuardadoExitoso != null) onGuardadoExitoso.run();
                    cerrarDialogo();
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    btnGuardar.setDisable(false);
                    btnGuardar.setText("Guardar cambios");
                    Alertas.error("Error", "No se pudo guardar la prenda.");
                });
            }
        }).start();
    }

    @FXML
    private void onCancelar() {
        cerrarDialogo();
    }

    private void cerrarDialogo() {
        Stage stage = (Stage) categoriaCombo.getScene().getWindow();
        stage.close();
    }
}
