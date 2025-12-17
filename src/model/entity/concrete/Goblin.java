package model.entity.concrete;

import model.core.CharacterType;
import model.core.Stats;
import model.entity.base.Enemy;

/** Represents a Goblin-type enemy.
 * <p>
 *     Goblins are typically fast but relatively weak creatures. This class extends {@link Enemy} and initializes a
 *     Goblin with predefined default statistics and its specific {@link CharacterType}.
 * </p> */
public class Goblin extends Enemy {
    /** Constructor
     * @param name the name of the Goblin character. */
    public Goblin(String name){
        super(name, new Stats(600, 150, 50, 7), CharacterType.GOBLIN);
    }
}
