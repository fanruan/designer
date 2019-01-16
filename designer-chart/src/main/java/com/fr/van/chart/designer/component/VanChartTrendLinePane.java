package com.fr.van.chart.designer.component;

import com.fr.chart.base.AttrColor;
import com.fr.chart.base.AttrLineStyle;
import com.fr.chart.base.LineStyleInfo;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.icombobox.LineComboBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.style.color.ColorSelectBox;

import com.fr.design.utils.gui.UIComponentUtils;
import com.fr.design.widget.FRWidgetFactory;
import com.fr.plugin.chart.base.TrendLineType;
import com.fr.plugin.chart.base.VanChartAttrTrendLine;
import com.fr.plugin.chart.base.VanChartConstants;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;

import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Component;

/**
 * Created by Mitisky on 15/10/19.
 */
public class VanChartTrendLinePane extends BasicPane{
    private static final TrendLineType[] TYPES = new TrendLineType[] {TrendLineType.EXP, TrendLineType.LINE, TrendLineType.LOG, TrendLineType.POLY};

    private UITextField trendLineName;
    private ColorSelectBox trendLineColor;
    private LineComboBox trendLineStyle;//线型

    private UIComboBox trendLineType;//趋势线函数类型
    private UISpinner prePeriod;
    private UISpinner afterPeriod;

    public VanChartTrendLinePane() {
        this.setLayout(new BorderLayout());
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] row = {p,p,p,p,p,p};
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double[] col = {f, e};
        trendLineName = new UITextField();
        trendLineColor = new ColorSelectBox(100);
        trendLineStyle = new LineComboBox(VanChartConstants.ALERT_LINE_STYLE);

        trendLineType = new UIComboBox(TYPES);
        prePeriod = new UISpinner(0, Integer.MAX_VALUE, 1, 0);
        afterPeriod = new UISpinner(0, Integer.MAX_VALUE, 1, 0);
        double[] r = {p, p};
        double[] c = {f, p, f, p};
        Component[][] periodComps = new Component[][]{
                new Component[]{prePeriod, new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Cycle")), afterPeriod, new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Cycle"))},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_TrendLine_Forward")), null, new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_After_Period")), null},
        };
        JPanel periodPane = TableLayoutHelper.createTableLayoutPane(periodComps, r, c);

        UILabel label = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Period"));
        label.setVerticalAlignment(SwingConstants.TOP);

        Component[][] components = new Component[][]{
                new Component[]{null, null},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Name")), trendLineName},
                new Component[]{
                    FRWidgetFactory.createLineWrapLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Line_Style")),
                    UIComponentUtils.wrapWithBorderLayoutPane(trendLineStyle)
                },
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Color")), trendLineColor},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Type")), trendLineType},
                new Component[]{label, periodPane}
        };

        JPanel panel = TableLayout4VanChartHelper.createGapTableLayoutPane(components, row, col);
        this.add(panel, BorderLayout.CENTER);
    }
    protected String title4PopupWindow(){
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_TrendLine");
    }

    public void populate(VanChartAttrTrendLine trendLine) {
        if(trendLine != null){
            trendLineName.setText(trendLine.getTrendLineName());
            LineStyleInfo lineStyleInfo = trendLine.getLineStyleInfo();
            trendLineColor.setSelectObject(lineStyleInfo.getAttrLineColor().getSeriesColor());
            trendLineStyle.setSelectedLineStyle(lineStyleInfo.getAttrLineStyle().getLineStyle());
            trendLineType.setSelectedItem(trendLine.getTrendLineType());
            prePeriod.setValue(trendLine.getPrePeriod());
            afterPeriod.setValue(trendLine.getAfterPeriod());
        }
    }

    public VanChartAttrTrendLine update() {
        VanChartAttrTrendLine  trendLine = new VanChartAttrTrendLine();
        trendLine.setTrendLineName(trendLineName.getText());

        LineStyleInfo lineStyleInfo = trendLine.getLineStyleInfo();
        lineStyleInfo.setAttrLineStyle(new AttrLineStyle(trendLineStyle.getSelectedLineStyle()));
        lineStyleInfo.setAttrLineColor(new AttrColor(trendLineColor.getSelectObject()));

        trendLine.setTrendLineType((TrendLineType) trendLineType.getSelectedItem());
        trendLine.setPrePeriod((int) prePeriod.getValue());
        trendLine.setAfterPeriod((int) afterPeriod.getValue());

        return trendLine;
    }

}