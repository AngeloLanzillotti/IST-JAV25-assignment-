package model.items.concrete;

import model.core.*;
import model.entity.base.PlayerCharacter;
import model.items.base.Equipable;
import model.items.base.Item;
import controller.eventListeners.EquipmentEventListener;
import controller.system.MessageController;

import java.awt.*;

/** Represents a Shield that can be equipped by a {@link PlayerCharacter}.
 * <p>
 *     Equipping the shield increases the player's defense by a fixed amount. Unequipping the shield removes the bonus defense.
 * </p>
 * @see Equipable */
public class Shield implements Equipable {
    /* --- Fields --- */
    /** The bonus defense points granted by this shield. */
    private final int defenseBonus;
    /** A custom listener used to handle equipment-related events for this Shield. */
    private final EquipmentEventListener customListener;

    /** Constructor
     * @param defenseBonus the number of defense points added when equipped. */
    public Shield(int defenseBonus) {
        this.defenseBonus = defenseBonus;
        this.customListener = new EquipmentEventListener() {
            @Override
            public void onEquipped(PlayerCharacter character, Item item) {
                MessageController.getInstance().showMessage(character.getName() + " equipped a shield with +" + defenseBonus + " defense!", 2000, Color.darkGray);
            }
        };
    }

    /* --- Getters --- */
    /** Getter method.
     * @return The defense bonus value. */
    public int getDefenseBonus(){
        return defenseBonus;
    }

    /* --- Equipable's interface methods --- */
    /** Equips the shield to the given player, increasing their defense.
     * @param player the {@link PlayerCharacter} equipping the shield. */
    @Override
    public void equipItem(PlayerCharacter player) {
        Stats stats = player.getStats();
        stats.setDefense(stats.getDefense() + this.defenseBonus);
        /* Notify listener */
        if(customListener != null)
            customListener.onEquipped(player, this);
    }

    /** Unequips the shield from the given player, removing its defense bonus.
     * @param player the {@link PlayerCharacter} unequipping the shield.*/
    @Override
    public void unequipItem(PlayerCharacter player) {
        Stats stats = player.getStats();
        stats.setDefense(stats.getDefense() - this.defenseBonus);
        /* Notify listener */
        if(customListener != null)
            customListener.onUnequipped(player, this);
    }

    /** Returns the type of item this is.
     * @return the {@link ItemType} for Shield. */
    @Override
    public ItemType getType() {
        return ItemType.SHIELD;
    }
}
