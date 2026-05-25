package controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import upv.ipc.sportlib.SportActivityApp;

public class DashboardController {

    @FXML
    private Label lblUser;

    @FXML
    private StackPane contentPane;

    private SportActivityApp app;

    public void initialize() {
        app = SportActivityApp.getInstance();

        if (app.getCurrentUser() != null) {
            lblUser.setText("Bienvenido, " + app.getCurrentUser().getNickName());
        }

        // Vista inicial al entrar
        loadContent("/views.components/activityPanel.fxml");
    }

    @FXML
    private void showActivities() {
        loadContent("/views.components/activityPanel.fxml");
    }

    @FXML
    private void showProfile() {
        loadContent("/views/Profile.fxml");
    }

    @FXML
    private void showSessions() {
        loadContent("/views.components/sessionPanel.fxml");
    }

    @FXML
    private void showStats() {
        loadContent("/views.components/statsPanel.fxml");
    }

    @FXML
    private void handleLogout() {
        app.logout();
        goToMain();
    }

    private void loadContent(String fxmlPath) {
        try {
            Parent content = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentPane.getChildren().setAll(content);

        } catch (IOException e) {
            e.printStackTrace();
            showError("No se pudo cargar la vista.");
        }
    }

    private void goToMain() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/Main.fxml"));

            Stage stage = (Stage) contentPane.getScene().getWindow();

            stage.setScene(new Scene(root, 600, 400));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showError("No se pudo volver al menú principal.");
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