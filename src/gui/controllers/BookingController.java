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
import services.BookingService;
import services.PaymentService;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.Random;

public class BookingController {

    @FXML
    private Label availabilityMessage;

    @FXML
    private Button backButton;

    @FXML
    private Label codeMessage;

    @FXML
    private Button confirmBookingButton;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button generateCodeButton;

    @FXML
    private ComboBox<String> timePicker;

    private final BookingService bookingService = new BookingService(); // Instance of BookingService
    private final PaymentService paymentService = new PaymentService();
    private User loggedInUser; // The logged-in user
    private Ground selectedGround; // The ground being booked
    private String generatedCode = null;

    // Initialize the time picker options and other components
    @FXML
    public void initialize() {
        for (int hour = 6; hour <= 23; hour++) { // Time slots from 6 AM to 11 PM
            timePicker.getItems().add(hour + ":00");
        }
    }

    // Receive data passed to this controller
    public void initializeData(User user, Ground ground) {
        this.loggedInUser = user;
        this.selectedGround = ground;
    }

    @FXML
    void checkAvailability(ActionEvent event) {
        LocalDate selectedDate = datePicker.getValue();
        String selectedTime = timePicker.getValue();

        if (selectedDate == null || selectedTime == null) {
            availabilityMessage.setText("Please select both date and time.");
            availabilityMessage.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: red; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0.8, 2, 2);");
            return;
        }

        // Convert LocalDate and String to java.sql.Date and java.sql.Time
        Date sqlDate = Date.valueOf(selectedDate);
        Time sqlTime = Time.valueOf(selectedTime + ":00"); // Append seconds if missing

        // Call the service to check availability
        boolean isAvailable = bookingService.checkAvailability(selectedGround.getId(), sqlDate, sqlTime);
        if (isAvailable) {
            availabilityMessage.setText("Selected slot is available.");
            availabilityMessage.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: green; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0.8, 2, 2);");
        } else {
            availabilityMessage.setText("Selected slot is not available.");
            availabilityMessage.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: red; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0.8, 2, 2);");
        }
    }

    @FXML
    void generateMatchUpCode(ActionEvent event) {
        LocalDate selectedDate = datePicker.getValue();
        String selectedTime = timePicker.getValue();

        if (selectedDate == null || selectedTime == null) {
            codeMessage.setText("Please select a date and time to generate a code.");
            codeMessage.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: red; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0.8, 2, 2);");
            return;
        }

        // Generate a 6-digit match-up code
        Random random = new Random();
        generatedCode = String.format("%06d", random.nextInt(999999));
        codeMessage.setText("Match-Up Code: " + generatedCode);
        codeMessage.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: green; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0.8, 2, 2);");
    }

    @FXML
    void handleConfirmBooking(ActionEvent event) {
        LocalDate selectedDate = datePicker.getValue();
        String selectedTime = timePicker.getValue();

        if (selectedDate == null || selectedTime == null) {
            availabilityMessage.setText("Please select both date and time.");
            availabilityMessage.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: red; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0.8, 2, 2);");
            return;
        }

        // Convert LocalDate and String to java.sql.Date and java.sql.Time
        Date sqlDate = Date.valueOf(selectedDate);
        Time sqlTime = Time.valueOf(selectedTime + ":00");

        // Confirm booking with or without match-up code
        int bookingID;
        if (generatedCode != null) {
            bookingID = bookingService.confirmBookingWithOpponent(loggedInUser.getId(), selectedGround.getId(), sqlDate, sqlTime, generatedCode);
        } else {
            bookingID = bookingService.bookGround(loggedInUser.getId(), selectedGround.getId(), sqlDate, sqlTime);
        }

        if (bookingID!=-1) {
            double paymentAmount = selectedGround.getPrice();
            boolean paymentSuccess;
            if (generatedCode != null) {
                paymentSuccess = paymentService.processMatchUpPayment(bookingID, loggedInUser.getId(), paymentAmount);
            } else {
                paymentSuccess = paymentService.processPayment(bookingID, loggedInUser.getId(), paymentAmount);
            }
            if (paymentSuccess) {
                availabilityMessage.setText("Booking confirmed and payment initiated.");
                availabilityMessage.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: green; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0.8, 2, 2);");
            } else {
                availabilityMessage.setText("Booking confirmed, but payment failed.");
                availabilityMessage.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: orange; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0.8, 2, 2);");
            }
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/scenes/MyBookings.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) backButton.getScene().getWindow();
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
        } else {
            availabilityMessage.setText("Failed to confirm booking.");
            availabilityMessage.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: red; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0.8, 2, 2);");
        }
    }

    @FXML
    void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/scenes/PlayerDashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Player Dashboard");
            stage.setMaximized(false);
            stage.show();
            stage.setMaximized(true);
            PlayerDashboardController controller = loader.getController();
            controller.initializeData(loggedInUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
