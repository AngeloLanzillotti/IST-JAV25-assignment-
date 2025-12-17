package model.items.base;

import model.entity.base.PlayerCharacter;

/** Represents an item that can be equipped by a {@link PlayerCharacter}.
 * <p>
 *     This interface extends {@link Item} and adds specific behavior for equippable objects, such as weapons or armor.
 *     Implementing classes must define how an item is equipped and unequipped.
 * </p> <p>
 *     By default, calling {@link #use(PlayerCharacter)} will equip the item, and calling {@link #unuse(PlayerCharacter)}
 *     will unequip it. This avoids code duplication in all equipment classes.
 * </p> */
public interface Equipable extends Item {
    /** Equips the item, applying its effects to the player.
     * @param player the player equipping the item. */
    void equipItem(PlayerCharacter player);
    /** Unequips the item, removing its effects from the player.
     * @param player the player unequipping the item. */
    void unequipItem(PlayerCharacter player);

    /** Default implementation of {@link Item#use(PlayerCharacter)}. Automatically calls {@link #equipItem(PlayerCharacter)}
     * so that all equipment types share the same default behavior without redefining the method.
     * @param player the player using (equipping) the item. */
    @Override
    default void use(PlayerCharacter player){
        equipItem(player);
    }
    /** Default implementation of {@link Item#unuse(PlayerCharacter)}. Automatically calls {@link #unequipItem(PlayerCharacter)}
     * to provide consistent default behavior for all equippable items.
     * @param player the player stopping the use (unequipping) the item. */
    @Override
    default void unuse(PlayerCharacter player){
        unequipItem(player);
    }
}
