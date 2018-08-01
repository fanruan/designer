package com.fr.van.chart.range.component;

import com.fr.design.gui.frpane.AbstractAttrNoScrollPane;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.mainframe.chart.gui.style.series.MapColorPickerPaneWithFormula;

import com.fr.van.chart.designer.TableLayout4VanChartHelper;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Component;
import java.awt.Dimension;

/**
 * Created by Mitisky on 16/10/20.
 * 没有主题颜色,自动的时候没有划分阶段
 */
public class SectionIntervalConfigPaneWithOutNum extends MapColorPickerPaneWithFormula{
    private BoldFontTextLabel numLabel;

    public SectionIntervalConfigPaneWithOutNum(AbstractAttrNoScrollPane container) {
        super(container);
        getDesignTypeButtonGroup().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(getDesignTypeButtonGroup().getSelectedIndex() == 0){
                    setRegionVisible(false);
                } else {
                    setRegionVisible(true);
                }
            }
        });
    }

    protected JPanel getUpControlPane (Component[][] components) {
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double d = TableLayout4VanChartHelper.DESCRIPTION_AREA_WIDTH;
        double[] columnSize = {d, e};
        JPanel panel = TableLayout4VanChartHelper.createGapTableLayoutPane(components, getRowSIze (), columnSize);
        return panel;
    }

    private void setRegionVisible(boolean visible){
        getRegionNumPane().setVisible(visible);
        numLabel.setVisible(visible);
    }

    @Override
    protected Component[][] createComponents() {
        numLabel = new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("FR-Chart-Value_Divided_stage"));

        setRegionVisible(false);

        return new Component[][]{
                new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_RangeNum")),getDesignTypeButtonGroup()},
                new Component[]{numLabel, getRegionNumPane()},
        };
    }

    public Dimension getPreferredSize(){
        Dimension dim = super.getPreferredSize();
        return new Dimension((int)dim.getWidth(), (int) dim.getHeight() - (numLabel.isVisible() ? 0 : 30));
    }
}
