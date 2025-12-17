package view.windows;

import controller.gameFlow.GameController;
import controller.system.EquipmentController;
import model.entity.base.PlayerCharacter;
import model.entity.concrete.Archer;
import model.entity.concrete.Warrior;
import model.entity.concrete.Wizard;
import model.items.base.Item;
import model.items.concrete.*;
import view.assets.Images;
import view.components.ItemLabel;
import view.components.RoundButton;
import view.panels.EquipmentPanel;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/** Represents the equipment selection window for a {@link PlayerCharacter}.
 * <p>
 *     This window allows the player to view, equip, or unequip various items including armor, weapons, and potions.
 *     The interface supports drag-and-drop between available items and predefined selection panels.
 *     Each item panel is represented by an {@link EquipmentPanel} and is initialized with the player's currently equipped
 *     item or a default fallback item. Equipped items are automatically reflected in the UI.
 * </p> */
public class EquipmentWindow extends JPanel implements ActionListener {
    /* --- Fields --- */
    /** Reference to the game window to return to when this window is closed. */
    private final GameWindow previousWindow;
    /** Button to navigate back to the previous frame. */
    private final RoundButton backButton;

    /** Panels representing the four slots where items can be equipped. */
    private final JPanel selected1 = new JPanel();
    /** Panels representing the four slots where items can be equipped. */
    private final JPanel selected2 = new JPanel();
    /** Panels representing the four slots where items can be equipped. */
    private final JPanel selected3 = new JPanel();
    /** Panels representing the four slots where items can be equipped. */
    private final JPanel selected4 = new JPanel();

    /** Panel representing the Armor equipment slot. */
    private final EquipmentPanel<Armor> armorPanel;
    /** Panel representing the HealthPotion equipment slot. */
    private final EquipmentPanel<HealthPotion> healthPotionPanel;
    /** Panel representing the Sword equipment slot. */
    private final EquipmentPanel<Sword> swordPanel;
    /** Panel representing the Shield equipment slot. */
    private final EquipmentPanel<Shield> shieldPanel;
    /** Panel representing the PowerfulPotion equipment slot. */
    private final EquipmentPanel<PowerfulPotion> powerfulPotionPanel;
    /** Panel representing the SpeedPotion equipment slot. */
    private final EquipmentPanel<SpeedPotion> speedPotionPanel;
    /** Panel representing the Crossbow equipment slot. */
    private final EquipmentPanel<Crossbow> crossbowPanel;

    /**  */
    private final static int DEFAULT_Y = 20;

