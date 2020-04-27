package com.fr.van.chart.designer.style.background;

import com.fr.base.BaseFormula;
import com.fr.base.Utils;
import com.fr.chart.base.AttrColor;
import com.fr.chart.base.AttrLineStyle;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.i18n.Toolkit;
import com.fr.design.gui.style.FRFontPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.style.color.ColorSelectBox;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.FRFont;
import com.fr.general.GeneralUtils;
import com.fr.plugin.chart.VanChartAttrHelper;
import com.fr.plugin.chart.attr.axis.VanChartAlertValue;
import com.fr.plugin.chart.type.LineType;
import com.fr.stable.Constants;
import com.fr.stable.StableUtils;
import com.fr.van.chart.designer.component.LineTypeComboBox;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

/**
 * Created by Mitisky on 15/10/13.
 */
public class VanChartAlertValuePane extends BasicBeanPane<VanChartAlertValue> {
    private static final int HT = 20;
    private static final int VALUE_WD = 100;
    private static final int TEXT_WD = 200;
    private static final long serialVersionUID = -1208941770684286439L;

    private static final int PREFERRED_WIDTH = 100;
    private static final double LINE_WIDTH_MIN_VALUE = 0.5;
    private static final double LINE_WIDTH_DIERTA_VALUE = 0.5;
    private static final double LINE_WIDTH_DEFAULT_VALUE = 2;

    private UIButtonGroup alertAxis;
    protected TinyFormulaPane alertValue;
    protected LineTypeComboBox alertLineStyle;//线型
    private UISpinner lineWidthSpinner;//线宽
    protected ColorSelectBox alertLineColor;

    private UIButtonGroup alertTextPosition;
    private TinyFormulaPane alertText;
    private UIComboBox fontSize;
    private UIComboBox fontName;
    private ColorSelectBox fontColor;

    private VanChartAlertValue chartAlertValue;

    public VanChartAlertValuePane() {
        initComponents();
    }

    private void initComponents() {
        alertValue = new TinyFormulaPane();

        //設置大小，防止文本過長導致界面“變形”
        alertValue.setPreferredSize(new Dimension(VALUE_WD, HT));

        alertLineStyle = new LineTypeComboBox(new LineType[]{LineType.SOLID, LineType.DASHED});
        lineWidthSpinner = new UISpinner(LINE_WIDTH_MIN_VALUE, Integer.MAX_VALUE, LINE_WIDTH_DIERTA_VALUE, LINE_WIDTH_DEFAULT_VALUE);
        alertLineColor = new ColorSelectBox(PREFERRED_WIDTH);
        alertTextPosition = new UIButtonGroup(new String[]{Toolkit.i18nText("Fine-Design_Chart_Axis_Top"), Toolkit.i18nText("Fine-Design_Chart_Axis_Bottom")});
        alertText = new TinyFormulaPane();
        //設置大小，防止文本過長導致界面“變形”
        alertText.setPreferredSize(new Dimension(TEXT_WD, HT));

        fontSize = new UIComboBox(FRFontPane.FONT_SIZES);
        fontName = new UIComboBox(Utils.getAvailableFontFamilyNames4Report());
        fontColor = new ColorSelectBox(100);
    }


    private void doLayoutPane() {
        this.removeAll();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        //警戒线设置
        JPanel top = FRGUIPaneFactory.createBorderLayout_L_Pane();
        this.add(top);
        top.setBorder(GUICoreUtils.createTitledBorder(Toolkit.i18nText("Fine-Design_Chart_Alert_Set") + ":", null));
        top.add(createTopPane());
        //提示文字
        JPanel bottom = FRGUIPaneFactory.createBorderLayout_L_Pane();
        this.add(bottom);
        bottom.setBorder(GUICoreUtils.createTitledBorder(Toolkit.i18nText("Fine-Design_Chart_Alert_Text") + ":", null));
        bottom.add(createBottomPane());
    }

