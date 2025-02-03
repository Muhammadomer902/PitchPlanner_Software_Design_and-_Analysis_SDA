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
import entities.Ground;
import entities.User;
import services.BookingService;
import services.ComplaintService;
import services.GroundService;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class ScheduledBookingsController {

    @FXML
    private TableColumn<BookingDetails, Button> actionColumn;

    @FXML
    private Button backButton;

    @FXML
    private TableColumn<BookingDetails, Integer> bookingIdColumn;

    @FXML
    private TableView<BookingDetails> bookingsTable;

    @FXML
    private TableColumn<BookingDetails, Date> dateColumn;

    @FXML
    private ComboBox<String> groundComboBox;

    @FXML
    private TableColumn<BookingDetails, String> playerNameColumn;

    @FXML
    private TableColumn<BookingDetails, Double> priceColumn;

    @FXML
    private TableColumn<BookingDetails, String> statusColumn;

    @FXML
    private TableColumn<BookingDetails, Time> timeColumn;

    private final GroundService groundService = new GroundService();
    private final BookingService bookingService = new BookingService();
    private final ComplaintService complaintService = new ComplaintService();
    private User loggedInUser; // Logged-in ground owner
    private List<Ground> ownedGrounds; // Grounds owned by the ground owner

    /**
     * Pass the logged-in user data to this controller.
     * 
     * @param user The logged-in user.
     */
    public void initializeData(User user) {
        this.loggedInUser = user;
        loadOwnedGrounds();
    }

    /**
     * Load the list of grounds owned by the logged-in user into the ComboBox.
     */
    @SuppressWarnings("unused")
    private void loadOwnedGrounds() {
        ownedGrounds = groundService.getGroundsByOwner(loggedInUser.getId());
        groundComboBox.getItems().clear();
        // Add ground names to the ComboBox
        for (Ground ground : ownedGrounds) {
            groundComboBox.getItems().add(ground.getName());
        }

        // Set listener to load bookings when a ground is selected
        groundComboBox.setOnAction(event -> loadBookingsForGround());
    }

    /**
     * Load bookings for the selected ground into the table.
     */
    @SuppressWarnings("unused")
    private void loadBookingsForGround() {
        int selectedIndex = groundComboBox.getSelectionModel().getSelectedIndex();
        Ground selectedGround = ownedGrounds.get(selectedIndex);
        if (selectedGround == null) {
            bookingsTable.getItems().clear();
            return;
        }
    
        List<BookingDetails> bookings = bookingService.getBookingsByGround(selectedGround.getId());
    
        bookingsTable.getItems().clear();
    
        bookingIdColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getId()));
    
        // Fetch player names dynamically
        playerNameColumn.setCellValueFactory(data -> {
            BookingDetails booking = data.getValue();
            String playerName = bookingService.getPlayerNameByBookingId(booking.getId()); // Fetch dynamically
            return new SimpleStringProperty(playerName != null ? playerName : "Unknown");
        });
    
        dateColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDate()));
        timeColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getTime()));
        priceColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getPrice()));
        statusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
    
        actionColumn.setCellValueFactory(data -> {
            BookingDetails booking = data.getValue();
            Button actionButton = new Button("Complain");
    
            // Enable "Complain" button for past bookings
            if (booking.getDate().toLocalDate().isBefore(java.time.LocalDate.now()) && !complaintService.hasComplaint(loggedInUser.getId(), booking.getId())) {
                actionButton.setOnAction(event -> fileComplaint(booking));
            } else {
                actionButton.setDisable(true);
            }
    
            return new SimpleObjectProperty<>(actionButton);
        });
    
        bookingsTable.getItems().addAll(bookings);
    }
    

    /**
     * Handle the "File Complaint" action for a booking.
     * 
     * @param booking The booking for which to file a complaint.
     */
    private void fileComplaint(BookingDetails booking) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/scenes/FileComplain.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) bookingsTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("File Complaint");
            stage.setMaximized(false);
            stage.show();
            stage.setMaximized(true);

            // Pass booking and user data to the FileComplainController
            FileComplainController controller = loader.getController();
            controller.initializeData(loggedInUser, booking);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handle the "Back" button click event.
     * 
     * @param event The action event.
     */
    @FXML
    void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/scenes/GroundOwnerDashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Ground Owner Dashboard");
            stage.setMaximized(false);
            stage.show();
            stage.setMaximized(true);

            // Pass user data to GroundOwnerDashboardController
            GroundOwnerDashboardController controller = loader.getController();
            controller.initializeData(loggedInUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
