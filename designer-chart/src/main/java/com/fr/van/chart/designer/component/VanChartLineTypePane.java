package com.fr.van.chart.designer.component;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icombobox.LineComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;

import com.fr.plugin.chart.base.VanChartAttrLine;
import com.fr.plugin.chart.type.LineStyle;
import com.fr.stable.Constants;
import com.fr.stable.CoreConstants;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;

/**
 * line相关设置
 */
public class VanChartLineTypePane extends BasicPane {

    private static final long serialVersionUID = -6581862503009962973L;
    protected LineComboBox lineWidth;//线型
    protected UIButtonGroup<LineStyle> lineStyle;//形态
    protected UIButtonGroup nullValueBreak;//空值断开

    public VanChartLineTypePane() {
        lineWidth = new LineComboBox(CoreConstants.STRIKE_LINE_STYLE_ARRAY_4_CHART);

        createLineStyle();

        nullValueBreak = new UIButtonGroup(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Open"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Close")});

        this.setLayout(new BorderLayout());
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        this.add(createContentPane(p, f), BorderLayout.CENTER);
    }

    protected void createLineStyle() {
        String[] textArray = new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Normal_Line"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_StepLine"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_CurveLine")};
        lineStyle = new UIButtonGroup<LineStyle>(textArray, LineStyle.values());
    }

    protected JPanel createContentPane(double p, double f) {
        double[] row = {p, p, p, p};
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double[] col = {f, e};

        Component[][] components = new Component[][]{
                new Component[]{null,null},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Line_Style")),lineWidth},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Chart-Style_Present")),lineStyle},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Null_Value_Break")),nullValueBreak},
        };

        return TableLayout4VanChartHelper.createGapTableLayoutPane(components, row, col);
    }

    public void checkLarge(boolean large){
        if(large){
            lineWidth.setSelectedLineStyle(Constants.LINE_NONE);
        }
        lineWidth.setEnabled(!large);
        lineStyle.setEnabled(!large);
    }

    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Line");
    }

    public void populate(VanChartAttrLine line) {
        if (line == null) {
            line = initVanChartAttrLine();
        }
        lineWidth.setSelectedLineStyle(line.getLineWidth());
        lineStyle.setSelectedItem(line.getLineStyle());
        nullValueBreak.setSelectedIndex(line.isNullValueBreak() ? 0 : 1);
    }

    protected VanChartAttrLine initVanChartAttrLine() {
        return new VanChartAttrLine();
    }

    public VanChartAttrLine update() {
        VanChartAttrLine line = new VanChartAttrLine();
        line.setLineWidth(lineWidth.getSelectedLineStyle());
        line.setLineStyle(lineStyle.getSelectedItem());
        line.setNullValueBreak(nullValueBreak.getSelectedIndex() == 0);
        return line;
    }
}