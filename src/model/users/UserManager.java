package model.users;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages user data and handles its persistence to and loading from a file system.
 * <p>
 *     This class implements the **Singleton** pattern to ensure only one instance manages the user list across the application.
 * </p>
 * Responsibilities include:
 * <ul>
 *     <li>Load users from file into memory.</li>
 *     <li>Append new users and prevent duplicates.</li>
 *     <li>Handle user login, logout, and session management.</li>
 *     <li>Update user experience efficiently by rewriting the file.</li>
 * </ul> */
public class UserManager {
    /* --- Implementation singleton --- */
    /** The single, static instance of {@code UserManager}. This field ensures that only one instance of the manager exists
     * throughout the application lifecycle (the Singleton pattern). */
    private static UserManager instance;
    /** Static method to get the single instance of UserManager (Singleton pattern).
     * @return The Singleton instance of UserManager.
     * @throws RuntimeException if an I/O error occurs during the initial loading of users from the file, which is critical
     * for application startup. */
    public static UserManager getInstance() {
        if (instance == null) {
            try {
                instance = new UserManager();
            } catch (IOException e) {
                throw new RuntimeException("Critical error during UserManager initialization (File I/O).", e);
            }
        }
        return instance;
    }

    /* --- Fields --- */
    /** Path to the file storing all users. */
    private final String FILE_NAME = "resources\\data\\users.txt";
    /** List of users loaded in memory from the file. */
    private List<User> users = new ArrayList<>();
    /** Currently logged-in user. */
    private User currentUser = null;

    /** Private Constructor.
     * @throws IOException if an I/O error occurs while reading the initial user file. */
    private UserManager() throws IOException {
        loadUsers();
    }

