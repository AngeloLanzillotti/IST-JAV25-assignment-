package model.entity.concrete;


import model.core.CharacterType;
import model.core.Stats;
import model.entity.base.Enemy;

/** Represents an Orc-type enemy.
 * <p>
 *     Orcs are powerful and resilient creatures with strong attack values but low speed. This class extends {@link Enemy}
 *     and initializes an Orc with its predefined default statistics and specific {@link CharacterType}.
 * </p> */
public class Orc extends Enemy {
    /** Constructor
     * @param name the name of the Orc character. */
    public Orc(String name){
        super(name, new Stats(2500, 600, 200, 2), CharacterType.ORC);
    }
}
