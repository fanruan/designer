package com.fr.design.gui.ipasswordfield;


import com.fr.design.constants.UIConstants;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created with IntelliJ IDEA.
 * User: 小灰灰
 * Date: 13-7-22
 * Time: 下午4:44
 * To change this template use File | Settings | File Templates.
 */
public class UIPassWordField extends JPasswordField {
    private boolean isRollOver;

    public UIPassWordField() {
        super();
        addRollOverListener();
    }

    public UIPassWordField(String text) {
        super(text);
        addRollOverListener();
    }

    public UIPassWordField(int columns) {
        super(columns);
        addRollOverListener();
    }

    public UIPassWordField(String text, int columns) {
        super(text, columns);
        addRollOverListener();
    }

    public UIPassWordField(Document doc, String txt, int columns) {
        super(doc, txt, columns);
        addRollOverListener();
    }

    private void addRollOverListener() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isRollOver = true;
                UIPassWordField.this.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isRollOver = false;
                UIPassWordField.this.repaint();
            }
        });
    }

    @Override
    protected void paintBorder(Graphics g) {
        if (isRollOver && this.isEnabled()) {
            g.setColor(UIConstants.TEXT_FILED_BORDER_SELECTED);
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        } else {
            super.paintBorder(g);
        }
    }
}