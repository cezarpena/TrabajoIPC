package controllers;

import upv.ipc.sportlib.SportActivityApp;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField txtNick;

    @FXML
    private PasswordField txtPassword;

    private SportActivityApp app;

    public void initialize() {
        app = SportActivityApp.getInstance();
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String nick = txtNick.getText().trim();
        String password = txtPassword.getText();

        if (nick.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Debe completar todos los campos.");
            return;
        }

        boolean ok = app.login(nick, password);

        if (ok) {
            showAlert("Éxito", "Login correcto.");

            // CAMBIAR A VENTANA PRINCIPAL
            // loadScene("/view/Main.fxml", event);

        } else {
            showAlert("Error", "Usuario o contraseña incorrectos.");
        }
    }

    @FXML
    private void goToRegister(ActionEvent event) {
        loadScene("/view/Register.fxml", event);
    }

    private void loadScene(String fxml, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = (Stage) txtNick.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo cargar la ventana.");
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
