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
import services.UserService;
import entities.User;

import java.io.IOException;

public class UserLoginController {

    @FXML
    private Button Login;

    @FXML
    private Button SignUp;

    @FXML
    private Label messageLabel;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    private final UserService userService; // Service for user authentication

    public UserLoginController() {
        this.userService = new UserService();
    }

    @FXML
    void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Username and password cannot be empty.");
            messageLabel.setStyle("-fx-font-size: 20px; \r\n" + //
                                "                          -fx-font-weight: bold; \r\n" + //
                                "                          -fx-text-fill: red; \r\n" + //
                                "                          -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0.8, 2, 2);");
            return;
        }
       
        // Attempt login
        User user = userService.loginUser(username, password);

        if (user != null) {
            messageLabel.setText("Login successful! Redirecting...");
            messageLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: green; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0.8, 2, 2);");
            navigateToDashboard(user);
        } else {
            messageLabel.setText("Invalid username or password.");
            messageLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: red; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0.8, 2, 2);");
        }
    }

    @FXML
    void handleSignUp(ActionEvent event) {
        try {
            // Navigate to Sign-Up page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/scenes/UserRegistration.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) SignUp.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("User Registration");
            stage.setMaximized(false); // Ensure the stage is maximized
            stage.show();
            stage.setMaximized(true);
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("An error occurred while navigating to the Sign-Up page.");
            messageLabel.setStyle("-fx-text-fill: red;");
        }
    }

    private void navigateToDashboard(User user) {
        try {
            FXMLLoader loader;
            if ("PLAYER".equalsIgnoreCase(user.getRole())) {
                loader = new FXMLLoader(getClass().getResource("/gui/scenes/PlayerDashboard.fxml"));
            } else if ("GROUND_OWNER".equalsIgnoreCase(user.getRole())) {
                loader = new FXMLLoader(getClass().getResource("/gui/scenes/GroundOwnerDashboard.fxml"));
            } else {
                messageLabel.setText("Unknown user role. Please contact support.");
                messageLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            Parent root = loader.load();
            Stage stage = (Stage) Login.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(user.getRole() + " Dashboard");
            stage.setMaximized(false); // Ensure the stage is maximized
            stage.show();
            stage.setMaximized(true);

            // Pass user data to the dashboard controller
            if ("PLAYER".equalsIgnoreCase(user.getRole())) {
                PlayerDashboardController controller = loader.getController();
                controller.initializeData(user);
            } else if ("GROUND_OWNER".equalsIgnoreCase(user.getRole())) {
                GroundOwnerDashboardController controller = loader.getController();
                controller.initializeData(user);
            }

        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("An error occurred while navigating to the dashboard.");
            messageLabel.setStyle("-fx-text-fill: red;");
        }
    }
}
