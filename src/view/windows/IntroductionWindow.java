package view.windows;

import controller.gameFlow.IntroductionController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Represents the introduction window of the RPG game.
 * <p>
 *     This window displays the game title, author description, and a "Start" button.
 * </p>
 * @see IntroductionController */
public class IntroductionWindow extends JPanel implements ActionListener {
    /* --- Fields --- */
    /** The button used to start the game. */
    private final JButton startButton;
    /** Controller responsible for handling actions from this view. */
    private final IntroductionController introductionController;

    /** Constructor
     * @param introductionController the controller that handles Start button actions. */
    public IntroductionWindow(IntroductionController introductionController) {
        this.introductionController = introductionController;
        setLayout(null);
        setBackground(Color.black);
        /* Title & Description label */
        JLabel title = new JLabel();
        title.setText("RPG GAME");
        title.setFont(new Font("MV Boli", Font.BOLD, 100));
        title.setForeground(Color.white);
        title.setBounds(0, 100, 1200, 200);
        title.setHorizontalAlignment(JLabel.CENTER);

        JLabel description = new JLabel();
        description.setText("By Angelo Lanzillotti");
        description.setFont(new Font("MV Boli", Font.BOLD, 15));
        description.setForeground(Color.white);
        description.setBounds(0, 150, 1200, 200);
        description.setHorizontalAlignment(JLabel.CENTER);

        /* Start button */
        startButton = new JButton("Start");
        startButton.setBounds(545, 500, 100, 30);
        startButton.setFocusable(false);
        startButton.setFont(new Font("Arial", Font.BOLD, 25));
        startButton.setBackground(Color.darkGray);
        startButton.setForeground(Color.white);
        startButton.setVisible(true);
        startButton.addActionListener(this);

        add(title);
        add(description);
        add(startButton);
        MyFrame.getInstance().setTitle("RPG Game");
        setVisible(true);
    }

    /* --- Listener --- */
    /** Handles action events from the components in this window.
     * Specifically, when the Start button is clicked, it notifies the controller to start the game and disposes this window.
     * @param e the action event triggered by a component. */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == startButton)
            introductionController.startGame();
    }
}
