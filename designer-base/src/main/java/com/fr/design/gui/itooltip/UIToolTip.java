package com.fr.design.gui.itooltip;

import com.fr.base.FRContext;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.general.ComparatorUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created with IntelliJ IDEA.
 * User: pony
 * Date: 13-5-16
 * Time: 下午2:40
 * To change this template use File | Settings | File Templates.
 */
public class UIToolTip extends JToolTip{
    private Icon icon;
    public UIToolTip() {
        this(new ImageIcon());
    }

    public UIToolTip(Icon icon) {
        this.icon = icon;
        setUI(new UIToolTipUI());
        try {
            this.addMouseListener(new MouseAdapter() {
                Robot robot = new Robot();
                public void mousePressed(MouseEvent e) {
                    setVisible(false);
                    robot.mousePress(MouseEvent.BUTTON3_MASK);
                    robot.mouseRelease(MouseEvent.BUTTON3_MASK);
                    robot.mousePress(MouseEvent.BUTTON1_MASK);
                }

                public void mouseReleased(MouseEvent e) {
                    robot.mouseRelease(MouseEvent.BUTTON1_MASK);
                }
            });
        } catch (AWTException e) {
            FRContext.getLogger().error(e.getMessage(),e);
        }

        this.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                setVisible(false);
            }
        });

        this.addMouseListener(new MouseAdapter() {
            public void mouseExited(MouseEvent e) {
                ToolTipManager.sharedInstance().setEnabled(false);
                ToolTipManager.sharedInstance().setEnabled(true);
            }
        });
        this.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                Container container = getComponent();
                while (!ComparatorUtils.equals(container.getClass(), UIScrollPane.class)) {
                    if (container.getParent() == null) {
                        break;
                    }
                    container = container.getParent();
                }
                Point mouse = e.getLocationOnScreen();
                Point parent = container.getLocationOnScreen();
                if (!isInParent(mouse, parent, container.getWidth(), container.getHeight())) {
                    ToolTipManager.sharedInstance().setEnabled(false);
                    ToolTipManager.sharedInstance().setEnabled(true);
                }

            }
        });

    }

    private boolean isInParent(Point mouse, Point parent, int width, int height) {
        int deltaX = (int) (mouse.getX() - parent.getX());
        int deltaY = (int) (mouse.getY() - parent.getY());
        return deltaX > 1 &&  deltaX < width - 10 &&   deltaY > 1 && deltaY < height - 1;
    }


    public Icon getIcon() {
        return icon;
    }
}