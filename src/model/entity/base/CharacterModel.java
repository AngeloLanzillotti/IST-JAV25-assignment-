package model.entity.base;

import controller.eventListeners.CharacterEventListener;
import model.core.*;

import java.beans.PropertyChangeListener;

/** Abstract class representing a character in the RPG game.
 * <p>
 *     This class provides the core structure and behavior for all characters, including players and enemies.
 *     It manages the character's name, level, type, and stats (health, attack, defense, speed). It also handles
 *     combat mechanics such as attacking, taking damage, and calculating powerful attacks based on the game logic.
 * </p> <p>
 *     Concrete character classes (e.g. Warrior, Archer, Goblin) should extend this class and provide specific
 *     implementations if needed.
 * </p>
 * @see model.core.Stats
 * @see model.core.CharacterType
 * @see #attack(CharacterModel)
 * @see #takeDamage(int)
 * @see #updateStatsFromLevel() */
public abstract class CharacterModel {
    /* --- Fields --- */
    /** The character's current name. */
    protected String name;
    /** Current stats of the character (health, attackPower, defense, speed). */
    protected Stats stats;
    /** Current level of the character (0–4), which modifies stats through a Difficulty multiplier. DEFAULT value is 0. */
    protected int level = 0;
    /** Unmodified base stats used as a reference when resetting or recalculating stats. */
    private final Stats baseStats;
    /** The character type (e.g. ARCHER, WARRIOR, WIZARD). This determines specific abilities or behaviors. */
    private final CharacterType type;
    /** Flag indicating whether the next attack will be a powerful attack. This is set to {@code true} if the character took
     * zero damage from the opponent's previous attack. */
    private boolean powerfulAttack = false;
    /** Listener notified of character events, such as a powerful attack being triggered. */
    private CharacterEventListener listener;

    /** Constructor
     * @param name  the name of the character; usually coincide with the type.
     * @param stats the base stats of the character (cloned internally)
     * @param type  the character type
     * @see model.core.Stats
     * @see model.core.CharacterType */
    protected CharacterModel(String name, Stats stats, CharacterType type) {
        this.name = name;
        this.stats = stats.clone();
        this.baseStats = stats.clone();
        this.type = type;
    }

    /* --- Methods --- */
    /** Print all the fields of a character. */
    public void printCharacter(){
        System.out.println("Name: " + this.name);
        this.stats.printStats();
        System.out.println("Level: " + Difficulty.fromLevel(this.level));
    }

    /** Inflicts an attack against a target character. Damage calculation incorporates the "Powerful Attack" state
     * and the speed ratio between the combatants.
     * @param target    the character being attacked.
     * @return the actual damage the CharacterModel inflicts against another CharacterModel. It does it calling another method.
     * @see #takeDamage(int) */
    public int attack(CharacterModel target){
        /* Compute the ratio between the speed of the two CharacterModel. */
        int baseDamage;
        if(this.powerfulAttack) {
            baseDamage = (int) (this.stats.getAttackPower() * 1.4);
            if(listener != null)
                listener.onAttack(this, this.name + " performs a powerful attack!");
            this.powerfulAttack = false;
        } else
            baseDamage = this.stats.getAttackPower();

        int damage = determineDamageBasedOnSpeed(target, baseDamage);
        if (damage == 0)
            target.powerfulAttack = true;

        return target.takeDamage(damage);
    }

    /** Applies damage to the character. If damage is blocked by defense, no health is lost and the next attack
     * becomes powerful. Otherwise, real damage is applied and defense is reduced by 10%.
     * @param damage the incoming damage value.
     * @return the real damage taken (never negative).
     * @see #attack(CharacterModel) */
    public int takeDamage(int damage){
        /* This if is used to prevent a character's health from increasing when another character doesn't deal enough
         * damage, or their defense is higher than the other's attack. */
        if(this.stats.getDefense() > damage){
            /* No damage taken --> unlock powerful attack. */
            this.powerfulAttack = true;
            return 0;
        }
        int realDamage = Math.max(0, damage - this.stats.getDefense());
        int h = this.stats.getHealth() - realDamage;                                // this.stats.setHealth(this.stats.getHealth() + this.stats.getDefense() - damage)
        int d = this.stats.getDefense() - (int) (0.1 * this.stats.getDefense());    // this.stats.setDefense(this.stats.getDefense() - (int) (0.1 * this.stats.getDefense()))
        this.stats.setHealth(h);
        this.stats.setDefense(d);
        if(!this.isAlive())
            System.out.println(this.name + " couldn't handle the attack and now is in a more peaceful place.");
        return realDamage;
    }

    /** Checks whether the character is still alive.
     * @return {@code true} if this.health > 0, {@code false} otherwise */
    public boolean isAlive(){
        return (this.stats.getHealth() > 0);
    }

    /** Updates the character's stats according to the current level.
     * Base stats are restored, then modified using the multipliers stored in {@link Difficulty}. */
    public void updateStatsFromLevel() {
        this.stats = baseStats.clone(); /* reset */
        Difficulty diff = Difficulty.fromLevel(this.level);
        this.stats.incrementStats(
                diff.getHealthMultiplier(),
                diff.getAttackMultiplier(),
                diff.getDefenseMultiplier(),
                diff.getSpeedAdder()
        );
    }

    /** Determines the final damage to an attack based on the speed ratio between the attacker and the target.
     * The logic uses the relative speed of both characters to decide whether the attack lands:
     * - If the attacker's speed is equal or higher than the target's → the hit is guaranteed.
     * - If the attacker's speed is lower → the hit succeeds with probability {@code attackerSpeed / targetSpeed}.
     * - If the hit fails → the resulting damage is {@code 0}.
     * @param target     the {@link CharacterModel} receiving the attack.
     * @param baseDamage the amount of damage to apply if the attack succeeds.
     * @return the actual damage inflicted: {@code baseDamage} if the hit succeeds, {@code 0} otherwise. */
    private int determineDamageBasedOnSpeed(CharacterModel target, int baseDamage) {
        int playerSpeed = this.stats.getSpeed();
        int targetSpeed = target.stats.getSpeed();
        float odds = (float) playerSpeed / targetSpeed;
        float random = (float) Math.random();
        if (odds >= 1f || odds >= random)
            return baseDamage;
        return 0;
    }

    /** Registers a listener to monitor property changes in the character's {@code stats} object. */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        stats.addPropertyChangeListener(listener);
    }

    /** Removes a previously registered property change listener from the character's {@code stats} object. */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        stats.removePropertyChangeListener(listener);
    }

    /* --- Getters --- */
    /** Getter method.
     * @return the character's name. */
    public String getName(){
        return this.name;
    }
    /** Getter method.
     * @return the character's current stats. */
    public Stats getStats(){
        return this.stats;
    }
    /** Getter method.
     * @return the character's current level (0–4). */
    public int getLevel(){
        return this.level;
    }
    /** Getter method.
     * @return the character's type. */
    public CharacterType getType(){
        return type;
    }
    /** Getter method.
     * @return the character's base stats. */
    public Stats getBaseStats(){
        return this.baseStats;
    }

    /* --- Setters --- */
    /** Setter method.
     * @param name the new name. */
    public void setName(String name){
        this.name = name;
    }
    /** Setter method.
     * @param level the new level. */
    public void setLevel(int level){
        if(level < 0)
            level = 0;
        if(level > 4)
            level = 4;
        this.level = level;
        updateStatsFromLevel();
    }
    /** Setter method.
     * @param listener The {@link CharacterEventListener} instance. */
    public void setListener(CharacterEventListener listener) {
        this.listener = listener;
    }
}
