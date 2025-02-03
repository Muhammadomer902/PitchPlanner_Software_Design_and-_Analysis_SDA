package services;

import entities.Booking;
import entities.BookingDetails;
import repositories.BookingRepository;
import repositories.PaymentRepository;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

public class BookingService {
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;

    public BookingService() {
        this.bookingRepository = new BookingRepository();
        this.paymentRepository = new PaymentRepository();
    }

    // Generate a random 6-digit opponent code
    private String generateOpponentCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

    // Check if the ground is available ************
    public boolean checkAvailability(int groundId, Date date, Time time) {
        return bookingRepository.isGroundAvailable(groundId, date, time);
    }

    // Handle booking request *******************
    public int bookGround(int playerId, int groundId, Date date, Time time) {
        if (bookingRepository.isGroundAvailable(groundId, date, time)) {
            Booking booking = new Booking(0, playerId, groundId, date, time, "CONFIRMED", null, null);
            return bookingRepository.saveBooking(booking);
        }
        return -1;
    }

    // Handle booking request with opponent ************
    public int confirmBookingWithOpponent(int playerId, int groundId, Date date, Time time, String opponentCode) {
        if (bookingRepository.isGroundAvailable(groundId, date, time)) {
            Booking booking = new Booking(0, playerId, groundId, date, time, "PENDING", opponentCode, null);
            return bookingRepository.saveBooking(booking);
        } else {
            System.out.println("No existing booking found for match-up.");
            return -1;
        }
    }

    // Handle booking details **********
    public BookingDetails getBookingDetails(String matchUpCode) {
        return bookingRepository.getBookingDetails(matchUpCode);
    }

    // Handle booking details **********
    public List<BookingDetails> getBookingsByUser(int playerId) {
        return bookingRepository.getBookingsByUser(playerId);
    }

    // Handle booking details **********
    public List<BookingDetails> getBookingsByGround(int groundId) {
        return bookingRepository.getBookingsByGround(groundId);
    }

    // get player name
    public String getPlayerNameByBookingId(int bookingId)
    {
        return bookingRepository.getPlayerNameByBookingId(bookingId);
    }

    // Handle match-up request
    public String createMatchUpRequest(int playerId, Date date, Time time, int groundId) {
        if (!bookingRepository.isGroundAvailable(groundId, date, time)) {
            String opponentCode = generateOpponentCode();
            Booking booking = new Booking(0, playerId, groundId, date, time, "MATCH-UP", opponentCode, null);
            if (bookingRepository.saveBooking(booking)!=-1) {
                return opponentCode;
            }
        }
        return null;
    }

    // Automatically find an opponent
    public boolean findOpponentAutomatically(int playerId, Date date, Time time, int groundId) {
        Booking existingBooking = bookingRepository.findBookingForMatchUp(groundId, date, time);
        if (existingBooking != null && existingBooking.getOpponentPlayerId() == null) {
            return bookingRepository.joinMatchUp(existingBooking.getId(), playerId);
        }
        return false;
    }

    

    // Check if cancellation is allowed
    public boolean canCancelBooking(Booking booking) {
        // Check if less than 24 hours remain
        LocalDateTime bookingDateTime = LocalDateTime.of(booking.getDate().toLocalDate(), booking.getTime().toLocalTime());
        LocalDateTime now = LocalDateTime.now();
        if (now.plusHours(24).isAfter(bookingDateTime)) {
            System.out.println("Booking cannot be canceled: less than 24 hours remain.");
            return false;
        }

        // Check if it's a match-up booking
        if (booking.getStatus().equals("MATCH-UP")) {
            System.out.println("Booking cannot be canceled: match-up finalized.");
            return false;
        }

        return true;
    }

    // Cancel a booking
    public boolean cancelBooking(int bookingId) {
        Booking booking = bookingRepository.getBookingById(bookingId);

        if (booking == null) {
            System.out.println("Booking not found.");
            return false;
        }

        if (!canCancelBooking(booking)) {
            return false;
        }

        // Update booking status to CANCELLED
        boolean isCanceled = bookingRepository.cancelBooking(bookingId);
        if (isCanceled) {
            // Update payment status to REFUNDED
            paymentRepository.updatePaymentStatusByBookingId(bookingId, "REFUNDED");
            System.out.println("Booking canceled successfully!");
            return true;
        } else {
            System.out.println("Failed to cancel booking.");
            return false;
        }
    }

    // Create a match-up request
    public boolean createMatchUpRequest(int bookingId) {
        Booking booking = bookingRepository.getBookingById(bookingId);
        if (booking == null) {
            System.out.println("Booking not found.");
            return false;
        }

        if (!"CONFIRMED".equals(booking.getStatus())) {
            System.out.println("Match-up request cannot be generated for this booking.");
            return false;
        }

        if (!"MATCH-UP".equals(booking.getStatus())) {
            System.out.println("Match-up already done for this booking.");
            return false;
        }

        String opponentCode = generateOpponentCode();
        return bookingRepository.generateMatchUpRequest(bookingId, opponentCode);
    }

    // Join a match-up
    public boolean joinMatchUp(int bookingId, int opponentPlayerId) {
        return bookingRepository.joinMatchUp(bookingId, opponentPlayerId);
    }
}
