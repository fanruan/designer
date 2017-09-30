package com.fr.plugin.chart.bubble.component;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.plugin.chart.bubble.attr.VanChartAttrBubble;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Mitisky on 16/3/31.
 * 气泡大小等设置界面
 */
public class VanChartBubblePane extends BasicBeanPane<VanChartAttrBubble> {
    private UISpinner minDiameter;
    private UISpinner maxDiameter;
    private UIButtonGroup<Integer> shadow;
    private UIButtonGroup<Integer> displayNegative;

    public VanChartBubblePane(){
        minDiameter = new UISpinner(0,Double.MAX_VALUE,1,0);
        maxDiameter = new UISpinner(0,Double.MAX_VALUE,1,0);
        shadow = new UIButtonGroup<Integer>(new String[]{Inter.getLocText("Plugin-ChartF_Open"),
                Inter.getLocText("Plugin-ChartF_Close")});
        displayNegative = new UIButtonGroup<Integer>(new String[]{Inter.getLocText("Plugin-ChartF_Open"),
                Inter.getLocText("Plugin-ChartF_Close")});



        this.setLayout(new BorderLayout());
        this.add(getContentPane(), BorderLayout.CENTER);
    }

    protected JPanel getContentPane () {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] row = {p, p, p, p, p};
        double[] col = {p, f};

        return TableLayoutHelper.createTableLayoutPane(getComponent(), row, col);
    }

    protected Component[][] getComponent () {
        return new Component[][]{
                new Component[]{null, null},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_MinDiameter")), minDiameter},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_MaxDiameter")), maxDiameter},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Shadow")), shadow},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_DisplayNegative")), displayNegative},

        };
    }

    public void populateBean(VanChartAttrBubble bubble) {
        if(bubble == null){
            bubble = new VanChartAttrBubble();
        }
        minDiameter.setValue(bubble.getMinDiameter());
        maxDiameter.setValue(bubble.getMaxDiameter());
        shadow.setSelectedIndex(bubble.isShadow() ? 0 : 1);
        displayNegative.setSelectedIndex(bubble.isDisplayNegative() ? 0 : 1);
    }

    public VanChartAttrBubble updateBean() {
        VanChartAttrBubble bubble = new VanChartAttrBubble();
        bubble.setMinDiameter(minDiameter.getValue());
        bubble.setMaxDiameter(maxDiameter.getValue());
        bubble.setShadow(shadow.getSelectedIndex() == 0);
        bubble.setDisplayNegative(displayNegative.getSelectedIndex() == 0);
        return bubble;
    }

    @Override
    public String title4PopupWindow() {
        return Inter.getLocText("Plugin-ChartF_Bubble");
    }
}
