package com.fr.design.data.datapane.preview;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.fr.base.BaseUtils;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.general.Inter;




public class PreviewLabel extends UIButton {
	Previewable previewable;
	
	public PreviewLabel(Previewable previewable) {
		super(BaseUtils.readIcon("/com/fr/design/images/m_file/preview.png"));
		this.setToolTipText(Inter.getLocText("Preview"));
		this.setCursor(new Cursor(Cursor.HAND_CURSOR));
		this.previewable = previewable;
		this.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				PreviewLabel.this.previewable.preview();
			}
		});
		this.setPreferredSize(new Dimension(24, 20));
	}

	public static interface Previewable {
		public void preview();
	}
	
}