package com.fr.design.foldablepane;

import com.fr.design.constants.UIConstants;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 * Created by MoMeak on 2017/7/5.
 */
public class HeaderPane extends JPanel {
    private static final long serialVersionUID = 1L;
    private int headWidth = 280;
    private int headHeight = 25;
    private Color bgColor;
    private boolean isShow;
    private String title;
    private Image image;
    private int fontSize = 13;
    public void setShow(boolean isShow) {
        this.isShow = isShow;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setHeadWidth(int headwidth){
        this.headWidth = headwidth;
    }

    public void setheadHeight(int headHeight){
        this.headHeight = headHeight;
    }

    public void setFontSize(int fontSize){
        this.fontSize = fontSize;
    }


    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        BufferedImage panelImage = createPanelImage();
        g2d.drawImage(panelImage, null, 0, 0);
    }

    private BufferedImage createPanelImage() {
        BufferedImage panelImage = new BufferedImage(getWidth(), headHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = panelImage.createGraphics();

        g2d.fillRect(0, 0, headWidth, headHeight);
        g2d.drawImage(UIConstants.DRAG_BAR, 0, 0, headWidth, headHeight, null);
        g2d.setFont(new Font("SimSun", 0, fontSize));
        g2d.setPaint(bgColor);
//        g2d.drawString(this.title, fontSize/2, headHeight-fontSize/3);
        g2d.drawString(this.title, 0, headHeight-fontSize/3);
        if(this.isShow)
        {
            image = UIConstants.DRAG_DOWN_PRESS;
            g2d.drawImage(image, title.length() * fontSize, headHeight/2-1, null);
        }
        else
        {
            image = UIConstants.DRAG_RIGHT_PRESS;
            g2d.drawImage(image, title.length() * fontSize, headHeight/3, null);
        }


        return panelImage;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(this.getWidth(), headHeight);
    }

    @Override
    public Dimension getSize() {
        return new Dimension(this.getWidth(), headHeight);
    }

    public HeaderPane(Color bgColor) {
        this.bgColor = bgColor;
        this.isShow = true;

    }

    public HeaderPane(Color bgColor, String title , int headWidth, int headHeight) {
        this(bgColor);
        this.title = title;
        this.headHeight = headHeight;
        this.headWidth = headWidth;
    }

    public static void main(String[] args)
    {
//        JFrame mainFrame = new JFrame("UI Demo - Gloomyfish");
//        mainFrame.getContentPane().setLayout(new BorderLayout());
//        mainFrame.getContentPane().add(new HeaderPane(Color.black, "基本",280,25), BorderLayout.CENTER);
//        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        mainFrame.pack();
//        mainFrame.setSize(280, 400);
//        mainFrame.setVisible(true);
    }

}
