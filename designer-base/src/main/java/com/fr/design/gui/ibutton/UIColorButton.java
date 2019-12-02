package com.fr.design.gui.ibutton;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import com.fr.base.Utils;
import com.fr.design.constants.UIConstants;
import com.fr.design.event.GlobalNameListener;
import com.fr.design.event.GlobalNameObserver;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.ipoppane.PopupHider;
import com.fr.general.ComparatorUtils;
import com.fr.design.style.color.ColorControlWindow;
import com.fr.design.utils.gui.GUICoreUtils;

public class UIColorButton extends UIButton implements PopupHider, UIObserver, GlobalNameObserver {
	private static final int SIZE = 16;
	private static final int SIZE_2 = 2;
	private static final int SIZE_4 = 4;
	private static final int SIZE_6 = 6;
	private static final int POPUP_MENU_SHIFT = -70;
	private Color color = Color.BLACK;
	private ColorControlWindow popupWin;
	private EventListenerList colorChangeListenerList = new EventListenerList();
	private boolean isEventBanned = false;
	private String colorButtonName = "";
	private UIObserverListener uiColorObserverListener;
	private GlobalNameListener globalNameListener = null;

	public UIColorButton() {
		this(UIConstants.FONT_ICON);
	}

	public UIColorButton(Icon icon) {
		super(icon);
		setUI(getButtonUI());
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				showPopupMenu();
			}
		});
		iniListener();
	}

	private void iniListener() {
		if (shouldResponseChangeListener()) {
			this.addColorChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					if (uiColorObserverListener == null) {
						return;
					}
					if (globalNameListener != null && shouldResponseNameListener()) {
						globalNameListener.setGlobalName(colorButtonName);
					}
					uiColorObserverListener.doChange();
				}
			});
		}
	}

	private UIButtonUI getButtonUI() {
		return new UIButtonUI() {
			@Override
			protected void paintIcon(Graphics g, JComponent c) {
				super.paintIcon(g, c);
				AbstractButton b = (AbstractButton) c;
				ButtonModel model = b.getModel();
				if (model.isEnabled()) {
					g.setColor(UIColorButton.this.getColor());
				} else {
					g.setColor(new Color(Utils.filterRGB(UIColorButton.this.getColor().getRGB(), 50)));
				}
				g.fillRect((b.getWidth() - SIZE) / SIZE_2, b.getHeight() - SIZE_6, SIZE, SIZE_4);
			}
		};
	}

	public void setEventBanned(boolean isEventBanned) {
		this.isEventBanned = isEventBanned;
	}

	public void setGlobalName(String name) {
		colorButtonName = name;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		if (ComparatorUtils.equals(this.color, color)) {
			return;
		}

		this.color = color;
		hidePopupMenu();
		fireColorStateChanged();
	}

	private void showPopupMenu() {
		if (isEventBanned) {
			return;
		}

		if (popupWin != null && popupWin.isVisible()) {
			hidePopupMenu();
			return;
		}

		if (!this.isEnabled()) {
			return;
		}

		popupWin = this.getColorControlWindow();

		GUICoreUtils.showPopupMenu(popupWin, this, POPUP_MENU_SHIFT, this.getSize().height);
	}

	/**
	 * 隐藏popupmenu
	 */
	public void hidePopupMenu() {
		if (popupWin != null) {
			popupWin.setVisible(false);
			repaint();
		}

		popupWin = null;
	}

	private ColorControlWindow getColorControlWindow() {
		//find parant.
		if (this.popupWin == null) {
			this.popupWin = new ColorControlWindow(UIColorButton.this) {
				@Override
				protected void colorChanged() {
					UIColorButton.this.setColor(this.getColor());
				}

			};
		}

		return popupWin;
	}

	/**
	 * 添加监听
	 * 
	 * @param changeListener 监听列表
	 */
	public void addColorChangeListener(ChangeListener changeListener) {
		colorChangeListenerList.add(ChangeListener.class, changeListener);
	}

	/**
	 * 移除监听
	 *  Removes an old ColorChangeListener.
	 * @param changeListener 监听列表
	 */
	public void removeColorChangeListener(ChangeListener changeListener) {
		colorChangeListenerList.remove(ChangeListener.class, changeListener);
	}

	/**
	 * 颜色状态改变
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


	/**
	 * 注册状态改变监听
	 *
	 * @param listener 观察者监听事件
	 */
	public void registerChangeListener(UIObserverListener listener) {
		uiColorObserverListener = listener;
	}

	/**
	 * 是否需要响应监听
	 *
	 * @return 是否响应
	 */
	public boolean shouldResponseChangeListener() {
		return true;
	}

	/**
	 * 注册监听
	 *
	 * @param listener 观察者监听事件
	 */
	public void registerNameListener(GlobalNameListener listener) {
		globalNameListener = listener;
	}

	/**
	 * 是否需要相应
	 *
	 * @return 是否响应
	 */
	public boolean shouldResponseNameListener() {
		return true;
	}

	/**
	 * 主函数
	 * 
	 * @param args 参数
	 */
	public static void main(String... args) {
		LayoutManager layoutManager = null;
		JFrame jf = new JFrame("test");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel content = (JPanel) jf.getContentPane();
		content.setLayout(layoutManager);

		UIColorButton bb = new UIColorButton(UIConstants.FONT_ICON);
		bb.setBounds(20, 20, bb.getPreferredSize().width, bb.getPreferredSize().height);
		content.add(bb);
		GUICoreUtils.centerWindow(jf);
		jf.setSize(400, 400);
		jf.setVisible(true);
	}
}