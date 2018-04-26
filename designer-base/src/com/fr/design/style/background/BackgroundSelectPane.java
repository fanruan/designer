package com.fr.design.style.background;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.dialog.BasicPane;
import com.fr.general.Background;
import com.fr.general.Inter;

/**
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2011-11-1 下午02:56:15
 * 类说明:  背景弹出设置界面
 */
public abstract class BackgroundSelectPane extends BasicPane {
	private static final long serialVersionUID = 1984800362352721869L;
	
	private Background background;
	private ArrayList<ChangeListener> listenerList = new ArrayList<ChangeListener>();
	
	public abstract BackgroundDetailPane getShowPane(double preWidth);
	
	protected void initBackgroundShowPane(final BackgroundDetailPane backgroundPane) {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		
		backgroundPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				try {
					BackgroundSelectPane.this.setSelectBackground(backgroundPane.update());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		this.add(backgroundPane, BorderLayout.CENTER);
		
		UIButton button = new UIButton(Inter.getLocText("Choose_None"));
		this.add(button, BorderLayout.SOUTH);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					BackgroundSelectPane.this.setSelectBackground(null);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
	}
	
	public void setSelectBackground(Background background) {
		this.background = background;
		
		if (listenerList.size() > 0) {
			ChangeEvent evt = new ChangeEvent(this);
			
			for (int i = 0; i < listenerList.size(); i++) {
				this.listenerList.get(i).stateChanged(evt);
			}
		}

		this.repaint();
	}
	
	public Background getSelectBackground() {
		return this.background;
	}
	
	public void addTextureChangeListener(ChangeListener changeListener) {
		this.listenerList.add(changeListener);
	}
}