import controller.gameFlow.IntroductionController;
import controller.system.MessageController;
import view.windows.IntroductionWindow;
import view.windows.MyFrame;

import javax.swing.*;
import java.awt.*;

class Main{
    public static void main(String[] args){
        MyFrame frame = new MyFrame();
        MessageController messageController = MessageController.getInstance(frame);
        IntroductionController introController = new IntroductionController();
        frame.setContent(new IntroductionWindow(introController));
        frame.setVisible(true);
    }
}