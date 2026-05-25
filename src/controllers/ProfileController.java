/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllers;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import upv.ipc.sportlib.SportActivityApp;
import upv.ipc.sportlib.User;

public class ProfileController implements Initializable {

    @FXML
    private ImageView imgAvatar;

    @FXML
    private TextField txtUsuario;

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField txtAvatarPath;

    private SportActivityApp app;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        app = SportActivityApp.getInstance();

        User user = app.getCurrentUser();

        if (user == null) {
            showError("Sesión inválida", "No hay usuario autenticado.");
            return;
        }

        txtUsuario.setEditable(false);

        txtUsuario.setText(user.getNickName());
        txtEmail.setText(user.getEmail());
        datePicker.setValue(user.getBirthDate());

        String avatarPath = user.getAvatarPath();

        if (avatarPath != null && !avatarPath.isBlank()) {
            txtAvatarPath.setText(avatarPath);

            File f = new File(avatarPath);

            if (f.exists()) {
                imgAvatar.setImage(new Image(f.toURI().toString()));
            }
        }
    }

    @FXML
    private void handleSelectAvatar(ActionEvent event) {

        FileChooser fc = new FileChooser();
        fc.setTitle("Seleccionar imagen de avatar");

        fc.getExtensionFilters().add(
            new FileChooser.ExtensionFilter(
                "Imágenes",
                "*.png", "*.jpg", "*.jpeg", "*.gif"
            )
        );

        File file = fc.showOpenDialog(txtUsuario.getScene().getWindow());

        if (file != null) {
            txtAvatarPath.setText(file.getAbsolutePath());
            imgAvatar.setImage(new Image(file.toURI().toString()));
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {

        String email = txtEmail.getText().trim();
        String passInput = txtPassword.getText();
        LocalDate birth = datePicker.getValue();
        String avatarPath = txtAvatarPath.getText().trim();

        if (!User.checkEmail(email)) {
            showError("Email inválido", "El formato debe ser usuario@dominio.");
            return;
        }

        String passToSave;

        if (passInput.isBlank()) {
            passToSave = app.getCurrentUser().getPassword();
        } else {
            if (!User.checkPassword(passInput)) {
                showError(
                    "Contraseña débil",
                    "Debe tener 8-20 caracteres con mayúsculas, minúsculas, números y símbolos."
                );
                return;
            }

            passToSave = passInput;
        }

        if (birth == null || !User.isOlderThan(birth, 12)) {
            showError(
                "Edad insuficiente",
                "Debes tener más de 12 años."
            );
            return;
        }

        if (avatarPath.isBlank()) {
            avatarPath = null;
        }

        boolean updated = app.updateCurrentUser(
                email,
                passToSave,
                birth,
                avatarPath
        );

        if (updated) {
            new Alert(
                Alert.AlertType.INFORMATION,
                "Perfil actualizado correctamente."
            ).showAndWait();
        } else {
            showError("Error", "No se pudo actualizar el perfil.");
        }
    }

    private void showError(String header, String content) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}