package com.fr.design.mainframe.widget.accessibles;

import com.fr.base.BaseUtils;
import com.fr.base.TemplateStyle;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

/**
 * Created by kerry on 2017/12/11.
 */
public class TemplateStylePreviewPane extends JPanel {

    private static final int WIDTH = 540;
    private static final int HEIGHT = 500;

    private TemplateStyle templateStyle;

    public TemplateStylePreviewPane(TemplateStyle templateStyle){
        this.templateStyle = templateStyle;
    }

    public void repaint (TemplateStyle templateStyle){
        this.templateStyle = templateStyle;
        super.repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g.create();
        Image image = BaseUtils.readImage(templateStyle.getPreview());
        g2d.drawImage(image, 0, 0, WIDTH, HEIGHT, null);
    }
}
