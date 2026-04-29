package com.wearby.controlador;

import com.wearby.modelo.*;
import com.wearby.servicio.CaracteristicasServicio;
import com.wearby.util.Alertas;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controlador de gestión de categorías del administrador
 * Permite añadir y eliminar valores de los desplegables
 * de características de las prendas
 */

public class AdminCategoriasControlador implements Initializable {

    @FXML
    private ComboBox<String> tipoCombo;
    @FXML
    private TextField nuevoValorField;
    @FXML
    private ListView<String> listaValores;

    private final CaracteristicasServicio caracteristicasServicio = new CaracteristicasServicio();

    private List<Categoria> categorias;
    private List<Estilo> estilos;
    private List<Temporada> temporadas;
    private List<Color> colores;
    private List<Formalidad> formalidades;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tipoCombo.getItems().addAll(
                "Categorías", "Estilos", "Temporadas", "Colores", "Formalidades"
        );
        tipoCombo.setValue("Categorías");
        cargarTodo();
    }

    private void cargarTodo() {
        new Thread(() -> {
            try {
                categorias = caracteristicasServicio.getCategorias();
                estilos = caracteristicasServicio.getEstilos();
                temporadas = caracteristicasServicio.getTemporadas();
                colores = caracteristicasServicio.getColores();
                formalidades = caracteristicasServicio.getFormalidades();
                Platform.runLater(this::actualizarLista);
            } catch (Exception e) {
                Platform.runLater(() ->
                        Alertas.error("Error", "No se pudieron cargar los valores."));
            }
        }).start();
    }

    @FXML
    private void onCambiarTipo() {
        actualizarLista();
    }

    private void actualizarLista() {
        listaValores.getItems().clear();
        String tipo = tipoCombo.getValue();

        if (tipo == null) return;

        switch (tipo) {
            case "Categorías" -> {
                if (categorias != null)
                    categorias.forEach(c ->
                            listaValores.getItems().add(c.getId() + " - " + c.getNombre()));
            }
            case "Estilos" -> {
                if (estilos != null)
                    estilos.forEach(e ->
                            listaValores.getItems().add(e.getId() + " - " + e.getNombre()));
            }
            case "Temporadas" -> {
                if (temporadas != null)
                    temporadas.forEach(t ->
                            listaValores.getItems().add(t.getId() + " - " + t.getNombre()));
            }
            case "Colores" -> {
                if (colores != null)
                    colores.forEach(c ->
                            listaValores.getItems().add(c.getId() + " - " + c.getNombre()));
            }
            case "Formalidades" -> {
                if (formalidades != null)
                    formalidades.forEach(f ->
                            listaValores.getItems().add(f.getId() + " - " + f.getNombre()));
            }
        }
    }

    @FXML
    private void onAnadir() {
        String valor = nuevoValorField.getText().trim();
        if (valor.isEmpty()) {
            Alertas.error("Error", "Escribe un valor antes de añadir");
            return;
        }

        new Thread(() -> {
            try {
                String tipo = tipoCombo.getValue();
                switch (tipo) {
                    case "Categorías" -> {
                        Categoria c = new Categoria();
                        c.setNombre(valor);
                        caracteristicasServicio.addCategoria(c);
                    }
                    case "Estilos" -> {
                        Estilo e = new Estilo();
                        e.setNombre(valor);
                        caracteristicasServicio.addEstilo(e);
                    }
                    case "Temporadas" -> {
                        Temporada t = new Temporada();
                        t.setNombre(valor);
                        caracteristicasServicio.addTemporada(t);
                    }
                    case "Colores" -> {
                        Color col = new Color();
                        col.setNombre(valor);
                        caracteristicasServicio.addColor(col);
                    }
                    case "Formalidades" -> {
                        Formalidad f = new Formalidad();
                        f.setNombre(valor);
                        caracteristicasServicio.addFormalidad(f);
                    }
                }
                Platform.runLater(() -> {
                    nuevoValorField.clear();
                    cargarTodo();
                });
            } catch (Exception e) {
                Platform.runLater(() ->
                        Alertas.error("Error", "No se pudo añadir el valor."));
            }
        }).start();
    }
}