package com.fr.design.chart.series.SeriesCondition.dlp;

import com.fr.design.gui.ilable.UILabel;
import com.fr.general.Inter;
import com.fr.stable.Constants;
import com.fr.chart.base.AttrContents;
import com.fr.design.layout.FRGUIPaneFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-29
 * Time   : 上午10:39
 */
public class PieDataLabelPane extends DataLabelPane {
    private JRadioButton insideButton;
    private JRadioButton outSideButton;
    private JCheckBox showGuidLine;

    protected Component[] createComponents4ShowGuidLine() {
        if (showGuidLine == null) {
            showGuidLine = new JCheckBox(Inter.getLocText("ChartF-Show_GuidLine"));
        }
        return new Component[]{null, showGuidLine};
    }
    
    private ActionListener getPositionListener() {
    	return new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			checkGuidBox();
    		}
    	};
    }

    public void checkGuidBox() {
    	if(insideButton != null && insideButton.isSelected()) {
    		showGuidLine.setSelected(false);
    		showGuidLine.setEnabled(false);
    	}
        
        if(outSideButton != null && outSideButton.isSelected()) {
        	showGuidLine.setEnabled(true);
        	showGuidLine.setSelected(true);
        }
    }

    protected JPanel createJPanel4Position() {
        insideButton = new JRadioButton(Inter.getLocText("Chart_In_Pie"));
        outSideButton = new JRadioButton(Inter.getLocText("Chart_Out_Pie"));
        outSideButton.addActionListener(getPositionListener());
        insideButton.addActionListener(getPositionListener());
        showPercent.addActionListener(getPieLeadActionListener());
        showSeriesNameCB.addActionListener(getPieLeadActionListener());
        showCategoryNameCB.addActionListener(getPieLeadActionListener());
        showValueCB.addActionListener(getPieLeadActionListener());

        ButtonGroup bg = new ButtonGroup();
        bg.add(insideButton);
        bg.add(outSideButton);
        insideButton.setSelected(true);
        
        JPanel buttonPane = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
        buttonPane.add(new UILabel(Inter.getLocText(new String[]{"Label", "Layout"}) + ":"));
        buttonPane.add(insideButton);
        buttonPane.add(outSideButton);

        return buttonPane;
    }
    
    private ActionListener getPieLeadActionListener() {
    	return new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			checkShowLineBox();
    		}
    	};
    }
    
    private void checkShowLineBox() {
        if (showGuidLine != null) {
            showGuidLine.setEnabled((showCategoryNameCB.isSelected()
                    || showSeriesNameCB.isSelected()
                    || showValueCB.isSelected()
                    || showPercent.isSelected()));
        }
    }

    public void populate(AttrContents seriesAttrContents) {
        super.populate(seriesAttrContents);
        if (showGuidLine != null) {
            showGuidLine.setSelected(seriesAttrContents.isShowGuidLine());
            showGuidLine.setEnabled(showSeriesNameCB.isSelected() || showCategoryNameCB.isSelected() || showValueCB.isSelected() || showPercent.isSelected());
        }
        int position = seriesAttrContents.getPosition();
        if (insideButton != null && position == Constants.INSIDE) {
            insideButton.setSelected(true);
        } else if (outSideButton != null && position == Constants.OUTSIDE) {
            outSideButton.setSelected(true);
        }
    }

    public void update(AttrContents seriesAttrContents) {
        super.update(seriesAttrContents);
        if (showGuidLine != null && showGuidLine.isEnabled()) {
            seriesAttrContents.setShowGuidLine(showGuidLine.isSelected());
        }
        if (insideButton != null && insideButton.isSelected()) {
            seriesAttrContents.setPosition(Constants.INSIDE);
        } else if (outSideButton != null && outSideButton.isSelected()) {
            seriesAttrContents.setPosition(Constants.OUTSIDE);
        }
    }
}