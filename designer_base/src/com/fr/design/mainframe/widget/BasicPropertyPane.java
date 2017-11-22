package com.fr.design.mainframe.widget;

import com.fr.design.designer.IntervalConstants;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;

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
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        widgetName = new UITextField();
        widgetName.setGlobalName(Inter.getLocText("FR-Designer_Basic"));
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_Widget_Name")), widgetName},
        };
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, IntervalConstants.INTERVAL_W1, IntervalConstants.INTERVAL_L1);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
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
