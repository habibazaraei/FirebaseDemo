package aydin.firebasedemo;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.HashMap;
import java.util.Map;

public class WelcomeController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextArea logArea;

    private DemoApp app;

    void setApp(DemoApp app) { this.app = app; }

    @FXML
    private void handleRegister() {
        String email = emailField.getText().trim();
        String pw    = passwordField.getText();

        if (email.isEmpty() || pw.isEmpty()) {
            log("Email and password required.");
            return;
        }

        try {
            UserRecord.CreateRequest req = new UserRecord.CreateRequest()
                    .setEmail(email)
                    .setPassword(pw);
            FirebaseAuth.getInstance().createUser(req);

            // 2) Save plaintext pw in Firestore (for class demo ONLY)
            Firestore db = FirestoreClient.getFirestore();
            Map<String, Object> data = new HashMap<>();
            data.put("password", pw);
            db.collection("Users").document(email).set(data);

            log("Registered: " + email);
        } catch (Exception e) {
            log("Register error: " + e.getMessage());
        }
    }

    @FXML
    private void handleSignIn() {
        String email = emailField.getText().trim();
        String pw    = passwordField.getText();
        if (email.isEmpty() || pw.isEmpty()) {
            log("Email and password required.");
            return;
        }

        try {
            FirebaseAuth.getInstance().getUserByEmail(email);

            Firestore db = FirestoreClient.getFirestore();
            ApiFuture<DocumentSnapshot> fut = db.collection("Users").document(email).get();
            DocumentSnapshot snap = fut.get();
            if (!snap.exists()) {
                log("No saved password found; register first.");
                return;
            }
            String stored = snap.getString("password");
            if (pw.equals(stored)) {
                log("Sign-in OK.");
                app.showAccessScene();
            } else {
                log("Wrong password.");
            }
        } catch (Exception e) {
            log("Sign-in error: " + e.getMessage());
        }
    }

    private void log(String s) { logArea.appendText(s + "\n"); }
}
