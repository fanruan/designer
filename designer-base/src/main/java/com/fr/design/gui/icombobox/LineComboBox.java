/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.gui.icombobox;

import com.fr.base.FRContext;
import com.fr.base.GraphHelper;
import com.fr.base.ScreenResolution;
import com.fr.general.FRFont;
import com.fr.stable.Constants;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JList;


/**
 * Combobox for selecting line styles.
 */
public class LineComboBox extends UIComboBox {
    /**
     * Constructor.
     *
     * @param lineStyleArray the array of lineStyle.
     */
    public LineComboBox(int[] lineStyleArray) {
        //copy lineStyle.
        Integer[] lineStyleIntegerArray = new Integer[lineStyleArray.length];

        for (int i = 0; i < lineStyleArray.length; i++) {
            lineStyleIntegerArray[i] = new Integer(lineStyleArray[i]);
        }

        this.setModel(new DefaultComboBoxModel(lineStyleIntegerArray));

        this.setRenderer(new LineCellRenderer());
    }

    /**
     * Get selected line style.
     */
    public int getSelectedLineStyle() {
        int style = ((Integer) getSelectedItem()).intValue();

        return (style < 0) ? Constants.LINE_NONE : style;
    }

    /**
     * Set the selected line style.
     */
    public void setSelectedLineStyle(int style) {
    	this.setSelectedItem(new Integer(style));
    }

    /**
     * CellRenderer.
     */
    class LineCellRenderer extends UIComboBoxRenderer {
        public Component getListCellRendererComponent(
                JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        	JLabel comp= (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            this.style = ((Integer) value).intValue();
            comp.setText(null);
            return comp;
        }

        public void paint(Graphics g) {
        	super.paint(g);
            Graphics2D g2d = (Graphics2D) g;

            Dimension d = getSize();
            g2d.setColor(getForeground());
            
            FRFont font = FRContext.getDefaultValues().getFRFont();
            int resolution = ScreenResolution.getScreenResolution();
            Font rfont = font.applyResolutionNP(resolution);
            g2d.setFont(rfont);
            FontMetrics fm = GraphHelper.getFontMetrics(rfont);
            if (style == Constants.LINE_NONE) {
                //draw "none" string
                GraphHelper.drawString(g2d, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_None"), 4, (d.height - fm.getHeight()) / 2D + fm.getAscent());
            } else {
                GraphHelper.drawLine(g2d, 4, d.height / 2D, d.width - 8D, d.height / 2D, style);
            }
            
            if(isShowAxisWithLineStyle()) { //  带有坐标轴箭头的样式. 
                drawArrow(g2d, new Point2D.Double(4, d.height / 2D), new Point2D.Double(d.width - 8D, d.height / 2D));
            }
        }
        
        private void drawArrow(Graphics2D g2d, Point2D p0, Point2D p1) {
        	Point2D s = new Point2D.Double(p1.getX() - p0.getX(), p1.getY() - p0.getY());
        	Point2D t = new Point2D.Double();
        	double d1 = p0.distance(p1);
        	//d2-d5设定箭头的大小,p1-p2为坐标轴的延长线,p2-p5-p3-p6为箭头4个点的具体位置
        	double d2 = 9;
        	double d3 = 15;
        	double d4 = 7;
        	double d5 = 3;
        	t.setLocation(d2 * s.getX() / d1, d2 * s.getY() / d1);
        	Point2D p2 = new Point2D.Double(p1.getX() + t.getX(), p1.getY() + t.getY());
        	t.setLocation(d3 * s.getX() / d1, d3 * s.getY() / d1);
        	Point2D p3 = new Point2D.Double(p1.getX() + t.getX(), p1.getY() + t.getY());
        	t.setLocation(d4 * s.getX() / d1, d4 * s.getY() / d1);
        	Point2D p4 = new Point2D.Double(p1.getX() + t.getX(), p1.getY() + t.getY());
        	Point2D p5 = new Point2D.Double(p4.getX() + s.getY() / d1 * d5, p4.getY() - s.getX() / d1 * d5);
        	Point2D p6 = new Point2D.Double(p4.getX() - s.getY() / d1 * d5, p4.getY() + s.getX() / d1 * d5);
        	
        	GeneralPath arrow = new GeneralPath();
        	arrow.moveTo((float) p2.getX() - 10, (float) p2.getY());
         	arrow.lineTo((float) p5.getX() - 10, (float) p5.getY());
        	arrow.lineTo((float) p3.getX() - 10, (float) p3.getY());
        	arrow.lineTo((float) p6.getX() - 10, (float) p6.getY());
        	arrow.closePath();
        	
        	g2d.draw(arrow);
        	g2d.fill(arrow);
        }
        
        private boolean isShowAxisWithLineStyle() {
        	return style == Constants.LINE_CHART_MED_ARROW 
        		|| style == Constants.LINE_CHART_THICK_ARROW || style == Constants.LINE_CHART_THIN_ARROW;
        }

        public Dimension getPreferredSize() {
            return new Dimension(60, 16);
        }

        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        private int style = Constants.LINE_NONE;
    }
}
