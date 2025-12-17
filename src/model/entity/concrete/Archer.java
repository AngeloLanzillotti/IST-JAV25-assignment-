package model.entity.concrete;

import controller.eventListeners.EquipmentEventListener;
import model.core.CharacterType;
import model.core.Stats;
import model.entity.base.PlayerCharacter;
import model.items.concrete.Crossbow;

import javax.swing.*;

/** Represents a playable {@link PlayerCharacter} of type ARCHER.
 * <p>
 *     The Archer is a ranged fighter who starts with balanced health, good attack power, and high speed, making them
 *     effective at striking first during combat. This class also manages the logic for equipping and unequipping a
 *     {@link Crossbow}.
 * </p>
 * @see PlayerCharacter
 * @see Crossbow */
public class Archer extends PlayerCharacter {
    /* --- Fields --- */
    /** The crossbow currently equipped by the Archer. May be {@code null} if no weapon is equipped. */
    private Crossbow equippedCrossbow = null;

    /** Constructor
    * @param name the name of the Archer character. */
    public Archer(String name){
        super(name, new Stats(800, 250, 100, 6), CharacterType.ARCHER);
        setEquipmentListener(new EquipmentEventListener()  {});
    }

    /* --- Methods --- */
    /** Equips the given {@link Crossbow} to the Archer. If another Crossbow is already equipped, it is first unequipped
     * automatically.
     * @param crossbow the crossbow to equip. */
    public void equipCrossbow(Crossbow crossbow){
        if(equippedCrossbow != null)
            equippedCrossbow.unuse(this);
        equippedCrossbow = crossbow;
        crossbow.use(this);
        /* Notify listener */
        if (getEquipmentListener() != null)
            getEquipmentListener().onEquipped(this, crossbow);
    }

    /** Unequips the currently equipped {@link Crossbow}, if it matches the given one. If no crossbow is equipped or
     * if the specified crossbow does not match the currently equipped one, the method does nothing.
     * @param crossbow the crossbow to unequip. */
    public void unequipCrossbow(Crossbow crossbow){
        if(equippedCrossbow == crossbow){
            crossbow.unuse(this);
            equippedCrossbow = null;
            /* Notify listener */
            if (getEquipmentListener() != null)
                getEquipmentListener().onUnequipped(this, crossbow);
        }
    }

    /* --- Getters --- */
    /** Getter method.
     * @return the equipped crossbow, or null. */
    public Crossbow getEquippedCrossbow() {
        return this.equippedCrossbow;
    }
}
