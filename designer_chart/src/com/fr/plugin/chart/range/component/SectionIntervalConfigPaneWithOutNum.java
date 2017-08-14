package com.fr.plugin.chart.range.component;

import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.mainframe.chart.gui.style.series.MapColorPickerPaneWithFormula;
import com.fr.general.Inter;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * Created by Mitisky on 16/10/20.
 * 没有主题颜色,自动的时候没有划分阶段
 */
public class SectionIntervalConfigPaneWithOutNum extends MapColorPickerPaneWithFormula{
    private BoldFontTextLabel numLabel;
    public SectionIntervalConfigPaneWithOutNum() {
        super();
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

    private void setRegionVisible(boolean visible){
        getRegionNumPane().setVisible(visible);
        numLabel.setVisible(visible);
    }

    @Override
    protected Component[][] createComponents() {
        numLabel = new BoldFontTextLabel(Inter.getLocText("FR-Chart-Value_Divided_stage"));

        setRegionVisible(false);

        return new Component[][]{
                new Component[]{new BoldFontTextLabel(""),getDesignTypeButtonGroup()},
                new Component[]{numLabel, getRegionNumPane()},
        };
    }

    public Dimension getPreferredSize(){
        Dimension dim = super.getPreferredSize();
        return new Dimension((int)dim.getWidth(), (int) dim.getHeight() - (numLabel.isVisible() ? 0 : 30));
    }
}
