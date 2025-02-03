package services;

import entities.Booking;
import entities.Ground;
import repositories.PlayerRepository;

import java.util.List;

public class PlayerService {
    private final PlayerRepository playerRepository;

    public PlayerService() {
        this.playerRepository = new PlayerRepository();
    }

    // Fetch available grounds
    public List<Ground> getAvailableGrounds(String type, Double minPrice, Double maxPrice, String location) {
        return playerRepository.getAvailableGrounds(type, minPrice, maxPrice, location);
    }

    // Fetch player's bookings
    public List<Booking> getBookings(int playerId, boolean isPast) {
        return playerRepository.getBookings(playerId, isPast);
    }

    // Cancel a booking
    public boolean cancelBooking(int bookingId) {
        return playerRepository.cancelBooking(bookingId);
    }
}
