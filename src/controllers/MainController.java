package controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainController {

    @FXML
    private void goToLogin() {
        loadScene("/views/Login.fxml");
    }

    @FXML
    private void goToRegister() {
        loadScene("/views/Register.fxml");
    }

    private void loadScene(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));

            Stage stage = (Stage) Stage.getWindows()
                    .stream()
                    .filter(window -> window.isShowing())
                    .findFirst()
                    .orElse(null);

            if (stage != null) {
                stage.setScene(new Scene(root));
                stage.show();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}