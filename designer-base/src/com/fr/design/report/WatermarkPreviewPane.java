package com.fr.design.report;

import com.fr.base.Watermark;
import com.fr.design.constants.UIConstants;
import com.fr.general.FRFont;

import javax.swing.JPanel;
import java.awt.Graphics;

/**
 * Created by plough on 2018/5/15.
 */
public class WatermarkPreviewPane extends JPanel {
    Watermark watermark;

    public WatermarkPreviewPane() {
        this.watermark = new Watermark();
        repaint();
    }

    /**
     * 重新画
     */
    public void repaint(Watermark watermark){
        this.watermark = watermark;
        super.repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(UIConstants.WATERMARK_BACKGROUND, 0, 0, this.getWidth(), this.getHeight(), null);
        g.setColor(watermark.getColor());
        g.setFont(FRFont.getInstance().applySize(watermark.getFontSize()));
        g.drawString(watermark.getText(), 20, 20);
    }
}
