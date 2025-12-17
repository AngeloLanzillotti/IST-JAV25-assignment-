package model.entity.concrete;

import model.core.CharacterType;
import model.core.Stats;
import model.entity.base.Enemy;

/** Represents a Dragon-type enemy.
 * <p>
 *     This class extends {@link Enemy} and initializes a Dragon with its predefined statistics and character type.
 *     Dragons typically have high health and offensive stats, making them strong enemies.
 * </p> */
public class Dragon extends Enemy {
    /** Constructor
     * @param name the name of the Dragon character. */
    public Dragon(String name){
        super(name, new Stats(5000, 400, 50, 2), CharacterType.DRAGON);
    }
}
