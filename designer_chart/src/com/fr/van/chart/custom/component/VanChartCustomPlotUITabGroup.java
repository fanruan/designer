package com.fr.van.chart.custom.component;

import com.fr.design.constants.UIConstants;
import com.fr.design.gui.ibutton.UITabGroup;
import com.fr.design.gui.ibutton.UIToggleButton;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.border.Border;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;

/**
 * Created by Fangjie on 2016/4/21.
 */
public class VanChartCustomPlotUITabGroup extends UITabGroup{

    private static final int WIDTH = 198;
    private static final int BUTTON_HEIGHT = 30;
    private int listNum = 0;

    public VanChartCustomPlotUITabGroup(Icon[] iconArray) {
        super(iconArray);
    }

    public VanChartCustomPlotUITabGroup(String[] textArray) {
        super(textArray);
    }

    @Override
    protected LayoutManager getGridLayout(int number) {
        //这个地方可以顺便获取list个数
        listNum = number;
        return new GridBagLayout();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Border border = getBorder();
        if (border != null) {
            border.paintBorder(this, g, 0, 0, getWidth(), getHeight());
        }
    }

    @Override
    protected Border getGroupBorder() {
        return BorderFactory.createEmptyBorder(1, 1, 1, 1);
    }

    @Override
    protected void initButton(UIToggleButton labelButton) {

        int ButtonWidth = WIDTH / 3;
        if (listNum <= 1){
            return;
        }else if (listNum == 2){
            ButtonWidth = WIDTH / 2;
        }

        //将button加入到pane中，以便可以对边框进行控制
        labelButton.setRoundBorder(false);
        labelButton.setBorderPainted(false);



        labelButtonList.add(labelButton);

        int index = labelButtonList.size() - 1;

        JPanel panel = getButtonPanel(labelButton, index);

        GridBagConstraints constraints=new GridBagConstraints();

        int end = listNum % 3;

        if (end == 1 && index == 0){
            constraints.gridy = 0;
            constraints.gridx = 0;
            constraints.gridheight = 1;
            constraints.gridwidth = 6;
            ButtonWidth = WIDTH + 2;
        }else if (end == 2 && (index == 0 || index == 1) && listNum != 2){
            constraints.gridy = 0;
            constraints.gridx = index == 1 ? 4 : 0;
            constraints.gridheight = 1;
            constraints.gridwidth = index == 0 ? 4 : 2;
            ButtonWidth = index == 0 ? ButtonWidth * 2 + 1 : ButtonWidth;
        }else {
            int l = ((index + ((end == 0) ? end : (3 - end))) / 3);
            constraints.gridy = l;
            constraints.gridx = ((index - (l * 3 - (end == 0 ? end : 3 - end))))*2;
            constraints.gridheight = 1;
            constraints.gridwidth = 2;
        }
        labelButton.setPreferredSize(new Dimension(ButtonWidth, BUTTON_HEIGHT));

        this.add(panel, constraints);
    }

    private JPanel getButtonPanel(UIToggleButton labelButton, int index) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(labelButton,BorderLayout.CENTER);

        setPanelBorder(panel, index);

        return panel;
    }

    public void setPanelBorder(JPanel panel, int index) {
        int end = listNum % 3;
        int num = listNum;

        //调整index
        if (end == 1 && index != 0){
            index += 2;
            num += 2;
        }else if (end == 2 && index != 0 && index != 1){
            index += 1;
            num += 1;
        }
        boolean isRightLine = (index+1) % 3 == 0 ? true : false;
        isRightLine = (end == 2 && index == 1) ? true : isRightLine;
        isRightLine = (end == 1 && index == 0) ? true : isRightLine;
        //是否画下边线
        int row = index / 3;
        int column = index % 3;
        boolean isBottomLine = ((row+1)*3 + column < num) ? false : true;

        panel.setBorder(BorderFactory.createMatteBorder(1, 1, isBottomLine ? 1 : 0, isRightLine ? 1 : 0, UIConstants.LINE_COLOR));
    }
}
