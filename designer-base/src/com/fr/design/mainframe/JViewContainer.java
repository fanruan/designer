package com.fr.design.mainframe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.fr.base.BaseUtils;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;

//august:具有扩展伸缩的容器组件
public class JViewContainer extends JPanel implements MouseListener {
	private static final long serialVersionUID = 1L;
	private static Icon leftIcon;
	private static Icon rightIcon;
	static {
		leftIcon = BaseUtils.readIcon("/com/fr/design/images/docking/right.gif");
		rightIcon = BaseUtils.readIcon("/com/fr/design/images/docking/left.gif");
	}
	private ContentPane contentPane;
	private ActionPanel aps;


	public JViewContainer() {
		super();
		this.contentPane = new ContentPane();
		this.setLayout(new BorderLayout());
		this.setBackground(new Color(73, 73, 73));
		this.setBorder(null);
		aps = new ActionPanel();
		aps.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		contentPane.setVisible(true);
		this.add(aps, BorderLayout.NORTH);
		this.add(contentPane, BorderLayout.CENTER);

	}

	public void setViewComposite(JComponent comp) {
		contentPane.setViewComposite(comp);
	}

	private class ContentPane extends JPanel {

		private static final long serialVersionUID = 1L;

		public ContentPane() {
			super();
			this.setLayout(FRGUIPaneFactory.createBorderLayout());
		}

		@Override
		public Dimension getPreferredSize() {
			Dimension di = super.getPreferredSize();
			return new Dimension(242, di.height);
		}

		public void setViewComposite(JComponent comp) {
			this.removeAll();

			if (comp != null) {
				this.add(comp, BorderLayout.CENTER);
//				this.setVisible(true);
//				aps.setEnabled(true);
			} else {
				this.setVisible(false);
				aps.setEnabled(false);
				JViewContainer.this.removeMouseListener(JViewContainer.this);
				JViewContainer.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				this.repaint();
			}
		}
	}

	private class ActionPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private UIButton leftButton;
		private UIButton rightButton;

		public ActionPanel() {
			this.setLayout(FRGUIPaneFactory.createBorderLayout());

			leftButton = GUICoreUtils.createTransparentButton(leftIcon, leftIcon, leftIcon);
			rightButton = GUICoreUtils.createTransparentButton(rightIcon, rightIcon, rightIcon);
			this.setBackground(new Color(73, 73, 73));
			this.setBorder(null);
			this.setRequestFocusEnabled(true);
			this.add(rightButton, BorderLayout.WEST);
			this.addActionListener();

		}

		public void setEnabled(boolean b) {
			rightButton.setEnabled(b);
			leftButton.setEnabled(b);
		}

		private void addActionListener() {
			rightButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					ActionPanel.this.removeAll();
					ActionPanel.this.add(leftButton, BorderLayout.WEST);
					ActionPanel.this.togglePanelVisibility();

				}
			});
			leftButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					ActionPanel.this.removeAll();
					ActionPanel.this.add(rightButton, BorderLayout.WEST);
					ActionPanel.this.togglePanelVisibility();
				}
			});
		};

		private void togglePanelVisibility() {
			if (contentPane.isShowing()) {
				contentPane.setVisible(false);
				JViewContainer.this.addMouseListener(JViewContainer.this);
			} else {
				contentPane.setVisible(true);
				JViewContainer.this.removeMouseListener(JViewContainer.this);
			}
			this.getParent().validate();
		}

		@Override
		public Dimension getPreferredSize() {
			Dimension di = super.getPreferredSize();
			return new Dimension(di.width, 10);
		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		aps.togglePanelVisibility();
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (!contentPane.isShowing()) {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

}