package com.fr.design.chart.series.SeriesCondition.dlp;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import com.fr.chart.base.AttrContents;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import com.fr.stable.Constants;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-29
 * Time   : 下午2:36
 * 
 * 柱形和条形图的 图表样式--"标签"界面
 */
public class Bar2DDataLabelPane extends DataLabelPane {
	private static final long serialVersionUID = -1422949139632959981L;
	private UIRadioButton insideButton;
    private UIRadioButton outSideButton;

    protected JPanel createJPanel4Position() {
        centerButton = new UIRadioButton(Inter.getLocText("FR-Designer-StyleAlignment_Center"));
        insideButton = new UIRadioButton(Inter.getLocText("FR-Chart_DataLabelInside"));
        outSideButton = new UIRadioButton(Inter.getLocText("FR-Chart_DataLabelOutSide"));

        ButtonGroup bg = new ButtonGroup();
        bg.add(insideButton);
        bg.add(outSideButton);
        bg.add(centerButton);

        outSideButton.setSelected(true);
        
        JPanel buttonPane = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
        buttonPane.add(new UILabel(Inter.getLocText(new String[]{"Label", "Layout"}) + ":"));
        buttonPane.add(outSideButton);
        buttonPane.add(insideButton);
        buttonPane.add(centerButton);

        return buttonPane;
    }

    public void populate(AttrContents seriesAttrContents) {
        super.populate(seriesAttrContents);
        int position = seriesAttrContents.getPosition();
        if (insideButton != null && position == Constants.INSIDE) {
            insideButton.setSelected(true);
        } else if (outSideButton != null && position == Constants.OUTSIDE) {
            outSideButton.setSelected(true);
        } else if (centerButton != null) {
            //柱形图除了内外都是居中。
            //组合图条件显示16.1.4之前是上下左右中，现改为内外中。之前存的上下左右都当做中。
            centerButton.setSelected(true);
        }
    }

    public void update(AttrContents seriesAttrContents) {
        super.update(seriesAttrContents);
        if (insideButton != null && insideButton.isSelected()) {
            seriesAttrContents.setPosition(Constants.INSIDE);
        } else if (outSideButton != null && outSideButton.isSelected()) {
            seriesAttrContents.setPosition(Constants.OUTSIDE);
        }
    }
}