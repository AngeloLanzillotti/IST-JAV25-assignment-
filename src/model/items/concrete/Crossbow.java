package model.items.concrete;

import model.core.*;
import model.entity.base.PlayerCharacter;
import model.items.base.Equipable;
import model.items.base.Item;
import controller.eventListeners.EquipmentEventListener;
import controller.system.MessageController;

import java.awt.*;

/** Represents a Crossbow weapon that can be equipped by a {@link PlayerCharacter}.
 * <p>
 *     Equipping a Crossbow increases the player's attack power. Only {@link CharacterType#ARCHER} characters are allowed
 *     to equip this item.
 * </p>
 * @see Equipable */
public class Crossbow implements Equipable {
    /* --- Fields --- */
    /** The bonus attack points granted by this Crossbow. */
    private final int attackPowerBonus;
    /** A custom listener used to handle equipment-related events for this Crossbow. */
    private final EquipmentEventListener customListener;

    /** Constructor
     * @param attackPowerBonus the number of attack points added when equipped. */
    public Crossbow(int attackPowerBonus) {
        this.attackPowerBonus = attackPowerBonus;
        this.customListener = new EquipmentEventListener() {
            @Override
            public void onEquipped(PlayerCharacter character, Item item) {
                MessageController.getInstance().showMessage(character.getName() + " equipped a crossbow with +" + attackPowerBonus + " attack!", 2000, Color.darkGray);
            }
        };
    }

    /* --- Getters --- */
    /** Getter method.
     * @return The attack power bonus value. */
    public int getAttackPowerBonus(){
        return attackPowerBonus;
    }

    /* --- Equipable's interface methods --- */
    /** Equips this Crossbow to the given player, increasing attack power.
     * @param player the {@link PlayerCharacter} who equips this Crossbow. */
    @Override
    public void equipItem(PlayerCharacter player) {
        if (!canBeEquipped(player))
            return;
        Stats stats = player.getStats();
        stats.setAttackPower(stats.getAttackPower() + this.attackPowerBonus);
        /* Notify listener */
        if(customListener != null)
            customListener.onEquipped(player, this);
    }

    /** Unequips this Crossbow from the given player, removing its attack bonus.
     * @param player the {@link PlayerCharacter} who unequips this Crossbow. */
    @Override
    public void unequipItem(PlayerCharacter player) {
        Stats stats = player.getStats();
        stats.setAttackPower(stats.getAttackPower() - this.attackPowerBonus);
        /* Notify listener */
        if(customListener != null)
            customListener.onUnequipped(player, this);
    }

    /* --- Item's interface method --- */
    /** Checks whether this Crossbow can be equipped by the given player. Only {@link CharacterType#ARCHER} can equip a Crossbow.
     * @param player the {@link PlayerCharacter} to check.
     * @return {@code true} if the player is an Archer, {@code false} otherwise. */
    @Override
    public boolean canBeEquipped(PlayerCharacter player) {
        return player.getType() == CharacterType.ARCHER;
    }

    /** Returns a reason why the player cannot equip this crossbow.
     * @param player the {@link PlayerCharacter} attempting to equip the crossbow.
     * @return a human-readable reason why the crossbow cannot be equipped. */
    @Override
    public String getEquipFailReason(PlayerCharacter player) {
        return player.getName() + " can't equip the crossbow. It's not: ARCHER";
    }

    /** Returns the type of item this is.
     * @return the {@link ItemType} for Crossbow. */
    @Override
    public ItemType getType() {
        return ItemType.CROSSBOW;
    }
}
