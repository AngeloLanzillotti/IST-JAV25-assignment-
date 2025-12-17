package model.entity.concrete;

import controller.eventListeners.EquipmentEventListener;
import model.core.CharacterType;
import model.core.Stats;
import model.entity.base.PlayerCharacter;
import model.items.concrete.PowerfulPotion;

/** Represents a Wizard-type playable character.
 * <p>
 *     Wizards specialize in magic attacks and rely on agility and spell power. This class extends {@link PlayerCharacter}
 *     and supports the ability to use a {@link PowerfulPotion}, which temporarily enhances their abilities.
 * </p> */
public class Wizard extends PlayerCharacter {
    /* --- Fields --- */
    /** The potion currently used by the Wizard. It is {@code null} if no potion is active. */
    private PowerfulPotion equippedPowerfulPotion = null;

    /** Constructor
     * @param name the name of the Wizard character. */
    public Wizard(String name){
        super(name, new Stats(1200, 350, 100, 6), CharacterType.WIZARD);
        setEquipmentListener(new EquipmentEventListener() {});
    }

    /* --- Methods --- */
    /** Uses a {@link PowerfulPotion} on the Wizard. If a potion is already active, its effects are first removed. Then
     * the new potion is applied by calling {@link PowerfulPotion#use}.
     * @param potion    the potion the Wizard wants to use. */
    public void usePowerfulPotion(PowerfulPotion potion){
        if(equippedPowerfulPotion != null)
            equippedPowerfulPotion.unuse(this);
        equippedPowerfulPotion = potion;
        potion.use(this);
        /* Notify listener */
        if (getEquipmentListener() != null)
            getEquipmentListener().onEquipped(this, potion);
    }

    /** Removes the effects of the specified {@link PowerfulPotion} from the Wizard. The potion is removed only if it is
     * the currently active one. No action is taken otherwise.
     * @param potion    the potion to deactivate. */
    public void unusePowerfulPotion(PowerfulPotion potion){
        if(equippedPowerfulPotion == potion){
            potion.unuse(this);
            equippedPowerfulPotion = null;
            /* Notify listener */
            if (getEquipmentListener() != null)
                getEquipmentListener().onUnequipped(this, potion);
        }
    }

    /* --- Getters --- */
    /** Getter method.
     * @return the active potion, or {@code null} if none is active. */
    public PowerfulPotion getEquippedPowerfulPotion() {
        return this.equippedPowerfulPotion;
    }
}
