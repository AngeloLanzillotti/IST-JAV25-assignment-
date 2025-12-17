package view.windows;

import controller.system.MessageController;

import javax.swing.*;
import java.awt.*;

/** Base JFrame for the game UI.
 * <p>
 *     This class represents the main window of the game. It is responsible solely for displaying components and rendering messages.
 * </p>
 * @see MyFrame
 * @see MessageController */
public class MyFrame extends JFrame {
    /* --- Fields --- */
    /** Singleton instance of the main game frame. Stores a reference to the single instance of {@link MyFrame} so that
     * controllers or other classes can access the main frame without creating multiple instances. */
    private static MyFrame instance;
    /** Label used to display messages to the player. */
    private final JLabel messageLabel = new JLabel();
    /** Controller for displaying messages. */
    private final MessageController messageController;

    /** Constructor */
    public MyFrame() {
        instance = this;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 675);
        setResizable(false);
        setLayout(null);

        /* Initialize the message label */
        messageLabel.setForeground(Color.CYAN);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setFont(new Font("MV Boli", Font.BOLD, 40));
        messageLabel.setBounds(0, 305, 1200, 50);
        messageLabel.setVisible(false);
        /* Add the message label to a high layer so it is always on top of the content. */
        getLayeredPane().add(messageLabel, JLayeredPane.PALETTE_LAYER);

        /* Initialize the singleton MessageController */
        messageController = new MessageController(this);
        setVisible(true);
    }

    /** Displays a message immediately on the {@code messageLabel}. The controller is responsible for deciding when and
     * how long to show the message.
     * @param message   the message text.
     * @param color     the text color. */
    public void displayMessage(String message, Color color) {
        messageLabel.setText(message);
        messageLabel.setForeground(color);
        messageLabel.setVisible(true);
    }

    /** Hides the currently displayed message by setting the label visibility to false. */
    public void hideMessage() {
        messageLabel.setVisible(false);
    }

    /* --- Getters --- */
    /** Getter method
     * @return the singleton instance of MyFrame. */
    public static MyFrame getInstance() {
        if (instance == null)
            instance = new MyFrame();
        return instance;
    }
    /** Getter method
     * @return the singleton instance of MessageController. */
    public MessageController getMessageController() {
        return messageController;
    }

    /** Replaces the entire content of the window with a new component.
     * This method removes all existing components from the content pane, adds the specified {@link JComponent} as the primary screen,
     * and refreshes the layout. The {@code messageLabel} remains on the {@link JLayeredPane} above the content.
     * Useful when switching between different game screens (e.g., Login -> Character Selection).
     * @param newContent the {@link JComponent} to display as the new content of the window. */
    public void setContent(JComponent newContent) {
        getContentPane().removeAll();
        newContent.setBounds(0, 0, getWidth(), getHeight());
        newContent.setOpaque(true);
        getContentPane().add(newContent);

        revalidate();
        repaint();
        getContentPane().requestFocusInWindow();
    }

}
