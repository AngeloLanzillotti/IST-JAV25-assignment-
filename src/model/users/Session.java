package model.users;

/** Singleton class that manages the current user session.
 * <p>
 *     This class stores a reference to the currently logged-in user and provides static methods to set, retrieve, or clear
 *     the session state throughout the application. This acts as a centralized access point for the active user {@link User} object.
 * </p> */
public class Session {
    /* --- Fields --- */
    /** The currently logged-in user. This is a static field to ensure that there is only one active session at any given
     * time across the application. It is {@code null} if no user is logged in. */
    private static User currentUser = null;

    /* --- Methods --- */
    /** Clears the current session by logging out the user. After calling this method, getCurrentUser() will return null. */
    public static void clear() {
        currentUser = null;
    }

    /* --- Getters --- */
    /** Getter method.
     * @return the current user, or null if no user is logged in. */
    public static User getCurrentUser() {
        return currentUser;
    }

    /* --- Setters --- */
    /** Setter method.
     * @param user the user to set as the current session user. */
    public static void setCurrentUser(User user) {
        currentUser = user;
    }
}
