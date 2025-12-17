package view.windows;

import controller.gameFlow.GameController;
import model.entity.base.CharacterModel;
import model.users.Session;
import view.assets.Images;
import view.components.ExperienceProgressBar;
import view.components.HealthProgressBar;
import view.components.RoundButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** Represents the main battle interface (View) of the RPG game.
 * <p>
 *     This window displays the visual elements of the two combatants (player and enemy),
 *     their respective health bars, and controls to start the battle or access equipment.
 *     User interactions are delegated to the {@link GameController}.
 * </p>
 * @see GameController
 * @see CharacterModel
 * @see MyFrame */
public class GameWindow extends JPanel implements ActionListener {
    /* --- Fields --- */
    /** Progress bar tracking the player's health. */
    private final HealthProgressBar playerBar;
    /** Progress bar tracking the enemy's health. */
    private final HealthProgressBar enemyBar;
    /** The button used to start the battle sequence. */
    private final JButton startButton;
    /** Progress bar tracking the current user's level and experience progress. */
    private final ExperienceProgressBar userExpBar;
    /** Button used to open the equipment management window. */
    private final RoundButton equipmentButton;
    /** Label accompanying the equipment button. */
    private final JLabel equipmentLabel = new JLabel("Equipment");
    /** Controller responsible for handling game logic and actions triggered by this view. */
    private final GameController gameController;
    /** Button displayed at the end of the battle, used to restart the game flow. */
    private final JButton restartButton = new JButton("Restart");

    /** Constructor
     * @param gameController    the controller that handles battle logic and button actions.
     * @param player1           the player's character model.
     * @param player2           the enemy's character model. */
    public GameWindow(GameController gameController, CharacterModel player1, CharacterModel player2) {
        MyFrame.getInstance().setTitle("Battle: " + player1.getName() + " vs " + player2.getName());
        this.gameController = gameController;
        setLayout(null);
        setBackground(Color.black);

        /* Experience bar */
        userExpBar = new ExperienceProgressBar(Session.getCurrentUser());
        userExpBar.setBounds(20, 20, 300, 30);
        add(userExpBar);

        /* Start button */
        startButton = new JButton("Start");
        startButton.setBounds(525, 500, 100, 30);
        startButton.setFocusable(false);
        startButton.setFont(new Font("Arial", Font.BOLD, 25));
        startButton.setBackground(Color.darkGray);
        startButton.setForeground(Color.white);
        startButton.setVisible(true);
        startButton.addActionListener(this);
        add(startButton);

        /* Equipment button */
        ImageIcon equipmentArrowImage = new ImageIcon("resources\\images\\frontArrow.png");
        equipmentButton = new RoundButton(equipmentArrowImage);
        equipmentButton.setBounds(1120, 575, 50, 50);
        equipmentButton.setTextColor(Color.black);
        equipmentButton.setBorderColor(Color.white);
        equipmentButton.setBorderSize(4);
        equipmentButton.setBackgroundColor(Color.gray);
        equipmentButton.addActionListener(this);
        add(equipmentButton);
        /* Equipment label */
        equipmentLabel.setBounds(1000, 575, 100, 50);
        equipmentLabel.setForeground(Color.white);
        equipmentLabel.setOpaque(false);
        equipmentLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        add(equipmentLabel);

        /* PLAYER PANEL */
        JPanel playerPanel = new JPanel(new BorderLayout());
        playerPanel.setBackground(Color.white);
        playerPanel.setBounds(20, 160, 325, 455);
        /* Player's name label */
        JLabel playerLabel = new JLabel();
        playerLabel.setText(player1.getName()); //here I will use the selected PlayerCharacter name
        playerLabel.setForeground(Color.blue);
        playerLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        playerLabel.setHorizontalAlignment(JLabel.CENTER);
        playerLabel.setVerticalAlignment(JLabel.TOP);
        playerLabel.setBounds(20,160,325, 30);
        /* Player's image */
        ImageIcon playerImage = Images.load("resources\\images\\" + player1.getName() + ".png", 325, 405);
        JLabel playerIconLabel = new JLabel(playerImage);
        playerIconLabel.setSize(325, 405);
        /* Player's health bar */
        playerBar = new HealthProgressBar(player1);
        playerBar.updateHealth(player1.getStats().getHealth());
        playerPanel.add(playerLabel, BorderLayout.NORTH);
        playerPanel.add(playerIconLabel, BorderLayout.CENTER);
        playerPanel.add(playerBar, BorderLayout.SOUTH);
        add(playerPanel);

        /* ENEMY PANEL */
        JPanel enemyPanel = new JPanel(new BorderLayout());
        enemyPanel.setBackground(Color.white);
        enemyPanel.setBounds(840, 20, 325, 455);
        /* Enemy's name label */
        JLabel enemyLabel = new JLabel();
        enemyLabel.setText(player2.getName()); //here I will use the selected Enemy name
        enemyLabel.setForeground(Color.red);
        enemyLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        enemyLabel.setHorizontalAlignment(JLabel.CENTER);
        enemyLabel.setVerticalAlignment(JLabel.TOP);
        /* Enemy's image */
        ImageIcon enemyImage = Images.load("resources\\images\\" + player2.getName() + ".png", 325, 405);
        JLabel enemyIconLabel = new JLabel(enemyImage);
        enemyIconLabel.setSize(325, 405);
        /* Enemy's health bar */
        enemyBar = new HealthProgressBar(player2);
        enemyBar.updateHealth(player2.getStats().getHealth());
        enemyPanel.add(enemyLabel, BorderLayout.NORTH);
        enemyPanel.add(enemyIconLabel, BorderLayout.CENTER);
        enemyPanel.add(enemyBar, BorderLayout.SOUTH);
        add(enemyPanel);

        /* Restart Button */
        restartButton.setBounds(545, 317, 100, 30);
        restartButton.setFocusable(false);
        restartButton.setVisible(false);
        add(restartButton);
        /* Heartbeat animation */
        Timer heartbeatTimer = new Timer(30, null);
        long start = System.currentTimeMillis();
        int baseWidth = restartButton.getWidth();
        int baseHeight = restartButton.getHeight();
        int baseX = restartButton.getX();
        int baseY = restartButton.getY();
        heartbeatTimer.addActionListener(_ -> {
            double t = (System.currentTimeMillis() - start) / 1000.0;
            float scale = 1.0f + 0.1f * (float)Math.sin(4 * t);
            int newWidth = (int)(baseWidth * scale);
            int newHeight = (int)(baseHeight * scale);
            int newX = baseX - (newWidth - baseWidth) / 2;
            int newY = baseY - (newHeight - baseHeight) / 2;
            restartButton.setBounds(newX, newY, newWidth, newHeight);
            restartButton.repaint();
        });
        heartbeatTimer.start();
    }

