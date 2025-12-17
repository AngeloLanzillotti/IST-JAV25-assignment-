package model.core;

/** Enumeration representing all the character types available in the RPG game.
 * <p>
 *     This enum includes both PlayerCharacter types (ARCHER, WARRIOR, WIZARD) and Enemy types (DRAGON, GOBLIN, ORC).
 *     It is used to classify each character and determine their behavior, abilities, or interactions in the game.
 * </p>
 * @see model.entity.base.CharacterModel
 * @see model.entity.base.PlayerCharacter
 * @see model.entity.base.Enemy */
public enum CharacterType {
    /** PlayerCharacter */
    WIZARD,
    /** PlayerCharacter */
    ARCHER,
    /** PlayerCharacter */
    WARRIOR,
    /** Enemy */
    ORC,
    /** Enemy */
    GOBLIN,
    /** Enemy */
    DRAGON
}
