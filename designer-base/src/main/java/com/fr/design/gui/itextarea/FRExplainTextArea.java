package com.fr.design.gui.itextarea;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Insets;

import javax.swing.*;

import com.fr.design.gui.ilable.UILabel;

import com.fr.design.gui.itextarea.UITextArea;
import com.fr.design.layout.FRGUIPaneFactory;

public class FRExplainTextArea extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private Icon icon;
	private String string; 
	private int column;
	
	public FRExplainTextArea(String string,int column){
		this.setString(string);
		this.column = column;
		init();
	}
	
	private void init(){
		this.setLayout(new /**/FlowLayout(FlowLayout.LEFT, 0, 0));
		JPanel pane =FRGUIPaneFactory.createX_AXISBoxInnerContainer_S_Pane();
//		pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));
		
		JPanel pane_icon =FRGUIPaneFactory.createBorderLayout_S_Pane();
		pane.add(pane_icon);
//		pane_icon.setLayout(FRGUIPaneFactory.createBorderLayout());
		this.setIcon(new ImageIcon("/com/fr/design/images/warnings/warning3.png"));
		UILabel UILabel = new UILabel(this.icon, SwingConstants.LEFT);
		pane_icon.add(UILabel, BorderLayout.NORTH);
		UITextArea jta = new UITextArea(this.string);
		jta.setMargin(new Insets(0, 3, 0, 0));
		jta.setForeground(new Color(255, 0, 0));
		jta.setColumns(column);
		jta.setLineWrap(true);
		jta.setEditable(false);
		pane.add(jta);
		this.add(pane);
		
		
	}

	public void setIcon(Icon icon) {
		this.icon = icon;
	}

	public Icon getIcon() {
		return icon;
	}

	public void setString(String string) {
		this.string = string;
	}

	public String getString() {
		return string;
	}
}