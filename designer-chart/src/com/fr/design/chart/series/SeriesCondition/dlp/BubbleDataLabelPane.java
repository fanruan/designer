package com.fr.design.chart.series.SeriesCondition.dlp;

import com.fr.chart.base.AttrContents;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import com.fr.stable.Constants;

import javax.swing.*;
import java.awt.*;

/**
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2012-4-13 上午10:08:39
 * 类说明: 气泡系列标签界面
 */
public class BubbleDataLabelPane extends DataLabelPane {
	private static final long serialVersionUID = 6122072293885219893L;
	
	private UIButton insideButton;
    private UIButton outSideButton;

	protected JPanel createJPanel4Position() {
		insideButton = new UIButton(Inter.getLocText("Chart_Bubble_Inside"));
		outSideButton = new UIButton(Inter.getLocText("Chart_Bubble_Outside"));
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(insideButton);
		bg.add(outSideButton);
		
		outSideButton.setSelected(true);
		
		JPanel buttonPane = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
		buttonPane.add(new UILabel(Inter.getLocText(new String[]{"Label", "Layout"}) + ":"));
		buttonPane.add(outSideButton);
		buttonPane.add(insideButton);
		
		return buttonPane;
	}

    protected Component[] createComponents4ShowCategoryName() {
        return new Component[0];
    }

    protected Component[] createComponents4PercentValue() {
        return new Component[0];
    }

	public void populate(AttrContents seriesAttrContents) {
        super.populate(seriesAttrContents);
        int position = seriesAttrContents.getPosition();
        if (insideButton != null && position == Constants.INSIDE) {
            insideButton.setSelected(true);
        } else if (outSideButton != null && position == Constants.OUTSIDE) {
            outSideButton.setSelected(true);
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