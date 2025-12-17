package model.items.base;

import model.entity.base.PlayerCharacter;
import controller.system.MessageController;

import java.awt.*;

/** Abstract base class representing a Potion item that can be used by a {@link PlayerCharacter}.
 * <p>
 *     Potions have a name and a power value. Using a potion applies its effect to the player. By default, most potions
 *     cannot have their effect removed once used, but subclasses can override {@link #unuse(PlayerCharacter)}
 *     if needed.
 * </p>
 * @see Item */
public abstract class Potion implements Item{
    /* --- Fields --- */
    /** The name of the potion. */
    protected String name;
    /** The power value of the potion, representing its effect magnitude. */
    protected int power;

    /** Constructor
     * @param name  the name of the potion.
     * @param power the effect strength of the potion. */
    public Potion(String name, int power){
        this.name = name;
        this.power = power;
    }

    /* --- Item's interface methods --- */
    /** Uses this potion on the given player. This default implementation prints a message. Subclasses should override
     * this method to apply specific effects (e.g., restore health, increase speed).
     * @param target the {@link PlayerCharacter} using the potion. */
    @Override
    public void use(PlayerCharacter target){
        MessageController.getInstance().showMessage("You drink " + this.name + " potion.", 2000, Color.darkGray);
    }

    /** Removes the effect of this potion from the given player. By default, potions cannot be “unused” once drunk, but
     * subclasses may override this behavior.
     * @param target the {@link PlayerCharacter} whose potion effect is removed. */
    @Override
    public void unuse(PlayerCharacter target) {
        MessageController.getInstance().showMessage("You can't unuse this potion once drunk.", 2000, Color.darkGray);
    }

    /* --- Getters --- */
    /** Getter method.
     * @return the potion's name. */
    public String getName(){
        return this.name;
    }
    /** Getter method.
     * @return the potion's power value. */
    public int getPower(){
        return this.power;
    }

    /* Setters */
    /** Setter method.
     * @param name the new name of the potion. */
    public void setName(String name){
        this.name = name;
    }
    /** Setter method.
     * @param power the new power value. */
    public void setPower(int power){
        this.power = power;
    }
}
