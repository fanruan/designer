package com.fr.van.chart.designer.component;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.TableLayout;
import com.fr.design.utils.gui.UIComponentUtils;
import com.fr.design.widget.FRWidgetFactory;
import com.fr.general.ComparatorUtils;
import com.fr.plugin.chart.base.VanChartAttrLine;
import com.fr.plugin.chart.type.LineStyle;
import com.fr.plugin.chart.type.LineType;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

/**
 * line相关设置
 */
public class VanChartLineTypePane extends BasicPane {

    private static final long serialVersionUID = -6581862503009962973L;
    private LineTypeComboBox lineTypeComboBox;//线型
    private UISpinner lineWidthSpinner;//线宽
    protected UIButtonGroup<LineStyle> lineStyle;//形态
    private UIButtonGroup nullValueBreak;//空值断开

    public VanChartLineTypePane() {
        lineTypeComboBox = new LineTypeComboBox(new LineType[]{LineType.NONE, LineType.NORMAL, LineType.DASH});

        lineWidthSpinner = new UISpinner(0.5, Integer.MAX_VALUE, 0.5, 2);

        createLineStyle();

        nullValueBreak = new UIButtonGroup(new String[]{Toolkit.i18nText("Fine-Design_Chart_Open"), Toolkit.i18nText("Fine-Design_Chart_Close")});

        lineTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkLineWidth();
            }
        });

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;

        Component[] lineTypeComponent = new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Line_Style")), lineTypeComboBox},
                lineWidthComponent = new Component[]{
                        FRWidgetFactory.createLineWrapLabel(Toolkit.i18nText("Fine-Design_Chart_Line_Width")),
                        UIComponentUtils.wrapWithBorderLayoutPane(lineWidthSpinner)},
                lineStyleComponent = new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Style_Present")), lineStyle},
                nullValueBreakComponent = new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Null_Value_Break")), nullValueBreak};

        Component[][] components = createContentComponent(lineTypeComponent, lineWidthComponent, lineStyleComponent, nullValueBreakComponent);

        double[] row = new double[components.length];
        Arrays.fill(row, p);
        double[] col = {f, e};

        JPanel contentPane = TableLayout4VanChartHelper.createGapTableLayoutPane(components, row, col);

        this.setLayout(new BorderLayout());
        this.add(contentPane, BorderLayout.CENTER);
    }

    private JPanel createTypeAndWidthPane() {
        //todo@shinerefactor:当前兼容工作到这边，因为finekit和移动端，暂停
        JPanel panel = new JPanel(new BorderLayout(0, 6));


        return panel;
    }

    protected void createLineStyle() {
        String[] textArray = new String[]{Toolkit.i18nText("Fine-Design_Chart_Normal_Line"),
                Toolkit.i18nText("Fine-Design_Chart_StepLine"), Toolkit.i18nText("Fine-Design_Chart_CurveLine")};
        lineStyle = new UIButtonGroup<LineStyle>(textArray, LineStyle.values());
    }

    protected Component[][] createContentComponent(Component[] lineTypeComponent, Component[] lineWidthComponent,
                                                   Component[] lineStyleComponent, Component[] nullValueBreakComponent) {
        return new Component[][]{
                new Component[]{null,null},
                lineTypeComponent,
                lineWidthComponent,
                lineStyleComponent,
                nullValueBreakComponent
        };
    }

    private void checkLineWidth() {
        if (lineWidthSpinner != null && lineTypeComboBox != null) {
            lineWidthSpinner.setVisible(!ComparatorUtils.equals(lineTypeComboBox.getSelectedItem(), LineType.NONE));
        }
    }

    public void checkLarge(boolean large){
        if(large){
            lineTypeComboBox.setSelectedItem(LineType.NONE);
        }
        lineTypeComboBox.setEnabled(!large);
        lineStyle.setEnabled(!large);
    }

    protected String title4PopupWindow() {
        return Toolkit.i18nText("Fine-Design_Chart_Line");
    }

    public void populate(VanChartAttrLine line) {
        if (line == null) {
            line = initVanChartAttrLine();
        }
        lineTypeComboBox.setSelectedItem(line.getLineType());
        lineWidthSpinner.setValue(line.getLineWidth());
        lineStyle.setSelectedItem(line.getLineStyle());
        nullValueBreak.setSelectedIndex(line.isNullValueBreak() ? 0 : 1);
    }

    protected VanChartAttrLine initVanChartAttrLine() {
        return new VanChartAttrLine();
    }

    public VanChartAttrLine update() {
        VanChartAttrLine line = new VanChartAttrLine();
        line.setLineType((LineType) lineTypeComboBox.getSelectedItem());
        line.setLineWidth(lineWidthSpinner.getValue());
        line.setLineStyle(lineStyle.getSelectedItem());
        line.setNullValueBreak(nullValueBreak.getSelectedIndex() == 0);
        return line;
    }
}