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
import entities.ComplaintDetails;
import services.ComplaintService;

import java.io.IOException;
import java.util.List;

public class HandleComplaintsController {

    @FXML
    private TableView<ComplaintDetails> complaintsTable;

    @FXML
    private TableColumn<ComplaintDetails, String> nameColumn;

    @FXML
    private TableColumn<ComplaintDetails, String> roleColumn;

    @FXML
    private TableColumn<ComplaintDetails, String> issueColumn;

    @FXML
    private TableColumn<ComplaintDetails, String> groundColumn;

    @FXML
    private TableColumn<ComplaintDetails, Button> actionColumn;

    @FXML
    private Button backButton;

    private final ComplaintService complaintService = new ComplaintService();

    /**
     * Initialize the complaints table with unresolved complaints.
     */
    @FXML
    public void initialize() {
        loadComplaints();
    }

    /**
     * Load unresolved complaints into the table.
     */
    @SuppressWarnings("unused")
    private void loadComplaints() {
        List<ComplaintDetails> complaints = complaintService.getOpenComplaintDetails();

        complaintsTable.getItems().clear();

        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getComplainantName()));
        roleColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getComplainantRole()));
        issueColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIssue()));
        groundColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGroundName()));

        actionColumn.setCellValueFactory(data -> {
            ComplaintDetails complaint = data.getValue();
            Button resolveButton = new Button("Resolve");

            resolveButton.setOnAction(event -> navigateToResolveComplaint(complaint));

            return new SimpleObjectProperty<>(resolveButton);
        });

        complaintsTable.getItems().addAll(complaints);
    }

    /**
     * Navigate to the ResolveComplaint page with the selected complaint data.
     *
     * @param complaint The complaint to resolve.
     */
    private void navigateToResolveComplaint(ComplaintDetails complaint) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/scenes/ResolveComplaint.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) complaintsTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Resolve Complaint");
            stage.setMaximized(false);
            stage.show();
            stage.setMaximized(true);

            // Pass complaint data to the ResolveComplaintController
            ResolveComplaintController controller = loader.getController();
            controller.initializeData(complaint);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to navigate to Resolve Complaint page.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Handle the "Back" button click.
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
     * Show an alert dialog with a given title, message, and alert type.
     *
     * @param title   The title of the alert.
     * @param message The message of the alert.
     * @param type    The alert type.
     */
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
