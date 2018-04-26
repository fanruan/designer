package com.fr.design.style.background.gradient;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import com.fr.base.background.GradientBackground;
import com.fr.design.constants.UIConstants;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.imenu.UIMenuItem;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.dialog.BasicPane;
import com.fr.general.Inter;
import com.fr.stable.Constants;

/**
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2011-11-29 上午10:42:43
 * 类说明: 渐变色的切换box. 当前区域和自定义区域.
 */
public class GradientChangeBoxPane extends BasicPane {
	private static final long serialVersionUID = -6747468244414651602L;
	private static String current = Inter.getLocText(new String[]{"Current", "Choose", "Filed"});
	private static String custom = Inter.getLocText(new String[]{"Custom", "Filed"});

	private JPanel cardPane;
	private CardLayout cardLayout;
	private UITextField currentField;
	private GradientFromToPixPane fromToPixPane;
	private UIButton arrowButton;
	
	private JPopupMenu popupMenu;
	private UIMenuItem currentItem;
	private UIMenuItem customItem;
	
	public GradientChangeBoxPane() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 14));
		
		cardLayout = new CardLayout();
		cardPane = FRGUIPaneFactory.createCardLayout_S_Pane();
		cardPane.setLayout(cardLayout);
		
		this.add(cardPane, BorderLayout.CENTER);
		
		arrowButton = new UIButton(UIConstants.ARROW_DOWN_ICON);
		arrowButton.setRoundBorder(true, Constants.LEFT);
		this.add(arrowButton, BorderLayout.EAST);
		
		currentField = new UITextField(current);
		currentField.setEditable(false);
		
		cardPane.add(currentField, "current");
		
		fromToPixPane = new GradientFromToPixPane();
		
		cardPane.add(fromToPixPane, "custom");
		
		popupMenu = new JPopupMenu();
		UIMenuItem currentItem = new UIMenuItem(current);
		currentItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(cardPane, "current");
			}
		});
		
		customItem = new UIMenuItem(custom);
		customItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(cardPane, "custom");
			}
		});
		
		popupMenu.add(currentItem);
		popupMenu.addSeparator();
		popupMenu.add(customItem);
		
		currentField.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent e) {
		    	showPopupMenu();
		    }
		});
		
		arrowButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showPopupMenu();
			}
		});
	}
	
	private void showPopupMenu() {
		Rectangle rec = cardPane.getBounds();
		popupMenu.setPopupSize(rec.width + arrowButton.getSize().width, popupMenu.getPreferredSize().height);
		popupMenu.show(cardPane, 0, rec.height);
	}
	
	public void populate(GradientBackground bg) {
		if(bg.isUseCell()) {
			cardLayout.show(cardPane, "current");
		} else {
			cardLayout.show(cardPane, "custom");
			fromToPixPane.populate(bg);
		}
	}
	
	public void update(GradientBackground bg) {
		if(fromToPixPane.isVisible()) {
			bg.setUseCell(false);
			fromToPixPane.update(bg);
		} else if(currentField.isVisible()) {
			bg.setUseCell(true);
		}
	}
	
	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText(new String[]{"Choose", "Gradient-Color"});
	}

}