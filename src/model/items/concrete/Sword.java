package model.items.concrete;

import model.core.*;
import model.entity.base.PlayerCharacter;
import model.items.base.Equipable;
import model.items.base.Item;
import controller.eventListeners.EquipmentEventListener;
import controller.system.MessageController;

import java.awt.*;

/** Represents a Sword that can be equipped by a {@link PlayerCharacter}.
 * <p>
 *     Equipping the sword increases the player's attack power. Only {@link CharacterType#WARRIOR} can equip this item.
 * </p>
 * @see Equipable */
public class Sword implements Equipable {
    /* --- Fields --- */
    /** The bonus attack power granted by this sword. */
    private final int attackPowerBonus;
    /** A custom listener used to handle equipment-related events for this Sword. */
    private final EquipmentEventListener customListener;

    /** Constructor
     * @param attackPowerBonus the amount of attack power added when equipped. */
    public Sword(int attackPowerBonus){
        this.attackPowerBonus = attackPowerBonus;
        this.customListener = new EquipmentEventListener() {
            @Override
            public void onEquipped(PlayerCharacter character, Item item) {
                MessageController.getInstance().showMessage(character.getName() + " equipped a sword with +" + attackPowerBonus + " attack!", 2000, Color.darkGray);
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
    /** Equips the sword to the given player, increasing their attackPower.
     * @param player the {@link PlayerCharacter} equipping the sword. */
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

    /** Unequips the sword from the given player, removing its attackPowerBonus.
     * @param player the {@link PlayerCharacter} unequipping the sword. */
    @Override
    public void unequipItem(PlayerCharacter player) {
        Stats stats = player.getStats();
        stats.setAttackPower(stats.getAttackPower() - this.attackPowerBonus);
        /* Notify listener */
        if(customListener != null)
            customListener.onUnequipped(player, this);
    }

    /* --- Item's interface methods --- */
    /** Determines if this sword can be equipped by the given player.
     * @param player the {@link PlayerCharacter} to check.
     * @return true if the player is a {@link CharacterType#WARRIOR}, false otherwise. */
    @Override
    public boolean canBeEquipped(PlayerCharacter player) {
        return player.getType() == CharacterType.WARRIOR;
    }

    /** Returns a reason why the player cannot equip this sword.
     * @param player the {@link PlayerCharacter} attempting to equip the sword.
     * @return a human-readable reason why the sword cannot be equipped. */
    @Override
    public String getEquipFailReason(PlayerCharacter player) {
        return player.getName() + " can't equip the sword. It's not: WARRIOR";
    }

    /** Returns the type of item this is.
     * @return the {@link ItemType} for Sword. */
    @Override
    public ItemType getType() {
        return ItemType.SWORD;
    }
}
