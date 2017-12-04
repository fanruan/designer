package com.fr.design.foldablepane;

import com.fr.base.GraphHelper;
import com.fr.design.constants.UIConstants;
import com.fr.design.gui.syntax.ui.rsyntaxtextarea.RSyntaxUtilities;
import com.fr.stable.OperatingSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * Created by MoMeak on 2017/7/5.
 */
public class HeaderPane extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final int TITLE_X = 5;
    private static final int LEFT_X = 16;
    private static final int LEFT_Y = 6;
    private static final int NORMAL_FONTSIZE = 12;
    private int headWidth;
    private int headHeight;
    private Color bgColor;
    private boolean isShow;
    private boolean isPressed = false;
    private String title;
    private Image image;
    private int fontSize;

    public void setPressed(boolean pressed) {
        this.isPressed = pressed;
    }

    public void setShow(boolean isShow) {
        this.isShow = isShow;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setHeadWidth(int headwidth) {
        this.headWidth = headwidth;
    }

    public void setheadHeight(int headHeight) {
        this.headHeight = headHeight;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }


    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        //mac下抗锯齿处理
        if(OperatingSystem.isMacOS()){
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }
        BufferedImage panelImage = createPanelImage();
        g2d.drawImage(panelImage, null, 0, 0);
        GraphHelper.drawString(g2d, this.title, TITLE_X, headHeight - fontSize / 2 - 1);
    }

    private BufferedImage createPanelImage() {
        BufferedImage panelImage = new BufferedImage(getWidth(), headHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = panelImage.createGraphics();

        g2d.setColor(isPressed ? UIConstants.POPUP_TITLE_BACKGROUND : UIConstants.COMPONENT_BACKGROUND_COLOR);
        headWidth = this.getWidth();
        g2d.fillRect(0, 0, headWidth, headHeight);
        g2d.setFont(new Font("SimSun", 0, fontSize));
        g2d.setPaint(bgColor);
        Map<?, ?> desktopHints = RSyntaxUtilities.getDesktopAntiAliasHints();
        if (desktopHints != null) {
            g2d.setRenderingHints(desktopHints);
        }
        int leftWdith = headWidth - LEFT_X;
        if (this.isShow) {
            image = UIConstants.DRAG_DOWN_SELECTED_SMALL;
            g2d.drawImage(image, leftWdith, LEFT_Y, null);
        } else {
            image = UIConstants.DRAG_LEFT_NORMAL_SMALL;
            g2d.drawImage(image, leftWdith, LEFT_Y, null);
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

    public HeaderPane(Color bgColor, String title, int headHeight) {
        this(bgColor);
        this.title = title;
        this.headHeight = headHeight;
        this.fontSize = NORMAL_FONTSIZE;
    }

    public static void main(String[] args) {
        JFrame mainFrame = new JFrame("UI Demo - Gloomyfish");
        mainFrame.getContentPane().setLayout(new BorderLayout());
        mainFrame.getContentPane().add(new HeaderPane(Color.black, "基本", 24), BorderLayout.CENTER);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setSize(300, 400);
        mainFrame.setVisible(true);
    }

}
