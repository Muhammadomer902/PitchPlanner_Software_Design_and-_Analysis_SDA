package services;

import entities.Booking;
import entities.Ground;
import repositories.GroundOwnerRepository;

import java.util.List;

public class GroundOwnerService {
    private final GroundOwnerRepository groundOwnerRepository;

    public GroundOwnerService() {
        this.groundOwnerRepository = new GroundOwnerRepository();
    }

    // Fetch grounds owned by the ground owner
    public List<Ground> getOwnedGrounds(int ownerId) {
        return groundOwnerRepository.getOwnedGrounds(ownerId);
    }

    // Fetch bookings for a specific ground owned by the ground owner
    public List<Booking> getBookingsForGround(int groundId,boolean isPast) {
        return groundOwnerRepository.getBookingsForGround(groundId,isPast);
    }
}
