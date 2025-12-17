package view.panels;

import controller.eventListeners.DropListener;
import model.items.base.Item;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.function.*;

/** Represents a customizable panel designed for a single equipment item (Armor, Sword, Potion, etc.).
 * <p>
 *     This panel is draggable and implements logic to detect when it is dropped onto a valid target slot ({@link JPanel})
 *     and handles the delegation of equip/unequip actions to the {@link DropListener} (the Controller).
 *     It enforces the "one item per slot" rule.
 * </p>
 * @param <T> The specific type of {@link Item} displayed in this panel. */
public class EquipmentPanel<T extends Item> extends JPanel {
    /* --- Fields --- */
    /** The item object represented by this panel. */
    private T item;
    /** The listener (Controller) responsible for handling the item drop logic (equip/unequip). */
    private DropListener dropListener;
    /** The name of the panel, used to identify the item type (e.g., "Armor", "Sword") when communicating with the Controller. */
    private final String panelName;
    /** The previous point of the mouse drag event, used for calculating movement. */
    private Point prevPt;
    /** Array of target panels where this equipment panel can be dropped and 'snapped'. */
    private JPanel[] dropTargets;
    /** The default color for the borders of the drop target panels. */
    private final Color NORMAL_COLOR = Color.red;
    /** The color used to highlight the border of a drop target panel when the cursor is near. */
    private final Color HIGHLIGHT_COLOR = Color.yellow;
    /** The thickness of the border used for highlighting drop targets. */
    private final int thickness = 6;
    /** The original location of the panel before any dragging occurred. */
    private Point originalLocation;
    /** The original parent container (usually the {@link view.windows.EquipmentWindow}), used as the drag layer. */
    private final JPanel originalParent;
    /** The currently occupied slot ({@link JPanel}) this equipment panel is logically associated with, or {@code null}. */
    private JPanel currentSlot = null;

    /** Constructor
     * @param mainPanel The main container panel (the {@link view.windows.EquipmentWindow}) that serves as the drag layer.
     * @param panelName The display name of the equipment type (e.g., "Armor"). */
    public EquipmentPanel(JPanel mainPanel, String panelName) {
        this.panelName = panelName;

        ClickListener clickListener = new ClickListener();
        DragListener dragListener = new DragListener();
        this.addMouseListener(clickListener);
        this.addMouseMotionListener(dragListener);
        this.originalLocation = getLocation();
        this.originalParent = mainPanel;
    }

    /* --- Methods --- */
    /** Sets the drop target panels where this equipment panel can be successfully dropped to trigger an equip action.
     * Initializes the border color of the targets.
     * @param panels An array of {@link JPanel} instances that serve as drop targets. */
    public void setDropTargets(JPanel... panels){
        this.dropTargets = panels;
        for (JPanel panel : panels)
            panel.setBorder(new LineBorder(NORMAL_COLOR, thickness));
    }

    /* --- Helpers --- */
    /** Checks if the panel has been released close enough to a drop target to 'snap' into it.
     * If a valid target is found, it first verifies if the slot is occupied by another panel.
     * If the slot is free, it notifies the {@link DropListener} (Controller).
     * If no snap occurs, it triggers {@link #notifyControllerUnequip()}. */
    private void checkForSnap() {
        Rectangle myBounds = SwingUtilities.convertRectangle(
                getParent(),
                getBounds(),
                originalParent
        );
        boolean snapped = false;
        for (JPanel target : dropTargets) {
            Rectangle targetBounds = SwingUtilities.convertRectangle(
                    target.getParent(),
                    target.getBounds(),
                    originalParent
            );
            if (myBounds.intersects(targetBounds)) {
                /* Verify if slot is free */
                if (target.getComponentCount() > 0 && target.getComponent(0) != this) {
                    /* Slot is occupied, prevent snap and notify Controller to revert the current panel. */
                    notifyControllerUnequip();
                    return;
                }
                snapped=true;
                if(dropListener!=null)
                    /* Notify listener */
                    dropListener.handleItemDrop(panelName, target, item);
                break;
            }
        }
        if(!snapped)
            notifyControllerUnequip();
    }

    /** Notifies the Controller when the panel is released outside a valid drop slot or when a snap is blocked.
     * This triggers the Controller to handle a potential unequip action or revert the panel's position. */
    private void notifyControllerUnequip(){
        if(dropListener != null)
            /* Target is null, indicating a potential unequip or position reset. */
            dropListener.handleItemDrop(panelName, null, item);
    }

    /** Helper method to prepare the panel for dragging.
     * Moves the panel from its current parent (if it was in a slot) to the {@code originalParent} (the drag layer),
     * and sets its location to maintain the visual position during the transition. */
    private void moveToDragLayer() {
        if (getParent() != originalParent) {
            Point currentAbsoluteLocation = SwingUtilities.convertPoint(
                    getParent(),
                    getLocation(),
                    originalParent
            );
            getParent().remove(this);
            /* Add and bring the panel in the front (Z-order 0) */
            originalParent.add(this, 0);
            setLocation(currentAbsoluteLocation);
            /* Update the GUI */
            originalParent.revalidate();
            originalParent.repaint();
        }
    }

