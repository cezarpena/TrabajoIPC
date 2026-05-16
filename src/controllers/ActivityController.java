/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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

        // Ocultar stats hasta que se seleccione actividad
        statsBox.setVisible(false);
        statsBox.setManaged(false);

        // Cargar actividades del usuario
        List<Activity> acts = app.getUserActivities();
        activityList.getItems().setAll(acts);

        btnDelete.setDisable(true);
    }
}