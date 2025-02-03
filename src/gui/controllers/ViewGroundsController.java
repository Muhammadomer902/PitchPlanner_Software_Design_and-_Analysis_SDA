package gui.controllers;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import entities.Ground;
import entities.User;
import services.GroundService;

import java.io.IOException;

public class ViewGroundsController {

    @FXML
    private Button backButton;

    @FXML
    private TableColumn<Ground, Button> bookButtonColumn;

    @FXML
    private TableColumn<Ground, String> groundNameColumn;

    @FXML
    private TableView<Ground> groundsTable;

    @FXML
    private TableColumn<Ground, String> locationColumn;

    @FXML
    private TextField locationFilter;

    @FXML
    private TextField maxPriceFilter;

    @FXML
    private TextField minPriceFilter;

    @FXML
    private TableColumn<Ground, Double> priceColumn;

    @FXML
    private Button searchButton;

    @FXML
    private TableColumn<Ground, String> typeColumn;

    @FXML
    private ComboBox<String> typeFilter;

    private final GroundService groundService = new GroundService(); // Instance of GroundService
    private User loggedInUser;

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

    public void initializeData(User user) {
        loggedInUser = user;
    }

    @FXML
    void handleSearch(ActionEvent event) {
        try {
            String type = typeFilter.getValue();
            String location = locationFilter.getText().trim();
            String minPriceText = minPriceFilter.getText().trim();
            String maxPriceText = maxPriceFilter.getText().trim();

            Double minPrice = minPriceText.isEmpty() ? null : Double.parseDouble(minPriceText);
            Double maxPrice = maxPriceText.isEmpty() ? null : Double.parseDouble(maxPriceText);

            // Fetch and filter grounds
            groundsTable.getItems().clear();
            groundsTable.getItems().addAll(groundService.getGrounds(type, minPrice, maxPrice, location));
        } catch (NumberFormatException e) {
            showError("Invalid input for price. Please enter numeric values.");
        } catch (Exception e) {
            e.printStackTrace();
            showError("An error occurred while searching for grounds.");
        }
    }

    @SuppressWarnings("unused")
    @FXML
    public void initialize() {
        // Initialize ComboBox for type filter
        typeFilter.getItems().addAll("Cricket", "Football");

        // Configure table columns
        groundNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        typeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getType()));
        locationColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLocation()));
        priceColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPrice()).asObject());
        bookButtonColumn.setCellValueFactory(data -> {
            Button bookButton = new Button("Book");
            bookButton.setStyle("-fx-background-color: #32CD32; " +  // Green background
                                "-fx-text-fill: white; " +           // White text
                                "-fx-font-size: 14px; " +            // Font size
                                "-fx-font-weight: bold; " +          // Bold text
                                "-fx-padding: 5px 10px; " +          // Padding for better look
                                "-fx-border-radius: 5px; " +         // Rounded corners
                                "-fx-background-radius: 5px; " +     // Match border radius
                                "-fx-cursor: hand; " +               // Hand cursor on hover
                                "-fx-border-color: #228B22; " +      // Darker green border
                                "-fx-border-width: 2px; ");          // Border width

            bookButton.setOnMouseEntered(event -> 
                bookButton.setStyle("-fx-background-color: #2E8B57; " + // Darker green on hover
                                    "-fx-text-fill: white; " +
                                    "-fx-font-size: 14px; " +
                                    "-fx-font-weight: bold; " +
                                    "-fx-padding: 5px 10px; " +
                                    "-fx-border-radius: 5px; " +
                                    "-fx-background-radius: 5px; " +
                                    "-fx-cursor: hand; " +
                                    "-fx-border-color: #228B22; " +
                                    "-fx-border-width: 2px; "));

            bookButton.setOnMouseExited(event -> 
                bookButton.setStyle("-fx-background-color: #32CD32; " + // Restore original green
                                    "-fx-text-fill: white; " +
                                    "-fx-font-size: 14px; " +
                                    "-fx-font-weight: bold; " +
                                    "-fx-padding: 5px 10px; " +
                                    "-fx-border-radius: 5px; " +
                                    "-fx-background-radius: 5px; " +
                                    "-fx-cursor: hand; " +
                                    "-fx-border-color: #228B22; " +
                                    "-fx-border-width: 2px; "));

            bookButton.setOnAction(event -> navigateToBooking(data.getValue()));
            return new SimpleObjectProperty<>(bookButton);
        });

        // Load initial data
        groundsTable.getItems().addAll(groundService.getAllAvailableGrounds());
    }

    private void navigateToBooking(Ground ground) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/scenes/Booking.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) groundsTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Book Ground");
            stage.setMaximized(false);
            stage.show();
            stage.setMaximized(true);
            
            // Pass selected ground to BookingController (implement initializeData if needed)
            BookingController controller = loader.getController();
            controller.initializeData(loggedInUser,ground);

            
        } catch (IOException e) {
            e.printStackTrace();
            showError("An error occurred: " + e.getMessage());
            showError("An error occurred while navigating to the booking page.");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
