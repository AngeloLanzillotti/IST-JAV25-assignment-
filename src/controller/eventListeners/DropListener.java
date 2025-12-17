package controller.eventListeners;

import model.items.base.Item;

import javax.swing.*;

/** Interface defining the contract for objects that manage and respond to item drop events within the graphical user interface,
 * typically involving equipment or inventory management.
 * <p>
 *     Implementing classes are responsible for executing the business logic associated with equipping or unequipping an
 *     item, based on the drop source and target.
 * </p>
 * @see Item
 * @see JPanel */
public interface DropListener {
    /** Processes the dropping of an {@link Item}, executing the logic for equipping, unequipping, or transferring
     * the item between slots.
     * @param <T> The specific type of {@link Item} being handled.
     * @param panelType The category or type identifier of the item (e.g., "Armor", "Weapon", "Potion").
     * @param target The destination graphical slot (represented by {@code JPanel}) where the item was dropped.
     *               This parameter is {@code null} if the item was deliberately removed or unequipped from an existing slot.
     * @param item The specific {@link Item} instance that was dropped. */
    <T extends Item> void handleItemDrop(String panelType, JPanel target, T item);
}
