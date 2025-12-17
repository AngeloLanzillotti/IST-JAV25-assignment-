package view.panels;

import controller.gameFlow.CharacterSelectionController;
import model.core.Difficulty;
import model.entity.base.CharacterModel;
import model.entity.base.PlayerCharacter;
import model.entity.concrete.Archer;
import model.entity.concrete.Warrior;
import model.entity.concrete.Wizard;
import view.assets.Images;
import view.windows.MyFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/** GUI window for selecting a player character and difficulty level in the RPG game.
 * <p>
 *     This class represents the character selection screen in the RPG game. It displays a set of PlayerCharacter
 *     characters, a difficulty ComboBox. User interactions are delegated to the {@link CharacterSelectionController}.
 * </p> <p>
 *     Responsibilities:
 *     <ul>
 *         <li>Display the available characters with their respective panels.</li>
 *         <li>Display and manage the difficulty selection ComboBox.</li>
 *         <li>Forward user actions (combo selection, character click) to the controller.</li>
 *     </ul>
 * @see CharacterSelectionController
 * @see PlayerCharacter
 * @see Difficulty
 * @see MyFrame */
public class CharacterSelection extends JPanel implements ActionListener, CharacterSelectionInterface, KeyListener {
    /* --- Fields --- */
    /** Array containing all playable characters. */
    private final PlayerCharacter[] characters = {
            new Archer("Archer"),
            new Warrior("Warrior"),
            new Wizard("Wizard")};
    /** ComboBox used to select difficulty levels. */
    private final JComboBox<Difficulty> combo;
    /** Controller responsible for handling all character selection logic. */
    private final CharacterSelectionController controller;
    /** Index of the currently selected playable character in the {@code characters} array. Used for navigation and rendering. */
    private int selectedIndex = 0;

    /** Constructor
     * @param controller the controller responsible for handling user actions. */
    public CharacterSelection(CharacterSelectionController controller) {
        this.controller = controller;
        MyFrame.getInstance().setTitle("Select your character");
        setLayout(null);
        setBackground(Color.black);
        setBounds(0, 0, 1200, 675);
        setFocusable(true);
        addKeyListener(this);
        requestFocusInWindow();

        /* ComboBox */
        combo = new JComboBox<>(Difficulty.values());
        combo.setToolTipText("Select the level");
        combo.setBounds(20, 595, 150, 30);
        combo.setBackground(Color.darkGray);
        combo.setForeground(Color.white);
        combo.setFont(new Font("Arial", Font.PLAIN, 20));
        combo.addActionListener(this);
        add(combo);
    }

    /* --- Methods --- */
    /** Callback from {@link CharacterSelectionInterface} when a character is chosen. Delegates the action to the {@link CharacterSelectionController}.
     * @param chosen the character selected by the user. */
    @Override
    public void onCharacterSelected(CharacterModel chosen) {
        controller.characterChosen(chosen);
    }

    /** Paints the panel, including character images, selection highlight, names, and stats.
     * @param g the Graphics object used for drawing. */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); /* to clean everytime the window */
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
            SwingUtilities.invokeLater(CharacterSelection.this::requestFocusInWindow);
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
        else if (code == KeyEvent.VK_ENTER)
            onCharacterSelected(characters[selectedIndex]);
        repaint();
    }
    /** Required implementation for KeyListener, not used for custom actions. */
    @Override
    public void keyTyped(KeyEvent e) {}
    /** Required implementation for KeyListener, not used for custom actions. */
    @Override
    public void keyReleased(KeyEvent e) {}
}
