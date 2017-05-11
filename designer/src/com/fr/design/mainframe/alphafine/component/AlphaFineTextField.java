package com.fr.design.mainframe.alphafine.component;

import com.fr.design.gui.itextfield.UITextField;
import com.fr.general.ComparatorUtils;
import com.fr.report.web.button.Image;
import com.fr.stable.StringUtils;

import java.awt.*;
import java.awt.event.*;

import static java.awt.event.KeyEvent.VK_ESCAPE;

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
        String text = super.getText();

        if (text.trim().length() == 0 && placeHolder != null) {
            text = placeHolder;
        }

        return text;
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

    public void initKeyListener(Component component) {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {

                int keyCode = e.getKeyCode();
                if (keyCode == VK_ESCAPE) {
                    if (StringUtils.isBlank(getText()) || ComparatorUtils.equals(getText(), placeHolder)) {
                        component.setVisible(false);
                    } else {
                        setText(null);
                    }
                }
            }
        });
    }
}
