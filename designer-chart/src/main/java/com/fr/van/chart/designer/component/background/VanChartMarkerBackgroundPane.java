package com.fr.van.chart.designer.component.background;

import com.fr.base.background.ColorBackground;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.backgroundpane.BackgroundQuickPane;
import com.fr.design.mainframe.backgroundpane.ColorBackgroundQuickPane;
import com.fr.design.mainframe.backgroundpane.NullBackgroundQuickPane;

import com.fr.van.chart.designer.TableLayout4VanChartHelper;

import javax.swing.JPanel;
import java.awt.Component;

/**
 * 系列色（无背景颜色）、颜色面板
 */
public class VanChartMarkerBackgroundPane extends VanChartBackgroundPane {
    private static final long serialVersionUID = -1032221277140976934L;

    protected JPanel initContentPanel() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;

        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double[] columnSize = {f, e};
        double[] rowSize = {p, p, p};

        return TableLayoutHelper.createTableLayoutPane(getPaneComponents(), rowSize, columnSize);
    }

    protected Component[][] getPaneComponents() {
        return  new Component[][]{
                new Component[]{null, null},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Chart-Shape_Fill")), typeComboBox},
                new Component[]{null, centerPane},
        };
    }

    protected void initList() {
        paneList.add(new NullBackgroundQuickPane(){
            @Override
            public String title4PopupWindow() {
                return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_SeriesColor");
            }

        });
        paneList.add(new ColorBackgroundQuickPane());
    }

    public void populate(ColorBackground colorBackground) {
        if(colorBackground == null) {
            return;
        }

        for (int i = 0; i < paneList.size(); i++) {
            BackgroundQuickPane pane = paneList.get(i);
            if (pane.accept(colorBackground)) {
                pane.populateBean(colorBackground);
                typeComboBox.setSelectedIndex(i);
                return;
            }
        }
    }

    public ColorBackground update() {
        return (ColorBackground)paneList.get(typeComboBox.getSelectedIndex()).updateBean();
    }
}