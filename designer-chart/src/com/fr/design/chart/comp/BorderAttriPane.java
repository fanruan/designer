package com.fr.design.chart.comp;

import com.fr.design.gui.ilable.UILabel;
import com.fr.general.Inter;
import com.fr.stable.Constants;
import com.fr.stable.CoreConstants;
import com.fr.design.gui.icombobox.LineComboBox;
import com.fr.design.dialog.BasicPane;
import com.fr.design.style.color.ColorSelectBox;

import java.awt.*;

public class BorderAttriPane extends BasicPane  {
	
    private LineComboBox lineCombo;
    private ColorSelectBox colorSelectBox;

    public BorderAttriPane() {
    	this(Inter.getLocText("Line-Style"), Inter.getLocText("Color"));
    }
    
    public BorderAttriPane(String lineString, String colorSting) {
    	this.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 0));
        
        this.add(new UILabel(lineString + ":"));
        this.add(lineCombo = new LineComboBox(CoreConstants.STRIKE_LINE_STYLE_ARRAY_4_CHART));
        lineCombo.setPreferredSize(new Dimension(60, 18));
        
        this.add(new UILabel(colorSting + ":"));
        this.add(colorSelectBox = new ColorSelectBox(80));
        colorSelectBox.setPreferredSize(new Dimension(60, 18));
        colorSelectBox.setSelectObject(null);
    }
    
    public void setLineColor(Color color) {
    	this.colorSelectBox.setSelectObject(color);
    }
    
    public Color getLineColor() {
    	return this.colorSelectBox.getSelectObject();
    }
    
    public void setLineStyle(int style) {
    	if (style != Constants.LINE_NONE && style != Constants.LINE_THICK 
    			&& style != Constants.LINE_THIN && style != Constants.LINE_MEDIUM) {
    		style = Constants.LINE_THIN;
    	}
        this.lineCombo.setSelectedLineStyle(style);
    }

    public int getLineStyle() {
        return lineCombo.getSelectedLineStyle();
    }
    
    @Override
    protected String title4PopupWindow() {
    	return "Border";
    }
}