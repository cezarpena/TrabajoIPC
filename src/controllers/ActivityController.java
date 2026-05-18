/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllers;

import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import upv.ipc.sportlib.Activity;
import upv.ipc.sportlib.SportActivityApp;

public class ActivityController implements Initializable {

    @FXML private ListView<Activity> activityList;
    @FXML private Button btnImport, btnDelete;
    @FXML private VBox statsBox;

    @FXML private Label lblActivityName, lblDistancia, lblDuracion, lblVelocidad;
    @FXML private Label lblAltMin, lblAltMax, lblRitmo, lblDesnPos, lblDesnNeg;
    @FXML private Label lblNumActs, lblTotalLoss, lblTotalGain, lblTotalTime, lblTotalDist;

    @FXML private ComboBox<String> monthSelector;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SportActivityApp app = SportActivityApp.getInstance();

        statsBox.setVisible(false);
        statsBox.setManaged(false);

        List<Activity> acts = app.getUserActivities();
        activityList.getItems().setAll(acts);

        activityList.setCellFactory(lv -> new ListCell<Activity>() {
            @Override
            protected void updateItem(Activity item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    String fecha = item.getStartTime()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    String dist = String.format("%.2f km", item.getTotalDistance() / 1000.0);
                    setText(item.getName() + "\n" + fecha + "  ·  " + dist);
                }
            }
        });

        btnDelete.setDisable(true);
        btnDelete.setOnAction(this::handleDelete);

        activityList.getSelectionModel().selectedItemProperty()
            .addListener((obs, old, selected) -> {
                btnDelete.setDisable(selected == null);
                if (selected != null) {
                    statsBox.setVisible(true);
                    statsBox.setManaged(true);
                    mostrarEstadisticas(selected);
                }
            });

        // Cargar acumulado mensual
    }

    @FXML
    private void handleImport(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Seleccionar fichero GPX");
        fc.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("GPX Files", "*.gpx")
        );
        File file = fc.showOpenDialog(btnImport.getScene().getWindow());

        if (file != null) {
            try {
                SportActivityApp app = SportActivityApp.getInstance();
                Activity nueva = app.importActivity(file);
                if (nueva != null) {
                    activityList.getItems().add(0, nueva);
                    activityList.getSelectionModel().select(nueva);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Actividad importada");
                    alert.setHeaderText(null);
                    alert.setContentText("\"" + nueva.getName() + "\" importada correctamente.");
                    alert.showAndWait();
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("No se pudo importar el fichero GPX: " + e.getMessage());
                alert.showAndWait();
            }
        }
    }

    private void handleDelete(ActionEvent event) {
        // añadir confirmacion
        Activity selected = activityList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            SportActivityApp app = SportActivityApp.getInstance();
            app.removeActivity(selected);
            activityList.getItems().remove(selected);
            statsBox.setVisible(false);
            statsBox.setManaged(false);
        }
    }

    private void mostrarEstadisticas(Activity act) {
        lblActivityName.setText(act.getName());
        lblDistancia.setText(String.format("%.2f km", act.getTotalDistance() / 1000.0));

        Duration dur = act.getDuration();
        long h = dur.toHours();
        long m = dur.toMinutesPart();
        long s = dur.toSecondsPart();
        lblDuracion.setText(String.format("%02d:%02d:%02d", h, m, s));

        lblVelocidad.setText(String.format("%.1f km/h", act.getAverageSpeed()));
        lblRitmo.setText(formatPace(act.getAveragePace()));

        lblDesnPos.setText(String.format("+ %.0f m", act.getElevationGain()));
        lblDesnNeg.setText(String.format("- %.0f m", act.getElevationLoss()));
        lblAltMin.setText(String.format("%.0f m", act.getMinElevation()));
        lblAltMax.setText(String.format("%.0f m", act.getMaxElevation()));
    }

    private String formatPace(double paceMinPerKm) {
        int min = (int) paceMinPerKm;
        int sec = (int) Math.round((paceMinPerKm - min) * 60);
        return min + "'" + String.format("%02d", sec) + "\" /km";
    }
}