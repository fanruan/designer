package com.fr.design.style;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.border.AbstractBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import com.fr.design.constants.UIConstants;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.stable.Constants;
import com.fr.design.style.background.BackgroundJComponent;

/**
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2011-10-31 下午03:28:31 类说明: 抽象出来的弹出box. 可以弹出颜色选择, 图案选择, 纹理选择.
 *          主要是 弹出界面的不同
 */
public abstract class AbstractSelectBox<T> extends AbstractPopBox implements MouseListener {
	private static final long serialVersionUID = 2355250206956896774L;

	private UIToggleButton triggleButton;

	protected void initBox(int preWidth) {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());

		displayComponent = new BackgroundJComponent();
		displayComponent.setEmptyBackground();
		displayComponent.setBorder(new TriggleLineBorder());
		triggleButton = new UIToggleButton(UIConstants.ARROW_DOWN_ICON);
		triggleButton.setPreferredSize(new Dimension(20, 20));

		JPanel displayPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		displayPane.add(displayComponent, BorderLayout.CENTER);
		displayComponent.setPreferredSize(new Dimension(preWidth, displayPane.getPreferredSize().height));

		displayComponent.addMouseListener(mouseListener);
		triggleButton.addMouseListener(mouseListener);
		displayComponent.addMouseListener(this);
		triggleButton.addMouseListener(this);

		this.add(displayPane, BorderLayout.CENTER);
		this.add(triggleButton, BorderLayout.EAST);

		this.addAncestorListener(new AncestorListener() {
			public void ancestorAdded(AncestorEvent event) {
			}

			public void ancestorRemoved(AncestorEvent evt) {
				hidePopupMenu();
			}

			public void ancestorMoved(AncestorEvent event) {
				hidePopupMenu();
			}
		});
	}

	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);

		displayComponent.setEnabled(enabled);
		triggleButton.setEnabled(enabled);
	}

	@Override
	public JPanel initWindowPane(double preWidth) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(UIConstants.POP_DIALOG_BORDER);
		g2d.drawRoundRect(0, 0, this.getWidth() - 1 , this.getHeight() - 1, 4, 4);
		triggleButton.setSelected(isPopupVisible());
	}


	public void addDemoPaneMouseListener(MouseListener l) {
		displayComponent.addMouseListener(l);
		triggleButton.addMouseListener(l);
	}

	public abstract T getSelectObject();

	public abstract void setSelectObject(T t);

	private  class TriggleLineBorder extends AbstractBorder {
		private static final long serialVersionUID = 1065857667981063530L;
		protected Insets borderInsets = new Insets(0, 0, 0, 0);

		public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
			g.translate(x, y);

			g.setColor(UIConstants.POP_DIALOG_BORDER);

			g.translate(-x, -y);
		}

		public Insets getBorderInsets(Component c) {
			return borderInsets;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		triggleButton.getModel().setRollover(true);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		triggleButton.getModel().setRollover(false);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}