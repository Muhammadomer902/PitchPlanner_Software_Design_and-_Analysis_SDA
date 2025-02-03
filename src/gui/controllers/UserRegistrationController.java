package gui.controllers;

import java.io.IOException;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import services.UserService;

public class UserRegistrationController {

    @FXML
    private TextField ageField;

    @FXML
    private Button back;

    @FXML
    private TextField cityField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private CheckBox groundOwnerRoleCheckBox;

    @FXML
    private Label messageLabel;

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField phoneField;

    @FXML
    private CheckBox playerRoleCheckBox;

    @FXML
    private Button registerButton;

    @FXML
    private TextField usernameField;

    private final UserService userService = new UserService(); // Service instance for user operations

    @FXML
    void handleBack(ActionEvent event) {
        try {
            // Load WelcomePage.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/scenes/WelcomePage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) back.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Welcome to Pitch Planner");
            stage.setMaximized(false);
            stage.show();
            stage.setMaximized(true);
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Error navigating back to the Welcome page.");
            messageLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: red; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0.8, 2, 2);");
        }
    }

    @SuppressWarnings("unused")
    @FXML
    void handleRegister(ActionEvent event) {
        String name = nameField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();
        String ageText = ageField.getText().trim();
        String city = cityField.getText().trim();
        String phone = phoneField.getText().trim();
        String role = playerRoleCheckBox.isSelected() ? "PLAYER" : groundOwnerRoleCheckBox.isSelected() ? "GROUND_OWNER" : "";

        // Validate input
        if (name.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || ageText.isEmpty() || city.isEmpty() || phone.isEmpty() || role.isEmpty()) {
            messageLabel.setText("All fields are required!");
            messageLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: red; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0.8, 2, 2);");
            return;
        }

        if (username.length() > 50) {
            messageLabel.setText("Username must not exceed 50 characters.");
            messageLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: red; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0.8, 2, 2);");
            return;
        }

        if (name.length() > 100) {
            messageLabel.setText("Name must not exceed 100 characters.");
            messageLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: red; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0.8, 2, 2);");
            return;
        }

        if (city.length() > 100) {
            messageLabel.setText("City must not exceed 100 characters.");
            messageLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: red; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0.8, 2, 2);");
            return;
        }

        if (!phone.matches("\\d{10,15}")) {
            messageLabel.setText("Phone must be a valid number (10-15 digits).");
            messageLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: red; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0.8, 2, 2);");
            return;
        }

        if (!password.equals(confirmPassword)) {
            messageLabel.setText("Passwords do not match!");
            messageLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: red; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0.8, 2, 2);");
            return;
        }

        if (password.length() < 8) {
            messageLabel.setText("Password must be at least 8 characters long.");
            messageLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: red; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0.8, 2, 2);");
            return;
        }

        try {
            int age = Integer.parseInt(ageText);
            if (age < 18 || age > 120) {
                messageLabel.setText("Age must be between 18 and 120.");
                messageLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: red; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0.8, 2, 2);");
                return;
            }

            // Call UserService to register the user
            boolean success = userService.registerUser(username, name, age, city, phone, password, role);
            if (success) {
                messageLabel.setText("Registration successful! Redirecting to login...");
                messageLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: green; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0.8, 2, 2);");
                
                // Delay for 1 second and navigate to UserLogin
                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                pause.setOnFinished(e -> navigateToUserLogin());
                pause.play();
            } else {
                messageLabel.setText("Registration failed. Username may already exist.");
                messageLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: red; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0.8, 2, 2);");
            }
        } catch (NumberFormatException e) {
            messageLabel.setText("Age must be a valid number.");
            messageLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: red; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0.8, 2, 2);");
        }
    }

    private void navigateToUserLogin() {
        try {
            // Load UserLogin.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/scenes/UserLogin.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) registerButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("User Login");
            stage.setMaximized(false);
            stage.show();
            stage.setMaximized(true);
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Error navigating to the User Login page.");
            messageLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: red; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0.8, 2, 2);");
        }
    }

    @FXML
    void handleRoleSelection(ActionEvent event) {
        // Ensure only one role checkbox is selected at a time
        if (playerRoleCheckBox.isSelected()) {
            groundOwnerRoleCheckBox.setSelected(false);
        } else if (groundOwnerRoleCheckBox.isSelected()) {
            playerRoleCheckBox.setSelected(false);
        }
    }
}
