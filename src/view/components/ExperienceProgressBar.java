package view.components;

import model.users.User;

import javax.swing.*;
import java.awt.*;

/** A custom JProgressBar that displays the user's current level and experience progress.
 * The progress bar updates based on the experience points toward the next level (0-999).
 * <p>
 *     The level and progress text is drawn manually using {@link #paintComponent(Graphics)} to ensure black color and a
 *     custom font, overriding the default {@code JProgressBar} text rendering.
 * </p> */
public class ExperienceProgressBar extends JProgressBar {
    /* --- Fields --- */
    /** The font used for drawing the experience and level text. */
    private static final Font TEXT_FONT = new Font("Arial", Font.BOLD, 20);
    /** The user model whose experience is being tracked. */
    private final User user;

    /** Constructor
     * @param user The user model whose experience is being tracked. */
    public ExperienceProgressBar(User user) {
        this.user = user;
        // Configure JProgressBar appearance
        this.setMinimum(0);
        this.setMaximum(1000);
        this.setForeground(Color.cyan);
        this.setBackground(Color.darkGray);
        this.setBorderPainted(true);
        this.setPreferredSize(new Dimension(300, 30));
        updateExperienceDisplay();
    }

    /* --- Methods --- */
    /** Updates the progress bar's value based on the User model's current experience (the remainder when divided by 1000)
     * and triggers a repaint to update the custom drawn text. */
    public void updateExperienceDisplay() {
        this.setValue(user.getExperienceProgress());
        repaint();
    }

    /** Overrides the default painting method to manually draw the level and experience text in black color, centered over the progress bar.
     * @param g The Graphics object used for drawing. */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (user != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.BLACK);
            g2d.setFont(TEXT_FONT);
            int progress = user.getExperienceProgress();
            int level = user.getLevel();
            String text = "Level: " + level + " | Experience: " + progress;
            /* Compute the coordinates to center the text */
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            int x = (getWidth() - textWidth) / 2;
            int y = (getHeight() / 2) + (fm.getAscent() / 2) - 2;
            g2d.drawString(text, x, y);
        }
    }
}