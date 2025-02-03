package gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import entities.BookingDetails;
import entities.User;
import services.ComplaintService;

import java.io.IOException;

public class FileComplainController {

    @FXML
    private Button backButton;

    @FXML
    private TextArea complaintDetailsField;

    @FXML
    private Button submitComplainButton;

    private final ComplaintService complaintService = new ComplaintService();
    private User loggedInUser;
    private BookingDetails bookingDetails;

    /**
     * Initialize the controller with user and booking data.
     *
     * @param user    Logged-in user.
     * @param booking Booking details.
     */
    public void initializeData(User user, BookingDetails booking) {
        this.loggedInUser = user;
        this.bookingDetails = booking;
    }

    @FXML
    void handleBack(ActionEvent event) {
        try {
            if(loggedInUser.getRole()=="PLAYER")
            {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/scenes/MyBookings.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) backButton.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("My Bookings");
                stage.setMaximized(false);
                stage.show();
                stage.setMaximized(true);
                // Pass user data back to MyBookingsController
                MyBookingsController controller = loader.getController();
                controller.initializeData(loggedInUser);
            }
            else
            {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/scenes/ScheduledBookings.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) backButton.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Scheduled Bookings");
                stage.setMaximized(false);
                stage.show();
                stage.setMaximized(true);
                ScheduledBookingsController controller = loader.getController();
                controller.initializeData(loggedInUser);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleSubmitComplaint(ActionEvent event) {
        String complaintDetails = complaintDetailsField.getText().trim();

        if (complaintDetails.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Complaint details cannot be empty.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        boolean success = complaintService.fileComplaint(loggedInUser.getId(), bookingDetails.getId(), complaintDetails);
        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Complaint submitted successfully!", ButtonType.OK);
            alert.showAndWait();
            handleBack(null); // Navigate back to ProvideFeedback page
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to submit complaint. Please try again later.", ButtonType.OK);
            alert.showAndWait();
        }
    }
}
