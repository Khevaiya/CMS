package com.shiavnskipayroll.configurations;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserStatusService {
    private final Set<String> loggedOutUsers = new HashSet<>();

    // Call this method when a user logs out
    public void logUserOut(String userId) {
        loggedOutUsers.add(userId);
    }
    // Call this method when a user logs back in
    public void logUserIn(String userId) {
        loggedOutUsers.remove(userId); // Remove user from the logged-out set
    }

    public boolean isUserLoggedIn(String userId) {
        return !loggedOutUsers.contains(userId);
    }
}