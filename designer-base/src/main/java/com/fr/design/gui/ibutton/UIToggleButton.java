package com.fr.design.gui.ibutton;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.design.constants.UIConstants;
import com.fr.design.event.GlobalNameListener;
import com.fr.design.event.GlobalNameObserver;
import com.fr.stable.StringUtils;

/**
 * SelectedAble button label
 *
 * @author zhou
 * @since 2012-5-11下午4:28:24
 */
public class UIToggleButton extends UIButton implements GlobalNameObserver{

	private static final int ICON_COUNT = 2;
	private boolean isSelected;
	private boolean isEventBannded = false;
	private String toggleButtonName = "";
	private GlobalNameListener globalNameListener = null;
	private Icon[] icons;

	public UIToggleButton() {
		this(StringUtils.EMPTY);
	}

	public UIToggleButton(Icon image) {
		this(StringUtils.EMPTY, image);
	}

	public UIToggleButton(String text) {
		this(text, null);
	}

	public UIToggleButton(String text, Icon image) {
		super(text, image);
		addMouseListener(getMouseListener());
	}

	/**
	 * 需要反白的按钮接口(组合按钮情况-UIButtonGroup)
	 * support icons[normalIcon, selectedIcon]
	 * @param icons
	 */
	public UIToggleButton(Icon[] icons) {
		super(icons[0], null, null);
		setExtraPainted(true);
		this.icons = icons;
		addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!UIToggleButton.super.isSelected()) {
					UIToggleButton.super.setSelected(!UIToggleButton.super.isSelected());
				}
			}
		});
		addMouseListener(getMouseListener());
	}

	/**
	 * 需要反白的按钮接口(单个按钮情况)-再次点击取消选中状态
	 * support icons[normalIcon, selectedIcon]
	 * @param icons
	 */
	public UIToggleButton(Icon[] icons, boolean needRelease) {
		super(icons[0], null, null);
		setBorderPainted(true);
		setExtraPainted(true);
		this.icons = icons;
		if (!needRelease) {
			addActionListener(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (UIToggleButton.super.isSelected()) {
						UIToggleButton.super.setSelected(false);
					} else {
						UIToggleButton.super.setSelected(true);
					}
				}
			});
		}
		addMouseListener(getMouseListener());
	}

	@Override
	public void setGlobalName(String name){
		toggleButtonName = name ;
	}

	/**
	 *
	 * @return
	 */
	@Override
	public boolean isSelected() {
		return isSelected;
	}

	/**
	 * 能触发事件
	 *
	 * @param isSelected
	 */

	@Override
	public void setSelected(boolean isSelected) {
		super.setSelected(isSelected);
		if (this.isSelected != isSelected) {
			this.isSelected = isSelected;
			refresh(isSelected);
		}
	}

	@Override
    protected void initListener(){
        if(shouldResponseChangeListener()){
            this.addChangeListener(new ChangeListener() {
            	@Override
                public void stateChanged(ChangeEvent e) {
                    if (uiObserverListener == null) {
                        return;
                    }
					if(globalNameListener!=null && shouldResponseNameListener()){
						globalNameListener.setGlobalName(toggleButtonName);
					}
                    uiObserverListener.doChange();
                }
            });
        }
    }

	public void setSelectedWithFireListener(boolean isSelected) {
		if (this.isSelected != isSelected) {
			this.isSelected = isSelected;
			fireSelectedChanged();
			refresh(isSelected);
		}
	}



	private void refresh(final boolean isSelected) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Icon[] icons = UIToggleButton.this.icons;
				if (icons != null && icons.length == ICON_COUNT) {
					if (isSelected) {
						UIToggleButton.this.setIcon(icons[1]);
					} else {
						UIToggleButton.this.setIcon(icons[0]);
					}
				}
				UIToggleButton.this.repaint();
			}
		});
	}


	protected MouseListener getMouseListener() {
		return new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (isEnabled() && !isEventBannded) {
					setSelectedWithFireListener(!isSelected());
				}
			}
		};
	}

	public void setEventBannded(boolean ban) {
		this.isEventBannded = ban;
	}

	@Override
	protected void fireStateChanged() {

	}


	protected void fireSelectedChanged() {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeListener.class) {
				((ChangeListener) listeners[i + 1]).stateChanged(new ChangeEvent(this));
			}
		}
	}

	@Override
	protected void paintBorder(Graphics g) {
		if (!isBorderPainted()) {
			return;
		}
		boolean isBorderPainted = isBorderPaintedOnlyWhenPressed && (getModel().isPressed() || isSelected);
		if (isBorderPainted || !isBorderPaintedOnlyWhenPressed) {
			if (ui instanceof UIButtonUI) {
				((UIButtonUI) ui).paintBorder(g, this);
			} else {
				super.paintBorder(g);
			}
		}
	}

	@Override
	protected void paintOtherBorder(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(UIConstants.BS);
		Shape shape = new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1F, getHeight() - 1F, UIConstants.ARC, UIConstants.ARC);
		g2d.setColor(UIConstants.LINE_COLOR);
		g2d.draw(shape);
	}

	/**
	 * 组件是否需要响应添加的观察者事件
	 *
	 * @return 如果需要响应观察者事件则返回true，否则返回false
	 */
	@Override
	public boolean shouldResponseChangeListener() {
		return true;
	}

	/**
	 *
	 * @param listener 观察者监听事件
	 */
	@Override
	public void registerNameListener(GlobalNameListener listener) {
       globalNameListener = listener;
	}

	/**
	 *
	 * @return
	 */
	public boolean shouldResponseNameListener() {
		return true;
	}

}
