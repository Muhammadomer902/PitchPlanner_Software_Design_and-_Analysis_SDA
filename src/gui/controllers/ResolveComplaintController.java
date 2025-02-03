package gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import entities.ComplaintDetails;
import services.AdminService;
import services.ComplaintService;
import services.GroundService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResolveComplaintController {

    @FXML
    private Label complainantNameLabel;

    @FXML
    private Label complainantRoleLabel;

    @FXML
    private Label issueLabel;

    @FXML
    private Label groundNameLabel;

    @FXML
    private ListView<String> involvedUsersList;

    @FXML
    private Button blacklistGroundButton;

    @FXML
    private Button blacklistUserButton;

    @FXML
    private Button resolveButton;

    @FXML
    private Button backButton;

    private final ComplaintService complaintService = new ComplaintService();
    private final GroundService groundService = new GroundService();
    private final AdminService adminService = new AdminService();
    private ComplaintDetails complaint;

    /**
     * Initialize the controller with complaint data.
     *
     * @param complaint The complaint to resolve.
     */
    public void initializeData(ComplaintDetails complaint) {
        this.complaint = complaint;

        // Populate the UI with complaint details
        complainantNameLabel.setText(complaint.getComplainantName());
        complainantRoleLabel.setText(complaint.getComplainantRole());
        issueLabel.setText(complaint.getIssue());
        groundNameLabel.setText(complaint.getGroundName());

        // Populate involved users list
        populateInvolvedUsersList();

        // Show the blacklist ground button only if the complainant is a player
        blacklistGroundButton.setVisible("Player".equalsIgnoreCase(complaint.getComplainantRole()));
    }

    /**
     * Populate the list of involved users.
     */
    private void populateInvolvedUsersList() {
        List<String> involvedUsers = new ArrayList<>();
        if (complaint.getOtherUserId1() != null) {
            involvedUsers.add("User ID: " + complaint.getOtherUserId1());
        }
        if (complaint.getOtherUserId2() != null) {
            involvedUsers.add("User ID: " + complaint.getOtherUserId2());
        }
        if (involvedUsers.isEmpty()) {
            involvedUsers.add("No additional users involved.");
        }
        involvedUsersList.getItems().addAll(involvedUsers);
    }

    /**
     * Handle the "Blacklist Ground" button click event.
     */
    @FXML
    void handleBlacklistGround(ActionEvent event) {
        boolean success = groundService.blacklistGround(complaint);
        if (success) {
            showAlert("Success", "The ground has been blacklisted.", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Error", "Failed to blacklist the ground. Please try again.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Handle the "Blacklist User" button click event.
     */
    @FXML
    void handleBlacklistUser(ActionEvent event) {
        if (complaint.getOtherUserId1() != null) {
            boolean success = adminService.blacklistUser(complaint.getOtherUserId1());
            if (success) {
                showAlert("Success", "The involved users have been blacklisted.", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Error", "Failed to blacklist users. Please try again.", Alert.AlertType.ERROR);
            }
        }
        if (complaint.getOtherUserId2() != null) {
            boolean success = adminService.blacklistUser(complaint.getOtherUserId2());
            if (success) {
                showAlert("Success", "The involved users have been blacklisted.", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Error", "Failed to blacklist users. Please try again.", Alert.AlertType.ERROR);
            }
        }

        
    }

    /**
     * Handle the "Resolve" button click event.
     */
    @FXML
    void handleResolveComplaint(ActionEvent event) {
        boolean success = complaintService.resolveComplaint(complaint.getId());
        if (success) {
            showAlert("Success", "Complaint marked as resolved.", Alert.AlertType.INFORMATION);
            resolveButton.setDisable(true);
        } else {
            showAlert("Error", "Failed to mark the complaint as resolved. Please try again.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Handle the "Back" button click event.
     */
    @FXML
    void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/scenes/HandleComplaints.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Handle Complaints");
            stage.setMaximized(false);
            stage.show();
            stage.setMaximized(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Display an alert dialog.
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
