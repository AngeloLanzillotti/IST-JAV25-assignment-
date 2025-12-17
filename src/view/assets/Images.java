package view.assets;

import javax.swing.*;
import java.awt.*;

/** Utility class providing static methods for loading and resizing image resources used throughout the application's graphical
 * user interface.
 * This class is abstract to prevent instantiation, as all its methods are static. */
public abstract class Images {
    /* --- Methods --- */
    /** Loads an image from the specified file path and scales it to the desired dimensions.
     * The method uses {@code Image.SCALE_SMOOTH} for a high-quality scaling algorithm, which is suitable for displaying images in a GUI.
     * @param path The file path (relative or absolute) of the image resource.
     * @param width The target width, in pixels, for the scaled image.
     * @param height The target height, in pixels, for the scaled image.
     * @return An {@link ImageIcon} containing the loaded and scaled image. */
    public static ImageIcon load(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image image = icon.getImage();
        Image scaled = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }
}