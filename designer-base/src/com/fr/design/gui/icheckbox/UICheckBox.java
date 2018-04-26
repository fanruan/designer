package com.fr.design.gui.icheckbox;

import com.fr.design.constants.UIConstants;
import com.fr.design.event.GlobalNameListener;
import com.fr.design.event.GlobalNameObserver;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.design.utils.gui.GUIPaintUtils;
import com.fr.stable.Constants;
import sun.swing.SwingUtilities2;

import javax.swing.*;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.metal.MetalCheckBoxUI;
import javax.swing.text.View;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class UICheckBox extends JCheckBox implements UIObserver, GlobalNameObserver {
	private UIObserverListener uiObserverListener;
	private GlobalNameListener globalNameListener = null;
	private String checkboxName = "";

	public UICheckBox(String string) {
		super(string);
		setUI(new UICheckBoxUI());
		initListener();
	}

	public UICheckBox() {
		super();
		setUI(new UICheckBoxUI());
		initListener();
	}

	public UICheckBox(String locText, boolean b) {
		super(locText, b);
		setUI(new UICheckBoxUI());
		initListener();
	}

	public UICheckBox(String text, Icon icon) {
		super(text, icon);
		setUI(new UICheckBoxUI());
		initListener();
	}

	private void initListener() {
		if (shouldResponseChangeListener()) {
			this.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					if (uiObserverListener == null) {
						return;
					}
					if (globalNameListener != null && shouldResponseNameListener()) {
						globalNameListener.setGlobalName(checkboxName);
					}
					uiObserverListener.doChange();
				}
			});
		}
	}

	/**
	 * 给组件登记一个观察者监听事件
	 *
	 * @param listener 观察者监听事件
	 */
	public void registerChangeListener(UIObserverListener listener) {
		this.uiObserverListener = listener;
	}


	public void setGlobalName(String name) {
		checkboxName = name;
	}

	/**
	 * 组件是否需要响应添加的观察者事件
	 *
	 * @return 如果需要响应观察者事件则返回true，否则返回false
	 */
	public boolean shouldResponseChangeListener() {
		return true;
	}

	/**
	 * 注册观察者监听事件
	 * @param listener 观察者监听事件
	 */
	public void registerNameListener(GlobalNameListener listener) {
		globalNameListener = listener;
	}

	/**
	 *  组件是否需要响应观察者事件
	 * @return 如果需要响应观察者事件则返回true，否则返回false
	 */
	public boolean shouldResponseNameListener() {
		return true;
	}

	private class UICheckBoxUI extends MetalCheckBoxUI {
		public void paint(Graphics g, JComponent c) {
			synchronized (this) {
				AbstractButton b = (AbstractButton) c;
				ButtonModel model = b.getModel();
				Dimension size = c.getSize();
				Font f = c.getFont();
				g.setFont(f);
				FontMetrics fm = SwingUtilities2.getFontMetrics(c, g, f);

				Rectangle viewRect = new Rectangle(size);
				Rectangle iconRect = new Rectangle();
				Rectangle textRect = new Rectangle();

				Insets i = c.getInsets();
				viewRect.x += i.left;
				viewRect.y += i.top;
				viewRect.width -= (i.right + viewRect.x);
				viewRect.height -= (i.bottom + viewRect.y);

				Icon altIcon = b.getIcon();

				String text = SwingUtilities.layoutCompoundLabel(
						c, fm, b.getText(), altIcon != null ? altIcon : getDefaultIcon(),
						b.getVerticalAlignment(), b.getHorizontalAlignment(),
						b.getVerticalTextPosition(), b.getHorizontalTextPosition(),
						viewRect, iconRect, textRect, b.getIconTextGap());

				// fill background
				if (c.isOpaque()) {
					g.setColor(b.getBackground());
					g.fillRect(0, 0, size.width, size.height);
				}
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				if ( model.isSelected()) {
					GUIPaintUtils.fillPaint(g2d, iconRect.x, iconRect.y, iconRect.width, iconRect.height,false, Constants.NULL,UIConstants.CHECKBOX_HOVER_SELECTED, 0);
				} else if (model.isRollover() && ! model.isSelected()) {
                    g.setColor(UIConstants.CHECKBOX_HOVER_SELECTED);
                    g2d.drawRoundRect(iconRect.x, iconRect.y, iconRect.width - 1, iconRect.height - 1, UIConstants.ARC, UIConstants.ARC);
                }else{
					g.setColor(UIConstants.LINE_COLOR);
					g2d.drawRoundRect(iconRect.x, iconRect.y, iconRect.width - 1, iconRect.height - 1, UIConstants.ARC, UIConstants.ARC);
				}

				if (model.isSelected()) {
					UIConstants.YES_ICON.paintIcon(c, g, iconRect.x + 2, iconRect.y + 2);
				}
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

				// Draw the Text
				drawLine(text, g, b, c, textRect, fm);
			}
		}

		private void drawLine(String text, Graphics g, AbstractButton b, JComponent c, Rectangle textRect, FontMetrics fm) {
			if (text != null) {
				View v = (View) c.getClientProperty(BasicHTML.propertyKey);
				if (v != null) {
					v.paint(g, textRect);
				} else {
					int mnemIndex = b.getDisplayedMnemonicIndex();
					if (model.isEnabled()) {
						g.setColor(b.getForeground());
					} else {
						g.setColor(getDisabledTextColor());
					}
					SwingUtilities2.drawStringUnderlineCharAt(c, g, text,
							mnemIndex, textRect.x, textRect.y + fm.getAscent());
				}
			}
		}

	}

	/**
	 * 测试
	 * @param args 参数
	 */
	public static void main(String... args) {
		LayoutManager layoutManager = null;
		JFrame jf = new JFrame("test");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel content = (JPanel) jf.getContentPane();
		content.setLayout(layoutManager);

		UICheckBox bb = new UICheckBox("sdf");
		bb.setEnabled(false);
		bb.setBounds(20, 20, bb.getPreferredSize().width, bb.getPreferredSize().height);
		content.add(bb);
		GUICoreUtils.centerWindow(jf);
		jf.setSize(400, 400);
		jf.setVisible(true);
	}
}