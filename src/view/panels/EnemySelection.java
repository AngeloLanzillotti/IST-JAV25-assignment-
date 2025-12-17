package view.panels;

import controller.gameFlow.CharacterSelectionController;
import model.entity.base.CharacterModel;
import model.entity.base.Enemy;
import model.entity.concrete.Dragon;
import model.entity.concrete.Goblin;
import model.entity.concrete.Orc;
import model.core.Difficulty;
import view.assets.Images;
import view.windows.MyFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/** GUI window that allows the player to select an enemy to fight against.
 * <p>
 *     This class represents the enemy selection screen in the RPG game. It displays a set of enemy characters, a difficulty
 *     ComboBox. User interactions are delegated to the {@link CharacterSelectionController}.
 * </p>
 * @see CharacterSelectionController
 * @see MyFrame
 * @see CharacterModel
 * @see Difficulty */
public class EnemySelection extends JPanel implements ActionListener, CharacterSelectionInterface, KeyListener {
    /* --- Fields --- */
    /** Array of enemy characters displayed in this window. */
    private final Enemy[] characters = {
            new Dragon("Dragon"),
            new Goblin("Goblin"),
            new Orc("Orc")};
    /** ComboBox used to select difficulty level for the enemies. */
    private final JComboBox<Difficulty> combo = new JComboBox<>(Difficulty.values());
    /** The player character chosen in the previous selection window. */
    private final CharacterModel playerCharacter;
    /** Controller that handles logic for difficulty changes and enemy selection. */
    private final CharacterSelectionController controller;
    /** Index of the currently selected enemy in the {@code characters} array. Used for navigation and rendering. */
    private int selectedIndex = 0;

    /** Constructor
     * @param playerCharacter   the player character previously selected.
     * @param controller        the controller responsible for handling selection logic. */
    public EnemySelection(CharacterModel playerCharacter, CharacterSelectionController controller) {
        this.playerCharacter = playerCharacter;
        this.controller = controller;
        MyFrame.getInstance().setTitle("Select your enemy");
        setLayout(null);
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(this);
        requestFocusInWindow();

        /* ComboBox */
        combo.setBounds(20, 595, 150, 30);
        combo.setToolTipText("Select the level");
        combo.setBackground(Color.darkGray);
        combo.setForeground(Color.white);
        combo.setFont(new Font("Arial", Font.PLAIN, 20));
        combo.addActionListener(this);
        add(combo);
    }

    /* --- Methods --- */
    /** Callback method triggered when the user selects an enemy.
     * Delegates the enemy selection handling to the {@link CharacterSelectionController} and closes the current window.
     * @param enemy the enemy character selected by the player. */
    @Override
    public void onCharacterSelected(CharacterModel enemy) {
        controller.enemyChosen(playerCharacter, enemy);
    }

    /** Paints the panel, including character images, selection highlight, names, and stats.
     * @param g the Graphics object used for drawing. */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); //to clean everytime the window
        /* Title */
        g.setColor(Color.blue);
        g.setFont(new Font("MV Boli", Font.BOLD, 30));
        g.drawString("Select a character", 440, 50);
        /* Rectangle for the characters. */
        int zeroX = 120;
        int zeroY = 110;
        int spacing = 353;
        for (int i = 0; i < this.characters.length; i++) {
            CharacterModel chosen = this.characters[i];
            int x = zeroX + i * spacing;
            /* To draw the yellow board when selecting */
            if (i == selectedIndex) {
                g.setColor(Color.YELLOW);
                g.drawRect(x - 10, zeroY - 10, 235, 320);
            }
            /* Character's body */
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(x, zeroY, 215, 300);
            /* Character's image */
            ImageIcon img = Images.load("resources/images/" + chosen.getName() + ".png", 215, 300);
            Image image = img.getImage();
            g.drawImage(image, x, zeroY, null);
            /* Character Name */
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString(chosen.getName(), x + 10, zeroY + 330);
            /* Character's statistics */
            g.setFont(new Font("Arial", Font.PLAIN, 14));
            g.drawString("Health: " + chosen.getStats().getHealth(), x + 10, zeroY + 360);
            g.drawString("Attack Power: " + chosen.getStats().getAttackPower(), x + 10, zeroY + 380);
            g.drawString("Defense: " + chosen.getStats().getDefense(), x + 10, zeroY + 400);
            g.drawString("Speed: " + chosen.getStats().getSpeed(), x + 10, zeroY + 420);
        }
    }

    /* --- Listeners --- */
    /** Handles user interactions with GUI components (e.g., difficulty ComboBox).
     * Delegates any logic to the {@link CharacterSelectionController}.
     * @param e the action event triggered by a component. */
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == combo){
            Difficulty selectedDifficulty = (Difficulty) combo.getSelectedItem();
            if (selectedDifficulty != null)
                controller.difficultyChanged(selectedDifficulty, characters);
            repaint();
            requestFocusInWindow();
        }
    }

    /* --- KeyListener methods --- */
    /** Handles arrow key navigation and Enter selection.
     * @param e the KeyEvent triggered by user input. */
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_LEFT && selectedIndex > 0)
                selectedIndex--;
        else if (code == KeyEvent.VK_RIGHT && selectedIndex < characters.length - 1)
                selectedIndex++;
        else if (code == KeyEvent.VK_ENTER) {
            CharacterModel chosen = this.characters[selectedIndex];
            onCharacterSelected(chosen);
        }
        repaint();
    }

    /** Required KeyListener method, not used for custom actions. */
    @Override
    public void keyTyped(KeyEvent e) {}
    /** Required KeyListener method, not used for custom actions. */
    @Override
    public void keyReleased(KeyEvent e) {}
}
