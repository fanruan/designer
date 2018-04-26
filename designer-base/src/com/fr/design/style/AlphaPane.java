package com.fr.design.style;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SpinnerListModel;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.fr.base.Utils;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.islider.UISlider;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import com.fr.design.utils.gui.GUICoreUtils;

public class AlphaPane extends JPanel {
	private static final long serialVersionUID = -1799504254802606373L;
    private static final int ALPHA_FLOAT = 100;
    private final static int START_VALUE = 30;
	
	private static String[] values = new String[101];
	static {
		for (int i = 0; i < 101; i++) {
			values[i] = i + "%";
		}
	}

	private JSlider alphaSlider;
	private UIBasicSpinner valueSpinner;
	
	public AlphaPane() {
		this.inits();
	}

	private void inits() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		
		// 透明选项
		JPanel alphaPane = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
		alphaPane.setBorder(BorderFactory.createEmptyBorder());
		this.add(alphaPane, BorderLayout.CENTER);
		
		alphaPane.add(new UILabel(Inter.getLocText("Plugin-Chart_Alpha") + ":"));
		
		alphaPane.add(alphaSlider = new UISlider(UISlider.HORIZONTAL, 0, ALPHA_FLOAT, START_VALUE));
		alphaSlider.setPreferredSize(new Dimension(80, 20));
		
		valueSpinner = new UIBasicSpinner(new SpinnerListModel(values));
		alphaPane.add(valueSpinner);
		
		JFormattedTextField field = GUICoreUtils.getSpinnerTextField(valueSpinner);
		field.setText(checkFormat(START_VALUE));
		if (field != null) {
			field.setColumns(4);
			field.setHorizontalAlignment(UITextField.LEFT);
			field.getDocument().addDocumentListener(textListener);
		}

		valueSpinner.setPreferredSize(new Dimension(60, 18));
		alphaSlider.addChangeListener(changeListener);
	}
	
	DocumentListener textListener = new DocumentListener() {
		public void removeUpdate(DocumentEvent e) {
			alphaSliderChanege();
		}
		
		public void insertUpdate(DocumentEvent e) {
			alphaSliderChanege();
		}
		
		public void changedUpdate(DocumentEvent e) {
			alphaSliderChanege();
		}
	};
	
	ChangeListener changeListener = new ChangeListener() {
		public void stateChanged(ChangeEvent e) {
			int alpha = alphaSlider.getValue();
			if(alpha >= 0 && alpha <= ALPHA_FLOAT) {
                JFormattedTextField field = GUICoreUtils.getSpinnerTextField(valueSpinner);
                field.setText(checkFormat(alpha));
            }
		}
	};
	
	private String checkFormat(int alpha) {
		alpha = Math.min(Math.max(0, alpha), ALPHA_FLOAT);
		return alpha + "%";
	}
	
	private void alphaSliderChanege() {
		String text = ((DefaultEditor)valueSpinner.getEditor()).getTextField().getText();
		if(text.endsWith("%")) {
			text = text.replace("%", "");
		} else {
			text = text + "%";
		}
		Number number = Utils.string2Number(text);
		if(number != null) {
			alphaSlider.removeChangeListener(changeListener);
			alphaSlider.setValue(number.intValue());
			alphaSlider.addChangeListener(changeListener);
		}
	}
	
	public void populate(int alpha) {
		this.alphaSlider.setValue(alpha);
	}
	
	public float update() {
		return (float)(this.alphaSlider.getValue())/(float)(ALPHA_FLOAT);
	}
}