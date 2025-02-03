package services;

import entities.ComplaintDetails;
import entities.Ground;
import repositories.GroundRepository;

import java.util.List;

public class GroundService {
    private final GroundRepository groundRepository;

    public GroundService() {
        this.groundRepository = new GroundRepository();
    }

    // Fetch pending grounds
    public List<Ground> getPendingGrounds() {
        return groundRepository.getPendingGrounds();
    }

    // Verify ground registration
    public boolean verifyGround(int groundId, boolean approve) {
        String status = approve ? "VERIFIED" : "REJECTED";
        return groundRepository.updateGroundStatus(groundId, status);
    }

    // Add a new ground
    public boolean addGround(String name, String location, double price, String type, int ownerId) {
        Ground ground = new Ground(0, name, location, price, type, ownerId, false);
        return groundRepository.addGround(ground);
    }

    // Remove ground
    public boolean removeGround(int groundId) {
        return groundRepository.removeGround(groundId);
    }

    // Update ground details
    public boolean updateGround(int groundId, String name, String location, double price, String type) {
        return groundRepository.updateGround(groundId, name, location, price, type);
    }

    // Fetch grounds with optional filters
    public List<Ground> getGrounds(String type, Double minPrice, Double maxPrice, String location) {
        return groundRepository.getGrounds(type, minPrice, maxPrice, location);
    }

    public List<Ground> getAllAvailableGrounds() {
        return groundRepository.getAllAvailableGrounds();
    }

    public List<Ground> getGroundsByOwner(int GroundOwnerId) {
        return groundRepository.getGroundsByOwner(GroundOwnerId);
    }
    
     // Blacklist a ground
     public boolean blacklistGround(ComplaintDetails complaint) {
        return groundRepository.blacklistGround(complaint);
    }
}
