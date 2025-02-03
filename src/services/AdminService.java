package services;

import entities.User;
import entities.Ground;
import repositories.AdminRepository;
import repositories.UserRepository;
import repositories.GroundRepository;

import java.util.List;

public class AdminService {
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final GroundRepository groundRepository;

    public AdminService() {
        this.adminRepository = new AdminRepository();
        this.userRepository = new UserRepository();
        this.groundRepository = new GroundRepository();
    }

    // Admin login
    public boolean loginAdmin(String username, String password) {
        return adminRepository.verifyAdmin(username, password);
    }

    // Get pending users
    public List<User> getPendingUsers() {
        return userRepository.getPendingUsers();
    }

    // Verify user
    public boolean verifyUser(int userId, boolean approve) {
        String status = approve ? "VERIFIED" : "REJECTED";
        return userRepository.updateUserStatus(userId, status);
    }

    // Get pending grounds
    public List<Ground> getPendingGrounds() {
        return groundRepository.getPendingGrounds();
    }

    // Verify ground
    public boolean verifyGround(int groundId, boolean approve) {
        String status = approve ? "VERIFIED" : "REJECTED";
        return groundRepository.updateGroundStatus(groundId, status);
    }

    // Blacklist a user
    public boolean blacklistUser(int userId) {
        return adminRepository.blacklistUser(userId);
    }

    // Blacklist a ground
    public boolean blacklistGround(int groundId) {
        return adminRepository.blacklistGround(groundId);
    }
}
