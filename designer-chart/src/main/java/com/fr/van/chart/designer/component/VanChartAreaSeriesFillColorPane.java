package com.fr.van.chart.designer.component;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;

import com.fr.plugin.chart.VanChartAttrHelper;
import com.fr.plugin.chart.base.AttrAreaSeriesFillColorBackground;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.designer.component.background.VanChartMarkerBackgroundPane;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;

/**
 * 面积图填充色设置界面
 */
public class VanChartAreaSeriesFillColorPane extends BasicPane {
    private static final long serialVersionUID = 9166866984438854779L;
    private VanChartMarkerBackgroundPane fillColorPane;
    private UINumberDragPane transparent;

    public VanChartAreaSeriesFillColorPane() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] row = {p,p};
        double[] col = {f};
        fillColorPane = new VanChartMarkerBackgroundPane(){
            protected Component[][] getPaneComponents() {
                return  new Component[][]{
                        new Component[]{null, null},
                        new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Fill_Color")), typeComboBox},
                        new Component[]{null, centerPane},
                };
            }
        };
        transparent = new UINumberDragPane(0, 100);

        JPanel transparentPane = TableLayout4VanChartHelper.createGapTableLayoutPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Alpha"), transparent);

        this.setLayout(new BorderLayout());
        this.add(fillColorPane, BorderLayout.NORTH);
        this.add(transparentPane, BorderLayout.CENTER);
    }

    protected String title4PopupWindow(){
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Fill_Color");
    }

    public void populate(AttrAreaSeriesFillColorBackground fillColorBackground) {
        if(fillColorBackground == null){
            fillColorBackground = new AttrAreaSeriesFillColorBackground();
        }
        fillColorPane.populate(fillColorBackground.getColorBackground());
        transparent.populateBean(fillColorBackground.getAlpha() * VanChartAttrHelper.PERCENT);
    }

    public AttrAreaSeriesFillColorBackground update() {
        AttrAreaSeriesFillColorBackground fillColorBackground = new AttrAreaSeriesFillColorBackground();
        fillColorBackground.setColorBackground(fillColorPane.update());
        fillColorBackground.setAlpha(transparent.updateBean()/VanChartAttrHelper.PERCENT);
        return fillColorBackground;
    }

    /**
     * 检查透明的设置是否有效
     * @param enable
     */
    public void checkoutAlpha(boolean enable) {
        transparent.setEnabled(enable);
    }
}