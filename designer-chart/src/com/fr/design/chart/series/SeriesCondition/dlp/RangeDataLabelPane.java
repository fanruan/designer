package com.fr.design.chart.series.SeriesCondition.dlp;

import com.fr.design.gui.ilable.UILabel;
import com.fr.stable.Constants;
import com.fr.general.Inter;
import com.fr.chart.base.AttrContents;
import com.fr.design.layout.FRGUIPaneFactory;

import javax.swing.*;

/**
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2012-4-13 上午10:54:56
 * 类说明: 全距图的标签界面
 */
public class RangeDataLabelPane extends DataLabelPane {

	private JRadioButton topButton;
	private JRadioButton centerButton;
	private JRadioButton bottomButton;
	  
    protected JPanel createJPanel4Position() {
    	topButton = new JRadioButton(Inter.getLocText("StyleAlignment-Top"));
    	bottomButton = new JRadioButton(Inter.getLocText("StyleAlignment-Bottom"));
    	centerButton = new JRadioButton(Inter.getLocText("Center"));
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(topButton);
		bg.add(bottomButton);
		bg.add(centerButton);
		
		topButton.setSelected(true);
		
		JPanel buttonPane = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
		buttonPane.add(new UILabel(Inter.getLocText(new String[]{"Label", "Layout"}) + ":"));
		buttonPane.add(topButton);
		buttonPane.add(bottomButton);
		buttonPane.add(centerButton);
		
		return buttonPane;
    }
    
    public void populate(AttrContents seriesAttrContents) {
        super.populate(seriesAttrContents);
        int position = seriesAttrContents.getPosition();
        if (centerButton != null && position == Constants.CENTER) {
        	centerButton.setSelected(true);
        } else if (bottomButton != null && position == Constants.BOTTOM) {
        	bottomButton.setSelected(true);
        } else if(topButton != null){
        	topButton.setSelected(true);
        }
    }

    public void update(AttrContents seriesAttrContents) {
        super.update(seriesAttrContents);
        if (centerButton != null && centerButton.isSelected()) {
            seriesAttrContents.setPosition(Constants.CENTER);
        } else if (bottomButton != null && bottomButton.isSelected()) {
            seriesAttrContents.setPosition(Constants.BOTTOM);
        } else {
        	seriesAttrContents.setPosition(Constants.TOP);
        }
    }
}