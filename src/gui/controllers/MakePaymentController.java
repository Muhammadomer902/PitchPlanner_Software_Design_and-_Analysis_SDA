package gui.controllers;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import entities.Payment;
import entities.User;
import services.PaymentService;

import java.io.IOException;
import java.util.List;

public class MakePaymentController {

    @FXML
    private TableColumn<Payment, Button> actionColumn;

    @FXML
    private TableColumn<Payment, Double> amountColumn;

    @FXML
    private Button backButton;

    @FXML
    private TableColumn<Payment, Integer> bookingIdColumn;

    @FXML
    private TableView<Payment> paymentTable;

    @FXML
    private TableColumn<Payment, String> statusColumn;

    private final PaymentService paymentService = new PaymentService(); // Instance of PaymentService
    private User loggedInUser; // The logged-in user's data

    /**
     * Initialize the table and other components.
     */
    @SuppressWarnings("unused")
    public void initializeData(User user) {
        this.loggedInUser = user;

        // Configure table columns
        bookingIdColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getBookingId()).asObject());
        amountColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getAmount()).asObject());
        statusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPaymentStatus()));
        actionColumn.setCellValueFactory(data -> {
            Button actionButton = new Button();
            if ("PENDING".equals(data.getValue().getPaymentStatus())) {
                actionButton.setText("Proceed");
                actionButton.setStyle("-fx-background-color: #32CD32; -fx-text-fill: white;");
                actionButton.setOnAction(event -> navigateToProceedPayment(data.getValue()));
            } else {
                actionButton.setText("Completed");
                actionButton.setDisable(true);
            }
            return new SimpleObjectProperty<>(actionButton);
        });

        // Load payment data into the table
        loadPayments();
    }

    /**
     * Loads the payments of the logged-in user into the table.
     */
    private void loadPayments() {
        List<Payment> payments = paymentService.getPaymentsByUser(loggedInUser.getId());
        ObservableList<Payment> paymentObservableList = FXCollections.observableArrayList(payments);
        paymentTable.setItems(paymentObservableList);
    }


    /**
     * Handle the Back button to navigate back to the Player Dashboard.
     */
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

    /**
     * Navigate to the ProceedPayment.fxml to handle payment.
     * @param payment The payment to process.
     */
    private void navigateToProceedPayment(Payment payment) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/scenes/ProceedPayment.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) paymentTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Proceed Payment");
            stage.setMaximized(false);
            stage.show();
            stage.setMaximized(true);

            // Pass payment data to ProceedPaymentController
            ProceedPaymentController controller = loader.getController();
            controller.initializeData(loggedInUser, payment);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
