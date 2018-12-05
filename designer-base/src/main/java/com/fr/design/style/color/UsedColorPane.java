package com.fr.design.style.color;

import com.fr.design.DesignerEnvManager;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.SpecialUIButton;

import javax.swing.*;
import java.awt.*;

public class UsedColorPane extends BasicPane {

    // 默认显示颜色
    public static final Color DEFAULT_COLOR = new Color(222, 222, 222);

    // 最近使用面板列数
    private int columns;
    // 最近使用面板行数
    private int rows;
    // 留白的单元格数量
    private int reserveCells;
    // 是否需要取色器按钮
    private boolean needPickColorButton;
    // 是否在取色时实时设定颜色
    private boolean setColorRealTime;

    // 最近使用面板
    private JPanel pane;

    private ColorSelectable selectable;

    public JPanel getPane() {
        return pane;
    }

    public void setPane(JPanel pane) {
        this.pane = pane;
    }


    /**
     * 构造函数
     *
     * @param rows                行
     * @param columns             列
     * @param reserveCells        留白的单元格个数
     * @param selectable
     * @param needPickColorButton 是否需要加上取色器按钮
     * @param setColorRealTime    取色器是否实时设定颜色
     */
    public UsedColorPane(int rows, int columns, int reserveCells, ColorSelectable selectable, boolean needPickColorButton, boolean setColorRealTime) {
        this.columns = columns;
        this.rows = rows;
        this.reserveCells = reserveCells;
        this.selectable = selectable;
        this.needPickColorButton = needPickColorButton;
        this.setColorRealTime = setColorRealTime;
        initialComponents();
    }

    public UsedColorPane(int rows, int columns, ColorSelectable selectable) {
        this(rows, columns, 0, selectable, false, false);
    }

    private void initialComponents() {
        int total = columns * rows;
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(rows, columns, 1, 1));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 0, 8));
        //最近使用颜色
        Color[] colors = DesignerEnvManager.getEnvManager().getColorConfigManager().getColors();
        int size = colors.length;
        int i = 0;
        if (needPickColorButton) {
            // 取色按钮
            JButton pickColorButton = PickColorButtonFactory.getPickColorButton(selectable, PickColorButtonFactory.IconType.ICON16, setColorRealTime);
            panel.add(pickColorButton);
            i++;
            this.reserveCells += 1;
        }
        while (i < this.reserveCells) {
            ColorCell cc = new ColorCell(DEFAULT_COLOR, selectable);
            cc.setVisible(false);
            panel.add(cc);
            i++;
        }
        while (i < total) {
            Color color = i - this.reserveCells < size ? colors[size - (i - this.reserveCells) - 1] :
                    DEFAULT_COLOR;
            panel.add(new ColorCell(color == null ? DEFAULT_COLOR : color, selectable));
            i++;
        }
        this.pane = panel;
    }

    /**
     * 更新最近使用颜色
     */
    public void updateUsedColor() {
        int total = columns * rows;
        Color[] colors = DesignerEnvManager.getEnvManager().getColorConfigManager().getColors();
        int size = colors.length;
        for (int i = this.reserveCells; i < total; i++) {
            ColorCell cell = (ColorCell) this.pane.getComponent(i);
            Color color = i - this.reserveCells < size ? colors[size - (i - this.reserveCells) - 1] : DEFAULT_COLOR;
            cell.setColor(color == null ? DEFAULT_COLOR : color);
        }
    }

    @Override
    protected String title4PopupWindow() {
        return null;
    }
}