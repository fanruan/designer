package com.fr.design.gui.ibutton;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: pony
 * Date: 13-5-27
 * Time: 下午4:51
 * To change this template use File | Settings | File Templates.
 */
public class FiveButtonLayout extends GridLayout {
    private static final double SECOND_ROW = 1.25;

    public FiveButtonLayout(int rows) {
        super(rows, 3, 1, 1);
    }

    /**
     * 容器布局
     *
     * @param parent 容器
     */
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int ncomponents = parent.getComponentCount();
            int nrows = 2;
            int ncols = 3;
            if (ncomponents == 3) {
                nrows = 1;
                ncols = 3;
            }

            if (ncomponents == 0) {
                return;
            }
            if (nrows > 0) {
                ncols = (ncomponents + nrows - 1) / nrows;
            } else {
                nrows = (ncomponents + ncols - 1) / ncols;
            }
            int w = parent.getWidth() - (insets.left + insets.right);
            int h = parent.getHeight() - (insets.top + insets.bottom);
            w = (w - (ncols - 1)) / ncols;
            h = (h - (nrows - 1)) / nrows;
            for (int i = 0, x = insets.left, y = insets.top; i < ncols; i++, x += w + 1) {
                parent.getComponent(i).setBounds(x, y, w, h);
            }
            int line2w = (int) (SECOND_ROW * w);
            int secondRowCount = ncomponents - ncols;
            int startx = (parent.getWidth() - line2w * secondRowCount - secondRowCount - 1) / 2;
            for (int i = ncols, x = startx, y = insets.top + h + 1; i < ncomponents; i++, x += line2w + 1) {
                parent.getComponent(i).setBounds(x, y, line2w, h);
            }

        }
    }


}