package controller.system;

import view.windows.MyFrame;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.*;

/** Controller responsible for managing message display in a {@link MyFrame}.
 * <p>
 *     This class handles:
 *     - Queueing messages
 *     - Timing message display
 *     - Executing a callback when the queue finishes
 *     It separates the display logic (timing, queuing) from the View, ensuring {@link MyFrame} remains a pure view component.
 *      This class implements the Singleton pattern.
 * </p>
 * @see MyFrame */
public class MessageController {
    /* --- Fields --- */
    /** Singleton instance of the MessageController. Ensures that all windows share the same controller for managing message
     * queues and display, preventing loss of messages when switching between views. */
    private static MessageController instance;
    /** Reference to the frame where messages are displayed. */
    private final MyFrame frame;
    /** Queue storing pending messages. */
    private final Queue<Message> messageQueue = new LinkedList<>();
    /** Flag indicating whether a message is currently being displayed. */
    private boolean messageShowing = false;
    /** A {@link Runnable} executed once the message queue is fully empty. */
    private Runnable callback;

    /** Constructor
     * @param frame the frame where messages will be displayed. */
    public MessageController(MyFrame frame) {
        this.frame = frame;
    }

    /* --- Methods --- */
    /** Adds a new message to the queue to be displayed sequentially.
     * @param text          the message text.
     * @param durationMs    how long to display the message in milliseconds.
     * @param color         the text color. */
    public void showMessage(String text, int durationMs, Color color) {
        messageQueue.add(new Message(text, durationMs, color));
        processQueue();
    }

    /** Sets a callback to be executed after all messages in the queue have been displayed. The callback is cleared and
     * executed only once upon queue completion.
     * @param callback the {@link Runnable} to execute after queue completion. */
    public void runAfterMessages(Runnable callback) {
        this.callback = callback;
    }

    /* --- Helpers --- */
    /** Processes the message queue, showing messages one by one using a {@link Timer}.
     * This method ensures only one message is shown at a time and triggers the {@link #callback} when the queue is empty. */
    private void processQueue() {
        if (messageShowing)
            return;
        if(messageQueue.isEmpty()) {
            if(callback != null) {
                Runnable cb = callback;
                callback = null;
                cb.run();
            }
            return;
        }
        Message message = messageQueue.poll();
        messageShowing = true;
        frame.displayMessage(message.text, message.color);
        Timer timer = new Timer(message.durationMs, _ -> {
            frame.hideMessage();
            messageShowing = false;
            processQueue();
        });
        timer.setRepeats(false);
        timer.start();
    }

    /* --- Getters --- */
    /** Getter method
     * @param frame the frame where messages will be displayed (used only when the singleton is first created).
     * @return the shared MessageController instance. */
    public static MessageController getInstance(MyFrame frame) {
        if (instance == null)
            instance = new MessageController(frame);
        return instance;
    }

    /** Returns the shared {@link MessageController} instance after it has been initialized.
     * This is a convenience method for accessing the instance from controllers or non-GUI classes.
     * @return the shared MessageController instance.
     * @throws IllegalStateException if the controller has not been initialized with a frame yet. */
    public static MessageController getInstance() {
        if (instance == null)
            throw new IllegalStateException("MessageController not initialized with a frame yet!");
        return instance;
    }

    /* --- Inner class --- */
    /** Inner class representing a message in the queue with its display parameters. */
    private static class Message {
        /** The text content of the message. */
        final String text;
        /** The duration (in milliseconds) the message should be displayed. */
        final int durationMs;
        /** The color of the message text. */
        final Color color;

        /** Constructor.
         * @param text          the message text.
         * @param durationMs    how long to display the message in milliseconds.
         * @param color         the text color. */
        Message(String text, int durationMs, Color color) {
            this.text = text;
            this.durationMs = durationMs;
            this.color = color;
        }
    }
}
