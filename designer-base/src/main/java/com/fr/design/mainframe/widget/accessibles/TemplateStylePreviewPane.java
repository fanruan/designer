package com.fr.design.mainframe.widget.accessibles;

import com.fr.general.cardtag.TemplateStyle;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * Created by kerry on 2017/12/11.
 */
public class TemplateStylePreviewPane extends JPanel {

    private Rectangle rectangle;
    private TemplateStyle templateStyle;

    public TemplateStylePreviewPane(TemplateStyle templateStyle, Rectangle rectangle){
        this.templateStyle = templateStyle;
        this.rectangle = rectangle;
    }

    public void repaint (TemplateStyle templateStyle){
        this.templateStyle = templateStyle;
        super.repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g.create();
        templateStyle.paintPreview(g2d, rectangle);
    }
}
