package com.wearby.controlador;

import com.wearby.modelo.Usuario;
import com.wearby.servicio.UsuarioServicio;
import com.wearby.util.Alertas;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controlador del módulo de gestión de usuarios del administrador.
 * Muestra todos los usuarios en una tabla con opciones de
 * activar/desactivar y eliminar
 */

public class AdminUsuariosControlador implements Initializable {

    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML private TableColumn<Usuario, String> colId;
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colEmail;
    @FXML private TableColumn<Usuario, String> colRol;
    @FXML private TableColumn<Usuario, String> colActivo;
    @FXML private TableColumn<Usuario, Void> colAcciones;

    private final UsuarioServicio usuarioServicio = new UsuarioServicio();

    @Override
    public void initialize(URL url, ResourceBundle rb){
        configurarColumnas();
        cargarUsuarios();
    }

    private void configurarColumnas(){
        colId.setCellValueFactory( d ->
                new SimpleStringProperty(String.valueOf(d.getValue().getId())));
        colNombre.setCellValueFactory( d ->
                new SimpleStringProperty(d.getValue().getNombre()));
        colEmail.setCellValueFactory( d ->
                new SimpleStringProperty(d.getValue().getEmail()));
        colRol.setCellValueFactory( d ->
                new SimpleStringProperty(d.getValue().getRol()));
        colActivo.setCellValueFactory( d ->
                new SimpleStringProperty(Boolean.TRUE.equals(d.getValue().getActivo()) ? "Activo" : "Inactivo"));
        colAcciones.setCellFactory(col -> new TableCell<>(){
            private final Button btnToggle = new Button();
            private final Button btnEliminar = new Button("Eliminar");

            {
                btnToggle.setStyle("-fx-cursor: hand;");
                btnEliminar.setStyle("-fx-background-color: #e74c3c; " +
                        "-fx-text-fill: white; -fx-cursor: hand; " +
                        "-fx-background-radius: 4; -fx-padding: 4 8 4 8;");

                btnToggle.setOnAction( e -> {
                    Usuario u = getTableView().getItems().get(getIndex());
                    onToggleActivo(u);
                });

                btnEliminar.setOnAction( e -> {
                    Usuario u = getTableView().getItems().get(getIndex());
                    onEliminar(u);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty){
                super.updateItem(item, empty);
                if (empty) {setGraphic(null); return;}

                Usuario u = getTableView().getItems().get(getIndex());
                btnToggle.setText(
                        Boolean.TRUE.equals(u.getActivo()) ? "Desactivar" : "Activar");

                btnToggle.setStyle(Boolean.TRUE.equals(u.getActivo()) ?
                        "-fx-background-color: #f39c12; -fx-text-fill: white; " +
                        "-fx-cursor: hand; -fx-background-radius: 4; -fx-padding: 4 8 4 8;" :
                        "-fx-background-color: #27ae60; -fx-text-fill: white; " +
                        "-fx-cursor: hand; -fx-background-radius: 4; -fx-padding: 4 8 4 8;");

                HBox box = new HBox(8, btnToggle, btnEliminar);
                setGraphic(box);
            }
        });
    }

    private void cargarUsuarios() {
        new Thread(() -> {
            try {
                List<Usuario> usuarios = usuarioServicio.getUsuarios();
                Platform.runLater(() ->
                        tablaUsuarios.getItems().setAll(usuarios));
            } catch (Exception e) {
                Platform.runLater(() ->
                        Alertas.error("Error", "No se pudieron cargar los usuarios"));
            }
        }).start();
    }

    private void onToggleActivo(Usuario usuario) {
        new Thread(() -> {
            try {
                usuarioServicio.toggleActivo(usuario.getId());
                Platform.runLater(this::cargarUsuarios);
            } catch (Exception e) {
                Platform.runLater(() ->
                        Alertas.error("Error", "No se pudo cambiar el estado"));
            }
        }).start();
    }

    private void onEliminar(Usuario usuario) {
        boolean confirmar = Alertas.confirmar(
                "Eliminar usuario",
                "¿Eliminar a " + usuario.getNombre() + " y todos sus datos?"
        );

        if (confirmar) {
            new Thread(() -> {
                try {
                    usuarioServicio.eliminar(usuario.getId());
                    Platform.runLater(this::cargarUsuarios);
                } catch (Exception e){
                    Platform.runLater(() ->
                            Alertas.error("Error", "No se pudo eliminar el usuario"));
                }
            }).start();
        }
    }
}