    /** Updates the borders of the drop targets, highlighting them if this panel is within the snap distance. */
    private void updateHighlights() {
        Rectangle myBounds = getBounds();
        for (JPanel target : dropTargets) {
            /* Compute the distance between the two panels */
            Rectangle targetBounds = target.getBounds();
            Point myCenter = new Point(myBounds.x + myBounds.width / 2, myBounds.y + myBounds.height / 2);
            Point targetCenter = new Point(targetBounds.x + targetBounds.width / 2, targetBounds.y + targetBounds.height / 2);
            double distance = myCenter.distance(targetCenter);
            int SNAP_DISTANCE = 50;
            if (distance <= SNAP_DISTANCE)
                target.setBorder(new LineBorder(HIGHLIGHT_COLOR, thickness));
            else
                target.setBorder(new LineBorder(NORMAL_COLOR, thickness));
        }
        getParent().repaint();
    }

    /** Resets the borders of all drop targets back to the {@code NORMAL_COLOR}. */
    private void clearHighlights() {
        for (JPanel target : dropTargets)
            target.setBorder(new LineBorder(NORMAL_COLOR, thickness));
    }

    public Point getOriginalLocation() {
        return originalLocation;
    }

    /* --- Inner class --- */
    /** Handles mouse press and release events, initiating the drag preparation and checking for snap on release. */
    private class ClickListener extends MouseAdapter {
        /** Saves the click point, moves the panel to the drag layer, and brings it to the front.
         * @param e The mouse event. */
        public void mousePressed(MouseEvent e){
            prevPt = e.getPoint();
            moveToDragLayer();

            originalParent.setComponentZOrder(EquipmentPanel.this, 0);
            originalParent.repaint();
        }
        /** Checks for snap into a drop target when the mouse is released and clears any highlights.
         * @param e The mouse event. */
        public void mouseReleased(MouseEvent e) {
            checkForSnap();
            clearHighlights();
        }
    }
    /* --- Inner class --- */
    /** Handles mouse motion events, updating the panel's location during a drag operation. */
    private class DragListener extends MouseMotionAdapter {
        /** Updates the panel's position and highlights nearby drop targets.
         * @param e The mouse event. */
        public void mouseDragged(MouseEvent e){
            Point newPoint = SwingUtilities.convertPoint(EquipmentPanel.this, e.getPoint(), originalParent);
            setLocation(newPoint.x - prevPt.x, newPoint.y - prevPt.y);
            if (dropTargets != null)
                updateHighlights();
        }
    }

    /* --- Getters --- */
    /** Getter method.
     * @return The item. */
    public T getItem() {
        return item;
    }
    /** Getter method.
     * @return The current slot, or {@code null} if the item is in its default position. */
    public JPanel getCurrentSlot() {
        return currentSlot;
    }
    /** Getter method.
     * @return The panel's name (e.g., "Armor", "Sword"). */
    public String getPanelName() {
        return panelName;
    }
    /** Getter method.
     * @return The previous mouse {@link Point}. */
    public Point getPrevPt() {
        return prevPt;
    }
    /** Getter method.
     * @return An array of target {@link JPanel}s. */
    public JPanel[] getDropTargets() {
        return dropTargets;
    }
    /** Getter method.
     * @return The {@link Color} for normal borders. */
    public Color getNORMAL_COLOR() {
        return NORMAL_COLOR;
    }
    /** Getter method.
     * @return The {@link Color} for highlighted borders. */
    public Color getHIGHLIGHT_COLOR() {
        return HIGHLIGHT_COLOR;
    }
    /** Getter method.
     * @return The thickness value in pixels. */
    public int getThickness() {
        return thickness;
    }
    /** Getter method.
     * @return The original parent {@link JPanel}. */
    public JPanel getOriginalParent() {
        return originalParent;
    }

    /* --- Setters --- */
    /** Setter method.
     * @param item The item to set. */
    public void setItem(T item) {
        this.item = item;
    }
    /** Setter method.
     * @param listener The {@link DropListener}. */
    public void setDropListener(DropListener listener) {
        this.dropListener = listener;
    }
    /** Setter method.
     * @param prevPt The new previous mouse {@link Point}. */
    public void setPrevPt(Point prevPt) {
        this.prevPt = prevPt;
    }
    /** Setter method.
     * @param originalLocation The initial {@link Point} location. */
    public void setOriginalLocation(Point originalLocation) {
        this.originalLocation = originalLocation;
    }
    /** Setter method.
     * @param currentSlot The new current slot, or {@code null} if unequipped. */
    public void setCurrentSlot(JPanel currentSlot) {
        this.currentSlot = currentSlot;
    }
}
