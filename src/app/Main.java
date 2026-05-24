// package app;
// 
// import javafx.application.Application;
// import javafx.fxml.FXMLLoader;
// import javafx.scene.Scene;
// import javafx.stage.Stage;
// 
// public class Main extends Application {
// 
//     @Override
//     public void start(Stage stage) throws Exception {
// 
//         FXMLLoader loader = new FXMLLoader(
//                 // getClass().getResource("/views/Main.fxml")
//                 getClass().getResource("/views/components/Main.fxml")
//         );
// 
//         Scene scene = new Scene(loader.load(), 1450, 850);
// 
//         stage.setTitle("Running la Safor");
//         stage.setScene(scene);
//         stage.show();
//     }
// 
//     public static void main(String[] args) {
//         launch();
//     }
// }

package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import upv.ipc.sportlib.SportActivityApp;
import java.time.LocalDate;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        
        // ==========================================
        //  BYPASS DE SESIÓN (Para poder importar GPX sin errores)
        // ==========================================
        SportActivityApp app = SportActivityApp.getInstance();
        
        System.out.println("👉 Intentando login automático con 'testuser'...");
        boolean loggedIn = app.login("testuser", "Test1234!"); // Credenciales válidas
        System.out.println("👉 ¿Login inicial correcto?: " + loggedIn);
        
        if (!loggedIn) {
            System.out.println("👉 Registrando nuevo usuario 'testuser' con contraseña robusta...");
            boolean registered = app.registerUser("testuser", "test@test.com", "Test1234!", LocalDate.of(2000, 1, 1), (String) null);
            System.out.println("👉 ¿Registro del usuario correcto?: " + registered);
            
            loggedIn = app.login("testuser", "Test1234!");
            System.out.println("👉 ¿Login tras registro correcto?: " + loggedIn);
        }
        // ==========================================

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/views/components/activityPanel.fxml")
        );

        Scene scene = new Scene(loader.load(), 1450, 850);

        stage.setTitle("Running la Safor - Modo Test");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
