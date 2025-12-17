package controller.gameFlow;

import view.windows.MyFrame;

/** Controller for IntroductionWindow.
 * Handles actions triggered in the introduction screen, like pressing the Start button. */
public class IntroductionController {
    /** Called when the Start button is pressed.
     * The controller decides what happens next (e.g., open UserWindow). */
    public void startGame() {
        MyFrame.getInstance().setContent(new view.windows.UserWindow());
    }
}
