package gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdminLoginController {

    @FXML
    private Button backButton;

    @FXML
    private Button loginButton;

    @FXML
    private Label messageLabel;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    private final services.AdminService adminService = new services.AdminService(); // Service to handle admin login

    @FXML
    void handleBack(ActionEvent event) {
        try {
            // Load WelcomePage.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/scenes/WelcomePage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Welcome to Pitch Planner");

            // Center the window on the screen
            stage.centerOnScreen();
            stage.setMaximized(false); // Ensure the stage is maximized
            stage.show();
            stage.setMaximized(true);
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error navigating back to the Welcome page.");
            messageLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Username and password cannot be empty.");
            messageLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        // Authenticate admin credentials
        boolean loginSuccess = adminService.loginAdmin(username, password);
        if (loginSuccess) {
            messageLabel.setText("Login successful! Redirecting...");
            messageLabel.setStyle("-fx-text-fill: green;");
            navigateToAdminDashboard();
        } else {
            messageLabel.setText("Invalid credentials. Please try again.");
            messageLabel.setStyle("-fx-text-fill: red;");
        }
    }

    private void navigateToAdminDashboard() {
        try {
            // Load AdminDashboard.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/scenes/AdminDashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Admin Dashboard");
            stage.setMaximized(false); // Ensure the stage is maximized
            stage.show();
            stage.setMaximized(true);
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error navigating to the Admin Dashboard.");
            messageLabel.setStyle("-fx-text-fill: red;");
        }
    }
}
