package com.fr.design.gui.itextarea;

import com.fr.design.constants.UIConstants;
import com.fr.design.utils.gui.GUIPaintUtils;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTextAreaUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UITextAreaUI extends BasicTextAreaUI {
    protected boolean isRollOver;
    private JComponent textField;

    public UITextAreaUI(final JComponent textField) {
        super();
        this.textField = textField;
        this.textField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isRollOver = true;
                textField.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isRollOver = false;
                textField.repaint();
            }
        });
    }

    public void paintBorder(Graphics2D g2d, int width, int height, boolean isRound, int rectDirection) {
        if (isRollOver && textField.isEnabled() && ((UITextArea)textField).isEditable()) {
            g2d.setColor(UIConstants.TEXT_FILED_BORDER_SELECTED);
            g2d.drawRect(0, 0, width - 1, height - 1);
        } else {
            GUIPaintUtils.drawBorder(g2d, 0, 0, width, height, isRound, rectDirection);
        }
    }

}