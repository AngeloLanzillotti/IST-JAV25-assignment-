package model.entity.base;

/** Represents a non-playable enemy character in the game.
 * <p>
 *     An {@code Enemy} is a type of {@link CharacterModel} used to generate opponents that the player can fight against.
 *     Unlike {@link PlayerCharacter}, enemies do not gain experience, cannot equip items, and generally have simpler
 *     behavior and fixed stats.
 * </p> <p>
 *     This class is abstract because each specific enemy type (e.g., Orc, Goblin, Dragon) provides its own unique
 *     characteristics or combat behavior.
 * </p> */
public abstract class Enemy extends CharacterModel {
    /** Constructor
     * @param name          the enemy's name.
     * @param defaultStats  the base stats assigned to the enemy.
     * @param type          the enemy’s character type (e.g. DRAGON, GOBLIN, ORC). */
    public Enemy(String name, model.core.Stats defaultStats, model.core.CharacterType type) {
        super(name, defaultStats, type);
    }

    /* --- Methods --- */
    /* No additional methods yet, but subclasses may implement enemy-specific behavior. */
}
