package model.users;

import java.io.Serializable;

/** Represents a user in the system, storing credentials and experience information.
 * <p>
 *     This class acts as a model in the MVC pattern. It holds data about the user, including
 *     username, password, and experience points. It provides methods to manipulate experience
 *     and calculate the user's level and progress toward the next level.
 * </p> */
public class User implements Serializable {
    /* --- Fields --- */
    /** The user's username. */
    private final String username;
    /** The user's password (should be hashed in production). */
    private final String password;
    /** The user's accumulated experience points. */
    private int experience;

    /** Constructor
     * @param username      the user's username.
     * @param password      the user's password.
     * @param experience    the initial experience points. */
    public User(String username, String password, int experience) {
        this.username = username;
        this.password = password;
        this.experience = experience;
    }

    /* --- Methods --- */
    /** Increases the character's experience by a given amount. When experience reaches 1000, the character levels up
     * and the remaining experience carries over.
     * @param experience the amount of experience gained. */
    public void increaseExperience(int experience){
        this.experience = this.experience + experience;

    }

    /* --- Getters --- */
    /** Getter method
     * @return the username. */
    public String getUsername() {
        return username;
    }
    /** Getter method
     * @return the password. */
    public String getPassword() {
        return password;
    }
    /** Getter method
     * @return the experience points. */
    public int getExperience() {
        return experience;
    }
    /** Getter method
     * @return the current level. */
    public int getLevel(){
        return experience / 1000;
    }
    /** Getter method
     * @return the experience points toward the next level. */
    public int getExperienceProgress(){
        return experience % 1000;
    }

    /* --- Setters --- */
    /** Setter method
     * @param experience the new experience value. */
    public void setExperience(int experience) {
        this.experience = experience;
    }
}
