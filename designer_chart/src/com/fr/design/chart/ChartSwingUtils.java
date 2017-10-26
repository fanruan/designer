package com.fr.design.chart;

import com.fr.base.BaseFormula;
import com.fr.base.Utils;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.formula.FormulaFactory;
import com.fr.design.formula.UIFormula;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 这里打算是放一些图表用的界面控件的. 
 */
public class ChartSwingUtils {
	
	                           
	public static void addListener(final UICheckBox box, final UITextField textField) {
		textField.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (box.isSelected()) {
					showFormulaPane(textField, null);
				}
			}
		});
		textField.addKeyListener(new KeyAdapter() {
			  public void keyTyped(KeyEvent e) {
				  if(box.isSelected()) {
					  e.consume();
					  showFormulaPane(textField, null);
				  }
			  }
		});
	}
	
	public static UITextField createFormulaUITextField(final OKListener l) {
		final UITextField textField = new UITextField();
		textField.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				showFormulaPane(textField, l);
			}
		});
		textField.addKeyListener(new KeyAdapter() {
			  public void keyTyped(KeyEvent e) {
				  e.consume();
				  showFormulaPane(textField, l);
			  }
		});
		
		return textField;
	}
	
	private static void showFormulaPane(final UITextField jTextField, final OKListener l) {
		final UIFormula formulaPane = FormulaFactory.createFormulaPane();
		formulaPane.populate(BaseFormula.createFormulaBuilder().build(jTextField.getText()));
		formulaPane.showLargeWindow(SwingUtilities.getWindowAncestor(FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane()), new DialogActionAdapter(){
			public void doOk() {
				BaseFormula formula = formulaPane.update();
				jTextField.setText(Utils.objectToString(formula));
				if (l != null) {
					l.action();
				}
			}
		}).setVisible(true);
	}
	
	public interface OKListener {
		public void action();
	}
}