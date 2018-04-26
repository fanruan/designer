package com.fr.design.style.color;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

import com.fr.design.constants.UIConstants;
import com.fr.general.ComparatorUtils;

public class ColorCell extends JComponent implements ColorSelectable {
	
	private Color color = Color.WHITE;
	
	private ColorSelectable colorSelectable;

    /**
     * Construct a default color cell. The default color is white.
     */
    public ColorCell() {
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        setPreferredSize(new Dimension(16, 16));
//        setBorder(new LineBorder(Color.gray));

        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    /**
     * Construct a color cell with the initial color.
     * @param color cell color.
     */
    public ColorCell(Color color, ColorSelectable colorSelectable) {
        this();
        setColor(color);
        this.colorSelectable = colorSelectable;
    }

    /**
     * Set the color of this cell.
     * @param color cell color.
     */
    public void setColor(Color color) {
        this.color = color;
        repaint();
    }

    /**
     * Get the color of this color cell.
     */
    public Color getColor() {
        return color;
    }

    //Paint color cell.
    public void paintComponent(Graphics g) {
        Dimension d = getSize();
        Insets b = getInsets();
        int right = d.width - b.left - b.right;
        int bottom = d.height - b.top - b.bottom;

        // transparent
        if (color == null) {
            g.setColor(Color.WHITE);
            g.fillRect(b.left, b.top, d.width - b.left - b.right,
                d.height - b.top - b.bottom);
            g.setColor(Color.gray);
            g.drawLine(b.left, b.top, right, bottom);
            g.drawLine(right, b.top, b.left, bottom);
        } else {
            g.setColor(color);
            g.fillRect(b.left, b.top, right, bottom);
        }

        if (colorSelectable.getColor() != null
            &&  ComparatorUtils.equals(colorSelectable.getColor(), this.getColor())) {

            g.setColor(UIConstants.LINE_COLOR);
            g.drawRect(b.left, b.top, right - 1, bottom - 1);
        }
    }

    /**
     * 鼠标事件处理
     * @param 鼠标事件
     */
    public void processMouseEvent(MouseEvent e) {
        if (e == null || e.getID() == MouseEvent.MOUSE_RELEASED) {
            colorSelectable.setColor(this.getColor());
            colorSelectable.colorSetted(this);
        }

        if (e != null) {
            super.processMouseEvent(e);
        }
    }

    /**
     * Get the minimum size of this cell.
     * @return minimum size.
     */
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    /**
     * 选中颜色
     * @param 颜色单元格
     * 
     */
	@Override
	public void colorSetted(ColorCell colorCell) {
		
	}
}