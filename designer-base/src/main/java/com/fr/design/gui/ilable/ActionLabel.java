package com.fr.design.gui.ilable;

import javax.swing.event.MouseInputAdapter;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

/**
 * Action label
 */
public class ActionLabel extends UILabel {
    private ActionListener actionListener;

    public ActionLabel(String text) {
        super(text);

        this.setForeground(Color.blue);
        this.addMouseListener(mouseInputAdapter);
        this.addMouseMotionListener(mouseInputAdapter);
    }

    public void addActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    /**
     * Repaints the text.
     */
    @Override
    public void paintComponent(Graphics _gfx) {
        super.paintComponent(_gfx);

        _gfx.setColor(Color.blue);
        _gfx.drawLine(0, this.getHeight() - 1, this.getWidth(), this.getHeight() - 1);
    }

    private MouseInputAdapter mouseInputAdapter = new MouseInputAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            //do nothing
        }

        @Override
        public void mousePressed(MouseEvent e) {
            //do nothing
        }

        @Override
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

        @Override
        public void mouseExited(MouseEvent evt) {
            Object source = evt.getSource();

            if (source instanceof UILabel) {
                ((UILabel) source).setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        }

        @Override
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