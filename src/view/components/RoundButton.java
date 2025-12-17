package view.components;

import javax.swing.*;
import java.awt.*;

/** Custom JButton implementation that renders itself as a fully rounded button.
 * <p>
 *     This class allows the creation of circular or rounded buttons with customizable colors for text, background, and
 *     border, as well as adjustable border thickness.
 *     It supports both text-based buttons and image-based buttons, ensuring that the content is properly centered inside
 *     the circular shape.
 * </p> <p>
 *     Unlike a standard JButton, this class overrides the default painting behavior to draw a rounded shape using
 *     {@link Graphics2D}. The button becomes visually circular based on its minimum dimension (width or height).
 *     By default, the button does not paint its standard background, border, or focus indicator.
 * </p>
 * This component is suitable for stylized UIs, icon buttons, or circular menu elements in the game's GUI.
 * @see JButton
 * @see Graphics2D */
public class RoundButton extends JButton {
    /* --- Fields --- */
    /** The color used to render the button's text. */
    private Color textColor;
    /** The fill color of the circular background. */
    private Color backgroundColor;
    /** The stroke color used to draw the rounded border. */
    private Color borderColor;
    /** The thickness of the border, measured in pixels. */
    private int borderSize;

    /** Constructor for text button
     * @param text the text to display inside the button. */
    public RoundButton(String text) {
        super(text);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
        setVisible(true);
    }
    /** Constructor for image button
     * @param icon the image icon to display inside the button. */
    public RoundButton(ImageIcon icon) {
        super(icon);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
        setVisible(true);
    }

    /* --- Methods --- */
    /** Custom painting method responsible for rendering the circular shape, border, and content of the button.
     * <p>
     *     This method:
     *     - Computes the diameter based on the smallest dimension
     *     - Draws a filled circle using {@code backgroundColor}
     *     - Draws a circular border using {@code borderColor} and {@code borderSize}
     *     - Centers and draws text manually if no icon is present
     *     - Falls back to the default JButton painting for icons
     * </p>
     * @param g the graphics context used for rendering. */
    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        int diameter = Math.min(getWidth(), getHeight());
        int offset = borderSize / 2;

        g2.setColor(backgroundColor);
        g2.fillOval(offset, offset, diameter - borderSize, diameter - borderSize);

        g2.setStroke(new BasicStroke(borderSize));
        g2.setColor(borderColor);
        g2.drawOval(offset, offset, diameter - borderSize, diameter - borderSize);

        if (getIcon() == null && getText() != null) {
            g2.setColor(textColor);
            /* Compute the coordinates */
            FontMetrics fm = g2.getFontMetrics();
            int x = (diameter - fm.stringWidth(getText())) / 2;
            int y = (diameter + fm.getAscent()) / 2 - 2;
            g2.drawString(getText(), x, y);
        } else
            super.paintComponent(g);
        g2.dispose();
    }

    /* --- Setters --- */
    /** Setter method
     * @param textColor the new text color. */
    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }
    /** Setter method
     * @param backgroundColor the new fill color. */
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    /** Setter method
     * @param borderColor the new border color. */
    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }
    /** Setter method
     * @param borderSize the new border size in pixels. */
    public void setBorderSize(int borderSize) {
        this.borderSize = borderSize;
    }
}
