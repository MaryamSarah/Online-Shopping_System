package com.example.shopping;

import com.example.shopping.User;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class UserManager {

    private static final String USERS_FILE_PATH = "users.txt";

    private List<User> userList;

    public UserManager() {
        this.userList = new ArrayList<>();
    }

    public boolean registerUser(String username, String password) {
        // Check if the username is already taken
        if (isUsernameTaken(username)) {
            System.out.println("Username already taken. Please choose another one.");
            return false;
        }

        // If the username is not taken, create a new user and add it to the list
        User newUser = new User(username, password);
        userList.add(newUser);

        // Save user information and shopping cart to separate files
        try {
            saveUserToFile(newUser);
            saveUsersToFile(); // Save the updated user list
        } catch (IOException e) {
            System.err.println("Error saving user: " + e.getMessage());
            return false;
        }
        return true;
    }

    public User loginUser(String username, String password) {
        // Load the user information from the file
        loadUsersFromFile();

        // Check if the username and password match any registered user
        for (User user : userList) {
            if (user.getUserName().equals(username) && user.getPassword().equals(password)) {
                System.out.println("Login successful for user: " + username);
                return user;
            }
        }

        System.out.println("Invalid username or password. Please try again.");
        return null;
    }

    private void loadUsersFromFile() {
        userList.clear(); // Clear the existing user list before loading from the file
        try {
            Path filePath = Path.of(USERS_FILE_PATH);

            // Check if the file exists, create it if not
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }

            List<String> lines = Files.readAllLines(filePath);
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    // Create a new User object and add it to the list
                    User user = new User(parts[0], parts[1]);
                    userList.add(user);
                }
            }
        } catch (IOException e) {
            // Handle the exception (print an error message, log, etc.)
            e.printStackTrace();
        }
    }

    public boolean isUsernameTaken(String username) {
        try {
            Path filePath = Path.of(USERS_FILE_PATH);

            // Check if the file exists, create it if not
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }

            List<String> lines = Files.readAllLines(filePath);
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[0].equals(username)) {
                    // Username found in the file
                    return true;
                }
            }
        } catch (IOException e) {
            // Handle the exception (print an error message, log, etc.)
            e.printStackTrace();
        }

        // Username not found
        return false;
    }

    public void saveUserToFile(User user) throws IOException {
        String filename = user.getUserName() + "_cart.txt"; // Use username-based filename
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(user.getCart().getCart()); // Write only the cart items
        }
    }

    public void loadUserCart(User user) throws IOException, ClassNotFoundException {
        String filename = user.getUserName() + "_cart.txt";
        user.getCart().loadCart(filename);
    }

    private void saveUsersToFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE_PATH, true))) {
            for (User user : userList) {
                writer.write(user.getUserName() + "," + user.getPassword());
                writer.newLine();
            }
        }
    }

}
