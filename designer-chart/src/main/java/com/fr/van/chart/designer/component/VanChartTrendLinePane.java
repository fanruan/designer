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
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.style.color.ColorSelectBox;
import com.fr.design.utils.gui.UIComponentUtils;
import com.fr.design.widget.FRWidgetFactory;
import com.fr.plugin.chart.base.TrendLineType;
import com.fr.plugin.chart.base.VanChartAttrTrendLine;
import com.fr.plugin.chart.base.VanChartConstants;
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

    //线型支持虚线 恢复用注释。下面1行删除。
    private LineComboBox trendLineStyle;//线型
    //线型支持虚线 恢复用注释。取消注释。
//    private LineTypeComboBox trendLineStyle;//线型
//    private UISpinner lineWidthSpinner;//线宽

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
        double[] row = {p, p, p, p, p, p};
        double[] col = {f, e};
        trendLineName = new UITextField();
        trendLineColor = new ColorSelectBox(100);
        //线型支持虚线 恢复用注释。下面1行删除。
        trendLineStyle = new LineComboBox(VanChartConstants.ALERT_LINE_STYLE);
        //线型支持虚线 恢复用注释。取消注释。
//        trendLineStyle = new LineTypeComboBox(new LineType[]{LineType.NONE, LineType.NORMAL, LineType.DASH});
//        lineWidthSpinner = new UISpinner(0.5, Integer.MAX_VALUE, 0.5, 2);
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

        //线型支持虚线 恢复用注释。开始删除。
        Component[][] components = new Component[][]{
                new Component[]{null, null},
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Name")), trendLineName},
                new Component[]{
                        FRWidgetFactory.createLineWrapLabel(Toolkit.i18nText("Fine-Design_Chart_Line_Style")),
                        UIComponentUtils.wrapWithBorderLayoutPane(trendLineStyle)
                },
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Color")), trendLineColor},
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Type")), trendLineType},
                new Component[]{label, periodPane}
        };
        //线型支持虚线 恢复用注释。结束删除。

        //线型支持虚线 恢复用注释。取消注释。
//        Component[][] components = new Component[][]{
//                new Component[]{null, null},
//                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Name")), trendLineName},
//                new Component[]{
//                        FRWidgetFactory.createLineWrapLabel(Toolkit.i18nText("Fine-Design_Chart_Line_Style")),
//                        UIComponentUtils.wrapWithBorderLayoutPane(trendLineStyle)
//                }
//        };
//
//        Component[][] componentsMayHide = new Component[][]{
//                new Component[]{
//                        FRWidgetFactory.createLineWrapLabel(Toolkit.i18nText("Fine-Design_Chart_Line_Width")),
//                        UIComponentUtils.wrapWithBorderLayoutPane(lineWidthSpinner)},
//                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Color")), trendLineColor},
//                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Type")), trendLineType},
//                new Component[]{label, periodPane}
//        };
//        trendLineHidePane = TableLayout4VanChartHelper.createGapTableLayoutPane(componentsMayHide, row, col);


        JPanel panel = TableLayout4VanChartHelper.createGapTableLayoutPane(components, row, col);

        trendLineStyle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkHidePaneVisible();
            }
        });

        checkHidePaneVisible();

        this.add(panel, BorderLayout.CENTER);
        //线型支持虚线 恢复用注释。取消注释。
        //this.add(trendLineHidePane, BorderLayout.SOUTH);
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
            //线型支持虚线 恢复用注释。下面1行删除。
            trendLineStyle.setSelectedLineStyle(lineStyleInfo.getAttrLineStyle().getLineStyle());
            //线型支持虚线 恢复用注释。取消注释。
//            trendLineStyle.setSelectedItem(lineStyleInfo.getAttrLineStyle().getLineType());
//            lineWidthSpinner.setValue(lineStyleInfo.getAttrLineStyle().getLineWidth());
            trendLineType.setSelectedItem(trendLine.getTrendLineType());
            prePeriod.setValue(trendLine.getPrePeriod());
            afterPeriod.setValue(trendLine.getAfterPeriod());
        }
    }

    public VanChartAttrTrendLine update() {
        VanChartAttrTrendLine  trendLine = new VanChartAttrTrendLine();
        trendLine.setTrendLineName(trendLineName.getText());

        LineStyleInfo lineStyleInfo = trendLine.getLineStyleInfo();
        //线型支持虚线 恢复用注释。下面1行删除。
        lineStyleInfo.setAttrLineStyle(new AttrLineStyle(trendLineStyle.getSelectedLineStyle()));
        //线型支持虚线 恢复用注释。取消注释。
//        lineStyleInfo.getAttrLineStyle().setLineWidth(lineWidthSpinner.getValue());
//        lineStyleInfo.getAttrLineStyle().setLineType((LineType) trendLineStyle.getSelectedItem());
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