package controller.eventListeners;

import controller.system.MessageController;
import model.entity.base.PlayerCharacter;
import model.items.base.Item;

import java.awt.*;

/** Interface defining callbacks for equipment-related events for {@link PlayerCharacter} instances.
 * <p>
 *     This interface allows the model (PlayerCharacter) to notify observers, such as controllers or views, whenever an
 *     item is equipped, unequipped, or fails to be equipped. Default methods for success events (onEquipped/onUnequipped)
 *     provide no action, while the default failure method (onEquipFailed) uses {@link MessageController} to display the error message.
 * </p> <p>
 *     The default methods provided are:
 *     1. {@link #onEquipped(PlayerCharacter, Item)} — called when an item is successfully equipped.
 *     2. {@link #onUnequipped(PlayerCharacter, Item)} — called when an item is successfully unequipped.
 *     3. {@link #onEquipFailed(String)} — called when an attempt to equip an item fails.
 * </p>
 * Implementing classes can override any of these methods to customize the behavior of equipment notifications,
 * or rely on the default behavior which displays messages using the {@link MessageController}.
 * @see PlayerCharacter
 * @see Item
 * @see MessageController */
public interface EquipmentEventListener {
    /* I don't need to print on the window, just onto the terminal */
    /** Called when a player character successfully equips an item.
     * @param character the {@link PlayerCharacter} equipping the item.
     * @param item      the {@link Item} that was equipped. */
    default void onEquipped(PlayerCharacter character, Item item) {}

    /** Called when a player character successfully unequips an item.
     * @param character the {@link PlayerCharacter} unequipping the item.
     * @param item      the {@link Item} that was unequipped. */
    default void onUnequipped(PlayerCharacter character, Item item) {}

    /** Called when a player character fails to equip an item.
     * @param reason a message explaining why the equip operation failed. */
    default void onEquipFailed(String reason){
        MessageController.getInstance().showMessage(reason, 2000, Color.gray);
    }
}
