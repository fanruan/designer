package com.fr.design.mainframe.widget.ui;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.form.ui.Widget;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

/**
 * Created by ibm on 2017/8/4.
 */
public class FormBasicPropertyPane extends BasicPane {
    private UITextField widgetName;

    public FormBasicPropertyPane(){
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
                new Component[]{new UILabel(Inter.getLocText("Form-Widget_Name") + ":"), widgetName},
        };
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, 20, 7);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        this.add(panel, BorderLayout.NORTH);
    }


    @Override
    public String title4PopupWindow() {
        return "basicProperty";
    }

    public void populate(Widget widget) {
        widgetName.setText(widget.getWidgetName());
    }

    public void update(Widget widget) {
        widget.setWidgetName(widgetName.getText());
    }

}
