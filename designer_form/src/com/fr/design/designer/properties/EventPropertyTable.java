package com.fr.design.designer.properties;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.design.actions.UpdateAction;
import com.fr.design.gui.frpane.ListenerUpdatePane;
import com.fr.design.gui.ilist.JNameEdList;
import com.fr.design.gui.itoolbar.UIToolbar;
import com.fr.design.javascript.EmailPane;
import com.fr.design.javascript.JavaScriptActionPane;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.design.write.submit.DBManipulationPane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.designer.creator.XCreator;
import com.fr.form.event.Listener;
import com.fr.design.form.javascript.FormEmailPane;
import com.fr.form.ui.Widget;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.general.NameObject;
import com.fr.js.JavaScriptImpl;
import com.fr.stable.Nameable;
import com.fr.design.utils.gui.GUICoreUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

public class EventPropertyTable extends BasicPane {

	private ShortCut[] shorts;
	private XCreator creator;
	private JNameEdList nameableList;
	private ToolBarDef toolbarDef;
	private AddItemMenuDef itemMenu;
	private ShortCut editItemAction;
	private ShortCut copyItemAction;
	private ShortCut removeItemAction;
	private UIToolbar toolbar;
	private ListenerUpdatePane listenerPane;
	private FormDesigner designer;

	public EventPropertyTable(FormDesigner designer) {
		super();
		this.designer = designer;
		this.initComponents();
	}

	protected void initComponents() {
		this.setLayout(new BorderLayout());
		nameableList = new JNameEdList(new DefaultListModel());
		this.add(new JScrollPane(nameableList), BorderLayout.CENTER);

		nameableList.setCellRenderer(new NameableListCellRenderer());
		nameableList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		nameableList.addMouseListener(listMouseListener);
		nameableList.setTransferHandler(new DnDTransferHandler());
		nameableList.setDropMode(DropMode.INSERT);
		nameableList.setDragEnabled(true);

		toolbarDef = new ToolBarDef();
		shorts = new ShortCut[] { itemMenu = new AddItemMenuDef(), editItemAction = new EditItemMenuDef(),
				copyItemAction = new CopyItemAction(), removeItemAction = new RemoveItemAction() };
		for (ShortCut sj : shorts) {
			toolbarDef.addShortCut(sj);
		}
		toolbar = ToolBarDef.createJToolBar();
		toolbarDef.updateToolBar(toolbar);
		this.add(toolbar, BorderLayout.NORTH);
	}

	/**
	 * 指定索引添加对象
	 * @param nameObject 对象名
	 * @param index  索引
	 */
	public void addNameObject(NameObject nameObject, int index) {
		DefaultListModel model = (DefaultListModel) nameableList.getModel();

		model.add(index, nameObject);
		nameableList.setSelectedIndex(index);
		nameableList.ensureIndexIsVisible(index);

		nameableList.repaint();
	}

	/**
	 * 刷新控件
	 * @param strings 字符数组
	 */
	public void refreshNameableCreator(String[] strings) {
		itemMenu.populate(strings);

		toolbarDef.updateToolBar(toolbar);
		toolbar.validate();
		toolbar.repaint();
		this.repaint();
	}

	private void checkButtonEnabled() {
		boolean enable = nameableList.getSelectedValue() instanceof NameObject;
		itemMenu.setEnabled(this.creator != null && itemMenu.getShortCutCount() > 0);
		editItemAction.setEnabled(enable);
		copyItemAction.setEnabled(enable);
		removeItemAction.setEnabled(enable);
	}

