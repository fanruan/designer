package com.fr.design.style.background.gradient;

import java.awt.BorderLayout;

import javax.swing.JFormattedTextField;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import com.fr.base.Utils;
import com.fr.base.background.GradientBackground;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.dialog.BasicPane;
import com.fr.general.Inter;

/**
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2011-11-29 上午10:43:25
 * 类说明: 当前选择区域, 自定义区域的切换下拉狂Box.
 */
public class GradientFromToPixPane extends BasicPane {
	private static final long serialVersionUID = -5235291383557225506L;
	
	private UIBasicSpinner startPixSpinner, endPixSpinner;
	
	public GradientFromToPixPane() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		
		JPanel secondFloorPane = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
		this.add(secondFloorPane, BorderLayout.CENTER);
		
		startPixSpinner = new UIBasicSpinner(new SpinnerNumberModel(1, 1, 9999, 1));
		secondFloorPane.add(startPixSpinner);
		JFormattedTextField startField = ((JSpinner.DefaultEditor) startPixSpinner.getEditor()).getTextField();
		startField.setHorizontalAlignment(UITextField.LEFT);
		startField.setColumns(3);
		
		secondFloorPane.add(new UILabel("  " + Inter.getLocText("To") + "  "));
		
		endPixSpinner = new UIBasicSpinner(new SpinnerNumberModel(1, 1, 9999, 1));
		secondFloorPane.add(endPixSpinner);
		secondFloorPane.add(new UILabel("(" + Inter.getLocText("Indent-Pixel") + ")"));
		
		JFormattedTextField endField = ((JSpinner.DefaultEditor)endPixSpinner.getEditor()).getTextField();
		endField.setHorizontalAlignment(UITextField.LEFT);
		endField.setColumns(3);
	}
	
	public void populate(GradientBackground bg) {
		startPixSpinner.setValue(bg.getBeginPlace());
		endPixSpinner.setValue(bg.getFinishPlace());
	}
	
	public void update(GradientBackground bg) {
		bg.setBeginPlace(Utils.objectToNumber(startPixSpinner.getValue(), false).floatValue());
		bg.setFinishPlace(Utils.objectToNumber(endPixSpinner.getValue(), false).floatValue());
	}

	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText(new String[]{"Choose", "Gradient-Color"});
	}

}