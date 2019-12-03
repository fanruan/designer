package com.fr.design.mainframe;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.JWindow;
import com.fr.design.constants.UIConstants;
import com.fr.design.gui.core.WidgetOption;
import com.fr.stable.os.OperatingSystem;

//august:
public class FormWidgetPopWindow extends JWindow {
	private WidgetOption[] options;
	private EditorChoosePane pane;
	public FormWidgetPopWindow() {
		super();
	}

	private void initComp() {
		if(pane != null) {
			this.remove(pane);
		}
		pane = new EditorChoosePane();
		this.getContentPane().add(pane);
		this.setSize(pane.getPreferredSize());
	}

	public void showToolTip(int xAbs, int yAbs, WidgetOption[] options) {
		Toolkit.getDefaultToolkit().addAWTEventListener(awt, AWTEvent.MOUSE_EVENT_MASK);
		this.setLocation(xAbs, yAbs);
		this.options = options;
		initComp();
		this.setVisible(true);
		this.doLayout();
	}
	
	private AWTEventListener awt = new AWTEventListener() {
		public void eventDispatched(AWTEvent event) {
			if (event instanceof MouseEvent) {
				MouseEvent mv = (MouseEvent) event;
				if (mv.getClickCount() > 0) {
					hideWindow(mv);
				}
			}
		}
	};

	private void hideWindow(MouseEvent mv){
		Point point = new Point((int) (mv.getLocationOnScreen().getX()), (int) mv.getLocationOnScreen().getY());
		if (OperatingSystem.isWindows()) {
			if (!FormWidgetPopWindow.this.contains(point)) {
				FormWidgetPopWindow.this.setVisible(false);
			}
		}else if(OperatingSystem.isMacos() || OperatingSystem.isLinux()){
			Dimension d = FormWidgetPopWindow.this.getSize();
			Point p = FormWidgetPopWindow.this.getLocation();
			Rectangle rect = new Rectangle(p, d);
			if (!rect.contains(point)) {
				FormWidgetPopWindow.this.setVisible(false);
			}
		}
	}

	private class EditorChoosePane extends JPanel {
		public EditorChoosePane() {
			super();
			this.setLayout(new EditorLayout());
			this.initComponents();
		}

		@Override
		public void paintComponent(Graphics g) {
			Rectangle r = this.getBounds();
			g.setColor(UIConstants.NORMAL_BACKGROUND);
			g.fillRoundRect(r.x, r.y, r.width, r.height, UIConstants.ARC, UIConstants.ARC);
			g.setColor(UIConstants.LINE_COLOR);
			g.drawRoundRect(r.x, r.y, r.width - 1, r.height - 1, UIConstants.ARC, UIConstants.ARC);
		}

		protected void initComponents() {
			for (WidgetOption o : options) {
				ToolBarButton toolBarButton = new ToolBarButton(o);
				this.add(toolBarButton);
			}
		}
	}

	private static class EditorLayout implements LayoutManager {

		int top = 4;
		int left = 4;
		int right = 4;
		int bottom = 4;
		int hgap = 5;
		int vgap = 4;
		int maxLine = 8;

		@Override
		public void addLayoutComponent(String name, Component comp) {

		}

		@Override
		public void layoutContainer(Container target) {
			synchronized (target.getTreeLock()) {
				Insets insets = target.getInsets();
				int nmembers = target.getComponentCount();
				for (int i = 0; i < nmembers; i++) {
					Component m = target.getComponent(i);
					if (m.isVisible()) {
						Dimension d = m.getPreferredSize();
						m.setBounds(insets.left + left + i % maxLine * (hgap + d.width), top + insets.top + i / maxLine
								* (vgap + d.height), d.width, d.height);
					}
				}
			}
		}

		@Override
		public Dimension minimumLayoutSize(Container parent) {
			return new Dimension(0, 0);
		}

		@Override
		public Dimension preferredLayoutSize(Container parent) {
			Insets insets = parent.getInsets();
			int nmembers = parent.getComponentCount();
			return new Dimension(maxLine * 28 + insets.left + insets.right + right + left, (nmembers / maxLine + 1)
					* 24 + insets.top + insets.bottom + top + bottom);
		}

		@Override
		public void removeLayoutComponent(Component comp) {

		}

	}
}