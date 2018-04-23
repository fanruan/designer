package com.fr.design.chart;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;

import com.fr.stable.core.PropertyChangeAdapter;
import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.layout.FRGUIPaneFactory;

/**
 * 存放ChartComponent, 可调整大小的Pane.
 */
public class ChartComponentPane extends JPanel implements MouseMotionListener, MouseListener {
	private static final long serialVersionUID = -77093070905580457L;

	// 给设计界面加一个边框，方便拖拽
    private static final int BORDER_STYLE = 10;

    private List<PropertyChangeAdapter> listeners = new ArrayList<PropertyChangeAdapter>();
    private List<PropertyChangeAdapter> slisteners = new ArrayList<PropertyChangeAdapter>();

    private ChartComponent chartComponent;

    private boolean select = false;

    public ChartComponentPane(ChartComponent chartComponent) {
        initComponents(chartComponent);
    }

    private void initComponents(ChartComponent chartComponent) {
        this.chartComponent = chartComponent;
        this.chartComponent.setLocation(this.getLocation());

        this.setBorder(designBorder);
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.chartComponent.addStopEditingListener(new PropertyChangeAdapter() {
            @Override
            public void propertyChange() {
                stopEditing();
            }
        });
        this.add(chartComponent);

        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    public void addSizeChangedListener(PropertyChangeAdapter l) {
        slisteners.add(l);
    }

    public void addStopListener(PropertyChangeAdapter l) {
        listeners.add(l);
    }

    private void stopEditing() {
        fireChanged(listeners);
    }

    private Border designBorder = new AbstractBorder() {
		private static final long serialVersionUID = 1802897702350872754L;

		@Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Color oldColor = g.getColor();
            g.setColor(new Color(157, 228, 245));
            for (int i = 0; i < BORDER_STYLE; i++) {
                g.drawRect(x + i, y + i, width - i - i - 1, height - i - i - 1);
            }
            g.setColor(oldColor);
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(0, 0, BORDER_STYLE, BORDER_STYLE);
        }
    };

    @Override
    public void mouseDragged(MouseEvent e) {
        if (select) {
            if (this.getCursor() == Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR)) {
                this.setSize(e.getX(), getSize().height);
                this.setPreferredSize(new Dimension(e.getX(), getSize().height));
            } else if (this.getCursor() == Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR)) {
                this.setSize(getSize().width, e.getY());
                this.setPreferredSize(new Dimension(getSize().width, e.getY()));
            } else if (this.getCursor() == Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR)) {
                this.setSize(e.getX(), e.getY());
                this.setPreferredSize(new Dimension(e.getX(), e.getY()));
            }
        }
        fireSizeChanged();
        super.repaint();
    }

    public void mouseMoved(MouseEvent e) {
        Rectangle rect = this.getBounds();
        if (atCorner(e, rect)) {
            setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
        } else if (atBottomLine(e, rect)) {
            setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
        } else if (atRightLine(e, rect)) {
            setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
        } else {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private boolean atBottomLine(MouseEvent e, Rectangle rect) {
        return e.getY() < rect.height && e.getY() > rect.height - BORDER_STYLE;
    }

    private boolean atRightLine(MouseEvent e, Rectangle rect) {
        return e.getX() < rect.width && e.getX() > rect.width - BORDER_STYLE;
    }

    private boolean atCorner(MouseEvent e, Rectangle rect) {
        return atBottomLine(e, rect) && atRightLine(e, rect);
    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {
        if (this.getCursor() == Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR)
                || this.getCursor() == Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR)
                || this.getCursor() == Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR)) {
            this.select = true;
        }
    }

    public void mouseReleased(MouseEvent e) {
        this.select = false;
        this.repaint();
    }

    private void fireSizeChanged() {
        fireChanged(slisteners);
    }

    private void fireChanged(List<PropertyChangeAdapter> ls) {
        for (int len = ls.size(), i = len; i > 0; i--) {
            ls.get(i - 1).propertyChange();
        }
    }
}