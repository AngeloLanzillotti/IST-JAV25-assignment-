package view.windows;

import controller.gameFlow.CharacterSelectionController;
import controller.system.MessageController;
import model.users.*;
import view.panels.CharacterSelection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/** Login window used to authenticate or automatically create users.
 * <p>
 *     This class represents the login interface of the RPG game. It allows the user to enter a username and password,
 *     validates their credentials, and either logs them in or creates a new user if no matching account is found.
 * </p> <p>
 *     The window is built on top of {@link MyFrame} and provides:
 *     - A title label indicating the login section
 *     - Input fields for username and password
 *     - A login button that triggers authentication
 * </p>
 * The class interacts with the {@link UserManager} to load existing users from disk, search for known accounts, and
 * append newly created users. Once a login succeeds, the authenticated user is stored inside the {@link Session} object,
 * and the window transitions to a {@link CharacterSelection} screen.
 * @see UserManager
 * @see Session
 * @see User
 * @see CharacterSelection
 * @see MyFrame
 * @see ActionListener */
public class UserWindow extends JPanel implements ActionListener {
    /* --- Fields --- */
    /** Text field used to input the username. */
    private final JTextField username;
    /** Password field used to input the user's password. */
    private final JPasswordField password;
    /** Button that triggers the login or user creation process. */
    private final JButton loginButton;
    /** The Singleton instance responsible for managing user data persistence, lookup, login operations, and experience updates across the application. */
    private final UserManager userManager = UserManager.getInstance();

    /** Constructor
     * During construction:
     * - The existing users are loaded from file through {@link UserManager}
     * - Labels and form fields are initialized and styled
     * - The login button is configured and connected to an action listener
     * - The window becomes visible immediately
     * @throws RuntimeException if user data cannot be loaded from the file system. */
    public UserWindow() {
        MyFrame.getInstance().setTitle("Login Window");
        setLayout(null);
        setBackground(Color.black);
        JLabel descriptionLabel = new JLabel("Login");
        descriptionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        descriptionLabel.setForeground(Color.white);
        descriptionLabel.setFont(new Font("Tahoma", Font.BOLD, 40));
        descriptionLabel.setBounds(0,150, 1200, 50);
        add(descriptionLabel);

        username = new JTextField();
        username.setBounds(490, 300, 200, 35);
        username.setFont(new Font("Times New Roman", Font.BOLD, 20));
        username.setBackground(Color.WHITE);
        username.setForeground(Color.BLACK);
        username.setOpaque(true);
        add(username);

        password = new JPasswordField();
        password.setBounds(490, 360, 200, 35);
        password.setFont(new Font("Times New Roman", Font.BOLD, 20));
        password.setBackground(Color.WHITE);
        password.setForeground(Color.BLACK);
        password.setOpaque(true);
        add(password);

        loginButton = new JButton("Login");
        loginButton.setBounds(540, 420, 100, 35);
        loginButton.setBackground(Color.LIGHT_GRAY);
        loginButton.setForeground(Color.BLACK);
        loginButton.setOpaque(true);
        loginButton.addActionListener(this);
        add(loginButton);

        showLoginFields(true);
        setVisible(true);
    }

    /* --- Helpers --- */
    /** Shows or hides the login fields (username, password, and login button).
     * @param visible true to show the fields, false to hide them. */
    private void showLoginFields(boolean visible) {
        username.setVisible(visible);
        password.setVisible(visible);
        loginButton.setVisible(visible);
    }

    /* --- Listener --- */
    /** Handles the login button action.
     * When the button is pressed, the following logic occurs:
     * <ol>
     *     <li>Hide the login fields while processing</li>
     *     <li>Retrieve the input username and password</li>
     *     <li>Reject empty input and request correction</li>
     *     <li>If the user exists:
     *         <ul>
     *             <li>Check password validity</li>
     *             <li>If correct, log in and open {@link CharacterSelection}</li>
     *             <li>If incorrect, show an error message</li>
     *         </ul>
     *     </li>
     *     <li>If the user does not exist:
     *         <ul>
     *             <li>Create a new user</li>
     *             <li>Save it through {@link UserManager#appendUser(User)}</li>
     *             <li>Start a new session</li>
     *             <li>Open {@link CharacterSelection}</li>
     *         </ul>
     *     </li>
     * </ol>
     * @param e the action event triggered by button interaction. */
    @Override
    public void actionPerformed(ActionEvent e) {
        /* Controller used to display error, success, and status messages to the user. */
        MessageController messageController = MessageController.getInstance();
        if(e.getSource() == loginButton){
            showLoginFields(false);
            String user = username.getText().trim();
            String pass = new String(password.getPassword()).trim();
            if (user.isEmpty() || pass.isEmpty()) {
                messageController.showMessage("Please enter username and password", 2000, Color.red);
                messageController.runAfterMessages(() -> showLoginFields(true));
                return;
            }
            User existing = userManager.findUser(user);
            /* CASE 1: user exists. */
            if (existing != null) {
                if (userManager.login(user, pass)) {
                    Session.setCurrentUser(existing);
                    messageController.showMessage("Welcome back, " + existing.getUsername(), 2000, Color.red);
                    messageController.runAfterMessages(() -> {
                        CharacterSelection characterSelectionPanel = new CharacterSelection(new CharacterSelectionController());
                        MyFrame.getInstance().setContent(characterSelectionPanel);
                        characterSelectionPanel.requestFocusInWindow();
                    });
                } else {
                    /* User exist but the password is incorrect. */
                    messageController.showMessage("Password incorrect for user: " + user, 2000, Color.red);
                    messageController.runAfterMessages(() -> showLoginFields(true));
                }
                return;
            }
            /* CASE 2: user doesn't exist; create a new one. */
            User newUser = new User(user, pass, 0);
            try {
                userManager.appendUser(newUser);
                Session.setCurrentUser(newUser);
                messageController.showMessage("New user created: " + newUser.getUsername(), 2000, Color.red);
                messageController.runAfterMessages(() -> {
                    CharacterSelection characterSelectionPanel = new CharacterSelection(new CharacterSelectionController());
                    MyFrame.getInstance().setContent(characterSelectionPanel);
                    characterSelectionPanel.requestFocusInWindow();
                });
            } catch (IOException ex) {
                messageController.showMessage("Error saving new user!", 2000, Color.red);
                ex.printStackTrace();
                messageController.runAfterMessages(() -> showLoginFields(true));
            }
        }
    }
}
