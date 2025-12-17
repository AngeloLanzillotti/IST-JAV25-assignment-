package model.core;

/** Enumeration defining the different categories of items that can exist and be used within the game, particularly those
 * that can be equipped or consumed by a character. */
public enum ItemType {
    /** Represents protective gear, typically providing defense bonuses. */
    ARMOR,
    /** Represents a defensive item, typically providing defense or blocking capabilities. */
    SHIELD,
    /** Represents a consumable item used to restore health. */
    HEALTH_POTION,
    /** Represents a consumable item used to increase speed attributes temporarily. */
    SPEED_POTION,
    /** Represents a melee weapon, typically used by Warriors. */
    SWORD,
    /** Represents a ranged weapon, typically used by Archers. */
    CROSSBOW,
    /** Represents a specialized, powerful consumable potion, typically associated with Wizards. */
    POWERFUL_POTION
}
