package com.fr.van.chart.designer.component.border;


import com.fr.chart.base.AttrBorder;
import com.fr.chart.chartglyph.GeneralInfo;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.icombobox.LineComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.style.color.ColorSelectBox;

import com.fr.design.utils.gui.UIComponentUtils;
import com.fr.stable.CoreConstants;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;

//线型 + 颜色
public class VanChartBorderPane extends BasicPane {
    private static final long serialVersionUID = -7770029552989609464L;
    protected LineComboBox currentLineCombo;
    protected ColorSelectBox currentLineColorPane;

    public VanChartBorderPane() {
        initComponents();
    }

    protected void initComponents() {
        currentLineCombo = new LineComboBox(CoreConstants.STRIKE_LINE_STYLE_ARRAY_4_CHART);
        currentLineColorPane = new ColorSelectBox(100);
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double[] columnSize = {f, e};
        double[] rowSize = {p, p, p, p};
        Component[][] components = getUseComponent();
        JPanel panel = TableLayout4VanChartHelper.createGapTableLayoutPane(components, rowSize, columnSize);
        this.setLayout(new BorderLayout());
        this.add(panel,BorderLayout.CENTER);
    }

    protected Component[][] getUseComponent() {
        UILabel lineStyleLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Line_Style"));
        UIComponentUtils.setLineWrap(lineStyleLabel);
        UILabel colorLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Color"));
        UIComponentUtils.setLineWrap(colorLabel);

        return new Component[][]{
                new Component[]{null,null},
                new Component[]{lineStyleLabel, currentLineCombo},
                new Component[]{colorLabel, currentLineColorPane},
        };
    }

    /**
     * 标题
     * @return 标题
     */
    public String title4PopupWindow() {
        return null;
    }

    public void populate(GeneralInfo attr) {
        if(attr == null) {
            return;
        }
        currentLineCombo.setSelectedLineStyle(attr.getBorderStyle());
        currentLineColorPane.setSelectObject(attr.getBorderColor());

    }

    public void update(GeneralInfo attr) {
        if(attr == null) {
            attr = new GeneralInfo();
        }
        attr.setBorderStyle(currentLineCombo.getSelectedLineStyle());
        attr.setBorderColor(currentLineColorPane.getSelectObject());

    }

    public void update(AttrBorder attrBorder){
        if(attrBorder == null){
            return;
        }
        attrBorder.setBorderStyle(currentLineCombo.getSelectedLineStyle());
        attrBorder.setBorderColor(currentLineColorPane.getSelectObject());
    }

    public void populate(AttrBorder attr) {
        if(attr == null) {
            return;
        }
        currentLineCombo.setSelectedLineStyle(attr.getBorderStyle());
        currentLineColorPane.setSelectObject(attr.getBorderColor());
    }

    public AttrBorder update() {
        AttrBorder attr = new AttrBorder();

        attr.setBorderStyle(currentLineCombo.getSelectedLineStyle());
        attr.setBorderColor(currentLineColorPane.getSelectObject());

        return attr;
    }
}