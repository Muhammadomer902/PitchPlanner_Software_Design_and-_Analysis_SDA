package gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.GroundService;
import entities.User;

import java.io.IOException;

public class AddGroundController {

    @FXML
    private Button addGroundButton;

    @FXML
    private Button backButton;

    @FXML
    private TextField locationField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField priceField;

    @FXML
    private ComboBox<String> typeComboBox;

    private final GroundService groundService = new GroundService(); // Service instance for managing grounds
    private User loggedInUser; // Logged-in ground owner

    /**
     * Initialize the ComboBox with ground types.
     */
    @FXML
    public void initialize() {
        typeComboBox.getItems().addAll("CRICKET", "FOOTBALL");
    }

    /**
     * Pass the logged-in user data to this controller.
     * 
     * @param user The logged-in user.
     */
    public void initializeData(User user) {
        this.loggedInUser = user;
    }

    /**
     * Handle the "Add Ground" button click event.
     * 
     * @param event The action event.
     */
    @FXML
    void handleAddGround(ActionEvent event) {
        // Retrieve form data
        String name = nameField.getText().trim();
        String location = locationField.getText().trim();
        String priceText = priceField.getText().trim();
        String type = typeComboBox.getValue();

        // Validate input
        if (name.isEmpty() || location.isEmpty() || priceText.isEmpty() || type == null) {
            showAlert("Validation Error", "All fields must be filled in.", Alert.AlertType.ERROR);
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
            if (price <= 0) {
                showAlert("Validation Error", "Price must be a positive number.", Alert.AlertType.ERROR);
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Price must be a valid number.", Alert.AlertType.ERROR);
            return;
        }

        // Add the ground using GroundService
        boolean success = groundService.addGround(name, location, price, type, loggedInUser.getId());

        if (success) {
            showAlert("Success", "Ground added successfully!", Alert.AlertType.INFORMATION);
            clearForm();
        } else {
            showAlert("Error", "Failed to add ground. Please try again.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Handle the "Back" button click event.
     * 
     * @param event The action event.
     */
    @FXML
    void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/scenes/GroundOwnerDashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Ground Owner Dashboard");
            stage.setMaximized(false);
            stage.show();
            stage.setMaximized(true);

            // Pass user data to the GroundOwnerDashboardController
            GroundOwnerDashboardController controller = loader.getController();
            controller.initializeData(loggedInUser);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Failed to navigate to the dashboard.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Display an alert dialog.
     * 
     * @param title   The title of the alert.
     * @param message The message of the alert.
     * @param type    The type of the alert.
     */
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Clear the form fields.
     */
    private void clearForm() {
        nameField.clear();
        locationField.clear();
        priceField.clear();
        typeComboBox.getSelectionModel().clearSelection();
    }
}
