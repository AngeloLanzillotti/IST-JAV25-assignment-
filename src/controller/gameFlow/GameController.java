package controller.gameFlow;

import controller.eventListeners.CharacterEventListener;
import controller.system.MessageController;
import model.entity.base.CharacterModel;
import model.entity.base.PlayerCharacter;
import model.users.*;
import utility.engine.DelayTimer;
import view.components.HealthProgressBar;
import view.panels.CharacterSelection;
import view.windows.EquipmentWindow;
import view.windows.GameWindow;
import view.windows.MyFrame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/** Controller managing the main game flow, specifically within the {@link GameWindow}.
 * <p>
 *     This class handles transitions between the {@code GameWindow} and the {@link EquipmentWindow}, manages the combat
 *     simulation using turn-based calls and timers, and ensures the View elements are synchronized with the Model state.
 * </p>
 * @see ActionListener
 * @see CharacterEventListener */
public class GameController implements ActionListener, CharacterEventListener {
    /* --- Fields --- */
    /** The main player character */
    private final CharacterModel player1;
    /** The enemy character */
    private final CharacterModel player2;
    /** The main game view component. */
    private GameWindow gameWindow;
    /** Flag indicating whether the battle has concluded, used to halt the turn cycle. */
    private boolean battleOver = false;
    /** The duration (in ms) for which damage messages are displayed. */
    private static final int DAMAGE_DURATION_MS = 2000;
    /** The extra pause (in ms). */
    private static final int PAUSE_MS = 1000;

    /** Constructor
     * @param player1 The player character model.
     * @param player2 The enemy character model. */
    public GameController(CharacterModel player1, CharacterModel player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.player1.setListener(this);
        this.player2.setListener(this);
    }

    /* --- Methods --- */
    /** Handles the action when the "Start Battle" button is pressed.
     * Initiates the combat simulation by starting the turn cycle. */
    public void onStartButton() {
        String message = "Battle starts: " + player1.getName() + " vs " + player2.getName();
        MessageController.getInstance().showMessage(message, 3000, Color.cyan);
        this.battleOver = false;
        gameWindow.hideButton();
        DelayTimer.waitAfter(3000, this::playerAttack);
    }
    /** Handles the action when the "Equipment" button is pressed.
     * Transitions the main frame's content from the {@code GameWindow} to the {@link EquipmentWindow}. */
    public void onEquipmentButton(){
        EquipmentWindow equipmentWindow = new EquipmentWindow(gameWindow, (PlayerCharacter) player1, this);
        MyFrame.getInstance().setContent(equipmentWindow);
    }

    /** Displays or initializes the {@link GameWindow} as the current content of the {@link MyFrame}.
     * This method is typically used when returning from the {@link EquipmentWindow}. */
    public void showGameWindow() {
        if(gameWindow == null)
            gameWindow = new GameWindow(this, player1, player2);
        MyFrame.getInstance().setContent(gameWindow);
    }

    /** Refreshes the main player's health bar displayed on the {@link GameWindow}. This is called by the {@link controller.system.EquipmentController}
     * when a stat change (e.g., max HP increase) occurs, ensuring the View is synchronized with the updated Model state. */
    public void refreshPlayerHealthBar() {
        /* Ensure gameWindow exists before attempting to access its components. */
        if (gameWindow == null)
            showGameWindow();

        HealthProgressBar playerBar = gameWindow.getPlayerBar();
        int currentHealth = player1.getStats().getHealth();
        if (playerBar != null)
            playerBar.updateHealth(currentHealth);
    }

    /** Manages {@code player1}'s attack turn. Executes the attack logic, updates the view with damage messages and a
     * visual flash, checks for battle end, and schedules the enemy's turn. */
    public void playerAttack(){
        /* Check if the battle is already finished. */
        if(battleOver)
            return;
        int damage = player1.attack(player2);
        MessageController.getInstance().showMessage(
                player2.getName() + " takes " + damage + " damage",
                DAMAGE_DURATION_MS,
                Color.blue);
        gameWindow.getEnemyBar().flash();
        checkBattleEnd();

        /* If the battle is not finished yet. */
        if(!battleOver) {
            MessageController.getInstance().runAfterMessages(() -> DelayTimer.waitAfter(PAUSE_MS, this::enemyAttack));
        }
    }