    /* --- Methods --- */
    /** Loads users from the file {@link #FILE_NAME} into the in-memory list {@link #users}.
     * The method clears the current list before loading to avoid duplicates if called multiple times.
     * Each line in the file after the header should be in the format: "username;password;experience".
     * Only lines with exactly three fields are processed; malformed lines are skipped.
     * If the file does not exist, the in-memory list is set to null and an error message is printed.
     * @throws IOException if an I/O error occurs while reading the file. */
    public void loadUsers() throws IOException {
        users.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line = reader.readLine(); /* skip the header */
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    String username = parts[0];
                    String password = parts[1];
                    int experience = Integer.parseInt(parts[2]);
                    User u = new User(username, password, experience);
                    users.add(u);
                }
            }
        } catch (FileNotFoundException e) {
            users = new ArrayList<>();
            System.err.println(e.getMessage());
        }
    }

    /** Appends a new user to both the in-memory list {@link #users} and the file {@link #FILE_NAME}.
     * Before appending, the method checks for duplicates by username using {@link #isDuplicate(String)}.
     * If the user already exists, the method returns {@code false} and does not modify the list or file.
     * If the file does not exist or is empty, a header line is written first: "username;password;experience".
     * Each user is stored in the format: "username;password;experience".
     * @param user the user to append.
     * @return {@code true} if the user was successfully added; {@code false} if the username already exists.
     * @throws IOException if an I/O error occurs while writing to the file. */
    public boolean appendUser(User user) throws IOException {
        if (isDuplicate(user.getUsername()))
            return false;
        users.add(user);
        boolean writeHeader = false;
        File f = new File(FILE_NAME);
        if (!f.exists() || f.length() == 0)
            writeHeader = true;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            if (writeHeader) {
                writer.write("username;password;experience");
                writer.newLine();
            }
            String line = user.getUsername() + ";" + user.getPassword() + ";" + user.getExperience();
            writer.write(line);
            writer.newLine();
        }
        return true;
    }

    /** Attempts to log in a user by matching the provided username and password against the in-memory list of users {@link #users}.
     * If a matching user is found, {@link #currentUser} is updated to this user and the method returns {@code true}.
     * Otherwise, the method returns {@code false}.
     * @param username the username of the user attempting to log in.
     * @param password the password of the user.
     * @return {@code true} if login is successful; {@code false} if no matching user is found. */
    public boolean login(String username, String password) {
        for (User u : users)
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                currentUser = u;
                return true;
            }
        return false;
    }

    /** Prints all users currently loaded in the in-memory list {@link #users}. This method is primarily intended for
     * debugging purposes. It prints the username, password, and experience of each user to the console. */
    public void printUsers(){
        for (User u : users)
            System.out.println("Username: " + u.getUsername() + " - Password: " + u.getPassword() + " - Experience: " + u.getExperience());
    }

    /** Deletes all users from both the in-memory list {@link #users} and the file {@link #FILE_NAME}. The in-memory list
     * is cleared, and the file is overwritten with only the header. Any IOException encountered during file writing is
     * printed to the console. */
    public void deleteAllUsers(){
        users.clear();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            writer.write("username;password;experience");
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** Updates the experience of a specific user in the in-memory list and persists the change to the file.
     * This method uses a temporary file approach to efficiently replace the single updated line without loading all content into memory,
     * followed by deleting the original file and renaming the temporary file.
     * @param user the {@link User} whose experience should be updated (the object must contain the new experience value).
     * @throws IOException if an I/O error occurs while updating the file or during file rename/delete operations. */
    public void updateUserExperience(User user) throws IOException {
        File inputFile = new File(FILE_NAME);
        File tempFile = new File("resources\\data\\users_tmp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String line = reader.readLine();
            if (line != null) {
                writer.write(line); /* Write the header */
                writer.newLine();
            }
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3 && parts[0].equals(user.getUsername()))
                    line = user.getUsername() + ";" + user.getPassword() + ";" + user.getExperience();
                writer.write(line);
                writer.newLine();
            }
        }
        /* Overwrite the original file with the temporary file */
        if (!inputFile.delete())
            throw new IOException("Impossible delete original file");
        if (!tempFile.renameTo(inputFile))
            throw new IOException("Impossible rename temporary file");

        for (User u : users) {
            if (u.getUsername().equals(user.getUsername())) {
                u.setExperience(user.getExperience());
                break;
            }
        }
    }

    /** Finds and returns a user by username from the in-memory list.
     * @param username the username to search for.
     * @return the matching {@link User} object, or {@code null} if not found. */
    public User findUser(String username) {
        for (User u : users)
            if (u.getUsername().equals(username))
                return u;
        return null;
    }

    /** Attempts to log in a user. If the user does not exist, a new user is created and saved.
     * @param username the username of the user.
     * @param password the password of the user.
     * @return a {@link LoginResult} containing success status and a message describing the outcome. */
    public LoginResult loginUser(String username, String password) {
        if(username.isEmpty() || password.isEmpty()) {
            return new LoginResult(false, "Please enter username and password");
        }

        User existing = findUser(username);

        if(existing != null) {
            if(existing.getPassword().equals(password)) {
                currentUser = existing;
                return new LoginResult(true, "Welcome back, " + existing.getUsername());
            } else {
                return new LoginResult(false, "Password incorrect for user: " + username);
            }
        }

        // User does not exist → create new
        User newUser = new User(username, password, 0);
        try {
            appendUser(newUser);
            currentUser = newUser;
            return new LoginResult(true, "New user created: " + newUser.getUsername());
        } catch (IOException e) {
            e.printStackTrace();
            return new LoginResult(false, "Error saving new user!");
        }
    }

    /* --- Helpers --- */
    /** Checks whether a user with the specified username already exists in the in-memory list {@link #users}.
     * This method is used internally to prevent adding duplicate users when appending new entries.
     * @param username the username to check for duplicates.
     * @return {@code true} if a user with the given username exists; {@code false} otherwise. */
    private boolean isDuplicate(String username) {
        for (User u : users)
            if (u.getUsername().equals(username))
                return true;
        return false;
    }

    /** Helper to persist the entire current list of users to the file, overwriting the existing content.
     * This method is generally used for bulk updates or cleanup, though {@link #updateUserExperience(User)} uses a more efficient temp-file method.
     * @throws IOException if an I/O error occurs while writing to the file. */
    private void saveUsersToFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            /* Always write the header */
            writer.write("username;password;experience");
            writer.newLine();
            for (User u : users) {
                String line = u.getUsername() + ";" + u.getPassword() + ";" + u.getExperience();
                writer.write(line);
                writer.newLine();
            }
        }
    }

    /* --- Getters --- */
    /** Getter method
     * return @return the current user, or {@code null} if no user is logged in. */
    public User getCurrentUser() {
        return currentUser;
    }

    /* Setters */
    /** Setter method
     * Logs out the current user by clearing {@link #currentUser}. */
    public void logout() {
        currentUser = null;
    }

    /* --- InnerClass --- */
    /** Represents the result of a login attempt, providing a success flag and a corresponding message. */
    public static class LoginResult {
        /* --- Fields --- */
        /** Flag indicating whether the login attempt or user creation was successful. {@code true} if successful, {@code false} otherwise. */
        public final boolean success;
        /** A descriptive message detailing the outcome of the login attempt (e.g., "Welcome back," "Password incorrect,"
         * or "Error saving new user!"). */
        public final String message;

        /** Constructor
         * @param success {@code true} if the login or creation was successful, {@code false} otherwise.
         * @param message A message describing the outcome.
         */
        public LoginResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }
}
