package com.fr.design.webattr;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.itoolbar.UIToolbar;
import com.fr.design.javascript.ListenerEditPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.ToolBarDef;
import com.fr.design.widget.EventCreator;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.form.event.Listener;

import com.fr.js.JavaScriptImpl;
import com.fr.report.web.WebContent;
import com.fr.design.utils.gui.GUICoreUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * richer:调用该类并且对事件名字国际化时需要严格按照"FR-Engine_Event_事件名"来进行命名
 */
public class EventPane extends BasicPane {
	private DefaultListModel listModel;
	private JList eventList;
	private AddMenuDef addAction;
	private EditAction editAction;
	private RemoveAction removeAction;
	private String[] eventName;

	private int itemHeight = 20;

	public EventPane(String[] eventName) {
		this.initComponents(eventName);
	}

	private void initComponents(String[] eventName) {
		int len = eventName.length;
		this.eventName = Arrays.copyOf(eventName, len);
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		listModel = new DefaultListModel();
		eventList = new JList(listModel);
//		eventList.setFixedCellHeight(20);
		eventList.setCellRenderer(render);
		eventList.addMouseListener(editListener);
		this.add(new UIScrollPane(eventList), BorderLayout.CENTER);
		addAction = new AddMenuDef(this.eventName);

		editAction = new EditAction();
		removeAction = new RemoveAction();
		ToolBarDef def = new ToolBarDef();
		def.addShortCut(addAction);
		def.addShortCut(editAction);
		def.addShortCut(removeAction);
		UIToolbar toolBar = ToolBarDef.createJToolBar();
		def.updateToolBar(toolBar);
		toolBar.setPreferredSize(new Dimension(toolBar.getWidth(), 26));
		this.add(toolBar, BorderLayout.NORTH);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		this.eventList.setEnabled(enabled);
		this.addAction.setEnabled(enabled); // TODO ALEX_SEP
		// 似乎UpdateAction.enable属性还是有用的...
		this.editAction.setEnabled(enabled);
		this.removeAction.setEnabled(enabled);

		this.checkEnableState();
	}

	private void checkEnableState() {
		if (this.listModel.size() == 0 || eventList.getSelectedIndex() < 0) {
			setEditEnabled(false);
		} else {
			setEditEnabled(true);
		}
	}

	private void setEditEnabled(boolean enabled) {
		this.editAction.setEnabled(enabled);
		this.removeAction.setEnabled(enabled);
	}

	@Override
	protected String title4PopupWindow() {
		return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Event_Set");
	}


	public void populate(List<Listener> listenerList) {
		listModel.removeAllElements();
		for (int i = 0; i < listenerList.size(); i++) {
			listModel.addElement(listenerList.get(i));
		}
		eventList.validate();
	}

	public List<Listener> update() {
		List<Listener> list = new ArrayList<Listener>();
		for (int i = 0; i < listModel.getSize(); i++) {
			list.add((Listener) listModel.get(i));
		}
		return list;
	}

	public void setMenu(String[] eventName) {
		this.eventName = eventName;
	}

	public static ListCellRenderer render = new DefaultListCellRenderer() {
		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			if (value instanceof Listener) {
				Listener l = (Listener) value;
				this.setText(EventCreator.switchLang(l.getEventName()));
			}
			return this;
		}
	};

	public MouseListener editListener = new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			if (eventList.getSelectedIndex() < 0) {
				return;
			}
			checkEnableState();
			if (e.getClickCount() >= 2 && SwingUtilities.isLeftMouseButton(e)) {
				edit();
			}

			if (SwingUtilities.isRightMouseButton(e)) {
				// ben:这个方法不是很好，需要改进
				int h = e.getY();
				eventList.setSelectedIndex((int) Math.floor(h / itemHeight));
				JPopupMenu popupMenu = new JPopupMenu();
				popupMenu.add(editAction);
				popupMenu.add(removeAction);
				GUICoreUtils.showPopupMenu(popupMenu, eventList, e.getX() - 1, e.getY() - 1);
			}
		}
	};

	public class RemoveAction extends UpdateAction {
		public RemoveAction() {
			this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Delete"));
			this.setSmallIcon(BaseUtils.readIcon("/com/fr/base/images/cell/control/remove.png"));
		}

		public void actionPerformed(ActionEvent e) {
			int i = eventList.getSelectedIndex();
			if (i < 0 || !(listModel.getElementAt(i) instanceof Listener)) {
				return;
			}

			int val = JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(EventPane.this), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Are_You_Sure_To_Delete_The_Data") + "?", "Message",
					JOptionPane.YES_NO_OPTION);
			if (val != JOptionPane.YES_OPTION) {
				return;
			}
			listModel.removeElementAt(i);
			checkEnableState();
			eventList.validate();
		}
	}

	public class EditAction extends UpdateAction {
		public EditAction() {
			this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Edit"));
			this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/control/edit.png"));
		}

		public void actionPerformed(ActionEvent e) {
			if (eventList.getSelectedIndex() < 0) {
				return;
			}
			edit();
		}

	}

	public void edit() {
		final int i = eventList.getSelectedIndex();
		if (!(listModel.getElementAt(i) instanceof Listener)) {
			return;
		}
		Listener lis = (Listener) listModel.getElementAt(i);
		final ListenerEditPane jsPane= new ListenerEditPane(WebContent.getDefaultArg(lis.getEventName()));
		jsPane.populateBean(lis);
//		BasicDialog dialog = jsPane.showWindow(DesignerContext.getDesignerFrame());
		// 不能直接建立在DesignerFrame 不然里面事件里有提交入库进行智能添加单元格就麻烦了
		BasicDialog dialog = jsPane.showWindow(SwingUtilities.getWindowAncestor(EventPane.this));
		dialog.addDialogActionListener(new DialogActionAdapter() {
			@Override
			public void doOk() {
				listModel.setElementAt(jsPane.updateBean(), i);
				eventList.validate();
			}
		});
		dialog.setVisible(true);
	}

	public class AddMenuDef extends MenuDef {
		private String[] menuName;

		public AddMenuDef(String[] menuName) {
			this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Add"));
			this.setTooltip(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Add"));
			this.setMnemonic('A');
			this.setIconPath("/com/fr/design/images/control/addPopup.png");
			this.menuName = menuName;
			showMenu();
		}

		public void showMenu() {
			for (int i = 0; i < this.menuName.length; i++) {
				final int j = i;
				this.addShortCut(new UpdateAction() {
					{
						this.setName(EventCreator.switchLang(menuName[j]));
					}

					public void actionPerformed(ActionEvent e) {
						String[] def = WebContent.getDefaultArg(menuName[j]);
						final ListenerEditPane listenerPane = def == null ? new ListenerEditPane() : new ListenerEditPane(def);
						Listener lis = new Listener(menuName[j], new JavaScriptImpl());
						listenerPane.populateBean(lis);

						BasicDialog dialog = listenerPane.showWindow(SwingUtilities.getWindowAncestor(EventPane.this));
						dialog.addDialogActionListener(new DialogActionAdapter() {
							@Override
							public void doOk() {
								listModel.addElement(listenerPane.updateBean());
								eventList.validate();
							}
						});
						dialog.setVisible(true);
					}

				});
			}
		}
	}
}
