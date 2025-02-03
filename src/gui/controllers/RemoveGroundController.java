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

public class RemoveGroundController {

    @FXML
    private ComboBox<String> groundComboBox;

    @FXML
    private Label nameLabel;

    @FXML
    private Label locationLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private Label typeLabel;

    @FXML
    private Button removeButton;

    @FXML
    private Button backButton;

    private final GroundService groundService = new GroundService(); // Instance for ground operations
    private User loggedInUser; // Logged-in ground owner

    private List<Ground> grounds; // List of grounds owned by the user

    /**
     * Initialize data for the controller.
     */
    @FXML
    public void initialize() {
        // No-op for now; initialization occurs when user data is passed
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
     * Load the grounds owned by the user into the ComboBox.
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
     * Populate the labels with the details of the selected ground.
     */
    private void populateGroundDetails() {
        int selectedIndex = groundComboBox.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Ground selectedGround = grounds.get(selectedIndex);
            nameLabel.setText(selectedGround.getName());
            locationLabel.setText(selectedGround.getLocation());
            priceLabel.setText(String.valueOf(selectedGround.getPrice()));
            typeLabel.setText(selectedGround.getType());
        } else {
            clearGroundDetails();
        }
    }

    /**
     * Clear the ground details from the labels.
     */
    private void clearGroundDetails() {
        nameLabel.setText("");
        locationLabel.setText("");
        priceLabel.setText("");
        typeLabel.setText("");
    }

    /**
     * Handle the "Remove Ground" button click event.
     *
     * @param event The action event.
     */
    @FXML
    void handleRemoveGround(ActionEvent event) {
        int selectedIndex = groundComboBox.getSelectionModel().getSelectedIndex();

        if (selectedIndex < 0) {
            showAlert("Validation Error", "Please select a ground to remove.", Alert.AlertType.ERROR);
            return;
        }

        Ground selectedGround = grounds.get(selectedIndex);

        // Confirmation dialog
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, 
                "Are you sure you want to remove this ground?", ButtonType.YES, ButtonType.NO);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.showAndWait();

        if (confirmationAlert.getResult() == ButtonType.YES) {
            boolean success = groundService.removeGround(selectedGround.getId());

            if (success) {
                showAlert("Success", "Ground removed successfully!", Alert.AlertType.INFORMATION);
                loadGrounds(); // Reload grounds to reflect changes
                clearGroundDetails(); // Clear the details of the removed ground
            } else {
                showAlert("Error", "Failed to remove the ground. Please try again.", Alert.AlertType.ERROR);
            }
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
