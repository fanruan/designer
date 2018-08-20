package com.fr.van.chart.gantt.designer.style.axis;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ColorSelectBoxWithOutTransparent;
import com.fr.design.mainframe.chart.gui.style.ChartTextAttrPane;

import com.fr.plugin.chart.gantt.attr.GanttAxisStyleAttr;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;

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
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Color")), colorSelectBox4button},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Alpha")), transparent}
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
