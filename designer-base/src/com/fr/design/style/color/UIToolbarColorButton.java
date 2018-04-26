/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.style.color;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.Icon;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import com.fr.base.BaseUtils;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIColorButton;
import com.fr.design.gui.ibutton.UICombinationButton;
import com.fr.design.gui.ipoppane.PopupHider;
import com.fr.design.utils.gui.GUICoreUtils;

/**
 * Color select pane2.
 */
public class UIToolbarColorButton extends UICombinationButton implements PopupHider, ColorSelectable, UIObserver {
	private static final long serialVersionUID = 3220957076370197935L;
	private Color color = null;
	private boolean isCanBeNull = false;
	private ColorControlWindow popupWin;
	//color setting action.
	private EventListenerList colorChangeListenerList = new EventListenerList();
	private UIObserverListener uiObserverListener;


	public UIToolbarColorButton(Icon icon) {
		super(new UIColorButton(icon), new UIButton(BaseUtils.readIcon("/com/fr/design/images/gui/popup.gif")));
		getLeftButton().setEventBanned(true);
		getRightButton().addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
			}

			@Override
			public void focusLost(FocusEvent e) {
				hidePopupMenu();
			}
		});
		iniListener();
	}

	private void iniListener() {
		if (shouldResponseChangeListener()) {
			this.addColorChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					if (uiObserverListener == null) {
						return;
					}
					uiObserverListener.doChange();
				}
			});
		}
	}

	@Override
	/**
	 *
	 */
	public UIColorButton getLeftButton() {
		// TODO Auto-generated method stub
		return (UIColorButton) super.getLeftButton();
	}

	/**
	 * @return
	 */
	public Color getColor() {
		return this.color;
	}

	/**
	 * @param color
	 */
	public void setColor(Color color) {
		setColorWithoutchanged(color);
		fireColorStateChanged();
	}

	/**
	 * 设置颜色
	 * @param color 颜色
	 */
	public void setColorWithoutchanged(Color color) {
		this.color = color;
		getLeftButton().setColor(color);
	}

	/**
	 * 是否可为NULL值
	 * @return 同上
	 */
	public boolean isCanBeNull() {
		return this.isCanBeNull;
	}

	/**
	 * @param isCanBeNull
	 */
	public void setCanBeNull(boolean isCanBeNull) {
		this.isCanBeNull = isCanBeNull;
	}

	@Override
	/**
	 *
	 */
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);

		if (!enabled) {
			color = null;
		}

		getLeftButton().setEnabled(enabled);
		getLeftButton().setEnabled(enabled);
	}

	@Override
	/**
	 *
	 */
	public void setToolTipText(String tooltipText) {
		getLeftButton().setToolTipText(tooltipText);
		getLeftButton().setToolTipText(tooltipText);
	}

	private void showPopupMenu() {
		if (popupWin != null && popupWin.isVisible()) {
			hidePopupMenu();
			return;
		}

		if (!this.isEnabled()) {
			return;
		}

		popupWin = this.getColorControlWindow();

		GUICoreUtils.showPopupMenu(popupWin, this, 0, this.getSize().height);
	}

	/**
	 * 隐藏弹出框
	 */
	public void hidePopupMenu() {
		if (popupWin != null) {
			popupWin.setVisible(false);
		}

		popupWin = null;
	}

	private ColorControlWindow getColorControlWindow() {
		//find parant.
		if (this.popupWin == null) {
			this.popupWin = new ColorControlWindow(this.isCanBeNull(), UIToolbarColorButton.this) {
				@Override
				protected void colorChanged() {
					UIToolbarColorButton.this.setColor(this.getColor());
				}

			};
		}

		return popupWin;
	}

	/**
	 * Adds a new ColorChangeListener
	 * 注册监听
	 * @param changeListener 监听
	 */
	public void addColorChangeListener(ChangeListener changeListener) {
		colorChangeListenerList.add(ChangeListener.class, changeListener);
	}

	/**
	 * Removes an old ColorChangeListener.
	 * 移除监听
	 * @param changeListener 监听
	 */
	public void removeColorChangeListener(ChangeListener changeListener) {
		colorChangeListenerList.remove(ChangeListener.class, changeListener);
	}

	/**
	 * 触发颜色改变事件
	 * 
	 */
	public void fireColorStateChanged() {
		Object[] listeners = colorChangeListenerList.getListenerList();
		ChangeEvent e = null;

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeListener.class) {
				if (e == null) {
					e = new ChangeEvent(this);
				}
				((ChangeListener) listeners[i + 1]).stateChanged(e);
			}
		}
	}

	protected void leftButtonClickEvent() {
		color = getLeftButton().getColor();
		fireColorStateChanged();
	}

	@Override
	protected void rightButtonClickEvent() {
		showPopupMenu();
	}

	@Override
	/**
	 * 选中颜色
	 *  @param colorCell 颜色单元格
	 */
	public void colorSetted(ColorCell colorCell) {
		hidePopupMenu();
	}

	@Override
	/**
	 * 注册监听
	 * @param listener 监听
	 */
	public void registerChangeListener(UIObserverListener listener) {
		uiObserverListener = listener;
	}

	@Override
	/**
	 * 是否响应监听
	 * @return 同上
	 */
	public boolean shouldResponseChangeListener() {
		return true;
	}
}