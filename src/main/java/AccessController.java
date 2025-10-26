package aydin.firebasedemo;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccessController {
    @FXML private TextField nameField;
    @FXML private TextField ageField;
    @FXML private TextField phoneField;
    @FXML private TextArea  output;

    private DemoApp app;

    void setApp(DemoApp app) { this.app = app; }

    @FXML
    private void handleWrite() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        int age;
        try { age = Integer.parseInt(ageField.getText().trim()); }
        catch (Exception e) { out("Age must be a number."); return; }

        if (name.isEmpty()) { out("Name is required."); return; }

        try {
            Firestore db = FirestoreClient.getFirestore();
            Map<String, Object> data = new HashMap<>();
            data.put("name", name);
            data.put("age", age);
            data.put("phone", phone);

            db.collection("Persons").document().set(data);
            out("Wrote: " + name + " (" + age + ", " + phone + ")");
            nameField.clear(); ageField.clear(); phoneField.clear();
        } catch (Exception e) {
            out("Write error: " + e.getMessage());
        }
    }

    @FXML
    private void handleRead() {
        try {
            Firestore db = FirestoreClient.getFirestore();
            ApiFuture<QuerySnapshot> fut = db.collection("Persons").get();
            List<QueryDocumentSnapshot> docs = fut.get().getDocuments();
            out("---- Persons (" + docs.size() + ") ----");
            for (QueryDocumentSnapshot d : docs) {
                out(d.getString("name") + " | age=" + d.getLong("age") + " | phone=" + d.getString("phone"));
            }
        } catch (Exception e) {
            out("Read error: " + e.getMessage());
        }
    }

    @FXML
    private void handleSignOut() throws Exception {
        app.showWelcomeScene();
    }

    private void out(String s) { output.appendText(s + "\n"); }
}
