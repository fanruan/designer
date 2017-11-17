package com.fr.design.mainframe.cell;

import com.fr.design.gui.iscrollbar.UIScrollBar;
import com.fr.design.mainframe.CellElementPropertyPane;
import com.fr.quickeditor.cellquick.layout.CellElementBarLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * 右侧单元格元素面板抽象类
 *
 * @author yaoh.wu
 * @version 2017年7月25日
 * @since 9.0
 */
public abstract class AbstractDSCellEditorPane extends JPanel {

    /**
     * 滚动条相关配置
     */
    private static final int MAXVALUE = 100;
    private static final int TITLE_HEIGHT = 95;
    private static final int CONTENT_PANE_WIDTH_GAP = 3;
    private static final int SCROLLBAR_WIDTH = 7;
    private static final int MOUSE_WHEEL_SPEED = 5;
    private int maxHeight = 280;

    private JPanel leftContentPane;
    private UIScrollBar scrollBar;

    protected abstract JPanel createContentPane();

    public abstract String getIconPath();

    public abstract String title4PopupWindow();

    /**
     * 从面板拿数据保存
     */
    public abstract void update();

    /**
     * 更新面板数据
     */
    public abstract void populate();

    protected void createScrollPane() {
        leftContentPane = this.createContentPane();
        this.prepareScrollBar();
        leftContentPane.setBorder(BorderFactory.createMatteBorder(10, 10, 0, 0, this.getBackground()));

        this.setLayout(new CellElementBarLayout(leftContentPane) {
            @Override
            public void layoutContainer(Container parent) {
                maxHeight = CellElementPropertyPane.getInstance().getHeight() - TITLE_HEIGHT;
                int beginY;
                if ((MAXVALUE - scrollBar.getVisibleAmount()) == 0) {
                    beginY = 0;
                } else {
                    int preferredHeight = leftContentPane.getPreferredSize().height;
                    int value = scrollBar.getValue();
                    beginY = value * (preferredHeight - maxHeight) / (MAXVALUE - scrollBar.getVisibleAmount());
                }
                int width = parent.getWidth();
                int height = parent.getHeight();
                if (leftContentPane.getPreferredSize().height > maxHeight) {
                    leftContentPane.setBounds(0, -beginY, width - SCROLLBAR_WIDTH - CONTENT_PANE_WIDTH_GAP, height + beginY);
                    scrollBar.setBounds(width - SCROLLBAR_WIDTH - CONTENT_PANE_WIDTH_GAP, 0, SCROLLBAR_WIDTH + CONTENT_PANE_WIDTH_GAP, height);
                } else {
                    leftContentPane.setBounds(0, 0, width - SCROLLBAR_WIDTH - CONTENT_PANE_WIDTH_GAP, height);
                }
            }
        });
        this.add(scrollBar);
        this.add(leftContentPane);
    }


    private void prepareScrollBar() {
        scrollBar = new UIScrollBar(UIScrollBar.VERTICAL) {
            @Override
            public int getVisibleAmount() {
                int preferredHeight = leftContentPane.getPreferredSize().height;
                int e = MAXVALUE * (maxHeight) / preferredHeight;
                setVisibleAmount(e);
                return e;
            }

            @Override
            public int getMaximum() {
                return MAXVALUE;
            }
        };

        scrollBar.addAdjustmentListener(new AdjustmentListener() {

            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                doLayout();
            }
        });
        this.addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int value = scrollBar.getValue();
                value += MOUSE_WHEEL_SPEED * e.getWheelRotation();
                scrollBar.setValue(value);
                doLayout();
            }
        });
        scrollBar.setPreferredSize(new Dimension(SCROLLBAR_WIDTH + CONTENT_PANE_WIDTH_GAP, this.getHeight()));
        scrollBar.setBlockIncrement(SCROLLBAR_WIDTH + CONTENT_PANE_WIDTH_GAP);
        scrollBar.setBorder(BorderFactory.createMatteBorder(0, CONTENT_PANE_WIDTH_GAP, 0, 0, this.getBackground()));
    }
}
