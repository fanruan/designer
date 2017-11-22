package com.fr.plugin.chart.gantt.designer.style.axis;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ColorSelectBoxWithOutTransparent;
import com.fr.design.mainframe.chart.gui.style.ChartTextAttrPane;
import com.fr.general.Inter;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.gantt.attr.GanttAxisStyleAttr;

import javax.swing.*;
import java.awt.*;

/**
 * Created by hufan on 2017/1/12.
 */
public class GanttAxisStylePane extends BasicBeanPane<GanttAxisStyleAttr> {
    private ChartTextAttrPane textAttrPane;
    private ColorSelectBoxWithOutTransparent colorSelectBox4button;
    private UINumberDragPane transparent;

    public GanttAxisStylePane() {
        textAttrPane = new ChartTextAttrPane();
        colorSelectBox4button = new ColorSelectBoxWithOutTransparent(100);
        transparent = new UINumberDragPane(0, 100);

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double[] row = {p,p,p};
        double[] col = {f, e};

        Component[][] components = new Component[][]{
                new Component[]{textAttrPane, null},
                new Component[]{new UILabel(Inter.getLocText("FR-Chart-Color_Color")), colorSelectBox4button},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Alpha")), transparent}
        };

        JPanel content = TableLayoutHelper.createTableLayoutPane(components, row, col);

        this.setLayout(new BorderLayout());
        this.add(content, BorderLayout.CENTER);
    }

    @Override
    public void populateBean(GanttAxisStyleAttr contentAttr) {
        textAttrPane.populate(contentAttr.getTextAttr());
        colorSelectBox4button.setSelectObject(contentAttr.getBackgroundColor());
        transparent.populateBean(contentAttr.getAlpha());
    }

    @Override
    public void updateBean(GanttAxisStyleAttr contentAttr) {
        contentAttr.setTextAttr(textAttrPane.update());
        contentAttr.setBackgroundColor(colorSelectBox4button.getSelectObject());
        contentAttr.setAlpha(transparent.updateBean());
    }

    @Override
    public GanttAxisStyleAttr updateBean() {
        GanttAxisStyleAttr styleAttr = new GanttAxisStyleAttr();
        updateBean(styleAttr);
        return styleAttr;
    }

    @Override
    protected String title4PopupWindow() {
        return null;
    }
}