    /* --- Methods --- */
    /** Hides the start and equipment control buttons from the view, typically when the battle begins. */
    public void hideButton(){
        startButton.setVisible(false);
        equipmentButton.setVisible(false);
        equipmentLabel.setVisible(false);
    }

    /** Makes the restart button visible and registers the provided {@link ActionListener} (from the GameController)
     * to handle the restart logic.
     * This method ensures that all previous listeners are removed before the new one is added to prevent multiple execution
     * of the restart logic.
     * @param listener the {@link ActionListener} provided by the GameController to handle the restart action. */
    public void showRestartButton(ActionListener listener) {
        /* Removes any previous listeners to prevent multiple executions */
        for (ActionListener l : restartButton.getActionListeners())
            restartButton.removeActionListener(l);
        restartButton.addActionListener(listener);
        restartButton.setVisible(true);
    }

    /** * Updates the experience progress bar to reflect the current level and progress of the logged-in user.
     * This method delegates the actual display logic to {@code userExpBar.updateExperienceDisplay()}.
     * It should be called by the {@link GameController} immediately after the user's experience
     * is modified and persisted. */
    public void updateExperienceBar() {
        userExpBar.updateExperienceDisplay();
    }

    /* --- Getters ---- */
    /** Getter method.
     * @return the HealthProgressBar instance tracking the player's health. */
    public HealthProgressBar getPlayerBar() {
        return playerBar;
    }
    /** Getter method.
     * @return the HealthProgressBar instance tracking the enemy's health. */
    public HealthProgressBar getEnemyBar() {
        return enemyBar;
    }

    /* --- Listeners --- */
    /** Handles action events triggered by the Start and Equipment buttons. Delegates the appropriate action to the {@link GameController}.
     * @param e the action event triggered by a component. */
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == startButton){
            /* Calls a method that make the battle to start */
            gameController.onStartButton();
            startButton.setVisible(false);
        } else if (e.getSource() == equipmentButton)
            /* Make visible the EquipmentWindow */
            gameController.onEquipmentButton();
    }
}