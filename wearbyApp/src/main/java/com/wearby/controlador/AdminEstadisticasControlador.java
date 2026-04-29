package com.wearby.controlador;

import com.wearby.modelo.PrendaFiltroDTO;
import com.wearby.servicio.AdminServicio;
import com.wearby.util.Alertas;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Controlador de la pantalla de estadísticas del administrador.
 * Muestra métricas generales del sistema obtenidas a través
 * del endpoint que usa el DAO con JDBC.
 */

public class AdminEstadisticasControlador implements Initializable {

    @FXML private Label totalUsuariosLabel;
    @FXML private Label totalPrendasLabel;
    @FXML private Label totalOutfitsLabel;
    @FXML private TableView<PrendaFiltroDTO> tablaPrendasUsuario;
    @FXML private TableColumn<PrendaFiltroDTO, String> colUsuario;
    @FXML private TableColumn<PrendaFiltroDTO, String> colTotal;

    private final AdminServicio adminServicio = new AdminServicio();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarEstadisticas();
    }

    private void configurarTabla() {
        colUsuario.setCellValueFactory( d ->
                new SimpleStringProperty(d.getValue().getNombre()));
        colTotal.setCellValueFactory( d ->
                new SimpleStringProperty(d.getValue().getCategoria()));
    }

    private void cargarEstadisticas(){
        new Thread(() -> {
            try {
                Map<String, Integer> stats = adminServicio.getEstadisticas();
                var prendasPorUsuario = adminServicio.getPrendasPorUsuario();

                Platform.runLater(() -> {
                    totalUsuariosLabel.setText(
                            String.valueOf(stats.getOrDefault("totalUsuarios", 0)));
                    totalPrendasLabel.setText(
                            String.valueOf(stats.getOrDefault("totalPrendas", 0)));
                    totalOutfitsLabel.setText(
                            String.valueOf(stats.getOrDefault("totalOutfits", 0)));
                    tablaPrendasUsuario.getItems().setAll(prendasPorUsuario);
                });
            } catch (Exception e) {
                Platform.runLater(() ->
                        Alertas.error("Error", "No se pudieron cargar las estadísticas."));
            }
        }).start();
    }

}