    protected JPanel createTopPane() {
        double p = TableLayout.PREFERRED;
        double[] columnSize = {p, p};
        double[] rowSize = {p, p, p, p, p};
        Component[][] components = getTopPaneComponents();

        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

    protected Component[][] getTopPaneComponents() {
        return new Component[][]{
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Layout_Position")), alertAxis},
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Use_Value")), alertValue},
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Line_Style")), alertLineStyle},
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Line_Width")), lineWidthSpinner},
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Color")), alertLineColor},
        };
    }

    private JPanel createBottomPane() {
        alertTextPosition.setSelectedIndex(0);
        double p = TableLayout.PREFERRED;
        double[] columnSize = {p, p};
        double[] rowSize = {p, p, p, p, p};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Layout_Position")), alertTextPosition},
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Content")), alertText},
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Form_Font")), fontName},
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Form_FRFont_Size")), fontSize},
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Color")), fontColor},
        };

        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

    private void checkPositionPane() {
        boolean selectXAxis = VanChartAttrHelper.isXAxis(alertAxis.getSelectedItem().toString());
        if (selectXAxis) {
            alertTextPosition = new UIButtonGroup(new String[]{Toolkit.i18nText("Fine-Design_Chart_Axis_Top"), Toolkit.i18nText("Fine-Design_Chart_Axis_Bottom")});
        } else {
            alertTextPosition = new UIButtonGroup(new String[]{Toolkit.i18nText("Fine-Design_Chart_Alert_Left"), Toolkit.i18nText("Fine-Design_Chart_Alert_Right")});
        }
        doLayoutPane();
    }

    protected String title4PopupWindow() {
        return Toolkit.i18nText("Fine-Design_Chart_Alert_Line");
    }

    public void populateBean(VanChartAlertValue chartAlertValue) {
        this.chartAlertValue = chartAlertValue;
        alertAxis = new UIButtonGroup(chartAlertValue.getAxisNamesArray(), chartAlertValue.getAxisNamesArray());
        alertAxis.setSelectedItem(chartAlertValue.getAxisName());
        alertAxis.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                checkPositionPane();
            }
        });

        checkPositionPane();

        alertValue.populateBean(Utils.objectToString(chartAlertValue.getAlertValueFormula()));

        AttrColor attrColor = chartAlertValue.getLineColor();
        AttrLineStyle attrLineStyle = chartAlertValue.getLineStyle();

        if (attrColor != null) {
            alertLineColor.setSelectObject(attrColor.getSeriesColor());
        }

        if (attrLineStyle != null) {
            alertLineStyle.setSelectedItem(attrLineStyle.getLineType());
            lineWidthSpinner.setValue(attrLineStyle.getLineWidth());
        }

        if (VanChartAttrHelper.isXAxis(chartAlertValue.getAxisName())) {
            alertTextPosition.setSelectedIndex(chartAlertValue.getAlertPosition() == Constants.BOTTOM ? 1 : 0);
        } else {
            alertTextPosition.setSelectedIndex(chartAlertValue.getAlertPosition() == Constants.LEFT ? 0 : 1);
        }

        if (chartAlertValue.getAlertContentFormula() instanceof BaseFormula) {
            alertText.populateBean(((BaseFormula) chartAlertValue.getAlertContentFormula()).getContent());
        } else {
            alertText.populateBean(GeneralUtils.objectToString(chartAlertValue.getAlertContentFormula()));
        }
        fontName.setSelectedItem(chartAlertValue.getAlertFont().getName());
        fontSize.setSelectedItem(chartAlertValue.getAlertFont().getSize());
        fontColor.setSelectObject(chartAlertValue.getAlertFont().getForeground());
    }

    public VanChartAlertValue updateBean() {
        chartAlertValue.setAxisName(alertAxis.getSelectedItem().toString());

        chartAlertValue.setAlertValueFormula(BaseFormula.createFormulaBuilder().build(alertValue.updateBean()));

        AttrColor attrColor = chartAlertValue.getLineColor();
        AttrLineStyle attrLineStyle = chartAlertValue.getLineStyle();

        if (attrColor != null) {
            attrColor.setSeriesColor(alertLineColor.getSelectObject());
        }

        if (attrLineStyle != null) {
            attrLineStyle.setLineType((LineType) alertLineStyle.getSelectedItem());
            attrLineStyle.setLineWidth(lineWidthSpinner.getValue());
        }

        String contentString = alertText.updateBean();
        Object contentObj;
        if (StableUtils.maybeFormula(contentString)) {
            contentObj = BaseFormula.createFormulaBuilder().build(contentString);
        } else {
            contentObj = contentString;
        }

        chartAlertValue.setAlertContentFormula(contentObj);
        String name = Utils.objectToString(fontName.getSelectedItem());
        int size = Utils.objectToNumber(fontSize.getSelectedItem(), true).intValue();
        Color color = fontColor.getSelectObject();
        chartAlertValue.setAlertFont(FRFont.getInstance(name, Font.PLAIN, size, color));
        if (VanChartAttrHelper.isXAxis(Utils.objectToString(alertAxis.getSelectedItem()))) {
            chartAlertValue.setAlertPosition(alertTextPosition.getSelectedIndex() == 0 ? Constants.TOP : Constants.BOTTOM);
        } else {
            chartAlertValue.setAlertPosition(alertTextPosition.getSelectedIndex() == 0 ? Constants.LEFT : Constants.RIGHT);
        }
        return chartAlertValue;
    }
}
