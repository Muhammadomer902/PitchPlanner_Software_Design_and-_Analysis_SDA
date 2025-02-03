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

public class PlayerDashboardController {

    @FXML
    private Button logoutButton;

    @FXML
    private Button myBookingsButton;

    @FXML
    private Button viewGroundsButton;

    @FXML
    private Button makePaymentButton; // Button for Make Payment

    @FXML
    private Button matchUpButton; // Button for Match-Up

    @FXML
    private Label welcomeLabel; // Added a label for displaying the player's name

    private User loggedInUser; // To store the logged-in user's data

    /**
     * Initializes the dashboard with the logged-in user's data.
     * @param user The user object containing the logged-in player's details.
     */
    public void initializeData(User user) {
        this.loggedInUser = user;
        welcomeLabel.setText("Welcome, " + user.getName() + "!");
    }

    @FXML
    void handleLogout(ActionEvent event) {
        try {
            // Navigate back to UserLogin.fxml
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

    @FXML
    void handleMyBookings(ActionEvent event) {
        try {
            // Navigate to MyBookings.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/scenes/MyBookings.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) myBookingsButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("My Bookings");
            stage.setMaximized(false);
            stage.show();
            stage.setMaximized(true);
            // Pass user data to MyBookingsController
            MyBookingsController controller = loader.getController();
            controller.initializeData(loggedInUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleViewGrounds(ActionEvent event) {
        try {
            // Navigate to ViewGrounds.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/scenes/ViewGrounds.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) viewGroundsButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("View Grounds");
            stage.setMaximized(false);
            stage.show();
            stage.setMaximized(true);
            ViewGroundsController controller = loader.getController();
            controller.initializeData(loggedInUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleMakePayment(ActionEvent event) {
        try {
            // Navigate to MakePayment.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/scenes/MakePayment.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) makePaymentButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Make Payment");
            stage.setMaximized(false);
            stage.show();
            stage.setMaximized(true);
            
            // Pass user data to MakePaymentController
            MakePaymentController controller = loader.getController();
            controller.initializeData(loggedInUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleMatchUp(ActionEvent event) {
        try {
            // Navigate to MatchUp.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/scenes/MatchUp.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) matchUpButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Match-Up");
            stage.setMaximized(false);
            stage.show();
            stage.setMaximized(true);
            
            // Pass user data to MatchUpController
            MatchUpController controller = loader.getController();
            controller.initializeData(loggedInUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
