package com.fr.design.chartx.component.button;

import com.fr.base.BaseUtils;
import com.fr.base.background.ColorBackground;
import com.fr.design.style.AbstractSelectBox;
import com.fr.design.style.color.ColorSelectPane;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * @author Bjorn
 * @version 10.0
 * Created by Bjorn on 2020-03-06
 */
public class ColorButton extends AbstractSelectBox<Color> {

    private static final double DEL_WIDTH = 7;

    public static final int WIDTH = 18;

    private BufferedImage closeIcon = BaseUtils.readImageWithCache("com/fr/design/images/toolbarbtn/chartChangeClose.png");

    private Color color;

    private boolean isMoveOn = false;

    private ColorSelectPane colorPane;

    private boolean lastButton;

    private ChangeListener changeListener;

    public ColorButton(Color color) {
        this.color = color;
        addMouseListener(getMouseListener());
    }

    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, WIDTH);
    }

    private void paintDeleteButton(Graphics g2d) {
        Rectangle2D bounds = this.getBounds();

        int x = (int) (bounds.getWidth() - DEL_WIDTH);
        int y = 1;

        g2d.drawImage(closeIcon, x, y, closeIcon.getWidth(), closeIcon.getHeight(), null);
    }

    public void setLastButton(boolean lastButton) {
        this.lastButton = lastButton;
    }

    @Override
    public void paint(Graphics g) {
        this.setSize(WIDTH, WIDTH);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(color);
        Rectangle2D rec = new Rectangle2D.Double(0, 0, WIDTH + 1, WIDTH + 1);
        g2d.fill(rec);

        if (isMoveOn && !lastButton) {
            paintDeleteButton(g);
        }
    }

    protected void deleteButton() {

    }


    private void checkMoveOn(boolean moveOn) {
        this.isMoveOn = moveOn;
        repaint();
    }

    protected MouseListener getMouseListener() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mouseClick(e);
            }

            public void mouseEntered(MouseEvent e) {
                checkMoveOn(true);
            }

            public void mouseExited(MouseEvent e) {
                checkMoveOn(false);
            }
        };
    }


    public void mouseClick(MouseEvent e) {
        if (!lastButton) {
            Rectangle2D bounds = this.getBounds();
            if (bounds == null) {
                return;
            }
            if (e.getX() >= bounds.getWidth() - DEL_WIDTH && e.getY() <= DEL_WIDTH) {
                deleteButton();
                hidePopupMenu();
                return;
            }
        }
        //打开颜色选择面板
        showPopupMenu();
    }

    public JPanel initWindowPane(double preferredWidth) {
        // 下拉的时候重新生成面板，刷新最近使用颜色
        colorPane = new ColorSelectPane(false) {
            @Override
            public void setVisible(boolean b) {
                super.setVisible(b);
            }
        };
        colorPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                hidePopupMenu();
                color = ((ColorSelectPane) e.getSource()).getColor();
                fireDisplayComponent(ColorBackground.getInstance(color));
                ColorButton.this.stateChanged();
            }
        });
        return colorPane;
    }

    public void stateChanged() {
        if (changeListener != null)  {
            changeListener.stateChanged(null);
        }
    }

    public void addChangeListener(ChangeListener changeListener) {
        this.changeListener = changeListener;
    }


    /**
     * 获取当前选中的颜色
     *
     * @return 当前选中的颜色
     */
    public Color getSelectObject() {
        return this.color;
    }

    /**
     * 设置选中的颜色
     *
     * @param color 颜色
     */
    public void setSelectObject(Color color) {
        this.color = color;
        colorPane.setColor(color);

        fireDisplayComponent(ColorBackground.getInstance(color));
    }

}
