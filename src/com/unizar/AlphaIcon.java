/*
 * @(#)AlphaIcon.java 1.0 08/16/10
 * https://tips4java.wordpress.com/2010/08/22/alpha-icons/
 */

package com.unizar;


import javax.swing.*;
import java.awt.*;

/**
 * An Icon wrapper that paints the contained icon with a specified transparency.
 * <p>
 * <B>Note:</B> This class is not suitable for wrapping an <CODE>ImageIcon</CODE>
 * that holds an animated image.  To show an animated ImageIcon with transparency,
 * use the companion class {@link AlphaImageIcon}.
 *
 * @author Darryl
 * @version 1.0 08/16/10
 */
public class AlphaIcon implements Icon {

    private final Icon icon;
    private float alpha;

    /**
     * Creates an <CODE>com.unizar.AlphaIcon</CODE> with the specified icon and opacity.
     * The opacity <CODE>alpha</CODE> should be in the range 0.0F (fully transparent)
     * to 1.0F (fully opaque).
     *
     * @param icon  the Icon to wrap
     * @param alpha the opacity
     */
    public AlphaIcon(Icon icon, float alpha) {
        this.icon = icon;
        this.alpha = alpha;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    /**
     * Paints the wrapped icon with this <CODE>com.unizar.AlphaIcon</CODE>'s transparency.
     *
     * @param c The component to which the icon is painted
     * @param g the graphics context
     * @param x the X coordinate of the icon's top-left corner
     * @param y the Y coordinate of the icon's top-left corner
     */
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.SrcAtop.derive(alpha));
        icon.paintIcon(c, g2, x, y);
        g2.dispose();
    }

    /**
     * Gets the width of the bounding rectangle of this <CODE>com.unizar.AlphaIcon</CODE>.
     * Returns the width of the wrapped icon.
     *
     * @return the width in pixels
     */
    @Override
    public int getIconWidth() {
        return icon.getIconWidth();
    }

    /**
     * Gets the height of the bounding rectangle of this <CODE>com.unizar.AlphaIcon</CODE>.
     * * Returns the height of the wrapped icon.
     *
     * @return the height in pixels
     */
    @Override
    public int getIconHeight() {
        return icon.getIconHeight();
    }
}
