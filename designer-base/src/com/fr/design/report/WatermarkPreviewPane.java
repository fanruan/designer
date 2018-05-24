package com.fr.design.report;

import com.fr.base.iofileattr.WatermarkAttr;
import com.fr.design.constants.UIConstants;
import com.fr.page.WatermarkPainter;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * Created by plough on 2018/5/15.
 */
public class WatermarkPreviewPane extends JPanel {
    private WatermarkAttr watermark;

    public WatermarkPreviewPane() {
        this.watermark = new WatermarkAttr();
        repaint();
    }

    /**
     * 重新画
     */
    public void repaint(WatermarkAttr watermark){
        this.watermark = watermark;
        super.repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.drawImage(UIConstants.WATERMARK_BACKGROUND, 0, 0, this.getWidth(), this.getHeight(), null);
        new WatermarkPainter(watermark).paint(g2d, this.getWidth(), this.getHeight());
    }
}
