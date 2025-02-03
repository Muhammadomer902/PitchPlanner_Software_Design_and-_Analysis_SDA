package gui.controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import entities.Ground;
import services.AdminService;

import java.io.IOException;
import java.util.List;

public class VerifyGroundsController {

    @FXML
    private TableView<Ground> groundsTable;

    @FXML
    private TableColumn<Ground, String> groundNameColumn;

    @FXML
    private TableColumn<Ground, String> locationColumn;

    @FXML
    private TableColumn<Ground, Double> priceColumn;

    @FXML
    private TableColumn<Ground, Integer> ownerIdColumn;

    @FXML
    private TableColumn<Ground, Button> verifyActionColumn;

    @FXML
    private TableColumn<Ground, Button> rejectActionColumn;

    @FXML
    private Button backButton;

    private final AdminService adminService = new AdminService();

    /**
     * Initialize the controller and load grounds to verify.
     */
    @FXML
    public void initialize() {
        loadGrounds();
    }

    /**
     * Load grounds pending verification into the table.
     */
    @SuppressWarnings("unused")
    private void loadGrounds() {
        List<Ground> pendingGrounds = adminService.getPendingGrounds();
        groundsTable.getItems().clear();

        groundNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        locationColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLocation()));
        priceColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getPrice()));
        ownerIdColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getOwnerId()));

        // Verify Action
        verifyActionColumn.setCellValueFactory(data -> {
            Ground ground = data.getValue();
            Button verifyButton = new Button("Verify");

            verifyButton.setOnAction(event -> verifyGround(ground));

            return new SimpleObjectProperty<>(verifyButton);
        });

        // Reject Action
        rejectActionColumn.setCellValueFactory(data -> {
            Ground ground = data.getValue();
            Button rejectButton = new Button("Reject");
            rejectButton.setStyle("-fx-background-color: #FF6347; -fx-text-fill: white;");

            rejectButton.setOnAction(event -> rejectGround(ground));

            return new SimpleObjectProperty<>(rejectButton);
        });

        groundsTable.getItems().addAll(pendingGrounds);
    }

    /**
     * Handle ground verification.
     *
     * @param ground The ground to verify.
     */
    private void verifyGround(Ground ground) {
        boolean success = adminService.verifyGround(ground.getId(),true);
        if (success) {
            showAlert("Success", "Ground verified successfully!", Alert.AlertType.INFORMATION);
            loadGrounds(); // Reload the table to update the list
        } else {
            showAlert("Error", "Failed to verify ground. Please try again.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Handle ground rejection.
     *
     * @param ground The ground to reject.
     */
    private void rejectGround(Ground ground) {
        boolean success = adminService.verifyGround(ground.getId(),false);
        if (success) {
            showAlert("Success", "Ground rejected successfully!", Alert.AlertType.INFORMATION);
            loadGrounds(); // Reload the table to update the list
        } else {
            showAlert("Error", "Failed to reject ground. Please try again.", Alert.AlertType.ERROR);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/scenes/AdminDashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Admin Dashboard");
            stage.setMaximized(false);
            stage.show();
            stage.setMaximized(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Display an alert dialog with a given title, message, and alert type.
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
