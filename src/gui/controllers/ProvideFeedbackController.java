package gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import entities.BookingDetails;
import entities.User;
import services.FeedbackService;

import java.io.IOException;

public class ProvideFeedbackController {

    @FXML
    private Button backButton;

    @FXML
    private TextArea commentField;

    @FXML
    private Spinner<Integer> ratingSpinner;

    @FXML
    private Button submitFeedbackButton;

    @FXML
    private Button fileComplainButton;

    private final FeedbackService feedbackService = new FeedbackService();
    private User loggedInUser;
    private BookingDetails bookingDetails;

    @FXML
    public void initialize() {
        // Initialize spinner for ratings (1-5)
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 3);
        ratingSpinner.setValueFactory(valueFactory);
    }

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleFileComplain(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/scenes/FileComplain.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) fileComplainButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("File Complain");
            stage.setMaximized(false);
            stage.show();
            stage.setMaximized(true);

            // Pass user and booking data to FileComplainController
            FileComplainController controller = loader.getController();
            controller.initializeData(loggedInUser, bookingDetails);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleSubmitFeedback(ActionEvent event) {
        int rating = ratingSpinner.getValue();
        String comment = commentField.getText().trim();

        boolean success = feedbackService.submitFeedback(bookingDetails.getId(), loggedInUser.getId(), rating, comment);
        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Feedback submitted successfully!", ButtonType.OK);
            alert.showAndWait();
            handleBack(null);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to submit feedback.", ButtonType.OK);
            alert.showAndWait();
        }
    }
}
