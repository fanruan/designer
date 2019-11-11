package com.fr.van.chart.designer.component;

import com.fr.chart.base.AttrColor;
import com.fr.chart.base.LineStyleInfo;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.style.color.ColorSelectBox;
import com.fr.design.utils.gui.UIComponentUtils;
import com.fr.design.widget.FRWidgetFactory;
import com.fr.plugin.chart.base.TrendLineType;
import com.fr.plugin.chart.base.VanChartAttrTrendLine;
import com.fr.plugin.chart.type.LineType;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;

import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Mitisky on 15/10/19.
 */
public class VanChartTrendLinePane extends BasicPane{
    private static final TrendLineType[] TYPES = new TrendLineType[] {TrendLineType.EXP, TrendLineType.LINE, TrendLineType.LOG, TrendLineType.POLY};

    private UITextField trendLineName;
    private ColorSelectBox trendLineColor;
    private LineTypeComboBox trendLineStyle;//线型
    private UISpinner lineWidthSpinner;//线宽

    private UIComboBox trendLineType;//趋势线函数类型
    private UISpinner prePeriod;
    private UISpinner afterPeriod;

    private JPanel trendLineHidePane;

    public VanChartTrendLinePane() {
        initComponents();
    }

    private void initComponents(){
        this.setLayout(new BorderLayout(0,6));
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double[] col = {f, e};
        trendLineName = new UITextField();
        trendLineColor = new ColorSelectBox(100);
        trendLineStyle = new LineTypeComboBox(new LineType[]{LineType.NONE, LineType.NORMAL, LineType.DASH});
        lineWidthSpinner = new UISpinner(0.5, Integer.MAX_VALUE, 0.5, 2);
        trendLineType = new UIComboBox(TYPES);
        prePeriod = new UISpinner(0, Integer.MAX_VALUE, 1, 0);
        afterPeriod = new UISpinner(0, Integer.MAX_VALUE, 1, 0);

        double[] r = {p, p};
        double[] c = {f, p, f, p};
        Component[][] periodComps = new Component[][]{
                new Component[]{prePeriod, new UILabel(Toolkit.i18nText("Fine-Design_Chart_Cycle")), afterPeriod, new UILabel(Toolkit.i18nText("Fine-Design_Chart_Cycle"))},
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_TrendLine_Forward")), null, new UILabel(Toolkit.i18nText("Fine-Design_Chart_After_Period")), null},
        };
        JPanel periodPane = TableLayoutHelper.createTableLayoutPane(periodComps, r, c);

        UILabel label = new UILabel(Toolkit.i18nText("Fine-Design_Chart_Period"));
        label.setVerticalAlignment(SwingConstants.TOP);

        Component[][] components = new Component[][]{
                new Component[]{null, null},
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Name")), trendLineName},
                new Component[]{
                        FRWidgetFactory.createLineWrapLabel(Toolkit.i18nText("Fine-Design_Chart_Line_Style")),
                        UIComponentUtils.wrapWithBorderLayoutPane(trendLineStyle)
                }
        };

        Component[][] componentsMayHide = new Component[][]{
                new Component[]{
                        FRWidgetFactory.createLineWrapLabel(Toolkit.i18nText("Fine-Design_Chart_Line_Width")),
                        UIComponentUtils.wrapWithBorderLayoutPane(lineWidthSpinner)},
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Color")), trendLineColor},
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Type")), trendLineType},
                new Component[]{label, periodPane}
        };

        JPanel panel = TableLayout4VanChartHelper.createGapTableLayoutPane(components, new double[]{p,p,p}, col);
        trendLineHidePane = TableLayout4VanChartHelper.createGapTableLayoutPane(componentsMayHide, new double[]{p,p,p,p}, col);

        trendLineStyle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkHidePaneVisible();
            }
        });

        checkHidePaneVisible();

        this.add(panel, BorderLayout.CENTER);
        this.add(trendLineHidePane, BorderLayout.SOUTH);
    }

    protected String title4PopupWindow(){
        return Toolkit.i18nText("Fine-Design_Chart_TrendLine");
    }

    public void populate(VanChartAttrTrendLine trendLine) {
        checkHidePaneVisible();
        if(trendLine != null){
            trendLineName.setText(trendLine.getTrendLineName());
            LineStyleInfo lineStyleInfo = trendLine.getLineStyleInfo();
            trendLineColor.setSelectObject(lineStyleInfo.getAttrLineColor().getSeriesColor());
            trendLineStyle.setSelectedItem(lineStyleInfo.getAttrLineStyle().getLineType());
            lineWidthSpinner.setValue(lineStyleInfo.getAttrLineStyle().getLineWidth());
            trendLineType.setSelectedItem(trendLine.getTrendLineType());
            prePeriod.setValue(trendLine.getPrePeriod());
            afterPeriod.setValue(trendLine.getAfterPeriod());
        }
    }

    public VanChartAttrTrendLine update() {
        VanChartAttrTrendLine  trendLine = new VanChartAttrTrendLine();
        trendLine.setTrendLineName(trendLineName.getText());

        LineStyleInfo lineStyleInfo = trendLine.getLineStyleInfo();
        lineStyleInfo.getAttrLineStyle().setLineWidth(lineWidthSpinner.getValue());
        lineStyleInfo.getAttrLineStyle().setLineType((LineType) trendLineStyle.getSelectedItem());
        lineStyleInfo.setAttrLineColor(new AttrColor(trendLineColor.getSelectObject()));

        trendLine.setTrendLineType((TrendLineType) trendLineType.getSelectedItem());
        trendLine.setPrePeriod((int) prePeriod.getValue());
        trendLine.setAfterPeriod((int) afterPeriod.getValue());

        return trendLine;
    }

    private void checkHidePaneVisible(){
        if (trendLineHidePane != null && trendLineStyle != null){
            trendLineHidePane.setVisible(trendLineStyle.getSelectedItem() != LineType.NONE);
        }
    }

}