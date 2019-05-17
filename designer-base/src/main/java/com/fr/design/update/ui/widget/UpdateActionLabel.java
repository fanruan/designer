package com.fr.design.update.ui.widget;

import com.fr.design.gui.ilable.UILabel;

import javax.swing.event.MouseInputAdapter;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

/**
 * Created by XINZAI on 2018/8/21.
 */
public class UpdateActionLabel extends UILabel {
    private ActionListener actionListener;
    private boolean drawLine = true;

    public UpdateActionLabel(String text, boolean drawUnderLine) {
        super(text);
        this.drawLine = drawUnderLine;
        this.setForeground(Color.BLUE);
        this.addMouseListener(mouseInputAdapter);
        this.addMouseMotionListener(mouseInputAdapter);
    }

    /**
     * 增加事件监听
     *
     * @param actionListener 监听器
     */
    public void addActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    /**
     * Repaints the text.
     */
    public void paintComponent(Graphics _gfx) {
        super.paintComponent(_gfx);

        _gfx.setColor(Color.BLUE);
        if (drawLine) {
            _gfx.drawLine(0, this.getHeight() - 1, this.getWidth(), this.getHeight() - 1);
        }
    }

    private MouseInputAdapter mouseInputAdapter = new MouseInputAdapter() {
        public void mouseClicked(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent evt) {
            Object source = evt.getSource();

            if (source instanceof UILabel) {
                //Action.
                if (actionListener != null) {
                    ActionEvent actionEvent = new ActionEvent(source, 99, "");
                    actionListener.actionPerformed(actionEvent);
                }
            }
        }

        public void mouseEntered(MouseEvent evt) {
            Object source = evt.getSource();

            if (source instanceof UILabel) {
                ((UILabel) source).setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        }

        public void mouseExited(MouseEvent evt) {
            Object source = evt.getSource();

            if (source instanceof UILabel) {
                ((UILabel) source).setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        }

        public void mouseDragged(MouseEvent e) {
        }

        public void mouseMoved(MouseEvent evt) {
            Object source = evt.getSource();

            if (source instanceof UILabel) {
                ((UILabel) source).setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        }
    };
}