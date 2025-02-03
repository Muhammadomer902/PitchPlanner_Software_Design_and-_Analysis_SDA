package gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import entities.Ground;
import entities.User;
import services.GroundService;

import java.io.IOException;
import java.util.List;

public class UpdateGroundController {

    @FXML
    private ComboBox<String> groundComboBox;

    @FXML
    private TextField nameField;

    @FXML
    private TextField locationField;

    @FXML
    private TextField priceField;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private Button updateButton;

    @FXML
    private Button backButton;

    private final GroundService groundService = new GroundService(); // Instance for ground operations
    private User loggedInUser; // Logged-in ground owner

    private List<Ground> grounds; // List of grounds owned by the user

    /**
     * Initialize the ComboBox for ground type.
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
        loadGrounds();
    }

    /**
     * Load the grounds owned by the logged-in user into the ComboBox.
     */
    @SuppressWarnings("unused")
    private void loadGrounds() {
        grounds = groundService.getGroundsByOwner(loggedInUser.getId());
        groundComboBox.getItems().clear();

        // Add ground names to the ComboBox
        for (Ground ground : grounds) {
            groundComboBox.getItems().add(ground.getName());
        }

        // Set listener to populate fields when a ground is selected
        groundComboBox.setOnAction(event -> populateGroundDetails());
    }

    /**
     * Populate the fields with the details of the selected ground.
     */
    private void populateGroundDetails() {
        int selectedIndex = groundComboBox.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Ground selectedGround = grounds.get(selectedIndex);
            nameField.setText(selectedGround.getName());
            locationField.setText(selectedGround.getLocation());
            priceField.setText(String.valueOf(selectedGround.getPrice()));
            typeComboBox.setValue(selectedGround.getType());
        }
    }

    /**
     * Handle the "Update Ground" button click event.
     *
     * @param event The action event.
     */
    @FXML
    void handleUpdateGround(ActionEvent event) {
        int selectedIndex = groundComboBox.getSelectionModel().getSelectedIndex();

        if (selectedIndex < 0) {
            showAlert("Validation Error", "Please select a ground to update.", Alert.AlertType.ERROR);
            return;
        }

        Ground selectedGround = grounds.get(selectedIndex);

        // Retrieve updated details
        String newName = nameField.getText().trim();
        String newLocation = locationField.getText().trim();
        String newPriceText = priceField.getText().trim();
        String newType = typeComboBox.getValue();

        // Validate input
        if (newName.isEmpty() || newLocation.isEmpty() || newPriceText.isEmpty() || newType == null) {
            showAlert("Validation Error", "All fields must be filled in.", Alert.AlertType.ERROR);
            return;
        }

        double newPrice;
        try {
            newPrice = Double.parseDouble(newPriceText);
            if (newPrice <= 0) {
                showAlert("Validation Error", "Price must be a positive number.", Alert.AlertType.ERROR);
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Price must be a valid number.", Alert.AlertType.ERROR);
            return;
        }

        // Update the ground
        boolean success = groundService.updateGround(selectedGround.getId(), newName, newLocation, newPrice, newType);

        if (success) {
            showAlert("Success", "Ground updated successfully!", Alert.AlertType.INFORMATION);
            // Reload grounds to reflect changes
            loadGrounds();
            // Clear fields
            nameField.clear();
            locationField.clear();
            priceField.clear();
            typeComboBox.getSelectionModel().clearSelection();
            groundComboBox.getSelectionModel().clearSelection();
        } else {
            showAlert("Error", "Failed to update ground. Please try again.", Alert.AlertType.ERROR);
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

            // Pass user data to GroundOwnerDashboardController
            GroundOwnerDashboardController controller = loader.getController();
            controller.initializeData(loggedInUser);
        } catch (IOException e) {
            e.printStackTrace();
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
}
