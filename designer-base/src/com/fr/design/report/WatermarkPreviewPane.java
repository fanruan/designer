package com.fr.design.report;

import com.fr.base.Watermark;
import com.fr.design.constants.UIConstants;
import com.fr.general.FRFont;
import com.fr.stable.StringUtils;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * Created by plough on 2018/5/15.
 */
public class WatermarkPreviewPane extends JPanel {
    private static final double SCALE_FACTOR = 3;  // 水印层的放大比例
    private static final int ROTATE_DEGREE = -20;  // 旋转角度
    // 绘制水印层的坐标，用来控制图层偏移
    private static final int MARK_X = 0;
    private static final int MARK_Y = -120;

    private int horizontalGap = 20;  // 水平间隔（一个中文字符宽度）
    private int verticalGap = 40;  // 垂直间隔（两个中文字符宽度）
    private Watermark watermark;


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

    private void updateGap(FontMetrics fontMetrics) {
        horizontalGap = fontMetrics.stringWidth("帆");  // 任意一个中文字符
        verticalGap = horizontalGap * 2;
    }

    private int getLineHeight() {
        return horizontalGap;
    }

    private BufferedImage paintWatermark() {
        int width = (int)(this.getWidth() * SCALE_FACTOR);
        int height = (int)(this.getHeight() * SCALE_FACTOR);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        g.setColor(watermark.getColor());
        g.setFont(FRFont.getInstance().applySize(watermark.getFontSize()));
        updateGap(g.getFontMetrics());
        for (int y = getLineHeight(); y < height; y += verticalGap) {
            for (int x = 0; x < width; x += horizontalGap) {
                g.drawString(watermark.getText(), x, y);
                x += g.getFontMetrics().stringWidth(watermark.getText());
            }
            y += getLineHeight();
        }
        return image;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.drawImage(UIConstants.WATERMARK_BACKGROUND, 0, 0, this.getWidth(), this.getHeight(), null);
        BufferedImage markImage = paintWatermark();
        g2d.rotate(
            Math.toRadians(ROTATE_DEGREE),
            (double) markImage.getWidth() / 2,
            (double) markImage.getHeight() / 2
        );
        g.drawImage(markImage, MARK_X, MARK_Y, null);
    }
}
