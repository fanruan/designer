package com.fr.design.style.color;

import javax.swing.*;
import java.awt.*;


import sun.swing.SwingUtilities2;

/**
 * Created by zhouping on 2015/7/24.
 */
public class ColorChooserPreview extends JPanel {

    private static final int SQUARE_SIZE = 25;
    private static final int SQUARE_GAP = 5;
    /**
     * 方形预览框，三层嵌套框之间的间隔
     */
    private static final int INNER_GAP = 5;
    private static final int INNER_GAP2 = (INNER_GAP * 2);
    private static final int INNER_GAP3 = (INNER_GAP * 4);


    private static final int TEXT_GAP = 5;

    private static final int PREFERSIZE_TEXT_GAP = (TEXT_GAP * 3);
    private static final int PREFERSIZE_SQUARE_GAP = (SQUARE_GAP * 2);
    private static final int PREFERSIZE_SQUARE = (SQUARE_SIZE * 3);

    private String sampleText;

    private static final int SWATH_WIDTH = 50;

    private Color oldColor = null;

    public Color myColor = Color.WHITE;

    public ColorChooserPreview(){
    }

    public void paint(Graphics g){
        g.setColor(myColor);
        paintComponent(g);
    }

    public void setMyColor(Color modifiedColor){
        myColor = modifiedColor;
    }

    private JColorChooser getColorChooser() {
        return (JColorChooser)SwingUtilities.getAncestorOfClass(
                JColorChooser.class, this);
    }

    public Dimension getPreferredSize() {
        JComponent host = getColorChooser();
        if (host == null) {
            host = this;
        }
        FontMetrics fm = host.getFontMetrics(getFont());

        int height = fm.getHeight();
        int width = SwingUtilities2.stringWidth(host, fm, getSampleText());

        int y = height * 3 + PREFERSIZE_TEXT_GAP;
        int x = PREFERSIZE_SQUARE + PREFERSIZE_SQUARE_GAP + SWATH_WIDTH + width + PREFERSIZE_TEXT_GAP;
        return new Dimension( x,y );
    }

    public void paintComponent(Graphics g) {
        if (oldColor == null){
            oldColor = getForeground();
        }

        g.setColor(getBackground());
        g.fillRect(0,0,getWidth(),getHeight());

        if (this.getComponentOrientation().isLeftToRight()) {
            int squareWidth = paintSquares(g, 0);
            int textWidth = paintText(g, squareWidth);
            paintSwatch(g, squareWidth + textWidth);
        } else {
            int SWATH_WIDTH = paintSwatch(g, 0);
            int textWidth = paintText(g, SWATH_WIDTH);
            paintSquares(g, SWATH_WIDTH + textWidth);

        }
    }

    private int paintSwatch(Graphics g, int offsetX) {
        int swatchX = offsetX;
        g.setColor(Color.WHITE);
        g.fillRect(swatchX, 0, SWATH_WIDTH, (SQUARE_SIZE) + (SQUARE_GAP / 2));
        g.setColor(myColor);
        g.fillRect(swatchX, (SQUARE_SIZE) + (SQUARE_GAP / 2), SWATH_WIDTH, (SQUARE_SIZE) + (SQUARE_GAP / 2) );
        return (swatchX + SWATH_WIDTH);
    }

    /**
     * 画颜色选择器-预览面板上的字体预览，共3行
     * @param g
     * @param offsetX
     * @return
     */
    private int paintText(Graphics g, int offsetX) {
        g.setFont(getFont());
        JComponent host = getColorChooser();
        if (host == null) {
            host = this;
        }
        FontMetrics fm = SwingUtilities2.getFontMetrics(host, g);

        int ascent = fm.getAscent();
        int height = fm.getHeight();
        int width = SwingUtilities2.stringWidth(host, fm, getSampleText());

        int textXOffset = offsetX + TEXT_GAP;

        Color color = myColor;

        g.setColor(color);

        SwingUtilities2.drawString(host, g, getSampleText(), textXOffset+(TEXT_GAP / 2), ascent + 2);

        g.fillRect(textXOffset, height + TEXT_GAP, width + TEXT_GAP, height + 2);

        g.setColor(Color.black);
        SwingUtilities2.drawString(host, g, getSampleText(), textXOffset + (TEXT_GAP / 2), height+ascent + TEXT_GAP + 2);


        g.setColor(Color.WHITE);

        g.fillRect(textXOffset, (height + TEXT_GAP) * 2, width + TEXT_GAP, height + 2);

        g.setColor(color);
        SwingUtilities2.drawString(host, g, getSampleText(), textXOffset + (TEXT_GAP / 2), ((height + TEXT_GAP) * 2) + ascent + 2);

        return width + TEXT_GAP * 3;

    }

