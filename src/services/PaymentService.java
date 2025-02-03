package services;
import java.util.List;
import entities.Payment;
import repositories.PaymentRepository;

public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentService() {
        this.paymentRepository = new PaymentRepository();
    }

    // Process solo booking payment
    public boolean processPayment(int bookingId, int payerId, double amount) {
        Payment payment = new Payment(0, bookingId, payerId, amount, "PENDING");
        boolean isSaved = paymentRepository.savePayment(payment);
        if (isSaved) {
            System.out.println("Payment initiated successfully!");
            return true;
        } else {
            System.out.println("Failed to initiate payment.");
            return false;
        }
    }

    // Process match-up payment
    public boolean processMatchUpPayment(int bookingId, int payer1Id, double totalAmount) {
        double splitAmount = totalAmount / 2;
        Payment payment1 = new Payment(0, bookingId, payer1Id, splitAmount, "PENDING");

        boolean isSaved1 = paymentRepository.savePayment(payment1);

        if (isSaved1) {
            System.out.println("Match-up payments initiated successfully!");
            return true;
        } else {
            System.out.println("Failed to initiate match-up payments.");
            return false;
        }
    }

    // Process payment
    public boolean processPayment(int paymentID) {
        return paymentRepository.processPayment(paymentID);
    }

    

    public List<Payment> getPaymentsByUser(int userId) {
        return paymentRepository.getPaymentsByUserId(userId);
    }

}
