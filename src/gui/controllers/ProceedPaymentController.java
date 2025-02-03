package gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import entities.Payment;
import entities.User;
import services.PaymentService;

import java.io.IOException;

public class ProceedPaymentController {

    @FXML
    private Button backButton;

    @FXML
    private TextField easypaisaNumberField;

    @FXML
    private Button proceedButton;

    private User loggedInUser; // Holds the logged-in user's data
    private Payment payment; // Holds the payment details
    private final PaymentService paymentService = new PaymentService(); // Instance of PaymentService

    /**
     * Initializes the data passed to the controller.
     *
     * @param user    The logged-in user.
     * @param payment The payment data for the current booking.
     */
    public void initializeData(User user, Payment payment) {
        this.loggedInUser = user;
        this.payment = payment;
    }

    @FXML
    void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/scenes/MakePayment.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Make Payment");
            stage.setMaximized(false);
            stage.show();
            stage.setMaximized(true);

            // Pass the logged-in user back to MakePaymentController
            MakePaymentController controller = loader.getController();
            controller.initializeData(loggedInUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleProceedPayment(ActionEvent event) {
        String easypaisaNumber = easypaisaNumberField.getText().trim();

        // Validate Easypaisa Number
        if (easypaisaNumber.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter your Easypaisa number.");
            return;
        }
        if (!easypaisaNumber.matches("\\d{11}")) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Easypaisa number must be exactly 11 digits.");
            return;
        }

        // Process the payment
        boolean success = paymentService.processPayment(payment.getId());
        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Payment Successful", "Your payment has been processed successfully.");
            handleBack(null); // Navigate back to MakePayment page
        } else {
            showAlert(Alert.AlertType.ERROR, "Payment Failed", "An error occurred while processing your payment. Please try again.");
        }
    }

    /**
     * Displays an alert dialog.
     *
     * @param alertType The type of alert.
     * @param title     The title of the alert.
     * @param message   The message to display in the alert.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
