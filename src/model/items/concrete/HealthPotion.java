package model.items.concrete;

import model.core.*;
import model.entity.base.PlayerCharacter;
import model.items.base.Item;
import model.items.base.Potion;
import controller.eventListeners.EquipmentEventListener;
import controller.system.MessageController;

import java.awt.*;

/** Represents a Health Potion that can be used by a {@link PlayerCharacter}.
 * <p>
 *     Using a Health Potion restores a certain amount of the player's health. The effect can also be reverted using
 *     {@link #unuse(PlayerCharacter)}.
 * </p>
 * @see Potion */
public class HealthPotion extends Potion {
    /* --- Fields --- */
    /** The amount of health points restored by this potion. */
    private final int healAmount;
    /** A custom listener used to handle equipment-related events for this HealthPotion. */
    private final EquipmentEventListener customListener;

    /** Constructor
     * @param healAmount the number of health points restored when used. */
    public HealthPotion(int healAmount){
        this.healAmount = healAmount;
        super("Health Potion", healAmount);
        this.customListener = new EquipmentEventListener() {
            @Override
            public void onEquipped(PlayerCharacter character, Item item) {
                MessageController.getInstance().showMessage(character.getName() + " increased his health by + " + healAmount + "!", 2000, Color.darkGray);
            }
        };
    }

    /* --- Getters --- */
    /** Getter method.
     * @return The heal amount value. */
    public int getHealAmount() {
        return healAmount;
    }

    /* --- Item's interface methods --- */
    /** Uses the HealthPotion on the given player, restoring health.
     * @param player the {@link PlayerCharacter} who uses this potion. */
    @Override
    public void use(PlayerCharacter player) {
        Stats stats = player.getStats();
        stats.setHealth(stats.getHealth() + this.healAmount);
        /* Notify listener */
        if(customListener != null)
            customListener.onEquipped(player, this);
    }

    /** Reverts the effect of the HealthPotion on the given player. The health restored by the potion is removed.
     * Usually called when unequipping or reversing a potion effect.
     * @param player the {@link PlayerCharacter} whose health increase is reverted. */
    @Override
    public void unuse(PlayerCharacter player) {
        Stats stats = player.getStats();
        stats.setHealth(stats.getHealth() - this.healAmount);
        /* Notify listener */
        if(customListener != null)
            customListener.onUnequipped(player, this);
    }

    /** Returns the type of item this is.
     * @return the {@link ItemType} for HealthPotion. */
    @Override
    public ItemType getType() {
        return ItemType.HEALTH_POTION;
    }
}