	private MouseListener listMouseListener = new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent evt) {
			checkButtonEnabled();
			if (evt.getClickCount() >= 2 && SwingUtilities.isLeftMouseButton(evt)) {
				NameObject object = (NameObject) nameableList.getSelectedValue();
				showEventPane(object);
			}
		}
	};

	private void showEventPane(final NameObject object) {
		if (listenerPane == null) {
			listenerPane = new ListenerUpdatePane() {

				@Override
				protected JavaScriptActionPane createJavaScriptActionPane() {
					return new JavaScriptActionPane() {

						@Override
						protected DBManipulationPane createDBManipulationPane() {
							return new DBManipulationPane(ValueEditorPaneFactory.formEditors());
						}

						@Override
						protected String title4PopupWindow() {
							return Inter.getLocText("Set_Callback_Function");
						}
						@Override
						protected EmailPane initEmaiPane() {
							return new FormEmailPane();
						}
						@Override
						public boolean isForm() {
							return  true;
						}

						protected String[] getDefaultArgs() {
							return new String[0];
						}

					};
				}
				@Override
				protected boolean supportCellAction() {
					return false;
				}
				
			};
		}
		listenerPane.populateBean((Listener) object.getObject());
		BasicDialog dialog = listenerPane.showWindow(SwingUtilities.getWindowAncestor(this));
		dialog.addDialogActionListener(new DialogActionAdapter() {
			@Override
			public void doOk() {
				object.setObject(listenerPane.updateBean());
				updateWidgetListener(creator);
			}
		});
		dialog.setVisible(true);
	}

	private class NameableListCellRenderer extends DefaultListCellRenderer {
		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			if (value instanceof NameObject) {
				Nameable wrappee = (NameObject) value;
				this.setText(wrappee.getName());
			}
			return this;
		}
	}

	/*
	 * 增加项的MenuDef
	 */
	protected class AddItemMenuDef extends MenuDef {
		public AddItemMenuDef() {
			this.setName(Inter.getLocText("Add"));
			this.setMnemonic('A');
			this.setIconPath("/com/fr/design/images/control/addPopup.png");
		}

		public void populate(String[] eventNames) {
			this.clearShortCuts();
			for (int i = 0; i < eventNames.length; i++) {
				final String eventname = eventNames[i];

				this.addShortCut(new UpdateAction() {
					{
						this.setName(switchLang(eventname));
					}

					public void actionPerformed(ActionEvent e) {
						NameObject nameable = new NameObject(createUnrepeatedName(switchLang(eventname)), new Listener(
								eventname,new JavaScriptImpl()));

						EventPropertyTable.this.addNameObject(nameable, EventPropertyTable.this.nameableList.getModel()
								.getSize());
						updateWidgetListener(creator);
					}
				});
			}
		}
	}

	protected class EditItemMenuDef extends UpdateAction {
		public EditItemMenuDef() {
			this.setName(Inter.getLocText("Edit"));
			this.setMnemonic('E');
			this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/control/edit.png"));
		}

		public void actionPerformed(ActionEvent evt) {
			if (nameableList.getSelectedValue() instanceof NameObject) {
				showEventPane((NameObject) nameableList.getSelectedValue());
			}
		}
	}
	
	private String switchLang(String eventName)	{
		return Inter.getLocText("Event-" + eventName);
	}

	/**
	 * 刷新
	 */
	public void refresh() {
		int selectionSize = designer.getSelectionModel().getSelection().size();
		if (selectionSize == 0 || selectionSize == 1) {
			this.creator = selectionSize == 0 ? designer.getRootComponent() : designer.getSelectionModel()
					.getSelection().getSelectedCreator();
		} else {
			this.creator = null;
			((DefaultListModel) nameableList.getModel()).removeAllElements();
			checkButtonEnabled();
			return;
		}
		Widget widget = creator.toData();

		refreshNameableCreator(widget.supportedEvents());

		((DefaultListModel) nameableList.getModel()).removeAllElements();
		for (int i = 0, size = widget.getListenerSize(); i < size; i++) {
			Listener listener = widget.getListener(i);
			if (!listener.isDefault()) {
				addNameObject(new NameObject(switchLang(listener.getEventName()) + (i + 1), listener), i);
			}
		}
		checkButtonEnabled();
		this.repaint();
	}

	/**
	 * 更新控件事件
	 * @param creator 控件
	 */
	public void updateWidgetListener(XCreator creator) {
		DefaultListModel listModel = (DefaultListModel) this.nameableList.getModel();
		(creator.toData()).clearListeners();
		for (int i = 0, len = listModel.getSize(); i < len; i++) {
			(creator.toData()).addListener((Listener) ((NameObject) listModel.getElementAt(i)).getObject());
		}
		designer.fireTargetModified();
		checkButtonEnabled();
	}

	private class RemoveItemAction extends UpdateAction {
		public RemoveItemAction() {
			this.setName(Inter.getLocText("Remove"));
			this.setMnemonic('R');
			this.setSmallIcon(BaseUtils.readIcon("/com/fr/base/images/cell/control/remove.png"));
		}

		public void actionPerformed(ActionEvent evt) {
			GUICoreUtils.removeJListSelectedNodes(SwingUtilities.getWindowAncestor(EventPropertyTable.this),
					nameableList);
			updateWidgetListener(creator);
		}

	}

	/*
	 * CopyItem
	 */
	private class CopyItemAction extends UpdateAction {
		public CopyItemAction() {
			this.setName(Inter.getLocText("Copy"));
			this.setMnemonic('C');
			this.setSmallIcon(BaseUtils.readIcon("/com/fr/base/images/cell/control/copy.png"));
		}

		public void actionPerformed(ActionEvent evt) {
			NameObject selectedNameObject = (NameObject) nameableList.getSelectedValue();

			// p: 用反射机制实现
			try {
				NameObject newNameObject = (NameObject) BaseUtils.cloneObject(selectedNameObject);
				newNameObject.setName("CopyOf" + selectedNameObject.getName());

				EventPropertyTable.this.addNameObject(newNameObject, nameableList.getSelectedIndex() + 1);
			} catch (Exception e) {
				FRContext.getLogger().error(e.getMessage(), e);
			}
			updateWidgetListener(creator);
		}
	}

	@Override
	protected String title4PopupWindow() {
		return "Event";
	}

	/**
	 * 生成不重复名字
	 * @param prefix  字符
	 * @return 返回
	 */
	public String createUnrepeatedName(String prefix) {
		DefaultListModel model = (DefaultListModel) nameableList.getModel();
		Nameable[] all = new Nameable[model.getSize()];
		for (int i = 0; i < model.size(); i++) {
			all[i] = (Nameable) model.get(i);
		}
		int count = 1;
		while (true) {
			String name_test = prefix + count;
			boolean repeated = false;
			for (int i = 0, len = model.size(); i < len; i++) {
				Nameable nameable = all[i];
				if (ComparatorUtils.equals(nameable.getName(), name_test)) {
					repeated = true;
					break;
				}
			}

			if (!repeated) {
				return name_test;
			}

			count++;
		}
	}

	private class DnDTransferHandler extends TransferHandler {
		private int action;

		public DnDTransferHandler() {
			this(TransferHandler.MOVE);
		}

		public DnDTransferHandler(int action) {
			this.action = action;
		}

		@Override
		public int getSourceActions(JComponent comp) {
			return action;
		}

		@Override
		public Transferable createTransferable(JComponent comp) {

			int index = nameableList.getSelectedIndex();
			if (index < 0 || index >= nameableList.getModel().getSize()) {
				return null;
			}

			return new DnDListItem((NameObject) nameableList.getSelectedValue());
		}

		@Override
		public boolean canImport(TransferSupport support) {
			if (!support.isDrop()) {
				return false;
			}

			if (!support.isDataFlavorSupported(DnDListItem.FLAVOR)) {
				return false;
			}

			boolean actionSupported = (action & support.getSourceDropActions()) == action;
			if (actionSupported) {
				support.setDropAction(action);
				return true;
			}

			return false;
		}

		@Override
		public boolean importData(TransferHandler.TransferSupport support) {
			if (!canImport(support)) {
				return false;
			}
			JList.DropLocation dl = (JList.DropLocation) support.getDropLocation();

			int importIndex = dl.getIndex();

			try {
				NameObject item = (NameObject) support.getTransferable().getTransferData(DnDListItem.FLAVOR);
				JList list = (JList) support.getComponent();
				DefaultListModel model = (DefaultListModel) list.getModel();
				for (int i = 0; i <= importIndex; i++) {
					if (ComparatorUtils.equals(item,model.getElementAt(i))) {
						importIndex--;
						break;
					}
				}
				model.removeElement(item);
				model.insertElementAt(item, importIndex);
				updateWidgetListener(creator);
			} catch (UnsupportedFlavorException e) {
				return false;
			} catch (java.io.IOException e) {
				return false;
			}

			return true;
		}
	}

	public static class DnDListItem implements Transferable {
		private NameObject no;
		public static final DataFlavor FLAVOR = new DataFlavor(DnDListItem.class, "DnDListItem.class");

		public DnDListItem(NameObject no) {
			this.no = no;
		}

		@Override
		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { FLAVOR };
		}

		@Override
		/**
		 *是否支持 dataFlavor
		 *@param DataFlavor类
		 *@return 支持返回true
		 */
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			if (ComparatorUtils.equals(flavor,FLAVOR)) {
				return true;
			}

			return false;
		}

		@Override
		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
			if (ComparatorUtils.equals(flavor,FLAVOR)) {
				return no;
			}

			return null;
		}
	}
}