    /** Constructor
     * @param previousWindow    the frame to return to when this window is closed.
     * @param player            the player whose equipment will be managed in this window.
     * @param gameController    the main game controller.*/
    public EquipmentWindow(GameWindow previousWindow, PlayerCharacter player, GameController gameController) {
        this.previousWindow = previousWindow;
        MyFrame.getInstance().setTitle("Choose " + player.getName() + "'s equipment");
        EquipmentController equipmentController = new EquipmentController(gameController, player, this);
        setSize(MyFrame.getInstance().getSize());
        setLocation(0, 0);
        setBackground(Color.black);
        setLayout(null);

        /* Back Button */
        ImageIcon icon = new ImageIcon("resources/images/backArrow.png");
        backButton = new RoundButton(icon);
        backButton.setTextColor(Color.black);
        backButton.setBackgroundColor(Color.gray);
        backButton.setBounds(20, 575,50,50);
        backButton.setBorderColor(Color.white);
        backButton.setBorderSize(4);
        backButton.addActionListener(equipmentController::goingBack);
        add(backButton);

        armorPanel = createEquipmentPanel("Armor", player.getEquippedArmor(), Armor.createForCharacter(player), 43, DEFAULT_Y);
        healthPotionPanel = createEquipmentPanel("Health Potion", player.getEquippedHealthPotion(), new HealthPotion(50), 208, DEFAULT_Y);
        swordPanel = createEquipmentPanel("Sword", (player instanceof Warrior w) ? w.getEquippedSword() : null, new Sword(80), 373, DEFAULT_Y);
        shieldPanel = createEquipmentPanel("Shield", player.getEquippedShield(), new Shield(100), 538, DEFAULT_Y);
        powerfulPotionPanel = createEquipmentPanel("Powerful Potion", (player instanceof Wizard w) ? w.getEquippedPowerfulPotion(): null, new PowerfulPotion(210), 703, DEFAULT_Y);
        speedPotionPanel = createEquipmentPanel("Speed Potion", player.getEquippedSpeedPotion(), new SpeedPotion(2), 868, DEFAULT_Y);
        crossbowPanel = createEquipmentPanel("Crossbow", (player instanceof Archer a) ? a.getEquippedCrossbow(): null, new Crossbow(50), 1033, DEFAULT_Y);

        /* First selection panel */
        selected1.setBounds(132,415,135, 210);
        selected1.setBackground(Color.lightGray);
        selected1.setLayout(null);
        selected1.setVisible(true);

        /* Second selection panel */
        selected2.setBounds(399,415,135, 210);
        selected2.setBackground(Color.lightGray);
        selected2.setLayout(null);
        selected2.setVisible(true);

        /* Third selection panel */
        selected3.setBounds(666,415,135, 210);
        selected3.setBackground(Color.lightGray);
        selected3.setLayout(null);
        selected3.setVisible(true);

        /* Forth selection panel */
        selected4.setBounds(933,415,135, 210);
        selected4.setBackground(Color.lightGray);
        selected4.setLayout(null);
        selected4.setVisible(true);

        add(armorPanel);
        add(healthPotionPanel);
        add(swordPanel);
        add(shieldPanel);
        add(powerfulPotionPanel);
        add(speedPotionPanel);
        add(crossbowPanel);
        add(selected1);
        add(selected2);
        add(selected3);
        add(selected4);

        /* This part calls methods from EquipmentPanel to manages the dragged panel. One for each different equipment panel. */
        EquipmentPanel<?>[] panels = {armorPanel, healthPotionPanel, swordPanel, shieldPanel, powerfulPotionPanel, speedPotionPanel, crossbowPanel};
        for (EquipmentPanel<?> panel : panels) {
            panel.setDropTargets(selected1, selected2, selected3, selected4);
            panel.setDropListener(equipmentController);
        }
        initializePanelSlots(player);
        refreshUI();
    }

    /** Populates the initial logical state (currentSlot) of the equipment panels based on the items currently equipped
     * by the {@link PlayerCharacter}. This ensures equipped items are displayed in the correct selection slots when the window opens.
     * This method currently assigns items sequentially to the first available slot (selected1, selected2, etc.).
     * @param player the player whose equipment state is used for initialization. */
    private void initializePanelSlots(PlayerCharacter player) {
        JPanel[] selectionSlots = {selected1, selected2, selected3, selected4};
        int slotIndex = 0;
        if (player.getEquippedArmor() != null)
            armorPanel.setCurrentSlot(selectionSlots[slotIndex++]);
        if (player.getEquippedHealthPotion() != null)
            healthPotionPanel.setCurrentSlot(selectionSlots[slotIndex++]);
        if (player instanceof Warrior w && w.getEquippedSword() != null)
            swordPanel.setCurrentSlot(selectionSlots[slotIndex++]);
        if (player.getEquippedShield() != null)
            shieldPanel.setCurrentSlot(selectionSlots[slotIndex++]);
        if (player instanceof Wizard w && w.getEquippedPowerfulPotion() != null && slotIndex < selectionSlots.length)
            powerfulPotionPanel.setCurrentSlot(selectionSlots[slotIndex++]);
        if (player.getEquippedSpeedPotion() != null && slotIndex < selectionSlots.length)
            speedPotionPanel.setCurrentSlot(selectionSlots[slotIndex++]);
        if (player instanceof Archer a && a.getEquippedCrossbow() != null && slotIndex < selectionSlots.length)
            crossbowPanel.setCurrentSlot(selectionSlots[slotIndex++]);
    }