    /**
     * 画颜色选择器-预览面板上的方形预览框，以2行3列方式排列，共6个
     * @param g
     * @param offsetX
     * @return
     */
    private int paintSquares(Graphics g, int offsetX) {
        int squareXOffset = offsetX;
        Color color = myColor;
        /** 方形预览框第1列第1行：从外到内（white,color,white） */
        g.setColor(Color.WHITE);
        g.fillRect(squareXOffset, 0, SQUARE_SIZE, SQUARE_SIZE);
        g.setColor(color);
        g.fillRect(squareXOffset + INNER_GAP, INNER_GAP, SQUARE_SIZE - INNER_GAP2, SQUARE_SIZE - INNER_GAP2);
        g.setColor(Color.WHITE);
        g.fillRect(squareXOffset+INNER_GAP2, INNER_GAP2, SQUARE_SIZE - INNER_GAP3, SQUARE_SIZE - INNER_GAP3);
        /** 方形预览框第1列第2行：color全色 */
        g.setColor(color);
        g.fillRect(squareXOffset, SQUARE_SIZE + SQUARE_GAP, SQUARE_SIZE, SQUARE_SIZE);
        /** 方形预览框第2列第1行：从外到内（black,color,white） */
        g.translate(SQUARE_SIZE + SQUARE_GAP, 0);
        g.setColor(Color.black);
        g.fillRect(squareXOffset, 0, SQUARE_SIZE, SQUARE_SIZE);
        g.setColor(color);
        g.fillRect(squareXOffset + INNER_GAP, INNER_GAP, SQUARE_SIZE - INNER_GAP2, SQUARE_SIZE - INNER_GAP2);
        g.setColor(Color.WHITE);
        g.fillRect(squareXOffset + INNER_GAP2, INNER_GAP2, SQUARE_SIZE - INNER_GAP3, SQUARE_SIZE - INNER_GAP3);
        g.translate(-(SQUARE_SIZE + SQUARE_GAP), 0);
        /** 方形预览框第2列第2行：从外到内（white,color） */
        g.translate(SQUARE_SIZE + SQUARE_GAP, SQUARE_SIZE + SQUARE_GAP);
        g.setColor(Color.WHITE);
        g.fillRect(squareXOffset,0,SQUARE_SIZE,SQUARE_SIZE);
        g.setColor(color);
        g.fillRect(squareXOffset + INNER_GAP, INNER_GAP, SQUARE_SIZE - INNER_GAP2, SQUARE_SIZE - INNER_GAP2);
        g.translate(-(SQUARE_SIZE + SQUARE_GAP), -(SQUARE_SIZE + SQUARE_GAP));
        /** 方形预览框第3列第1行：从外到内（white,color,black） */
        g.translate((SQUARE_SIZE + SQUARE_GAP) * 2, 0);
        g.setColor(Color.WHITE);
        g.fillRect(squareXOffset, 0, SQUARE_SIZE,SQUARE_SIZE);
        g.setColor(color);
        g.fillRect(squareXOffset + INNER_GAP, INNER_GAP, SQUARE_SIZE - INNER_GAP2, SQUARE_SIZE - INNER_GAP2);
        g.setColor(Color.black);
        g.fillRect(squareXOffset + INNER_GAP2, INNER_GAP2, SQUARE_SIZE - INNER_GAP3, SQUARE_SIZE - INNER_GAP3);
        g.translate(-((SQUARE_SIZE + SQUARE_GAP) * 2), 0);
        /** 方形预览框第3列第2行：从外到内（black,color） */
        g.translate((SQUARE_SIZE + SQUARE_GAP) * 2, (SQUARE_SIZE + SQUARE_GAP));
        g.setColor(Color.black);
        g.fillRect(squareXOffset, 0, SQUARE_SIZE, SQUARE_SIZE);
        g.setColor(color);
        g.fillRect(squareXOffset + INNER_GAP, INNER_GAP, SQUARE_SIZE - INNER_GAP2, SQUARE_SIZE - INNER_GAP2);
        g.translate(-((SQUARE_SIZE + SQUARE_GAP) * 2), -(SQUARE_SIZE + SQUARE_GAP));

        return (PREFERSIZE_SQUARE + PREFERSIZE_SQUARE_GAP);
    }

    private String getSampleText() {
        if (this.sampleText == null) {
            this.sampleText = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Sample_Text");
        }
        return this.sampleText;
    }
}