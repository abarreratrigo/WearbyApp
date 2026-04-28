package com.wearby.controlador;

import com.wearby.modelo.*;
import com.wearby.servicio.CaracteristicasServicio;
import com.wearby.sesion.SesionUsuario;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controlador de diálogo de añadir prenda.
 * Gestiona la selección de imagen desde el sistema de archivos
 * y el envío de los datos a la API REST como Multipart/form-data
 */

public class AnadirPrendaControlador implements Initializable {

    @FXML private ImageView imagenPreview;
    @FXML private TextField nombreField;
    @FXML private ComboBox<Categoria> categoriaCombo;
    @FXML private ComboBox<Color> colorCombo;
    @FXML private ComboBox<Estilo> estiloCombo;
    @FXML private ComboBox<Formalidad> formalidadCombo;
    @FXML private ComboBox<Temporada> temporadaCombo;
    @FXML private TextArea notasArea;
    @FXML private Label errorLabel;
    @FXML private Button guardarBtn;

    private File imagenSeleccionada;
    private Runnable onGuardadoExitoso;

    private final CaracteristicasServicio caracteristicasServicio = new CaracteristicasServicio();

    public void setOnGuardadoExitoso(Runnable callback) {
        this.onGuardadoExitoso = callback;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        configurarCombos();
        cargarCaracteristicas();
    }

    private void configurarCombos() {
        categoriaCombo.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(Categoria c, boolean empty) {
                super.updateItem(c, empty);
                setText(empty || c == null ? null : c.getNombre());
            }
        });
        categoriaCombo.setButtonCell(new ListCell<>(){
            @Override protected void updateItem(Categoria c, boolean empty){
                super.updateItem(c, empty);
                setText(empty || c == null ? "Categoría" : c.getNombre());
            }
        });

        colorCombo.setCellFactory(lv -> new ListCell<>(){
            @Override protected void updateItem(Color c, boolean empty) {
                super.updateItem(c, empty);
                setText(empty || c == null ? null : c.getNombre());
            }
        });
        colorCombo.setButtonCell(new ListCell<>(){
            @Override protected void updateItem(Color c, boolean empty) {
                super.updateItem(c, empty);
                setText(empty || c == null ? "Color" : c.getNombre());
            }
        });

        estiloCombo.setCellFactory(lv -> new ListCell<>(){
            @Override protected void updateItem(Estilo e, boolean empty) {
                super.updateItem(e, empty);
                setText(empty || e == null ? null : e.getNombre());
            }
        });
        estiloCombo.setButtonCell(new ListCell<>(){
            @Override protected void updateItem(Estilo e, boolean empty) {
                super.updateItem(e, empty);
                setText(empty || e == null ? "Estilo" : e.getNombre());
            }
        });

        formalidadCombo.setCellFactory(lv -> new ListCell<>(){
            @Override protected void updateItem(Formalidad f, boolean empty){
                super.updateItem(f, empty);
                setText(empty || f == null ? null : f.getNombre());
            }
        });
        formalidadCombo.setButtonCell(new ListCell<>(){
            @Override protected void updateItem(Formalidad f, boolean empty){
                super.updateItem(f, empty);
                setText(empty || f == null ? "Formalidad" : f.getNombre());
            }
        });

        temporadaCombo.setCellFactory(lv -> new ListCell<>(){
            @Override protected void updateItem(Temporada t, boolean empty){
                super.updateItem(t, empty);
                setText(empty || t == null ? null : t.getNombre());
            }
        });
        temporadaCombo.setButtonCell(new ListCell<>(){
            @Override protected void updateItem(Temporada t, boolean empty){
                super.updateItem(t, empty);
                setText(empty || t == null ? "Temporada" : t.getNombre());
            }
        });
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
                    categoriaCombo.getItems().setAll(categorias);
                    colorCombo.getItems().setAll(colores);
                    estiloCombo.getItems().setAll(estilos);
                    formalidadCombo.getItems().setAll(formalidades);
                    temporadaCombo.getItems().setAll(temporadas);
                });
            } catch (Exception e) {
                Platform.runLater(() ->
                        errorLabel.setText("Error al cargar las características"));
            }
        }).start();
    }

    @FXML
    private void onSeleccionarImagen(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen de la prenda");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imágenes", "*.jpg", "*.jpeg", "*.png", "*.webp")
        );

        Stage stage = (Stage) nombreField.getScene().getWindow();
        imagenSeleccionada = fileChooser.showOpenDialog(stage);

        if (imagenSeleccionada != null) {
            imagenPreview.setImage(new Image(imagenSeleccionada.toURI().toString()));
        }
    }

    @FXML
    private void onGuardar(){
        if (nombreField.getText().trim().isEmpty()) {
            errorLabel.setText("El nombre es obligatorio.");
            return;
        }
        if (categoriaCombo.getValue() == null){
            errorLabel.setText("Selecciona una categoría.");
            return;
        }

        guardarBtn.setDisable(true);
        errorLabel.setText("");

        new Thread(() -> {
            try {
                OkHttpClient cliente = new OkHttpClient();

                MultipartBody.Builder builder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("usuarioId",
                                String.valueOf(SesionUsuario.getInstancia().getId()))
                        .addFormDataPart("nombre", nombreField.getText().trim())
                        .addFormDataPart("categoriaId",
                                String.valueOf(categoriaCombo.getValue().getId()))
                        .addFormDataPart("colorId",
                                colorCombo.getValue() != null ?
                                String.valueOf(colorCombo.getValue().getId()) : "1")
                        .addFormDataPart("estiloId",
                                estiloCombo.getValue() != null ?
                                String.valueOf(estiloCombo.getValue().getId()): "1")
                        .addFormDataPart("formalidadId",
                                formalidadCombo.getValue() != null ?
                                String.valueOf(formalidadCombo.getValue().getId()) : "1")
                        .addFormDataPart("temporadaId",
                                temporadaCombo.getValue() != null ?
                                String.valueOf(temporadaCombo.getValue().getId()) : "5")
                        .addFormDataPart("notas", notasArea.getText());

                if (imagenSeleccionada != null) {
                    builder.addFormDataPart("imagen",
                            imagenSeleccionada.getName(),
                            RequestBody.create(imagenSeleccionada,
                                    MediaType.parse("image/*")));
                }

                Request request = new Request.Builder()
                        .url("http://localhost:8080/api/prendas")
                        .post(builder.build())
                        .build();

                Response respuesta = cliente.newCall(request).execute();

                Platform.runLater(() -> {
                    if (respuesta.isSuccessful()) {
                        if (onGuardadoExitoso != null) onGuardadoExitoso.run();
                        onCancelar();
                    } else {
                        errorLabel.setText("Error al guardar la prenda.");
                        guardarBtn.setDisable(false);
                    }
                });
            } catch (IOException e){
                Platform.runLater(() -> {
                    errorLabel.setText("No se puede conectar con el servidor.");
                    guardarBtn.setDisable(false);
                });
            }
        }).start();
    }

    @FXML
    private void onCancelar() {
        Stage stage = (Stage) nombreField.getScene().getWindow();
        stage.close();
    }
}
