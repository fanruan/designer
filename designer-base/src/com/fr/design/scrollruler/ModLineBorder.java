package com.fr.design.scrollruler;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.AbstractBorder;

import com.fr.design.constants.UIConstants;
import com.fr.design.utils.gui.GUICoreUtils;

/**
 * ModLineBorder, 显示某几个边框
 * august：写这个类的人，还是对swing的api不熟悉，不是已经有一个：
 * BorderFactory.createMatteBorder(top, left, bottom, right, color)方法了吗？
 */
public class ModLineBorder extends AbstractBorder {
    public static final int TOP = 0x00000001;
    public static final int LEFT = 0x00000002;
    public static final int BOTTOM = 0x00000004;
    public static final int RIGHT = 0x00000008;

    private int modifiers = 0;
    private int thickness;
    private Color lineColor;

    /**
     * Creates a line border with the specified color and a
     * thickness = 1.
     *
     * @param modifiers modifiers
     */
    public ModLineBorder(int modifiers) {
        this(modifiers, UIConstants.TITLED_BORDER_COLOR, 1);
    }

    /**
     * Creates a line border with the specified color and a
     * thickness = 1.
     *
     * @param modifiers modifiers
     * @param color     the color for the border
     */
    public ModLineBorder(int modifiers, Color color) {
        this(modifiers, color, 1);
    }

    /**
     * Creates a line border with the specified color, thickness,
     * and corner shape.
     *
     * @param modifiers modifiers
     * @param color     the color of the border
     * @param thickness the thickness of the border
     */
    public ModLineBorder(int modifiers, Color color, int thickness) {
        this.modifiers = modifiers;
        lineColor = color;
        this.thickness = thickness;
    }

    /**
     * Paints the border for the specified component with the
     * specified position and size.
     *
     * @param c      the component for which this border is being painted
     * @param g      the paint graphics
     * @param x      the x position of the painted border
     * @param y      the y position of the painted border
     * @param width  the width of the painted border
     * @param height the height of the painted border
     */
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Color oldColor = g.getColor();

        g.setColor(lineColor);
        for (int i = 0; i < thickness; i++) {
            if ((this.modifiers & TOP) != 0) {
                g.drawLine(x + i, y + i, x + width - i - 1, y + i);
            }
            if ((this.modifiers & LEFT) != 0) {
                g.drawLine(x + i, y + i, x + i, y + height - i - 1);
            }
            if ((this.modifiers & BOTTOM) != 0) {
                g.drawLine(x + i, y + height - i - 1, x + width - i - 1, y + height - i - 1);
            }
            if ((this.modifiers & RIGHT) != 0) {
                g.drawLine(x + width - i - 1, y + i, x + width - i - 1, height - i - i - 1);
            }
        }
        
        g.setColor(oldColor);
    }

    /**
     * Returns the insets of the border.
     *
     * @param c the component for which this border insets value applies
     */
    public Insets getBorderInsets(Component c) {    	
    	return getBorderInsets(c, new Insets(
    			thickness, thickness, thickness, thickness));
    }

    /**
     * Reinitialize the insets parameter with this Border's current Insets.
     *
     * @param c      the component for which this border insets value applies
     * @param insets the object to be reinitialized
     */
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = insets.top = insets.right = insets.bottom = thickness;

    	if ((this.modifiers & TOP) != 0 && (this.modifiers & BOTTOM) == 0) {
            insets.bottom = 0;
        }
        if ((this.modifiers & LEFT) != 0 && (this.modifiers & RIGHT) == 0) {
        	insets.right = 0;
        }
        if ((this.modifiers & BOTTOM) != 0 && (this.modifiers & TOP) == 0) {
        	insets.top = 0;
        }
        if ((this.modifiers & RIGHT) != 0 && (this.modifiers & LEFT) == 0) {
        	insets.left = 0;
        }
        
        return insets;
    }

    /**
     * TOP + LEFT + BOTTOM + RIGHT
     *
     * @return modifiers.
     */
    public int getModifiers() {
        return modifiers;
    }

    /**
     * TOP + LEFT + BOTTOM + RIGHT
     *
     * @param modifiers modifiers.
     */
    public void setModifiers(int modifiers) {
        this.modifiers = modifiers;
    }

    /**
     * Gets the color of the border.
     *
     * @return the color of the border.
     */
    public Color getLineColor() {
        return lineColor;
    }

    /**
     * Gets the thickness of the border.
     *
     * @return the thickness of the border.
     */
    public int getThickness() {
        return thickness;
    }
}