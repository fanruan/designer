package com.fr.design.mainframe.chart.gui.style;

import com.fr.chart.base.ChartConstants;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

public class ChartBeautyPane extends BasicBeanPane<Integer>{
    private static final int FOUR = 4;
    private static final int FIVE = 5;
	private UIComboBox styleBox;

	public ChartBeautyPane() {
		String[] nameArray = {Inter.getLocText("Common"),
                Inter.getLocText("Plane3D"),Inter.getLocText(new String[]{"Gradient", "HighLight"}),
                Inter.getLocText("TopDownShade"),Inter.getLocText("Transparent")   //新加的两种风格，注意兼容处理
           };

		styleBox = new UIComboBox(nameArray);
		
		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] columnSize = { f };
		double[] rowSize = { p};
        Component[][] components = new Component[][]{
                new Component[]{styleBox},
        } ;
        JPanel panel = TableLayoutHelper.createTableLayoutPane4Chart(new String[]{"InterfaceStyle"},components,rowSize,columnSize);
        this.setLayout(new BorderLayout());
        this.add(panel,BorderLayout.CENTER);
	}
	@Override
	public void populateBean(Integer ob) {
        //兼容处理
        int finalIndex = 0;
        switch (ob){ //ob是style的编号
            case 0: finalIndex = 0; break;
            case 1: finalIndex = 1; break;
            case 2: finalIndex = 2; break;
            case 3: finalIndex = 0; break;
            case FOUR: finalIndex = 3; break;
            case FIVE: finalIndex = FOUR; break;
            default: finalIndex = 0;
        }
        if(finalIndex != styleBox.getSelectedIndex()){
            styleBox.setSelectedIndex(finalIndex);
        }
	}

	@Override
	public Integer updateBean() {
		int index =  styleBox.getSelectedIndex();
        int style = ChartConstants.STYLE_NONE;
        switch (index){
            case 0: style = ChartConstants.STYLE_NONE; break;
            case 1: style = ChartConstants.STYLE_3D; break;
            case 2: style = ChartConstants.STYLE_OUTER; break;
            case 3: style = ChartConstants.STYLE_SHADE; break;
            case FOUR: style = ChartConstants.STYLE_TRANSPARENT; break;
            default: style = ChartConstants.STYLE_NONE;
        }
        return style;
	}

	@Override
	protected String title4PopupWindow() {
		return "";
	}
}