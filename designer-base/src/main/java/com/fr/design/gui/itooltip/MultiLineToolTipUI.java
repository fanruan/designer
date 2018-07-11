package com.fr.design.gui.itooltip;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JToolTip;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ToolTipUI;

import com.fr.base.FRContext;
import com.fr.base.GraphHelper;

public class MultiLineToolTipUI extends ToolTipUI {

    private String[] strs;
    public void paint(Graphics g, JComponent c) {
        FontMetrics metrics = GraphHelper.getFontMetrics(c.getFont());
        Dimension size = c.getSize();
        int width = size.width;
        int height = size.height;
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint paint = new GradientPaint(0, 0, new Color(250, 250, 250), 0, 50,
            new Color(165, 217, 249));
        g2.setPaint(paint);
        g2.fillRoundRect(0, 0, width, height, 5, 5);
        g2.setColor(Color.WHITE);
        g2.drawRoundRect(1, 1, width - 3, height - 3, 5, 5);
        g2.setColor(new Color(198, 198, 198));
        g2.drawRoundRect(0, 0, width - 1, height - 1, 5, 5);
        g2.setColor(c.getForeground());
        if (strs != null) {
            for (int i = 0; i < strs.length; i++) {
                g2.drawString(strs[i], 3, (metrics.getHeight()) * (i + 1));
            }
        }
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
    	FontMetrics metrics = GraphHelper.getFontMetrics(c.getFont());
        String tipText = ((JToolTip) c).getTipText();
        if (tipText == null) {
            tipText = " ";
        }
        BufferedReader br = new BufferedReader(new StringReader(tipText));
        String line;
        int maxWidth = 0;
        Vector<String> v = new Vector<String>();
        try {
            while ((line = br.readLine()) != null) {
                int width = SwingUtilities.computeStringWidth(metrics, line);
                maxWidth = (maxWidth < width) ? width : maxWidth;
                v.addElement(line);
            }
        } catch (IOException ex) {
            FRContext.getLogger().error(ex.getMessage(), ex);
        }
        int lines = v.size();
        if (lines < 1) {
            strs = null;
            lines = 1;
        } else {
            strs = new String[lines];
            int i = 0;
			for (Enumeration<String> e = v.elements(); e.hasMoreElements(); i++) {
                strs[i] = e.nextElement();
            }
        }
        int height = metrics.getHeight() * lines;
        return new Dimension(maxWidth + 11, height + 9);
    }
}