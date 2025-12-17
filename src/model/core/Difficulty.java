package model.core;

/** Represents the predefined difficulty levels available in the game. Each difficulty applies specific multipliers to
 * {@link model.entity.base.CharacterModel} statistics, allowing for the dynamic scaling of health, attack power, defense, and speed.
 * <p>
 *     The difficulty levels are ordered by an integer {@code level}, which can be retrieved or used to reconstruct a
 *     difficulty setting via {@link #fromLevel(int)}.
 * </p> */
public enum Difficulty {
    /** Default difficulty (level 0). Stats are unchanged. */
    DEFAULT(0, 1.0, 1.0, 1.0, 0),
    /** Easy difficulty (level 1). Stats are lightly increased. */
    EASY(1, 1.4, 1.4, 1.4, 1),
    /** Medium difficulty (level 2). Balanced stat increase. */
    MEDIUM(2, 1.7, 1.7, 1.7, 2),
    /** Hard difficulty (level 3). Strong stat increase. */
    HARD(3, 2.0, 2.0, 2.0, 3),
    /** Supersaiyan difficulty (level 4). Maximum enemy power scaling. */
    SUPERSAIYAN(4, 2.5, 2.5, 2.5, 4);

    /* --- Fields --- */
    /** The level associated with this difficulty setting. */
    private final int level;
    /** Multiplier applied to the base health of a character. Determines how much the health increases with this
     * difficulty level. */
    private final double healthMultiplier;
    /** Multiplier applied to the base attackPower stat. Higher values result in stronger offensive capabilities. */
    private final double attackMultiplier;
    /** Multiplier applied to the base defense stat. Affects how much incoming damage is reduced. */
    private final double defenseMultiplier;
    /** Flat speed bonus added to the base speed value. Unlike the other stats, speed scales additively instead of multiplicatively. */
    private final int speedAdder;

    /** Constructor
     * @param level             numeric value identifying the difficulty.
     * @param healthMultiplier  multiplier applied to base health.
     * @param attackMultiplier  multiplier applied to base attackPower.
     * @param defenseMultiplier multiplier applied to base defense.
     * @param speedAdder        amount added to base speed. */
    Difficulty(int level, double healthMultiplier, double attackMultiplier, double defenseMultiplier, int speedAdder) {
        this.level = level;
        this.healthMultiplier = healthMultiplier;
        this.attackMultiplier = attackMultiplier;
        this.defenseMultiplier = defenseMultiplier;
        this.speedAdder = speedAdder;
    }

    /* --- Methods --- */
    /** Retrieves a {@code Difficulty} based on its numeric level.
     * @param level the difficulty level as an integer.
     * @return the corresponding {@code Difficulty}, or {@link #DEFAULT} if none match. */
    public static Difficulty fromLevel(int level) {
        for (Difficulty d : values())
            if (d.level == level)
                return d;
        return DEFAULT;
    }

    /* --- Getters --- */
    /** Getter method.
     * @return the numeric level representing this difficulty. */
    public int getLevel() {
        return this.level;
    }
    /** Getter method.
     * @return multiplier applied to the health stat. */
    public double getHealthMultiplier() {
        return this.healthMultiplier;
    }
    /** Getter method.
     * @return multiplier applied to the attack stat. */
    public double getAttackMultiplier() {
        return this.attackMultiplier;
    }
    /** Getter method.
     * @return multiplier applied to the defense stat. */
    public double getDefenseMultiplier() {
        return this.defenseMultiplier;
    }
    /** Getter method.
     * @return value added to the base speed stat. */
    public int getSpeedAdder() {
        return this.speedAdder;
    }
}
