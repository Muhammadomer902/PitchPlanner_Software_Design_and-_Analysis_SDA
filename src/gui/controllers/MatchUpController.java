package gui.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import entities.BookingDetails;
import entities.User;
import services.BookingService;
import services.PaymentService;

import java.io.IOException;
import java.util.Optional;

public class MatchUpController {

    @FXML
    private Label availabilityMessage;

    @FXML
    private Button backButton;

    @FXML
    private TableView<BookingDetails> bookingTable;

    @FXML
    private Button confirmMatchUpButton;

    @FXML
    private TableColumn<BookingDetails, String> dateColumn;

    @FXML
    private TableColumn<BookingDetails, String> groundLocationColumn;

    @FXML
    private TableColumn<BookingDetails, String> groundNameColumn;

    @FXML
    private TextField matchUpCodeField;

    @FXML
    private TableColumn<BookingDetails, String> priceColumn;

    @FXML
    private Button searchButton;

    @FXML
    private TableColumn<BookingDetails, String> timeColumn;

    private final BookingService bookingService = new BookingService();
    private final PaymentService paymentService = new PaymentService();
    private BookingDetails selectedBooking;
    private User loggedInUser;

    /**
     * Initializes data passed to this controller.
     *
     * @param user The currently logged-in user.
     */
    public void initializeData(User user) {
        this.loggedInUser = user;
    }

    @FXML
    public void initialize() {
        // Configure table columns
        groundNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGroundName()));
        groundLocationColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGroundLocation()));
        priceColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getPrice()/2)));
        dateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDate().toString()));
        timeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTime().toString()));
    }

    @FXML
    void handleSearch(ActionEvent event) {
        String matchUpCode = matchUpCodeField.getText().trim();

        if (matchUpCode.isEmpty() || matchUpCode.length() != 6) {
            availabilityMessage.setText("Please enter a valid 6-digit match-up code.");
            availabilityMessage.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: red; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0.8, 2, 2);");
            return;
        }

        // Search for booking by match-up code
        Optional<BookingDetails> bookingOptional = Optional.ofNullable(bookingService.getBookingDetails(matchUpCode));

        if (bookingOptional.isPresent()) {
            selectedBooking = bookingOptional.get();
            bookingTable.getItems().clear();
            bookingTable.getItems().add(selectedBooking);
            availabilityMessage.setText("Booking found. Please confirm the match-up.");
            availabilityMessage.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: green; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0.8, 2, 2);");
        } else {
            bookingTable.getItems().clear();
            selectedBooking = null;
            availabilityMessage.setText("No booking found with the given match-up code.");
            availabilityMessage.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: red; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0.8, 2, 2);");
        }
    }

    @FXML
    void handleConfirmMatchUp(ActionEvent event) {
        if (selectedBooking == null) {
            availabilityMessage.setText("Please search for and select a booking before confirming.");
            availabilityMessage.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: red; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0.8, 2, 2);");
            return;
        }

        // Confirm the match-up
        boolean success = bookingService.joinMatchUp(selectedBooking.getId(), loggedInUser.getId());

        if (success) {
            double paymentAmount = selectedBooking.getPrice();
            boolean paymentSuccess;
            paymentSuccess = paymentService.processMatchUpPayment(selectedBooking.getId(), loggedInUser.getId(), paymentAmount);
            if (paymentSuccess) {
                availabilityMessage.setText("Booking confirmed and payment initiated.");
                availabilityMessage.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: green; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0.8, 2, 2);");
            } else {
                availabilityMessage.setText("Booking confirmed, but payment failed.");
                availabilityMessage.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: orange; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0.8, 2, 2);");
            }
            bookingTable.getItems().clear();
            matchUpCodeField.clear();
            selectedBooking = null;
        } else {
            availabilityMessage.setText("Failed to confirm the match-up. Please try again.");
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

            // Pass user data back to PlayerDashboardController
            PlayerDashboardController controller = loader.getController();
            controller.initializeData(loggedInUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
