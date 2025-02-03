package services;

import entities.User;
import repositories.UserRepository;

import java.util.List;

public class UserService {
    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    // Register a new user
    public boolean registerUser(String username, String name, int age, String city, String phone, String password, String role) {
        if (userRepository.findUserByUsername(username) != null) {
            System.out.println("Username already exists.");
            return false;
        }
        User user = new User(0, username, name, age, city, phone, password, role, "PENDING", false);
        return userRepository.saveUser(user);
    }

    // Login a user
    public User loginUser(String username, String password) {
        User user = userRepository.findUserByUsername(username);
        if (user != null && user.isBlacklisted()) {
            System.out.println("Your account has been blacklisted. Contact support for more information.");
            return null;
        }
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        System.out.println("Invalid username or password.");
        return null;
    }

    // Fetch pending users
    public List<User> getPendingUsers() {
        return userRepository.getPendingUsers();
    }

    // Verify or reject a user
    public boolean verifyUser(int userId, boolean approve) {
        String status = approve ? "VERIFIED" : "REJECTED";
        return userRepository.updateUserStatus(userId, status);
    }
}
