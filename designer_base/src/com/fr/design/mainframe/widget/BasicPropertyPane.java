package com.fr.design.mainframe.widget;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

/**
 * Created by plough on 2017/8/7.
 */
public class BasicPropertyPane extends BasicPane {
    protected UITextField widgetName;

    public BasicPropertyPane(){
        initContentPane();
    }

    protected void initContentPane() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        widgetName = new UITextField();

        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_Widget_Name")), widgetName},
        };
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, 20, 7);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 15));
        this.add(panel, BorderLayout.NORTH);
    }

    public UITextField getWidgetNameField() {
        return widgetName;
    }


    @Override
    public String title4PopupWindow() {
        return "basicProperty";
    }
}
