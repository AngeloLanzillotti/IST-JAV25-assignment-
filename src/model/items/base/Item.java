package model.items.base;

import model.core.ItemType;
import model.entity.base.PlayerCharacter;

/** Represents a generic item that can be used or equipped by a {@link PlayerCharacter}.
 * <p>
 *     Items may apply effects to the player (such as modifying stats, providing bonuses, or granting temporary abilities).
 *     Some items may also be unequipped to remove their effects.
 * </p> */
public interface Item {
    /* --- Methods --- */
    /** Applies the item's effect to the given player.
     * @param player    the player who uses the item. */
    void use(PlayerCharacter player);
    /** Removes the item's effect from the given player.
     * @param player    the player who unequips or stops using the item. */
    void unuse(PlayerCharacter player);

    /** Determines whether the given player can equip this item. By default, all player types can equip any item.
     * Implementations may override this method to restrict equipment based on class type (e.g. only Warriors can equip heavy armor).
     * @param player the player attempting to equip the item.
     * @return {@code true} if the item can be equipped, {@code false} otherwise. */
    default boolean canBeEquipped(PlayerCharacter player){
        return true;
    }

    /** Returns the reason why the given player cannot equip this item. This method is used when {@link #canBeEquipped(PlayerCharacter)}
     * returns {@code false}. By default, it returns {@code null}, meaning no specific failure message is provided.
     * Implementations may override this method to supply detailed explanations (e.g. "Only mages can equip magical staffs").
     * @param player the player who attempted to equip the item.
     * @return a string describing the reason for the failed equip attempt, or an empty string ({@code ""}) if no message is defined. */
    default String getEquipFailReason(PlayerCharacter player) {
        return "";
    }

    /** Returns the type of item this is.
     * @return the {@link ItemType} identifying the type of item. */
    ItemType getType();
}
