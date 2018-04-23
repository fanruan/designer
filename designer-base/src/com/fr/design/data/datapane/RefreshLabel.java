package com.fr.design.data.datapane;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.fr.base.BaseUtils;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.general.Inter;



public class RefreshLabel extends UIButton {
	private Refreshable refreshable;

	public RefreshLabel(Refreshable refreshable) {
		super(BaseUtils.readIcon("/com/fr/design/images/control/refresh.png"));
		
		this.refreshable = refreshable;
		
		this.setToolTipText(Inter.getLocText("Refresh_Database"));
		this.setCursor(new Cursor(Cursor.HAND_CURSOR));
		this.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				RefreshLabel.this.refreshable.refresh();
			}
		});
		this.setPreferredSize(new Dimension(24, 20));
	}
	
	public static interface Refreshable {
		public void refresh();
	}
}