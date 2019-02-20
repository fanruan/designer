package com.fr.design.gui.itooltip;

import com.fr.base.GraphHelper;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JToolTip;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ToolTipUI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: pony
 * Date: 13-5-16
 * Time: 下午2:42
 * To change this template use File | Settings | File Templates.
 */
public class UIToolTipUI extends ToolTipUI {
    private String[] strs;
    private Icon icon;
    private boolean needPaint;
    public void paint(Graphics g, JComponent c) {
        if (!needPaint) {
            return;
        }
        FontMetrics metrics = GraphHelper.getFontMetrics(c.getFont());
        Dimension size = c.getSize();
        int width = size.width;
        int height = size.height;
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint paint = new GradientPaint(0, 0, new Color(212, 212, 216), 0, height ,new Color(171, 183, 203));
        g2.setPaint(paint);
        g2.fillRoundRect(0, 0, width - 3, height - 3, 6, 6);
        g2.setColor(new Color(100, 100, 100));
        g2.drawRoundRect(0, 0, width - 3, height - 3, 6, 6);

        GradientPaint cornerPaint = new GradientPaint(width-3, height-3, new Color(100, 100, 100, 191), width - 1, height - 1, new Color(212, 212, 216));
        g2.setPaint(cornerPaint);
        g2.fillArc(width - 6, height - 6, 6, 6, 270, 90 );

        GeneralPath shadowDown = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 4);
        shadowDown.moveTo(0, height - 3);
        shadowDown.lineTo(3, height);
        shadowDown.lineTo(width - 3, height);
        shadowDown.lineTo(width - 3, height - 3);
        GradientPaint downPaint = new GradientPaint(0, height-3, new Color(100, 100, 100, 191), 0, height, new Color(212, 212, 216));
        g2.setPaint(downPaint);
        GraphHelper.fill(g2, shadowDown);

        GeneralPath shadowRight = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 4);
        shadowRight.moveTo(width - 3, height - 3);
        shadowRight.lineTo(width, height - 3);
        shadowRight.lineTo(width, 3);
        shadowRight.lineTo(width - 3, 0);
        GradientPaint rightPaint = new GradientPaint(width - 3, 0, new Color(100, 100, 100, 191), width, 0, new Color(212, 212, 216));
        g2.setPaint(rightPaint);
        GraphHelper.fill(g2, shadowRight);

        g2.setColor(c.getForeground());
        if (icon instanceof ImageIcon) {
            ImageIcon imageIcon = (ImageIcon)icon;
            g2.drawImage(imageIcon.getImage(), 2 , 2, imageIcon.getImageObserver());
        }
        if (strs != null) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);
            for (int i = 0; i < strs.length; i++) {
                g2.drawString(strs[i], icon.getIconWidth() + 6, (metrics.getHeight()) * (i + 1));
            }
        }
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        FontMetrics metrics = GraphHelper.getFontMetrics(c.getFont());
        String tipText = ((JToolTip) c).getTipText();
        icon = ((UIToolTip)c).getIcon();
        needPaint = true;
        if (tipText == null) {
            if(icon.getIconWidth() == -1) {
                needPaint = false;
            }
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
            FineLoggerFactory.getLogger().error(ex.getMessage(), ex);
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
        return new Dimension(maxWidth + icon.getIconWidth() + 10, Math.max(height, icon.getIconHeight()) + 6);
    }

}