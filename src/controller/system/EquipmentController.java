package controller.system;

import controller.eventListeners.DropListener;
import controller.gameFlow.GameController;
import model.entity.base.PlayerCharacter;
import model.entity.concrete.Archer;
import model.entity.concrete.Warrior;
import model.entity.concrete.Wizard;
import model.items.base.Item;
import model.items.concrete.*;
import view.windows.EquipmentWindow;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;

/** Handles user inputs (clicks, drag-and-drop) and mediates between the View (EquipmentWindow) and the Model (PlayerCharacter).
 * <p>
 *     It acts as the delegate for drag-and-drop events, applying business logic (equip, use, restriction checks)
 *     and updating both the {@link PlayerCharacter} state (Model) and the {@link EquipmentWindow} (View).
 * </p>
 * @see DropListener
 * @see EquipmentWindow
 * @see PlayerCharacter */
public class EquipmentController implements DropListener {
    /* --- Fields --- */
    /** Reference to the Model being manipulated by the Controller. */
    private final PlayerCharacter player;
    /** Reference to the View that needs to be updated after a successful operation. */
    private final EquipmentWindow view;
    /** Reference to the main GameController for navigation and global UI updates. */
    private final GameController gameController;

    /** Constructor
     * @param gameController The main game controller.
     * @param player The character whose stats and equipment are managed.
     * @param view The equipment window requiring updates. */
    public EquipmentController(GameController gameController, PlayerCharacter player, EquipmentWindow view) {
        this.gameController = gameController;
        this.player = player;
        this.view = view;
    }

    /* --- Methods --- */
    /** Handles the click action on the "Back" button.
     * Calls the {@link GameController} to navigate back to the main game window.
     * @param e The action event triggered by the button. */
    public void goingBack(ActionEvent e) {
        gameController.showGameWindow();
    }

    /* DropListener interface's methods */
    /** * Handles the drag-and-drop event received from the View (EquipmentWindow). This method executes the business logic (equip/unequip/use).
     * If the operation is successful, it updates the panel's logical slot state in the View
     * and triggers a refresh of the UI, including the player's health bar.
     * @param panelType The identifying string for the panel type (e.g., "Armor", "Sword").
     * @param target The destination slot (JPanel) if equipping, or null if unequipping.
     * @param item The {@link Item} object that was dropped.
     * @param <T> The type of {@link Item} dropped. */
    @Override
    public <T extends Item> void handleItemDrop(String panelType, JPanel target, T item) {
        boolean success = false;
        switch (panelType) {
            case "Armor":
                if (target != null) {
                    if (item.canBeEquipped(player)) {
                        player.equipArmor((Armor) item);
                        success = true;
                    } else
                        MessageController.getInstance().showMessage(item.getEquipFailReason(player), 2000, Color.red);
                } else {
                    player.unequipArmor((Armor) item);
                    success = true;
                }
                break;
            case "Health Potion":
                if (target != null)
                    player.useHealthPotion((HealthPotion) item);
                else
                    player.unuseHealthPotion((HealthPotion) item);
                success = true;
                break;
            case "Sword":
                if (target != null) {
                    if (item.canBeEquipped(player)) {
                        if (player instanceof Warrior warrior) {
                            warrior.equipSword((Sword) item);
                            success = true;
                        }
                    } else
                        MessageController.getInstance().showMessage(item.getEquipFailReason(player), 2000, Color.red);
                } else {
                    if (player instanceof Warrior warrior) {
                        warrior.unequipSword((Sword) item);
                        success = true;
                    }
                }
                break;
            case "Shield":
                if(target != null) {
                    player.equipShield((Shield) item);
                } else {
                    player.unequipShield((Shield) item);
                }
                success = true;
                break;
            case "Powerful Potion":
                if (target != null) {
                    if (item.canBeEquipped(player)) {
                        if (player instanceof Wizard wizard) {
                            wizard.usePowerfulPotion((PowerfulPotion) item);
                            success = true;
                        }
                    } else
                        MessageController.getInstance().showMessage(item.getEquipFailReason(player), 2000, Color.red);
                } else {
                    if (player instanceof Wizard wizard) {
                        wizard.unusePowerfulPotion((PowerfulPotion) item);
                        success = true;
                    }
                }
                break;
            case "Speed Potion":
                if(target != null)
                    player.useSpeedPotion((SpeedPotion) item);
                else
                    player.unuseSpeedPotion((SpeedPotion) item);
                success = true;
                break;
            case "Crossbow":
                if (target != null) {
                    if (item.canBeEquipped(player)) {
                        if (player instanceof Archer archer) {
                            archer.equipCrossbow((Crossbow) item);
                            success = true;
                        }
                    } else
                        MessageController.getInstance().showMessage(item.getEquipFailReason(player), 2000, Color.red);
                } else {
                    if (player instanceof Archer archer) {
                        archer.unequipCrossbow((Crossbow) item);
                        success = true;
                    }
                }
                break;
        }
        if(success){
            view.setPanelSlot(panelType, target);
            gameController.refreshPlayerHealthBar();
        }
        view.refreshUI();
    }
}