import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import java.io.File;

public class UI {

    public void showUI(Stage stage) {
        stage.setTitle("Excel Data Loader");

        Label statusLabel = new Label("Select file containing customer data");

        // Create a FileChooser to allow the user to select an Excel file
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Excel Files", "*.xls", "*.xlsx")
        );

        // Button to open the file chooser dialog
        Button loadButton = new Button("Choose File");

        loadButton.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                // If the user selects a file, display success message
                statusLabel.setText("FOUND \"" + file.getName() + "\", SUCCESSFUL LOADING OF DATA");
            } else {
                // If no file is selected, show this message
                statusLabel.setText("No file selected.");
            }
        });

        // Layout for UI
        VBox layout = new VBox(10, statusLabel, loadButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 400, 200);
        stage.setScene(scene);
        stage.show();
    }
}




