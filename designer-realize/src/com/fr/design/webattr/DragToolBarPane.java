package com.fr.design.webattr;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;

import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.gui.core.WidgetOption;
import com.fr.form.ui.Widget;
import com.fr.general.Inter;
import com.fr.report.web.Location;
import com.fr.report.web.ToolBarManager;

/**
 * richer:拖拽ToolBar button以实现自定义工具栏.服务器配置那儿的
 * 
 * @editor zhou 2012-3-22下午3:59:00
 */
public class DragToolBarPane extends WidgetToolBarPane {
	private DefaultListModel toolbarButtonListModel = new DefaultListModel();
	private JList toolbarButtonList;

	public DragToolBarPane() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());

		JPanel leftPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		toolbarButtonList = new JList(toolbarButtonListModel);
		leftPane.add(new JScrollPane(new JPanel()));
		toolbarButtonList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() > 1 && !(SwingUtilities.isRightMouseButton(e))) {
					if (toolbarButtonList.getSelectedValue() == null) {
						return;
					}
					WidgetOption no = (WidgetOption)toolbarButtonList.getSelectedValue();
					Widget widget = no.createWidget();
					ToolBarButton tb = new ToolBarButton(no.optionIcon(), widget);
					tb.setNameOption(no);
					northToolBar.add(tb);
					northToolBar.validate();
					northToolBar.repaint();
				}
			}
		});

		toolbarButtonList.setCellRenderer(optionRenderer);
		toolbarButtonList.setDropMode(DropMode.ON_OR_INSERT);
		toolbarButtonList.setDragEnabled(true);
		toolbarButtonList.setTransferHandler(new FromTransferHandler());
		northToolBar = new ToolBarPane();
		northToolBar.setPreferredSize(new Dimension(ImageObserver.WIDTH, 26));
		northToolBar.setBackground(Color.lightGray);
		southToolBar = new ToolBarPane();
		southToolBar.setPreferredSize(new Dimension(ImageObserver.WIDTH, 26));
		southToolBar.setBackground(Color.lightGray);
		JPanel northContentPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		SettingToolBar top = new SettingToolBar(Inter.getLocText("ToolBar_Top"), northToolBar);
		northContentPane.add(top, BorderLayout.EAST);
		northContentPane.add(northToolBar, BorderLayout.CENTER);
		northContentPane.setBackground(Color.lightGray);

		JPanel southContentPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		SettingToolBar bottom = new SettingToolBar(Inter.getLocText("ToolBar_Bottom"), southToolBar);
		southContentPane.add(bottom, BorderLayout.EAST);
		southContentPane.add(southToolBar, BorderLayout.CENTER);
		southContentPane.setBackground(Color.lightGray);
		JPanel movePane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		movePane.add(northContentPane, BorderLayout.NORTH);
		movePane.add(toolbarButtonList, BorderLayout.CENTER);
		movePane.add(southContentPane, BorderLayout.SOUTH);

		// SplitPane
		this.add(new JScrollPane(movePane), BorderLayout.CENTER);

		JPanel buttonPane = FRGUIPaneFactory.createCenterFlowInnerContainer_S_Pane();
		UIButton defaultButton = new UIButton(Inter.getLocText("Use_Default_ToolBar"));
		defaultButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				southToolBar.removeAll();
				southToolBar.removeButtonList();
				southToolBar.repaint();
				northToolBar.removeButtonList();

				if (defaultToolBar == null) {
					return;
				}
				ToolBarManager toolBarManager = defaultToolBar;
				toolBarManager.setToolBarLocation(Location.createTopEmbedLocation());
				ToolBarManager[] tbm = new ToolBarManager[] { toolBarManager };
				populateBean(tbm);
				DragToolBarPane.this.repaint();
			}
		});
		buttonPane.add(defaultButton);
		UIButton removeButton = new UIButton(Inter.getLocText("Remove_All_Button"));
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				northToolBar.removeAll();
				northToolBar.removeButtonList();
				southToolBar.removeAll();
				southToolBar.removeButtonList();
				southToolBar.repaint();
				northToolBar.repaint();
			}
		});
		this.add(buttonPane, BorderLayout.SOUTH);
	}

	public void setDefaultToolBar(ToolBarManager defaultToolBar, WidgetOption[] buttonArray) {
		super.setDefaultToolBar(defaultToolBar);
		if (buttonArray != null) {
			for (int i = 0; i < buttonArray.length; i++) {
				toolbarButtonListModel.addElement(buttonArray[i]);
			}
		}
		toolbarButtonList.validate();
	}

	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText(new String[]{"ReportServerP-Toolbar", "Set"});
	}

	ListCellRenderer optionRenderer = new DefaultListCellRenderer() {
		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			if (value instanceof WidgetOption) {
				WidgetOption nameOption = (WidgetOption)value;
				this.setText(nameOption.optionName());

				Icon icon = nameOption.optionIcon();
				if (icon != null) {
					this.setIcon(icon);
				}
			}
			return this;
		}
	};

	private class FromTransferHandler extends TransferHandler {
		public int getSourceActions(JComponent comp) {
			return COPY_OR_MOVE;
		}

		private int index = 0;

		@Override
		public Transferable createTransferable(JComponent comp) {
			index = toolbarButtonList.getSelectedIndex();
			if (index < 0 || index >= toolbarButtonListModel.getSize()) {
				return null;
			}
			return new NameOptionSelection((WidgetOption)toolbarButtonList.getSelectedValue());
		}

		@Override
		public void exportDone(JComponent comp, Transferable trans, int action) {
			if (action != MOVE) {
				return;
			}
			toolbarButtonListModel.removeElementAt(index);
		}
	}

}