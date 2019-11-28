package com.fr.design.mainframe.loghandler;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;



public class LogHandlerBarUI extends ComponentUI implements MouseListener, FocusListener {
	private static final Color DEFAULT_FOREGROUND = new Color(0, 0, 0);
	private static final int TEXT_LEADING_GAP = 14;

	private boolean armed;
	private int textLeadingGap = TEXT_LEADING_GAP;

	// 渐变色起始色
	private Color lightColor;
	// 渐变色结束色
	private Color darkColor;

	// 该UI实例对应的CaptionButton
	protected LogHandlerBar button;

	public LogHandlerBarUI() {
	}

    /**
     * 创建UI
     * @param c 组件
     * @return 组件UI
     */
	public static ComponentUI createUI(JComponent c) {
		return new LogHandlerBarUI();
	}

    /**
     *配置组件
     * @param c 组件
     */
	public void installUI(JComponent c) {
		button = (LogHandlerBar) c;
		button.setForeground(DEFAULT_FOREGROUND);
		button.setFocusable(true);

		button.addMouseListener(this);
		button.addFocusListener(this);
	}

    /**
     * 解除组件配置
     * @param c 组件
     */
	public void uninstallUI(JComponent c) {
		button.removeMouseListener(this);
		button.removeFocusListener(this);
	}

	protected void paintBackground(Graphics g) {
		int w = button.getWidth();
		int h = button.getHeight();
		Graphics2D g2d = (Graphics2D) g;
		GradientPaint gp = new GradientPaint(1, 1, darkColor, 1, (float)(h - 1), darkColor);
		g2d.setPaint(gp);
		g2d.fillRect(0, 0, w, h);
	}

	public void paint(Graphics g, JComponent c) {
		super.paint(g, c);
		if (armed) {
			button.setWithSerious(false);
		}
		if (button.IsWithSerious()) {
			lightColor = new Color(255, 242, 218);
			darkColor = new Color(255, 219, 108);
		} else {
			lightColor = new Color(226, 230, 234);
			darkColor = new Color(183, 188, 195);
		}
		paintBackground(g);
		paintCaptionText(g);

	}

	protected void paintCaptionText(Graphics g) {
		FontMetrics fm = g.getFontMetrics();
		Color color = button.getForeground();
		g.setColor(color);
		int y = ((button.getHeight() - fm.getHeight()) / 2) + fm.getAscent();
		if (button.getText() != null) {
			g.drawString(button.getText(), textLeadingGap, y);
		}

		g.drawString(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_NNormal") + '(' + button.getInfo() + ')', button.getWidth() - 310, y);
		g.drawString(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Alert") + '(' + button.getError() + ')', button.getWidth() - 250, y);
		g.drawString(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Seriously") + '(' + button.getServer() + ')', button.getWidth() - 190, y);
	}

    /**
     * 鼠标点击
     * @param e 鼠标事件
     */
	public void mousePressed(MouseEvent e) {
		armed = true;
		button.requestFocus();
	}

    /**
     * 鼠标按下并释放
     * @param e 鼠标事件
     */
	public void mouseEntered(MouseEvent e) {

	}

    /**
     *事件结束
     * @param e 鼠标事件
     */
	public void mouseExited(MouseEvent e) {
		button.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

    /**
     *失去键盘焦点
     * @param e 焦点事件
     */
	public void focusLost(FocusEvent e) {
		armed = false;
	}
    /**
     *鼠标点击
     * @param e 鼠标事件
     */
	public void mouseClicked(MouseEvent e) {
	}
    /**
     *鼠标释放
     * @param e 鼠标事件
     */
	public void mouseReleased(MouseEvent e) {
	}
    /**
     *获取键盘焦点
     * @param e 焦点事件
     */
	public void focusGained(FocusEvent e) {
	}
}
