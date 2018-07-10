package com.fr.design.data.datapane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;

import com.fr.design.constants.UIConstants;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.border.Border;

public class FlashLookLabelMouseAdapter extends MouseAdapter {
	boolean mouseEntered = false;
	boolean buttonPressed = false;
	
	private UILabel label;
	private ReleaseAction releaseAction;
	private static final Border NORMAL_BORDER = BorderFactory.createLineBorder(UIConstants.NORMAL_BACKGROUND);
	private static final Border ENTERED_BORDER = BorderFactory.createLineBorder(java.awt.Color.GRAY);
	
	public FlashLookLabelMouseAdapter(UILabel label, ReleaseAction releaseAction) {
		this.label = label;
		label.setBorder(NORMAL_BORDER);
		this.releaseAction = releaseAction;
	}
	
	public void mouseEntered(MouseEvent e) { //当鼠标进入时候调用. 
		mouseEntered = true;
		if(!buttonPressed){
			label.setBackground(java.awt.Color.WHITE);
			label.setOpaque(true);
			label.setBorder(ENTERED_BORDER);
		}
	}
	public void mouseExited(MouseEvent e) {
		mouseEntered = false;
		label.setOpaque(false);
		label.setBorder(NORMAL_BORDER);
	}
	
	public void mousePressed(MouseEvent e){
		buttonPressed = true;
		label.setBackground(java.awt.Color.lightGray);
	}
	public void mouseReleased(MouseEvent e){
		buttonPressed = false;
		if(mouseEntered){
			label.setBackground(java.awt.Color.WHITE);
			
			this.releaseAction.releaseAction();
		}
	}
	
	public static interface ReleaseAction {
		public void releaseAction();
	}
}