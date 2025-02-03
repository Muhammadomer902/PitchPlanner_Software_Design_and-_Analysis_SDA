package gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomePageController {

    @FXML
    private Button AdminLogin;

    @FXML
    private Button UserLogin;

    @FXML
    private Button UserRegisteration;

    @FXML
    void handleAdminLogin(ActionEvent event) {
        try {
            // Load Admin Login page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/scenes/AdminLogin.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) AdminLogin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Admin Login");
            stage.setMaximized(false); // Ensure the stage is maximized
            stage.show();
            stage.setMaximized(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleLogin(ActionEvent event) {
        try {
            // Load User Login page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/scenes/UserLogin.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) UserLogin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("User Login");
            stage.setMaximized(false); // Ensure the stage is maximized
            stage.show();
            stage.setMaximized(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleRegister(ActionEvent event) {
        try {
            // Load User Registration page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/scenes/UserRegistration.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) UserRegisteration.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("User Registration");
            stage.setMaximized(false); // Ensure the stage is maximized
            stage.show();
            stage.setMaximized(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
