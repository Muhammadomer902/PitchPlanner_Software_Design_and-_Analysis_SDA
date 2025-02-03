import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the WelcomePage.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/scenes/WelcomePage.fxml"));
            Parent root = loader.load();

            // Create a scene and set it on the stage
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);

            // Set up the stage
            primaryStage.setTitle("Pitch Planner");
            primaryStage.setMaximized(true);
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Error loading FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    
    public static void main(String[] args) {
        launch(args);
    }
}