    /** Manages {@code player2}'s (enemy's) attack turn. Executes the attack logic, updates the view with damage messages
     * and a visual flash, checks for battle end, and schedules the player's turn. */
    public void enemyAttack(){
        /* Check if the battle is already finished. */
        if(battleOver)
            return;
        int damage = player2.attack(player1);
        MessageController.getInstance().showMessage(
                player1.getName() + " takes " + damage + " damage",
                DAMAGE_DURATION_MS,
                Color.red);
        gameWindow.getPlayerBar().flash();
        checkBattleEnd();

        /* If the battle is not finished yet. */
        if(!battleOver) {
            MessageController.getInstance().runAfterMessages(() -> DelayTimer.waitAfter(PAUSE_MS, this::playerAttack));
        }
    }

    /** Checks the health status of both characters to determine if the battle has ended. If concluded, it sets the {@link #battleOver}
     * flag, displays Victory/Defeat messages, and triggers the experience update via {@link #handleBattleResult(boolean)}. */
    public void checkBattleEnd() {
        if (!player1.isAlive() || !player2.isAlive()) {
            battleOver = true;
            MessageController.getInstance().showMessage("Battle over!", 3000, Color.green);
            DelayTimer.waitAfter(2000, () -> {
                if (player1.isAlive()) {
                    MessageController.getInstance().showMessage("Victory!", 2000, Color.blue);
                    handleBattleResult(true);
                } else if (player2.isAlive()) {
                    MessageController.getInstance().showMessage("Defeated!", 2000, Color.red);
                    handleBattleResult(false);
                }
            });
        }
    }

    /* --- Helpers --- */
    /** Manages the experience gain, persistence, and view update based on the battle result. The gained experience is
     * calculated based on the difference between the characters' base healths.
     * This method adds XP and Level Up messages to the {@link MessageController} queue, updates the {@link User} model,
     * saves the data via {@link UserManager#updateUserExperience(User)}, and finally schedules the display of the
     * Restart button via {@link GameWindow#showRestartButton(ActionListener)}.
     * @param isWinner True if the player won, false otherwise. */
    private void handleBattleResult(boolean isWinner) {
        final int XP = Math.abs(this.player1.getStats().getHealth() - this.player2.getStats().getHealth());
        int xpGained = isWinner ? XP/2 : XP/10;
        User currentUser = Session.getCurrentUser();
        if (currentUser == null) {
            System.err.println("FATAL ERROR: No user logged in to grant experience.");
            return;
        }
        int oldLevel = currentUser.getLevel();
        currentUser.increaseExperience(xpGained);
        int newLevel = currentUser.getLevel();
        MessageController.getInstance().showMessage(
                "Gained " + xpGained + " XP!",
                DAMAGE_DURATION_MS,
                Color.yellow
        );
        if (newLevel > oldLevel) {
            MessageController.getInstance().showMessage(
                    currentUser.getUsername() + " reached LEVEL " + newLevel + "!",
                    DAMAGE_DURATION_MS * 2,
                    Color.green
            );
        }
        /* Upload the Experience progress bar */
        MessageController.getInstance().runAfterMessages(() -> {
            gameWindow.updateExperienceBar();
            DelayTimer.waitAfter(PAUSE_MS, () -> gameWindow.showRestartButton(this));
        });
        /* Save the changes */
        try {
            UserManager.getInstance().updateUserExperience(currentUser);
        } catch (IOException e) {
            System.err.println("Error saving user experience: " + e.getMessage());
        }
    }

    /* --- Listener --- */
    /** Handles actions triggered by the UI, specifically the Restart button. Resets the game state by returning to the
     * {@code CharacterSelection} window.
     * @param e The action event triggered by the UI component (the Restart button). */
    @Override
    public void actionPerformed(ActionEvent e){
        CharacterSelection characterSelection = new CharacterSelection(new CharacterSelectionController());
        MyFrame.getInstance().setContent(characterSelection);
    }

    /* --- CharacterEventListener interface's methods --- */
    /** Called by the {@link CharacterModel} when a special attack or event occurs.
     * Displays a distinct message on the screen to inform the user of the event.
     * @param attacker The character model that triggered the event.
     * @param message The message describing the special attack (e.g., "Powerful Attack!"). */
    @Override
    public void onAttack(CharacterModel attacker, String message) {
        MessageController.getInstance().showMessage(
                message,
                DAMAGE_DURATION_MS,
                Color.yellow
        );
    }
}
