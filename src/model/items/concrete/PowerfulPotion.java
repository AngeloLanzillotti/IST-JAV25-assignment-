package model.items.concrete;

import model.core.*;
import model.entity.base.PlayerCharacter;
import model.items.base.Potion;
import model.items.base.Item;
import controller.eventListeners.EquipmentEventListener;
import controller.system.MessageController;

import java.awt.*;

/** Represents a Powerful Potion that can be used by a {@link PlayerCharacter}.
 * <p>
 *     Using this potion increases the player's attack power. Only {@link CharacterType#WIZARD} characters are allowed
 *     to use this potion.
 * </p>
 * @see Potion */
public class PowerfulPotion extends Potion{
    /* --- Fields --- */
    /** The bonus attack points granted by this potion. */
    private final int attackPowerBonus;
    /** A custom listener used to handle equipment-related events for this PowerfulPotion. */
    private final EquipmentEventListener customListener;

    /** Creates a new PowerfulPotion with a specified attack power bonus.
     * @param attackPowerBonus the number of attack points added when used. */
    public PowerfulPotion(int attackPowerBonus) {
        this.attackPowerBonus = attackPowerBonus;
        super("Powerful Potion", attackPowerBonus);
        this.customListener = new EquipmentEventListener() {
            @Override
            public void onEquipped(PlayerCharacter character, Item item) {
                MessageController.getInstance().showMessage(character.getName() + " drank a powerful potion, gaining + " + attackPowerBonus + " attack!", 2000, Color.darkGray);
            }
        };
    }

    /* --- Getters --- */
    /** Getter method.
     * @return The attack power bonus value. */
    public int getAttackPowerBonus(){
        return attackPowerBonus;
    }

    /* --- Item's interface methods --- */
    /** Uses the PowerfulPotion on the given player, increasing attack power.
     * @param player the {@link PlayerCharacter} who uses this potion. */
    @Override
    public void use(PlayerCharacter player) {
        if (!canBeEquipped(player))
            return;
        Stats stats = player.getStats();
        stats.setAttackPower(stats.getAttackPower() + this.attackPowerBonus);
        /* Notify listener */
        if(customListener != null)
            customListener.onEquipped(player, this);
    }

    /** Reverts the effect of the PowerfulPotion on the given player. The attack bonus granted by the potion is removed.
     * @param player the {@link PlayerCharacter} whose potion effects are removed. */
    @Override
    public void unuse(PlayerCharacter player) {
        Stats stats = player.getStats();
        stats.setAttackPower(stats.getAttackPower() - this.attackPowerBonus);
        /* Notify listener */
        if(customListener != null)
            customListener.onUnequipped(player, this);
    }

    /** Checks whether this potion can be used by the given player. Only {@link CharacterType#WIZARD} can use a PowerfulPotion.
     * @param player the {@link PlayerCharacter} to check.
     * @return {@code true} if the player is a Wizard, {@code false} otherwise. */
    @Override
    public boolean canBeEquipped(PlayerCharacter player) {
        return player.getType() == CharacterType.WIZARD;
    }

    /** Returns a reason why the player cannot drink this powerful potion.
     * @param player the {@link PlayerCharacter} attempting to drink the powerful potion.
     * @return a human-readable reason why the powerful potion cannot be used. */
    @Override
    public String getEquipFailReason(PlayerCharacter player) {
        return player.getName() + " can't drink the PowerfulPotion. It's not: WIZARD";
    }

    /** Returns the type of item this is.
     * @return the {@link ItemType} for PowerfulPotion. */
    @Override
    public ItemType getType() {
        return ItemType.POWERFUL_POTION;
    }
}
