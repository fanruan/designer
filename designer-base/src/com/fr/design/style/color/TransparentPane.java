package com.fr.design.style.color;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.dialog.BasicPane;
import com.fr.general.Inter;

public abstract class TransparentPane extends BasicPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TransparentPane(boolean isSupportTransparent) {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));

		// richer:能否拖动
		if (isSupportTransparent) {
			UIButton transparentButton = new UIButton(Inter.getLocText("ChartF-Transparency"));
			transparentButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			transparentButton.addActionListener(new TransparentActionListener());

			this.add(transparentButton, BorderLayout.NORTH);
		}

		// center
		JPanel centerPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
		this.add(centerPane, BorderLayout.CENTER);

		initCenterPaneChildren(centerPane);

		UIButton customButton = new CustomButton(Inter.getLocText("Custom")
				+ "...");
		this.add(customButton, BorderLayout.SOUTH);
		customButton.setCursor(new Cursor(Cursor.HAND_CURSOR));        
	}

	public abstract void initCenterPaneChildren(JPanel centerPane);

	public abstract void doTransparent() ;

	public abstract void customButtonPressed();

	class TransparentActionListener implements ActionListener {

		public void actionPerformed(ActionEvent evt) {
			doTransparent();
		}
	}

	// richer：自定义按钮，让用户可以按自己的需求自定义边框样式
	private class CustomButton extends UIButton {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public CustomButton(String text) {
			super(text);
			enableEvents(AWTEvent.MOUSE_EVENT_MASK);
		}

		public void processMouseEvent(MouseEvent evt) {
			if(evt.getID() == MouseEvent.MOUSE_PRESSED) {
				customButtonPressed();
				return;
			}
			super.processMouseEvent(evt);
		}
	}

}