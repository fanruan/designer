package com.fr.design.mainframe.chart.gui.style;


import com.fr.chart.chartglyph.GeneralInfo;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.LineComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.dialog.BasicPane;
import com.fr.general.Inter;
import com.fr.stable.CoreConstants;
import com.fr.design.style.color.ColorSelectBox;

import javax.swing.*;
import java.awt.*;

public class ChartBorderPane extends BasicPane{
	private static final long serialVersionUID = -7770029552989609464L;
	private LineComboBox currentLineCombo;
	private ColorSelectBox currentLineColorPane;
	private UICheckBox isRoundBorder;
	
	public ChartBorderPane() {
		initComponents();
	}

	private void initComponents() {
		currentLineCombo = new LineComboBox(CoreConstants.STRIKE_LINE_STYLE_ARRAY_4_CHART);
		currentLineColorPane = new ColorSelectBox(100);
		isRoundBorder = new UICheckBox(Inter.getLocText("Border-Style-Radius"));
		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] columnSize = { p,f };
		double[] rowSize = {p, p, p, p};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("Line-Style")+":"),currentLineCombo},
                new Component[]{new UILabel(Inter.getLocText("Color")+":"),currentLineColorPane},
                new Component[]{null,isRoundBorder}
        } ;
        JPanel panel = TableLayoutHelper.createTableLayoutPane4Chart(new String[]{"Border"},components,rowSize,columnSize);
        this.setLayout(new BorderLayout());
        this.add(panel,BorderLayout.CENTER) ;
        this.add(new JSeparator(), BorderLayout.SOUTH);
	}

    /**
     * 标题
     * @return 标题
     */
	public String title4PopupWindow() {
		return null;
	}
	
	public void populate(GeneralInfo attr) {
		if(attr == null) {
			return;
		}
		currentLineCombo.setSelectedLineStyle(attr.getBorderStyle());
		currentLineColorPane.setSelectObject(attr.getBorderColor());
		isRoundBorder.setSelected(attr.isRoundBorder());
	}
	
	public void update(GeneralInfo attr) {
		if(attr == null) {
			attr = new GeneralInfo();
		}
		attr.setBorderStyle(currentLineCombo.getSelectedLineStyle());
		attr.setBorderColor(currentLineColorPane.getSelectObject());
		attr.setRoundBorder(isRoundBorder.isSelected());
		
	}
}