    /* --- Helpers --- */
    /** Creates an {@link EquipmentPanel} for a specific item type. Each panel displays the item's name, icon, and
     * references the player's equipped or fallback item.
     * @param name      the display name of the item.
     * @param equipped  the item currently equipped by the player, or null.
     * @param fallback  the default item to display if nothing is equipped.
     * @param x         the x-coordinate of the panel's initial location.
     * @param y         the y-coordinate of the panel's initial location.
     * @param <T>       the type of item displayed in the panel.
     * @return the configured {@link EquipmentPanel}. */
    private <T extends Item> EquipmentPanel<T> createEquipmentPanel(String name, T equipped, T fallback, int x, int y) {
        EquipmentPanel<T> panel = new EquipmentPanel<>(this, name);
        panel.setLayout(new BorderLayout());
        panel.setBounds(0, 0, 125, 200);
        panel.setOriginalLocation(new Point(x, DEFAULT_Y));
        panel.setBackground(Color.lightGray);

        ItemLabel label = new ItemLabel(name);
        label.setVisible(true);
        ImageIcon icon = Images.load("resources/images/"+ name + ".png", 125, 180);
        JLabel iconLabel = new JLabel(icon);
        panel.add(label, BorderLayout.NORTH);
        panel.add(iconLabel, BorderLayout.CENTER);

        if (equipped != null)
            panel.setItem(equipped);
        else
            panel.setItem(fallback);
        return panel;
    }

    /** Refreshes the UI, updating all equipment panels based on the player's current equipment.
     * This method ensures panels are displayed in the correct selection slot or fallback position. */
    public void refreshUI() {
        moveEquipmentPanel(armorPanel, 43, DEFAULT_Y);
        moveEquipmentPanel(healthPotionPanel, 208, DEFAULT_Y);
        moveEquipmentPanel(shieldPanel, 538, DEFAULT_Y);
        moveEquipmentPanel(speedPotionPanel, 868, DEFAULT_Y);
        moveEquipmentPanel(swordPanel, 373, DEFAULT_Y);
        moveEquipmentPanel(powerfulPotionPanel, 703, DEFAULT_Y);
        moveEquipmentPanel(crossbowPanel, 1033, DEFAULT_Y);

        revalidate();
        repaint();
    }

    /** Moves an equipment panel to its proper location depending on whether it is equipped or in a selection slot.
     * @param panel     the equipment panel to move.
     * @param defaultX  the default x-coordinate if the panel is not in a slot.
     * @param defaultY  the default y-coordinate if the panel is not in a slot.
     * @param <T>       the type of item in the panel. */
    private <T extends Item> void moveEquipmentPanel(EquipmentPanel<T> panel, int defaultX, int defaultY) {
        if (panel.getCurrentSlot() != null)
            panel.getCurrentSlot().add(panel);
        else
            this.add(panel);

        if (panel.getParent() == this)
            panel.setLocation(defaultX, defaultY);
        else
            panel.setLocation(5, 5);

        panel.getParent().revalidate();
        panel.getParent().repaint();
    }

    /** Allows the Controller to set the internal logical slot ({@link EquipmentPanel#setCurrentSlot(JPanel)}) of an {@link EquipmentPanel}.
     * This is the mechanism used to maintain the panel's position across refresh operations.
     * @param panelType The string name of the equipment type (e.g., "Armor").
     * @param slot The destination slot {@link JPanel} or {@code null} if the item was unequipped. */
    public void setPanelSlot(String panelType, JPanel slot) {
        EquipmentPanel<?> panel = switch (panelType) {
            case "Armor" -> armorPanel;
            case "Health Potion" -> healthPotionPanel;
            case "Sword" -> swordPanel;
            case "Shield" -> shieldPanel;
            case "Powerful Potion" -> powerfulPotionPanel;
            case "Speed Potion" -> speedPotionPanel;
            case "Crossbow" -> crossbowPanel;
            default -> null;
        };
        if (panel != null)
            panel.setCurrentSlot(slot);
    }

    /* --- Listeners --- */
    /** Handles actions triggered by UI components, such as the back button. Pressing the back button closes this window
     * and shows the previous frame.
     * @param e the action event triggered by a UI component. */
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == backButton){
            previousWindow.setVisible(true);
            this.setVisible(false);
        }
    }
}
