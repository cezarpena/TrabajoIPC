package controllers;

import upv.ipc.sportlib.User;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import upv.ipc.sportlib.SportActivityApp;

public class RegisterController {

    @FXML
    private TextField txtNick;

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private DatePicker dpBirthDate;

    private String avatarPath = null;

    private SportActivityApp app;

    public void initialize() {
        app = SportActivityApp.getInstance();
    }

    @FXML
    private void selectAvatar(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Seleccionar avatar");

        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        File file = chooser.showOpenDialog(txtNick.getScene().getWindow());

        if (file != null) {
            avatarPath = file.getAbsolutePath();
            showAlert("Avatar", "Avatar seleccionado correctamente.");
        }
    }

    @FXML
    private void handleRegister(ActionEvent event) {

        String nick = txtNick.getText().trim();
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText();
        LocalDate birthDate = dpBirthDate.getValue();

        if (nick.isEmpty() || email.isEmpty() || password.isEmpty() || birthDate == null) {
            showAlert("Error", "Complete todos los campos obligatorios.");
            return;
        }

        if (!User.checkNickName(nick)) {
            showAlert("Error", "Nickname no válido.");
            return;
        }

        if (!User.checkEmail(email)) {
            showAlert("Error", "Email no válido.");
            return;
        }

        if (!User.checkPassword(password)) {
            showAlert("Error", "Contraseña no válida.");
            return;
        }

        if (!User.isOlderThan(birthDate, 12)) {
            showAlert("Error", "Debe ser mayor de 12 años.");
            return;
        }

        boolean ok = app.registerUser(
                nick,
                email,
                password,
                birthDate,
                avatarPath
        );

        if (ok) {
            showAlert("Éxito", "Usuario registrado correctamente.");
            loadScene("/views/Login.fxml");
        } else {
            showAlert("Error", "No se pudo registrar. Quizá el nickname ya existe.");
        }
    }

    @FXML
    private void goToLogin(ActionEvent event) {
        loadScene("/views/Login.fxml");
    }

    private void loadScene(String fxml) {
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
