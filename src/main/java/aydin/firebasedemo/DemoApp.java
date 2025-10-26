package aydin.firebasedemo;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileInputStream;

public class DemoApp extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        FileInputStream serviceAccount = new FileInputStream("src/main/resources/key.json");
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
        this.primaryStage = stage;
        showWelcomeScene();
        stage.setTitle("FirebaseDemo");
        stage.show();
    }

    public void showWelcomeScene() throws Exception {
        FXMLLoader fx = new FXMLLoader(getClass().getResource("/aydin/firebasedemo/welcome_screen.fxml"));
        Scene scene = new Scene(fx.load());
        WelcomeController c = fx.getController();
        c.setApp(this);
        primaryStage.setScene(scene);
    }

    public void showAccessScene() throws Exception {
        FXMLLoader fx = new FXMLLoader(getClass().getResource("/aydin/firebasedemo/AccessDataView.fxml"));
        Scene scene = new Scene(fx.load());
        AccessController c = fx.getController();
        c.setApp(this);
        primaryStage.setScene(scene);
    }


    public static void main(String[] args) { launch(args); }
}

