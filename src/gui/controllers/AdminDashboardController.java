package gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminDashboardController {

    @FXML
    private Button handleComplaintsButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button verifyGroundButton;

    /**
     * Navigate to the "Handle Complaints" page.
     *
     * @param event The action event.
     */
    @FXML
    void handleHandleComplaints(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/scenes/HandleComplaints.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) handleComplaintsButton.getScene().getWindow();
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
     * Navigate to the "Verify Grounds" page.
     *
     * @param event The action event.
     */
    @FXML
    void handleVerifyGround(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/scenes/VerifyGrounds.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) verifyGroundButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Verify Grounds");
            stage.setMaximized(false);
            stage.show();
            stage.setMaximized(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handle the logout action and navigate to the login page.
     *
     * @param event The action event.
     */
    @FXML
    void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/scenes/AdminLogin.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Admin Login");
            stage.setMaximized(false); // Ensure the stage is maximized
            stage.show();
            stage.setMaximized(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
