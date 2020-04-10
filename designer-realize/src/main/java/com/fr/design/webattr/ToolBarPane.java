package com.fr.design.webattr;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.ToolBar;
import com.fr.form.ui.Widget;

import com.fr.report.web.annotation.OldPrintMethod;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

public class ToolBarPane extends BasicBeanPane<ToolBar> {
	private FToolBar ftoolbar = new FToolBar();
	private boolean populateFinished = false;  // 正在 populate 数据

	public ToolBarPane() {
		super();
		this.initComponent();
	}


	/**
	 * 添加鼠标监听
	 *
	 * @param mouselistener 鼠标监听
	 */
	public void addAuthorityListener(MouseListener mouselistener) {
		List<ToolBarButton> list = ftoolbar.getButtonlist();
		for (int i = 0; i < list.size(); i++) {
			list.get(i).addMouseListener(mouselistener);
		}
	}

	public ToolBarPane(ToolBarButton button) {
		super();
		this.initComponent();
		this.add(button);
	}

	/**
	 * 初始化组件
	 */
	public void initComponent() {
		this.addMouseListener(listener);
		this.setLayout(FRGUIPaneFactory.createBoxFlowLayout());
		this.setTransferHandler(new ToolBarHandler(TransferHandler.COPY));
		this.setBorder(BorderFactory.createTitledBorder(""));
	}

	/**
	 * 删除鼠标事件
	 */
	public void removeDefaultMouseListener() {
		this.removeMouseListener(listener);
	}

	@Override
	protected String title4PopupWindow() {
		return "Toolbar";
	}

	public void setSelectedButton(ToolBarButton button) {
		this.ftoolbar.addButton(button);
	}

	/**
	 * 添加组件
	 *
	 * @param comp 组件
	 *
	 * @return 被添加的组件
	 */
	public Component add(Component comp) {
		if (comp instanceof ToolBarButton) {
			if (isPopulateFinished() && ((ToolBarButton) comp).getWidget().getClass().isAnnotationPresent(OldPrintMethod.class)) {
				FineJOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Use_New_Print_Tip"));
				return comp;
			}
			this.ftoolbar.addButton((ToolBarButton) comp);
		}
		return super.add(comp);
	}

	private Component addComp(Component comp) {
		return super.add(comp);
	}


	protected void removeButtonList() {
		this.ftoolbar.clearButton();
	}

	protected void setFToolBar(FToolBar ftoolbar) {
		if (ftoolbar == null) {
			ftoolbar = new FToolBar();
		}
		this.ftoolbar = ftoolbar;
		this.setToolBar(this.ftoolbar.getButtonlist());
	}

	public List<ToolBarButton> getToolBarButtons() {
		return ftoolbar.getButtonlist();
	}

	protected FToolBar getFToolBar() {
		return this.ftoolbar;
	}

	protected boolean isEmpty() {
		return this.ftoolbar.getButtonlist().size() <= 0;
	}

	private void setToolBar(List<ToolBarButton> list) {
		if (list == null ) {
			return;
		}
		this.removeAll();
		for (int i = 0; i < list.size(); i++) {
			this.addComp(list.get(i));
		}
		this.validate();
		this.repaint();

	}

	@Override
	public void populateBean(ToolBar toolbar) {
		setPopulateFinished(false);
		this.removeAll();
		this.getFToolBar().clearButton();
		for (int j = 0; j < toolbar.getWidgetSize(); j++) {
			Widget widget = toolbar.getWidget(j);
			WidgetOption no = WidgetOption.getToolBarButton(widget.getClass());
			if (no == null){
				//如果装了什么插件, 放到了工具栏上, 后来删除了插件, 模板里还存着之前的控件
				continue;
			}

			ToolBarButton button = new ToolBarButton(no.optionIcon(), widget);
			button.setNameOption(no);
			this.add(button);
			this.validate();
			this.repaint();
		}
		this.getFToolBar().setBackground(toolbar.getBackground());
		this.getFToolBar().setDefault(toolbar.isDefault());
		setPopulateFinished(true);
	}

	@Override
	public ToolBar updateBean() {
		return this.ftoolbar.getToolBar();
	}

	MouseListener listener = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() >= 2 && !SwingUtilities.isRightMouseButton(e)) {
				final EditToolBar tb = new EditToolBar();
				tb.populate(getFToolBar());
				BasicDialog dialog = tb.showWindow(SwingUtilities.getWindowAncestor(ToolBarPane.this));
				dialog.addDialogActionListener(new DialogActionAdapter() {
					@Override
					public void doOk() {
						ToolBarPane.this.setFToolBar(tb.update());
					}
				});
				dialog.setVisible(true);
			}
		}
	};

	private boolean isPopulateFinished() {
		return populateFinished;
	}

	private void setPopulateFinished(boolean populateFinished) {
		this.populateFinished = populateFinished;
	}


	/*
	 * 拖拽属性设置
	 */
	private class ToolBarHandler extends TransferHandler {
		private int action;

		public ToolBarHandler(int action) {
			this.action = action;
		}


		@Override
		public boolean canImport(TransferHandler.TransferSupport support) {
			if (!support.isDrop()) {
				return false;
			}

			if (!support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
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
			WidgetOption data;
			try {
				data = (WidgetOption) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
			} catch (UnsupportedFlavorException e) {
				return false;
			} catch (java.io.IOException e) {
				return false;
			}

			Widget widget = data.createWidget();
			ToolBarButton btn = new ToolBarButton(data.optionIcon(), widget);
			btn.setNameOption(data);
			ToolBarPane.this.add(btn);
			ToolBarPane.this.validate();
			ToolBarPane.this.repaint();
			return true;
		}

	}


}
