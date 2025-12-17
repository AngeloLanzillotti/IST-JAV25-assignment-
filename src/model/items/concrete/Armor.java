package model.items.concrete;

import model.core.*;
import model.entity.base.CharacterModel;
import model.entity.base.PlayerCharacter;
import model.items.base.Equipable;
import model.items.base.Item;
import controller.eventListeners.EquipmentEventListener;
import controller.system.MessageController;

import java.awt.*;

/** Represents an Armor item that can be equipped by a {@link PlayerCharacter}.
 * <p>
 *     Equipping armor increases the player's defense and decreases speed by 1. Each armor has a minimum required
 *     difficulty level to be equipped.
 * </p>
 * @see Equipable */
public class Armor implements Equipable{
    /* --- Fields --- */
    /** The bonus defense points granted by this armor. */
    private final int defenseBonus;
    /** The minimum difficulty level required to equip this armor. */
    public final Difficulty minDifficulty;
    /** A custom listener used to handle equipment-related events for this Armor. */
    private final EquipmentEventListener equipmentListener;

    /** Constructor
     * @param defenseBonus  the number of defense points added when equipped.
     * @param minLevel      the minimum {@link Difficulty} required to equip the armor. */
    public Armor(int defenseBonus, Difficulty minLevel){
        this.defenseBonus = defenseBonus;
        this.minDifficulty = minLevel;
        this.equipmentListener = new EquipmentEventListener() {
            @Override
            public void onEquipped(PlayerCharacter character, Item item) {
                MessageController.getInstance().showMessage(character.getName() + " equipped armor with + " + defenseBonus + " defense!", 2000, Color.darkGray);
            }
        };
    }

    /* --- Methods --- */
    /** Method that creates an armor suitable for a given character. The armor's minimum difficulty and bonus are determined
     * based on the character type and level.
     * @param player the {@link CharacterModel} that will use the armor.
     * @return a new {@link Armor} instance suitable for the player. */
    public static Armor createForCharacter(CharacterModel player) {
        Difficulty minLevel;
        int baseBonus = 50;
        int extraPerLevel = 50;
        switch (player.getType()) {
            case WARRIOR -> minLevel = Difficulty.EASY;
            case ARCHER -> minLevel = Difficulty.MEDIUM;
            case WIZARD -> minLevel = Difficulty.HARD;
            default -> minLevel = Difficulty.SUPERSAIYAN;
        }
        int bonus = baseBonus + Math.max(0, player.getLevel() - minLevel.getLevel()) * extraPerLevel;
        return new Armor(bonus, minLevel);
    }

    /* --- Getters --- */
    /** Getter method.
     * @return The defense bonus value. */
    public int getDefenseBonus(){
        return defenseBonus;
    }
    /** Getter method.
     * @return The minimum required {@link Difficulty}. */
    public Difficulty getMinDifficulty(){
        return minDifficulty;
    }

    /* --- Equipable's interface methods --- */
    /** Equips this armor to the given player, increasing defense and reducing speed by 1.
     * @param player the {@link PlayerCharacter} who equips this armor. */
    @Override
    public void equipItem(PlayerCharacter player){
        if (!canBeEquipped(player))
            return;
        Stats stats = player.getStats();
        stats.setDefense(stats.getDefense() + this.defenseBonus);
        stats.setSpeed(stats.getSpeed() - 1);
        /* Notify listener */
        if(equipmentListener != null)
            equipmentListener.onEquipped(player, this);
    }

    /** Unequips this armor from the given player, removing its effects. Defense is decreased by the armor's bonus and
     * speed is restored.
     * @param player the {@link PlayerCharacter} who unequips this armor. */
    @Override
    public void unequipItem(PlayerCharacter player){
        Stats stats = player.getStats();
        stats.setDefense(stats.getDefense() - this.defenseBonus);
        stats.setSpeed(stats.getSpeed() + 1);
        /* Notify listener */
        if(equipmentListener != null)
            equipmentListener.onUnequipped(player, this);
    }

    /** Checks whether the player can equip this armor.
     * Returns {@code true} if the player's level is greater than or equal to the armor's minimum difficulty level.
     * @param player the {@link PlayerCharacter} to check.
     * @return {@code true} if equippable, {@code false} otherwise. */
    @Override
    public boolean canBeEquipped(PlayerCharacter player) {
        return player.getLevel() >= this.minDifficulty.getLevel();
    }

    /** Returns a reason why the player cannot equip this armor.
     * @param player the {@link PlayerCharacter} attempting to equip the armor.
     * @return a human-readable reason why the armor cannot be equipped. */
    @Override
    public String getEquipFailReason(PlayerCharacter player) {
        return player.getName() + " can't wear the armor. Required level: " + this.minDifficulty;
    }

    /* Item's interface methods */
    /** Returns the type of item this is.
     * @return the {@link ItemType} for Armor. */
    @Override
    public ItemType getType() {
        return ItemType.ARMOR;
    }
}
