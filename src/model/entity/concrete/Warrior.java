package model.entity.concrete;

import controller.eventListeners.EquipmentEventListener;
import model.core.CharacterType;
import model.core.Stats;
import model.entity.base.PlayerCharacter;
import model.items.concrete.Sword;

/** Represents a Warrior-type playable character.
 * <p>
 *     Warriors are strong melee fighters with high health and defense. This class extends {@link PlayerCharacter} and
 *     adds the ability to equip and unequip a {@link Sword}.
 * </p> */
public class Warrior extends PlayerCharacter {
    /* --- Fields --- */
    /** The sword currently equipped by the Warrior. It is {@code null} if the Warrior has no sword equipped. */
    private Sword equippedSword = null;

    /** Constructor
     * @param name the name of the Warrior character. */
    public Warrior(String name){
        super(name, new Stats(1500, 350, 200, 4), CharacterType.WARRIOR);
        setEquipmentListener(new EquipmentEventListener(){});
    }

    /* --- Methods --- */
    /** Equips a sword to the Warrior. If the Warrior already has a sword equipped, that sword is first unequipped. Then
     * the new sword is equipped by calling its {@link Sword#use} method.
     * @param sword     the sword the Warrior wants to equip. */
    public void equipSword(Sword sword){
        if(equippedSword != null)
            sword.unuse(this);
        equippedSword = sword;
        sword.use(this);
        /* Notify listener */
        if (getEquipmentListener() != null)
            getEquipmentListener().onEquipped(this, sword);
    }
    /** Unequips the given sword from the Warrior. The sword is removed only if it is the one currently equipped. No
     * action is performed otherwise.
     * @param sword     the sword to remove from the Warrior. */
    public void unequipSword(Sword sword){
        if(equippedSword == sword){
            equippedSword.unuse(this);
            equippedSword = null;
            /* Notify listener */
            if (getEquipmentListener() != null)
                getEquipmentListener().onUnequipped(this, sword);
        }
    }

    /* --- Getters --- */
    /** Getter method.
     * @return the equipped {@link Sword}, or {@code null} if none is equipped. */
    public Sword getEquippedSword() {
        return this.equippedSword;
    }
}
