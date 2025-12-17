package controller.gameFlow;

import controller.system.MessageController;
import model.core.Difficulty;
import model.entity.base.CharacterModel;
import model.entity.base.PlayerCharacter;
import view.panels.EnemySelection;
import view.windows.MyFrame;

import javax.swing.*;
import java.awt.*;

/** Controller for the {@link view.panels.CharacterSelection} view.
 * <p>
 *     Handles all user interactions from the Character Selection window,
 *     including character selection and difficulty changes. Delegates message
 *     display to the {@link MessageController} and handles transitions to the
 *     next screen ({@link EnemySelection}) after a character is chosen.
 * </p> <p>
 *     Responsibilities:
 *     <ul>
 *         <li>Update character levels when the difficulty is changed.</li>
 *         <li>Notify the {@link MessageController} when a character is selected.</li>
 *         <li>Open the {@link EnemySelection} window after messages finish displaying.</li>
 *     </ul>
 * @see view.panels.CharacterSelection
 * @see MessageController
 * @see EnemySelection
 * @see PlayerCharacter
 * @see Difficulty */
public class CharacterSelectionController {
    /* --- Methods --- */
    /** Handles the event when a character is chosen by the user.
     * Shows a confirmation message via {@link MessageController} and, after the message queue completes, opens the {@link EnemySelection} screen.
     * @param chosen the {@link CharacterModel} selected by the user. */
    public void characterChosen(CharacterModel chosen) {
        SwingUtilities.invokeLater(() -> {
            EnemySelection enemySelectionPanel = new EnemySelection(chosen, this);
            MyFrame.getInstance().setContent(enemySelectionPanel);
            enemySelectionPanel.requestFocusInWindow();
        });
    }

    /** Called when an enemy is chosen.
     * Shows a message and opens the GameWindow.
     * @param player the selected player character
     * @param enemy the selected enemy */
    public void enemyChosen(CharacterModel player, CharacterModel enemy) {
        SwingUtilities.invokeLater(() -> {
            GameController gameController = new GameController(player, enemy);
            gameController.showGameWindow();
        });
    }

    /** Handles a change in difficulty.
     * Updates the level of all provided {@link CharacterModel} instances according to the selected {@link Difficulty}.
     * @param difficulty    the difficulty chosen by the user.
     * @param characters    the array of characters whose levels will be updated. */
    public void difficultyChanged(Difficulty difficulty, CharacterModel[] characters) {
        int level = difficulty.getLevel();
        for (CharacterModel c : characters)
            c.setLevel(level);
    }
}
