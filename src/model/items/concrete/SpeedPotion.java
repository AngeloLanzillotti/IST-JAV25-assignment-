package model.items.concrete;

import model.core.*;
import model.entity.base.PlayerCharacter;
import model.items.base.Item;
import model.items.base.Potion;
import controller.eventListeners.EquipmentEventListener;
import controller.system.MessageController;

import java.awt.*;

/** Represents a Speed Potion that can be used by a {@link PlayerCharacter}.
 * <p>
 *     Using this potion increases the player's speed by a fixed amount. The effect can be removed by calling
 *     {@link #unuse(PlayerCharacter)}.
 * </p>
 * @see Potion
 */
public class SpeedPotion extends Potion {
    /* --- Fields --- */
    /** The bonus speed granted by this potion. */
    private final int speedBonus;
    /** A custom listener used to handle equipment-related events for this SpeedPotion. */
    private final EquipmentEventListener customListener;

    /** Constructor
     * @param speedBonus the amount of speed added when used. */
    public SpeedPotion(int speedBonus) {
        this.speedBonus = speedBonus;
        super("Speed Potion", speedBonus);
        this.customListener = new EquipmentEventListener() {
            @Override
            public void onEquipped(PlayerCharacter character, Item item) {
                MessageController.getInstance().showMessage(character.getName() + " increased their speed by +" + speedBonus + "!", 2000, Color.darkGray);
            }
        };
    }

    /* --- Getters --- */
    /** Getter method.
     * @return The speed bonus value. */
    public int getSpeedBonus() {
        return speedBonus;
    }

    /* --- Item's interface methods --- */
    /** Uses the Speed Potion on the given player, increasing their speed, up to a maximum cap.
     * @param player the {@link PlayerCharacter} using the potion. */
    @Override
    public void use(PlayerCharacter player) {
        Stats stats = player.getStats();
        /* Guarantee speed does not exceed maximum of 10 */
        int newSpeed = stats.getSpeed() + this.speedBonus;
        int maxSpeed = 10;
        stats.setSpeed(Math.min(newSpeed, maxSpeed));
        /* Notify listener */
        if(customListener != null)
            customListener.onEquipped(player, this);
    }

    /** Removes the effect of the Speed Potion from the player.
     * @param player the {@link PlayerCharacter} whose potion effect is reverted. */
    @Override
    public void unuse(PlayerCharacter player) {
        Stats stats = player.getStats();
        stats.setSpeed(stats.getSpeed() - this.speedBonus);
        /* Notify listener */
        if(customListener != null)
            customListener.onUnequipped(player, this);
    }

    /** Returns the type of item this is.
     * @return the {@link ItemType} for SpeedPotion. */
    @Override
    public ItemType getType() {
        return ItemType.SPEED_POTION;
    }
}
