package controllers;

import java.net.URL;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import upv.ipc.sportlib.Session;
import upv.ipc.sportlib.SportActivityApp;
import upv.ipc.sportlib.User;

public class SessionController implements Initializable {

    @FXML
    private TableView<Session> tblSessions;

    @FXML
    private TableColumn<Session, String> colStart;

    @FXML
    private TableColumn<Session, String> colEnd;

    @FXML
    private TableColumn<Session, String> colDuration;

    @FXML
    private TableColumn<Session, Integer> colImported;

    @FXML
    private TableColumn<Session, Integer> colViewed;

    @FXML
    private TableColumn<Session, Integer> colAnnotations;

    private SportActivityApp app;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        app = SportActivityApp.getInstance();

        User user = app.getCurrentUser();

        if (user == null) {
            showError("No hay usuario autenticado.");
            return;
        }

        configureTable();
        loadSessions(user);
    }

    private void configureTable() {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        colStart.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(() ->
                        cellData.getValue().getStartTime().format(formatter)
                )
        );

        colEnd.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(() ->
                        cellData.getValue().getEndTime().format(formatter)
                )
        );

        colDuration.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(() -> {
                    Duration d = cellData.getValue().getDuration();

                    long hours = d.toHours();
                    long minutes = d.toMinutesPart();

                    return hours + "h " + minutes + "m";
                })
        );

        colImported.setCellValueFactory(
                new PropertyValueFactory<>("importedActivities")
        );

        colViewed.setCellValueFactory(
                new PropertyValueFactory<>("viewedActivities")
        );

        colAnnotations.setCellValueFactory(
                new PropertyValueFactory<>("annotationsCreated")
        );
    }

    private void loadSessions(User user) {
        try {

            List<Session> sessions = app.getSessionsByUser(user);

            tblSessions.setItems(
                    FXCollections.observableArrayList(sessions)
            );

        } catch (Exception e) {
            e.printStackTrace();
            showError("No se pudo cargar el historial de sesiones.");
        }
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}