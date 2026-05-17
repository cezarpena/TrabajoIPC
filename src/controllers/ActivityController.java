/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllers;

import java.net.URL;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
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

        // Celda personalizada
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

        // Listener selección
        activityList.getSelectionModel().selectedItemProperty()
            .addListener((obs, old, selected) -> {
                btnDelete.setDisable(selected == null);
                if (selected != null) {
                    statsBox.setVisible(true);
                    statsBox.setManaged(true);
                    mostrarEstadisticas(selected);
                }
            });
    }

    @FXML
    private void handleImport(ActionEvent event) {
        // TODO: implementar importacion GPX
    }

    private void handleDelete(ActionEvent event) {
        // TODO: implementar borrado con confirmacion
    }

    private void mostrarEstadisticas(Activity act) {
        lblActivityName.setText(act.getName());
        lblDistancia.setText(String.format("%.2f km", act.getTotalDistance() / 1000.0));
        // TODO: completar resto de estadisticas
    }
}