package com.fr.design.widget.ui;

import com.fr.base.FRContext;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.Radio;
import com.fr.general.FRFont;


import javax.swing.*;
import java.awt.*;

/**
 * @deprecated
 */
@Deprecated
public class RadioDefinePane extends AbstractDataModify<Radio> {
	public RadioDefinePane() {
		this.iniComoponents();
	}
	
	private void iniComoponents() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		
		UILabel infoLabel = new UILabel();
        FRFont frFont = FRContext.getDefaultValues().getFRFont();
        infoLabel.setFont(new Font(frFont.getFamily(), Font.BOLD, 24));
	    infoLabel.setText(com.fr.design.i18n.Toolkit.i18nText(
				"Fine-Design_Report_No_Editor_Property_Definition") + ".");
	    infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    
		this.add(infoLabel, BorderLayout.CENTER);
	}
	
	@Override
	protected String title4PopupWindow() {
		return "radio";
	}
	
	@Override
	public void populateBean(Radio cellWidget) {
	}
	
	@Override
	public Radio updateBean() {
		return new Radio();
	}
}
