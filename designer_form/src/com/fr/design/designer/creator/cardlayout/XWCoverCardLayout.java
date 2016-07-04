package com.fr.design.designer.creator.cardlayout;

import com.fr.form.ui.container.cardlayout.WCardMainBorderLayout;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Created by Fangjie on 2016/6/30.
 */
public class XWCoverCardLayout extends XWCardMainBorderLayout{

    private boolean mouseEnter;

    public boolean isMouseEnter() {
        return mouseEnter;
    }

    public void setMouseEnter(boolean mouseEnter) {
        this.mouseEnter = mouseEnter;
    }

    /**
     * 构造函数
     *
     * @param border
     * @param dimension
     */
    public XWCoverCardLayout(WCardMainBorderLayout border, Dimension dimension) {
        super(border, dimension);
    }

    public void paint(Graphics g) {
        super.paint(g);

        if (mouseEnter) {
            Graphics2D g2d = (Graphics2D) g;
            Composite oldComposite = g2d.getComposite();
            Paint old_paint = g2d.getPaint();

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));

            g2d.setColor(Color.YELLOW);

            int x = 1;
            int y = 1;
            int w = getWidth();
            int h = getHeight();

            g2d.fillRect(x, y, w, h);

            g2d.setColor(Color.BLACK);

            g2d.drawRect(getX(), getY(), getWidth(), getHeight());

            g2d.setComposite(oldComposite);

            g2d.setPaint(old_paint);
        }
    }

}
