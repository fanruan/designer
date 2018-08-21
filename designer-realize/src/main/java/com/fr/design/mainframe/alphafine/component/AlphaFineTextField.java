package com.fr.design.mainframe.alphafine.component;

import com.fr.design.gui.itextfield.UITextField;
import com.fr.report.web.button.Image;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;


/**
 * Created by XiaXiang on 2017/3/21.
 */
public class AlphaFineTextField extends UITextField {

    private String placeHolder;

    private Image image;

    public AlphaFineTextField(String placeHolder) {
        this.placeHolder = placeHolder;
    }


    public AlphaFineTextField() {
        this.placeHolder = null;
    }


    @Override
    public String getText() {
        return super.getText();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (super.getText().length() > 0 || placeHolder == null) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(super.getDisabledTextColor());
        g2.drawString(placeHolder, getInsets().left, g.getFontMetrics().getMaxAscent() + getInsets().top + 15);
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getPlaceHolder() {
        return placeHolder;
    }
}
