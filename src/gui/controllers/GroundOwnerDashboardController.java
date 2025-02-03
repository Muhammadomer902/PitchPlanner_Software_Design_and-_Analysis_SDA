package gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import entities.User;

import java.io.IOException;

public class GroundOwnerDashboardController {

    @FXML
    private Button addGroundButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button removeGroundButton;

    @FXML
    private Button scheduledBookingsButton;

    @FXML
    private Button updateGroundButton;

    @FXML
    private Label welcomeLabel;

    private User loggedInUser; // Store the logged-in user's details

    /**
     * Initialize the dashboard with the ground owner's details.
     *
     * @param user The logged-in ground owner.
     */
    public void initializeData(User user) {
        this.loggedInUser = user;
        welcomeLabel.setText("Welcome, " + user.getName() + "!");
    }

    @FXML
    void handleAddGround(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/scenes/AddGround.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) addGroundButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Ground");
            stage.setMaximized(false);
            stage.show();
            stage.setMaximized(true);

            // Pass user data to the AddGroundController
            AddGroundController controller = loader.getController();
            controller.initializeData(loggedInUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleUpdateGround(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/scenes/UpdateGround.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) updateGroundButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Update Ground");
            stage.setMaximized(false);
            stage.show();
            stage.setMaximized(true);

            // Pass user data to the UpdateGroundController
            UpdateGroundController controller = loader.getController();
            controller.initializeData(loggedInUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleRemoveGround(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/scenes/RemoveGround.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) removeGroundButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Remove Ground");
            stage.setMaximized(false);
            stage.show();
            stage.setMaximized(true);

            // Pass user data to the RemoveGroundController
            RemoveGroundController controller = loader.getController();
            controller.initializeData(loggedInUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleScheduledBookings(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/scenes/ScheduledBookings.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) scheduledBookingsButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Scheduled Bookings");
            stage.setMaximized(false);
            stage.show();
            stage.setMaximized(true);

            // Pass user data to the ScheduledBookingsController
            ScheduledBookingsController controller = loader.getController();
            controller.initializeData(loggedInUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/scenes/UserLogin.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("User Login");
            stage.setMaximized(false);
            stage.show();
            stage.setMaximized(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
