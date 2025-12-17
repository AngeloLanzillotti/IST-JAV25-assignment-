package model.entity.base;

import controller.eventListeners.EquipmentEventListener;
import model.items.base.Item;
import model.items.concrete.Armor;
import model.items.concrete.HealthPotion;
import model.items.concrete.Shield;
import model.items.concrete.SpeedPotion;
import model.core.*;

/** Represents a playable character in the game.
 * <p>
 *     A {@code PlayerCharacter} extends the base {@link CharacterModel} with player-specific mechanics such as
 *     equipment management (armor, shields, and potions).
 * </p> <p>
 *     This class is abstract because each specific player type (e.g., Wizard, Archer, Warrior) provides its own
 *     implementation of character abilities and behaviors.
 * </p> */
public abstract class PlayerCharacter extends CharacterModel {
    /* --- Fields --- */
    /** The Armor currently equipped by the player, or {@code null} if none. */
    private Armor equippedArmor = null;
    /** The Shield currently equipped by the player, or {@code null} if none. */
    private Shield equippedShield = null;
    /** The HealthPotion currently applied on the player, or {@code null} if none. */
    private HealthPotion equippedHealthPotion = null;
    /** The SpeedPotion currently applied on the player, or {@code null} if none. */
    private SpeedPotion equippedSpeedPotion = null;
    /** This listener is notified whenever the character equips, unequips, or fails to equip an item.
     * @see #setEquipmentListener(EquipmentEventListener)
     * @see #notifyEquipped(Item)
     * @see #notifyUnequipped(Item)
     * @see #notifyEquipFailed(String) */
    private EquipmentEventListener equipmentListener;

    /** Constructor
     * @param name          the character's name.
     * @param defaultStats  the default stats assigned to the character.
     * @param type          the type of the character (e.g. ARCHER, WARRIOR, WIZARD). */
    public PlayerCharacter(String name, model.core.Stats defaultStats, model.core.CharacterType type) {
        super(name, defaultStats, type);
    }

    /* --- Methods --- */
    /** Equips the specified Armor, replacing any previously equipped Armor. The character must meet the Armor's minimum
     * difficulty requirement.
     * @param armor     the Armor to equip. */
    public void equipArmor(Armor armor){
        /* Check if the CharacterModel has the minimum level to wear the armor. */
        if(this.getLevel() < armor.minDifficulty.getLevel())
            return;
        /* Remove the previous armor, if the PlayerCharacter has one, before adding the new one. */
        if (this.equippedArmor != null)
            equippedArmor.unuse(this);
        armor.use(this);
        equippedArmor = armor;
        notifyEquipped(armor);
    }
    /** Unequips the specified Armor if it is currently equipped.
     * @param armor     the Armor to unequip. */
    public void unequipArmor(Armor armor){
        if(this.equippedArmor == armor) {
            armor.unuse(this);
            equippedArmor = null;
            notifyUnequipped(armor);
        }
    }

    /** Equips a Shield for the character.
     * @param shield    the Shield to equip. */
    public void equipShield(Shield shield){
        shield.use(this);
        this.equippedShield = shield;
        notifyEquipped(shield);
    }
    /** Unequips the Shield currently equipped by the character.
     * @param shield    the Shield to unequip. */
    public void unequipShield(Shield shield){
        if(this.equippedShield != null) {
            shield.unuse(this);
            this.equippedShield = null;
            notifyUnequipped(shield);
        }
    }

    /** Uses a HealthPotion on the character, increasing health.
     * @param potion    the health potion to use. */
    public void useHealthPotion(HealthPotion potion){
        potion.use(this);
        this.equippedHealthPotion = potion;
        notifyEquipped(potion);
    }
    /** Removes the effect of the previously used HealthPotion.
     * @param potion    the HealthPotion whose effect should be removed. */
    public void unuseHealthPotion(HealthPotion potion){
        potion.unuse(this);
        equippedHealthPotion = null;
        notifyUnequipped(potion);
    }

    /** Uses a Speed Potion to increase the character's speed.
     * @param potion    the SpeedPotion to use. */
    public void useSpeedPotion(SpeedPotion potion){
        potion.use(this);
        this.equippedSpeedPotion = potion;
        notifyEquipped(potion);
    }
    /** Removes the effect of the previously used SpeedPotion.
     * @param potion    the SpeedPotion whose effect should be removed. */
    public void unuseSpeedPotion(SpeedPotion potion){
        potion.unuse(this);
        equippedSpeedPotion = null;
        notifyUnequipped(potion);
    }

    /* --- Helpers --- */
    /** Notifies the listener that an item has been successfully equipped by this character.
     * This method is called internally by the model after the character's state has been updated (for example, after
     * equipArmor, equipShield, useHealthPotion).
     * @param item the item that was equipped. */
    private void notifyEquipped(Item item) {
        if (equipmentListener != null)
            equipmentListener.onEquipped(this, item);
    }

    /** Notifies the listener that an item has been successfully unequipped by this character.
     * This method is called internally after the character's state has been updated (for example, after unequipArmor,
     * unequipShield, unuseHealthPotion).
     * @param item the item that was unequipped. */
    private void notifyUnequipped(Item item) {
        if (equipmentListener != null)
            equipmentListener.onUnequipped(this, item);
    }

    /** Notifies the listener that an attempt to equip an item has failed.
     * This method is called internally when an equip operation is not allowed,
     * for example due to level or type restrictions.
     * @param reason    a human-readable message explaining why the equip failed. */
    private void notifyEquipFailed(String reason) {
        if (equipmentListener != null)
            equipmentListener.onEquipFailed(reason);
    }

    /* --- Getters --- */
    /** Getter method.
     * @return the Armor currently equipped by the character, or {@code null} if none. */
    public Armor getEquippedArmor(){
        return this.equippedArmor;
    }
    /** Getter method.
     * @return the Shield currently equipped by the character, or {@code null} if none. */
    public Shield getEquippedShield(){
        return this.equippedShield;
    }
    /** Getter method.
     * @return the HealthPotion effect currently applied, or {@code null} if none. */
    public HealthPotion getEquippedHealthPotion(){
        return this.equippedHealthPotion;
    }
    /** Getter method.
     * @return the SpeedPotion effect currently applied, or {@code null} if none. */
    public SpeedPotion getEquippedSpeedPotion(){
        return this.equippedSpeedPotion;
    }

    public EquipmentEventListener getEquipmentListener(){
        return this.equipmentListener;
    }
    /* --- Setters --- */
    /** Setter method.
     * @param listener an instance of {@link EquipmentEventListener} to receive equipment notifications. */
    public void setEquipmentListener(EquipmentEventListener listener) {
        this.equipmentListener = listener;
    }
}
