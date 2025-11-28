package com.ems.services;

import com.ems.dao.UserDAO;
import com.ems.models.User;

public class AuthService {
    private final UserDAO userDAO = new UserDAO();

    public User login(String username, String password) {
        User u = userDAO.findByUsername(username);
        if (u == null) {
            System.out.println("User not found.");
            return null;
        }
        // NOTE: plaintext compare. For production use hashed passwords.
        if (u.getPassword().equals(password)) {
            return u;
        } else {
            System.out.println("Invalid password.");
            return null;
        }
    }

    public boolean register(String username, String password, String role, Integer linkedId) {
        return userDAO.createUser(username, password, role, linkedId);
    }
}
