package view.components;

import model.entity.base.CharacterModel;
import utility.engine.DelayTimer;

import javax.swing.*;
import java.awt.*;

/** A progress bar that displays a {@link CharacterModel}'s health.
 * <p>
 *     The bar updates automatically whenever the character's health changes, using a {@link java.beans.PropertyChangeListener}
 *     registered on the CharacterModel.
 * </p>
 * @see CharacterModel
 * @see JProgressBar */
public class HealthProgressBar extends JProgressBar {
    /* --- Fields --- */
    /** The maximum health value to display. */
    private int maxHealth;
    /** The default background color used when the bar is not flashing (e.g., after taking damage). */
    private static final Color NORMAL_BACKGROUND_COLOR = Color.darkGray;

    /** Constructor
     * @param characterModel the character whose health will be displayed*/
    public HealthProgressBar(CharacterModel characterModel) {
        super(0, 1);
        this.maxHealth = Math.max(1,characterModel.getStats().getHealth());

        setMinimum(0);
        setMaximum(maxHealth);
        setValue(maxHealth);
        setString(maxHealth + " / " + maxHealth);
        setForeground(Color.red);
        setStringPainted(true);
        setFont(new Font("Arial", Font.BOLD, 20));
        setVisible(true);
        setOpaque(true);

        /* Register a listener on the CharacterModel's stats */
        characterModel.addPropertyChangeListener(evt -> {
            if ("health".equals(evt.getPropertyName())) {
                int newHealth = (int) evt.getNewValue();
                updateHealth(newHealth);
            }
        });
    }

    /* --- Methods --- */
    /** Updates the progress bar display according to the current health.
     * @param current the current health value. */
    public void updateHealth(int current) {
        SwingUtilities.invokeLater(() -> {
            int displayHealth = current;
            if (displayHealth < 0)
                displayHealth = 0;

            if (displayHealth > maxHealth) {
                maxHealth = displayHealth;
                setMaximum(maxHealth);
            }
            setValue(displayHealth);
            setString(displayHealth + " / " + maxHealth);
            repaint();
        });
    }

    /** Implements a visual effect, making the progress bar flash yellow briefly to signify damage taken.
     * Uses {@link DelayTimer} to revert the color after a short delay, ensuring the GUI update is thread-safe. */
    public void flash() {
        setBackground(Color.yellow);
        DelayTimer.waitAfter(300, () -> SwingUtilities.invokeLater(() -> setBackground(NORMAL_BACKGROUND_COLOR)));
    }

    /* --- Getters --- */
    /** Getter method
     * @return the maximum health. */
    public int getMaxHealth() {
        return maxHealth;
    }
}
