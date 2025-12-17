package model.core;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/** Class representing the combat statistics of a character in the RPG game.
 * <p>
 *     It stores the core attributes: health, attack power, defense, and speed. This class is designed to be {@link java.lang.Cloneable}
 *     to facilitate resetting character stats. It also implements {@link PropertyChangeSupport} to notify listeners
 *     (e.g., UI elements like health bars) when stats change.
 * </p> */
public class Stats implements Cloneable{
    /* --- Fields --- */
    /** Character's health points. */
    private int health;
    /** Character's attack power. */
    private int attackPower;
    /** Character's defense points. */
    private int defense;
    /** Character's speed. */
    private int speed;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /** Constructs.
     * @param health        the initial health points.
     * @param attackPower   the initial attackPower.
     * @param defense       the initial defense points.
     * @param speed         the initial speed attribute. */
    public Stats(int health, int attackPower, int defense, int speed){
        this.health = health;
        this.attackPower = attackPower;
        this.defense = defense;
        this.speed = speed;
    }

    /* --- Methods --- */
    /** Prints all the stats. */
    public void printStats(){
        System.out.println("Health: " + this.health);
        System.out.println("Attack Power: " + this.attackPower);
        System.out.println("Defense : " + this.defense);
        System.out.println("Speed : " + this.speed);
    }

    /** Increments stats by applying multipliers and adding to speed.
     * @param healthMultiplier  multiplier applied to health.
     * @param attackMultiplier  multiplier applied to attackPower.
     * @param defenseMultiplier multiplier applied to defense.
     * @param speedAdder        value added to speed. */
    public void incrementStats(double healthMultiplier, double attackMultiplier, double defenseMultiplier, int speedAdder) {
        this.health *= healthMultiplier;
        this.attackPower *= attackMultiplier;
        this.defense *= defenseMultiplier;
        this.speed += speedAdder;
    }

    /** Creates and returns a copy of this Stats object.
     * @return a clone of this Stats instance. */
    public Stats clone() {
        try {
            return (Stats) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    /* --- Property Change Support --- */
    /** Registers a listener to receive notifications when bound properties (stats) change.
     * @param listener The listener to be added. */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    /** Removes a previously registered property change listener.
     * @param listener The listener to be removed. */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    /* --- Getters --- */
    /** Getter method.
     * @return current health points. */
    public int getHealth(){
        return this.health;
    }
    /** Getter method.
     * @return current attackPower. */
    public int getAttackPower(){
        return this.attackPower;
    }
    /** Getter method.
     * @return current defense points. */
    public int getDefense(){
        return this.defense;
    }
    /** Getter method.
     * @return current speed. */
    public int getSpeed(){
        return this.speed;
    }

    /* --- Setters --- */
    /** Setter method. Sets all stats from another Stats object.
     * @param stats the Stats object to copy values from. */
    public void setStats(Stats stats){
        this.health = stats.health;
        this.attackPower = stats.attackPower;
        this.defense = stats.defense;
        this.speed = stats.speed;
    }
    /** Setter method.
     * This method ensures the health value is non-negative and notifies all registered {@link java.beans.PropertyChangeListener}s
     * (such as a {@code HealthProgressBar}) if the value has actually changed, using the property name "health".
     * @param health new health value. */
    public void setHealth(int health){
        int oldHealth = this.health;
        /* To have always a health value not negative */
        int finalHealth = Math.max(0, health);
        /* Check if there was an actual change */
        if (oldHealth != finalHealth) {
            this.health = finalHealth;
            pcs.firePropertyChange("health", oldHealth, finalHealth);
        }
    }
    /** Setter method.
     * @param attackPower new attack power value. */
    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }
    /** Setter method.
     * @param defense new defense value. */
    public void setDefense(int defense) {
        this.defense = defense;
    }
    /** Setter method.
     * @param speed new speed value. */
    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
