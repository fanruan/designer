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

    private static final double LINE_WIDTH_MIN_VALUE = 0.5;
    private static final double LINE_WIDTH_DIERTA_VALUE = 0.5;
    private static final double LINE_WIDTH_DEFAULT_VALUE = 2;

    private LineTypeComboBox lineTypeComboBox;//线型
    private UISpinner lineWidthSpinner;//线宽
    protected UIButtonGroup<LineStyle> lineStyle;//形态
    private UIButtonGroup nullValueBreak;//空值断开

    private JPanel lineWidthPane;
    private JPanel lineStylePane;

    public VanChartLineTypePane() {

        JPanel typeAndWidthPane = createTypeAndWidthPane();

        createLineStyle();

        nullValueBreak = new UIButtonGroup(new String[]{
                Toolkit.i18nText("Fine-Design_Chart_Open"),
                Toolkit.i18nText("Fine-Design_Chart_Close")});

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;

        Component[] lineStyleComponent = new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Style_Present")), lineStyle},
                nullValueBreakComponent = new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Null_Value_Break")), nullValueBreak};

        Component[][] components = createContentComponent(lineStyleComponent, nullValueBreakComponent);

        double[] row = new double[components.length];
        Arrays.fill(row, p);
        double[] col = {f, e};

        lineStylePane = TableLayout4VanChartHelper.createGapTableLayoutPane(components, row, col);

        this.setLayout(new BorderLayout(0, 6));
        this.add(typeAndWidthPane, BorderLayout.NORTH);
        this.add(lineStylePane, BorderLayout.CENTER);
    }

    private JPanel createTypeAndWidthPane() {

        lineTypeComboBox = new LineTypeComboBox(new LineType[]{LineType.NONE, LineType.SOLID, LineType.DASHED});

        lineWidthSpinner = new UISpinner(LINE_WIDTH_MIN_VALUE, Integer.MAX_VALUE, LINE_WIDTH_DIERTA_VALUE, LINE_WIDTH_DEFAULT_VALUE);

        lineTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkLineWidth();
                checkLineStyle();
            }
        });

        Component[][] lineTypeComps = new Component[][]{
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Line_Style")), lineTypeComboBox}
        };
        Component[][] lineWidthComps = new Component[][]{
                new Component[]{
                        FRWidgetFactory.createLineWrapLabel(Toolkit.i18nText("Fine-Design_Chart_Line_Width")),
                        UIComponentUtils.wrapWithBorderLayoutPane(lineWidthSpinner)}
        };

        double p = TableLayout.PREFERRED, f = TableLayout.FILL, e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double[] row = {p}, col = {f, e};

        JPanel lineTypePane = TableLayout4VanChartHelper.createGapTableLayoutPane(lineTypeComps, row, col);
        lineWidthPane = TableLayout4VanChartHelper.createGapTableLayoutPane(lineWidthComps, row, col);

        JPanel contentPane = new JPanel(new BorderLayout(0, 6));

        contentPane.add(lineTypePane, BorderLayout.CENTER);
        contentPane.add(lineWidthPane, BorderLayout.SOUTH);

        return contentPane;
    }

    protected void createLineStyle() {
        String[] textArray = new String[]{Toolkit.i18nText("Fine-Design_Chart_Normal_Line"),
                Toolkit.i18nText("Fine-Design_Chart_StepLine"), Toolkit.i18nText("Fine-Design_Chart_CurveLine")};
        lineStyle = new UIButtonGroup<LineStyle>(textArray, LineStyle.values());
    }

    protected Component[][] createContentComponent(Component[] lineStyleComponent, Component[] nullValueBreakComponent) {
        return new Component[][]{
                lineStyleComponent,
                nullValueBreakComponent
        };
    }

    private void checkLineWidth() {
        if (lineWidthPane != null && lineTypeComboBox != null) {
            lineWidthPane.setVisible(!ComparatorUtils.equals(lineTypeComboBox.getSelectedItem(), LineType.NONE));
        }
    }

    private void checkLineStyle() {
        if (lineStylePane != null && lineTypeComboBox != null) {
            lineStylePane.setVisible(!ComparatorUtils.equals(lineTypeComboBox.getSelectedItem(),LineType.NONE));
        }
    }

    public void checkLarge(boolean large) {
        if (large) {
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