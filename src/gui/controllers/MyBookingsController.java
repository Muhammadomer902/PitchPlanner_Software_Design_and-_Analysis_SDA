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
import entities.BookingDetails;
import entities.User;
import services.BookingService;
import services.FeedbackService;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class MyBookingsController {

    @FXML
    private Button backButton;

    @FXML
    private TableView<BookingDetails> bookingsTable;

    @FXML
    private TableColumn<BookingDetails, Integer> bookingIdColumn;

    @FXML
    private TableColumn<BookingDetails, String> groundNameColumn;

    @FXML
    private TableColumn<BookingDetails, Date> dateColumn;

    @FXML
    private TableColumn<BookingDetails, String> timeColumn;

    @FXML
    private TableColumn<BookingDetails, String> statusColumn;

    @FXML
    private TableColumn<BookingDetails, Button> actionColumn;

    private final BookingService bookingService = new BookingService();
    private final FeedbackService feedbackService = new FeedbackService();

    private User loggedInUser;

    public void initializeData(User user) {
        this.loggedInUser = user;
        loadBookings();
    }

    @FXML
    private void handleBack(ActionEvent event) {
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

    @SuppressWarnings("unused")
    private void loadBookings() {
    List<BookingDetails> bookings = bookingService.getBookingsByUser(loggedInUser.getId());
    bookingsTable.getItems().clear();

    bookingIdColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getId()));
    groundNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGroundName()));
    dateColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDate()));
    timeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTime().toString()));
    statusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));

    actionColumn.setCellValueFactory(data -> {
        BookingDetails booking = data.getValue();
        Button actionButton = new Button();

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        // Past bookings: allow feedback if not already given
        if (booking.getDate().toLocalDate().isBefore(today) || 
            (booking.getDate().toLocalDate().isEqual(today) && booking.getTime().toLocalTime().isBefore(now))) {
            if (!feedbackService.hasFeedback(booking.getId(), loggedInUser.getId())) {
                actionButton.setText("Provide Feedback");
                actionButton.setOnAction(event -> navigateToFeedback(booking));
            } else {
                actionButton.setDisable(true);
            }
        }
        // Future bookings: allow cancellation
        else if (booking.getDate().toLocalDate().isAfter(today) || 
                (booking.getDate().toLocalDate().isEqual(today) && booking.getTime().toLocalTime().isAfter(now))) {
            if (!"CANCELLED".equals(booking.getStatus())) {
                actionButton.setText("Cancel Booking");
                actionButton.setOnAction(event -> cancelBooking(booking));
            } else {
                actionButton.setDisable(true);
            }
        } else {
            actionButton.setDisable(true);
        }

        return new SimpleObjectProperty<>(actionButton);
    });

    bookingsTable.getItems().addAll(bookings);
}

    private void navigateToFeedback(BookingDetails booking) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/scenes/ProvideFeedback.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) bookingsTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Provide Feedback");
            stage.setMaximized(false);
            stage.show();
            stage.setMaximized(true);
            // Pass booking data to the feedback controller
            ProvideFeedbackController controller = loader.getController();
            controller.initializeData(loggedInUser, booking);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cancelBooking(BookingDetails booking) {
        boolean success = bookingService.cancelBooking(booking.getId());
        if (success) {
            loadBookings();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Booking canceled successfully!", ButtonType.OK);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Failed to cancel booking.", ButtonType.OK);
            alert.showAndWait();
        }
    }
}
