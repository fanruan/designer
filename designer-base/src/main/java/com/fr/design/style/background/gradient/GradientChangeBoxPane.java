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
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.dialog.BasicPane;

import com.fr.stable.Constants;

/**
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2011-11-29 上午10:42:43
 * 类说明: 渐变色的切换box. 当前区域和自定义区域.
 */
public class GradientChangeBoxPane extends BasicPane {
	private static final long serialVersionUID = -6747468244414651602L;
	private static final String CURRENT = Toolkit.i18nText("Fine-Design_Style_Background_Gradient_Current_Area");
	private static final String CUSTOM = Toolkit.i18nText("Fine-Design_Style_Background_Gradient_Custom_Area");

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
		
		currentField = new UITextField(CURRENT);
		currentField.setEditable(false);
		
		cardPane.add(currentField, "CURRENT");
		
		fromToPixPane = new GradientFromToPixPane();
		
		cardPane.add(fromToPixPane, "CUSTOM");
		
		popupMenu = new JPopupMenu();
		UIMenuItem currentItem = new UIMenuItem(CURRENT);
		currentItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(cardPane, "CURRENT");
			}
		});
		
		customItem = new UIMenuItem(CUSTOM);
		customItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(cardPane, "CUSTOM");
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
			cardLayout.show(cardPane, "CURRENT");
		} else {
			cardLayout.show(cardPane, "CUSTOM");
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
		return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Style_Background_Choose_Gradient_Color");
	}

}