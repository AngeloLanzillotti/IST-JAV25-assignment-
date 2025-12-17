package utility.engine;

import java.awt.event.*;
import javax.swing.*;

/** Utility class that allows delaying the execution of a {@link Runnable} action.
 * <p>
 *     This class uses a {@link Timer} to execute a specified action after a given delay in milliseconds. Useful for
 *     scheduling events or animations in a thread-safe manner within the Swing environment.
 *     The primary usage is through the static {@link #waitAfter(int, Runnable)} method.
 * </p>
 * @see javax.swing.Timer
 * @see Runnable */
public class DelayTimer implements ActionListener {
    /* --- Fields --- */
    /** The action to execute after the delay. */
    private final Runnable action;
    /** Internal timer managing the delayed execution. */
    private Timer timer;

    /** Constructor
     * @param action the {@link Runnable} to execute after the delay. */
    public DelayTimer(Runnable action) {
        this.action = action;
    }

    /* --- Methods --- */
    /** Executes a given {@link Runnable} action after a specified delay.
     * @param delay     the delay in milliseconds before executing the action.
     * @param action    the {@link Runnable} action to execute. */
    public static void waitAfter(int delay, Runnable action) {
        Timer timer = new Timer(delay, _ -> action.run());
        timer.setRepeats(false);
        timer.start();
    }

    /* --- Listener --- */
    /** Called when the timer completes. Executes the stored action.
     * @param e the {@link ActionEvent} triggered by the timer. */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (action != null)
            action.run();
        if (timer != null)
            timer.stop();
    }
}