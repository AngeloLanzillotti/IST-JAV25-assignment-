package view.components;

import javax.swing.*;
import java.awt.*;

/** Custom JLabel implementation used within the equipment window UI.
 * <p>
 *     This class provides a predefined visual style for labels that display equipment names or item descriptions in the
 *     game's {@code EquipmentWindow}. It centers the label's text horizontally, aligns it to the top vertically, and
 *     applies a bold red font for visual emphasis.
 * </p> <p>
 *     Interaction with the label is intentionally disabled, making it a purely visual and non-interactive component.
 *     The label ignores repaint requests and user focus events to ensure consistent rendering inside static UI panels.
 * </p>
 * Typical usage includes headers, section titles, or item name displays within inventory or equipment interfaces.
 * @see JLabel */
public class ItemLabel extends JLabel {
    /** Constructor
     * @param text the string to display inside the label. */
    public ItemLabel(String text){
        setText(text);
        setHorizontalAlignment(JLabel.CENTER);
        setVerticalAlignment(JLabel.TOP);
        setForeground(Color.red);
        setFont(new Font("Times New Roman", Font.BOLD, 15));
        setSize(125, 30);

        /* disable label interaction */
        addMouseListener(new java.awt.event.MouseAdapter() {});
        setFocusable(false);
        setIgnoreRepaint(true);
        setOpaque(false);
        setEnabled(true);
    }
}
