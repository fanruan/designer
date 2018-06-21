package com.fr.design.gui.iprogressbar;

import com.fr.design.utils.ThemeUtils;

import javax.swing.LookAndFeel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

/**
 * 新进度条UI(暂时只处理了非模糊场景)
 * Created by zack on 2018/6/21.
 */
public class ModernUIProgressBarUI extends UIProgressBarUI {
    // draw determinate
    @Override
    protected void drawXpHorzProgress(Graphics g, int x, int y,
                                      int w, int h, int amountFull) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(x, y);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (!progressBar.isOpaque()) {
            //绘制进度条背板
            g2d.setColor(progressBar.getBackground());
            RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(0, 0, w, h, 10, 10);
            g2d.fill(roundedRectangle);
        }
        //
        g2d.setColor(progressBar.getForeground());
        int mx = 0;
        while (mx < amountFull) {
            RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(0, 0, amountFull, h, 10, 10);
            g2d.fill(roundedRectangle);
            mx += 8;
        }
        g2d.translate(-x, -y);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }


    // draw determinate
    @Override
    protected void drawXpVertProgress(Graphics g, int x, int y,
                                      int w, int h, int amountFull) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.translate(x, y);
        // paint the track
        if (!progressBar.isOpaque()) {
            g.setColor(progressBar.getBackground());
            RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(0, 0, w, h, 10, 10);
            g2d.fill(roundedRectangle);
        }
        // paints bottom to top...
        int my = 0;
        while (my < amountFull) {
            RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(0, 0, w, amountFull, 10, 10);
            g2d.fill(roundedRectangle);
            my += 8;
        }
        g.translate(-x, -y);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }

    @Override
    protected Color getSelectionForeground() {
        return ThemeUtils.PROCESS_SELECTED_FORECOLOR;
    }

    @Override
    protected Color getSelectionBackground() {
        return ThemeUtils.PROCESS_SELECTED_BACKCOLOR;
    }

    @Override
    protected void installDefaults() {
        LookAndFeel.installBorder(progressBar, "ProgressBar.border");
        LookAndFeel.installColorsAndFont(progressBar,
                "ProgressBar.modern.background", "ProgressBar.modern.foreground", "ProgressBar.font");
    }
}
