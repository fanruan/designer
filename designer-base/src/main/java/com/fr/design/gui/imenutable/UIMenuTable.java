package com.fr.design.gui.imenutable;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.constants.UIConstants;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.dialog.UIDialog;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itable.UITable;
import com.fr.design.hyperlink.ReportletHyperlinkPane;
import com.fr.design.hyperlink.WebHyperlinkPane;
import com.fr.design.javascript.EmailPane;
import com.fr.design.utils.gui.GUICoreUtils;

import com.fr.js.EmailJavaScript;
import com.fr.js.ReportletHyperlink;
import com.fr.js.WebHyperlink;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.TableUI;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UIMenuTable extends JTable {
	protected int selectedRowIndex = -1;
	protected int rollOverRowIndex = -1;
	protected int draggingIndex = -1;

	public UIMenuTable() {
		super(new UIMenuTableDataModel());
		initComponents();
	}

	public void populateBean(List<UIMenuNameableCreator> values) {
		((UIMenuTableDataModel)dataModel).populateBean(values);
	}

	public List<UIMenuNameableCreator> updateBean() {
		return ((UIMenuTableDataModel)dataModel).updateBean();
	}

	public void editingEvent(int rowIndex, int mouseY) {
		selectedRowIndex = rowIndex;
		repaint();

		final UIMenuNameableCreator nameObject = UIMenuTable.this.getLine(rowIndex);

		final BasicBeanPane<Object> baseShowPane = nameObject.getPane();

		final Object showValue = nameObject.getObj();

		baseShowPane.populateBean(showValue);

        UIDialog dialog = baseShowPane.showUnsizedWindow(SwingUtilities.getWindowAncestor(new JPanel()), new DialogActionAdapter() {
			public void doOk() {
				baseShowPane.updateBean(showValue);
				fireTargetChanged();
			}
		});

		dialog.setSize(500, 600);
		GUICoreUtils.centerWindow(dialog);

		dialog.setVisible(true);
	}

	protected Color getRenderBackground(int row) {
		if(selectedRowIndex == row ) {
			return UIConstants.SKY_BLUE;
		} else {
			return (rollOverRowIndex == row) ? UIConstants.LIGHT_BLUE : UIConstants.NORMAL_BACKGROUND;
		}
	}

	/**
	 *
	 * @param value 该行列的值(字符串)
	 * @param row
	 * @param column
	 * @return 列表中默认显示的东西，如果有很多内容，可以装载一个JPanel里再嵌进来
	 */
	protected JComponent getRenderCompoment(Object value, int row,int column) {
		UILabel text = new UILabel();
		if(value != null) {
			text.setText(value.toString());
		}
		return text;
	}

	/**
	 * @param line 该行的内容
	 * 在table底部增加一行内容
	 */
	public void addLine(UIMenuNameableCreator line) {
		((UIMenuTableDataModel)dataModel).addLine(line);
	}

	/**
	 * @param rowIndex
	 * @return 某一行的内容
	 */
	public UIMenuNameableCreator getLine(int rowIndex) {
		return ((UIMenuTableDataModel)dataModel).getLine(rowIndex);
	}

	/**
	 * 删除某行内容
	 * @param rowIndex
	 */
	public void removeLine(int rowIndex) {
		((UIMenuTableDataModel)dataModel).removeLine(rowIndex);
	}

	/**
	 * 清除所有的内容
	 */
	public void clearAll() {
		int rowCount = dataModel.getRowCount();
		for(int i = 0; i < rowCount; i++) {
			removeLine(i);
		}
	}

	/**
	 * 对某一行拖动时进行排序
	 * @param rowIndex
	 * @param positive 鼠标移动的距离
	 */
	public void dragSort(int rowIndex, boolean positive) {
		((UIMenuTableDataModel)dataModel).dragSort(rowIndex, positive);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		if(column == 0) {
			return false;
		} else {
			return super.isCellEditable(row, column);
		}
	}

	@Override
	public TableUI getUI() {
		return new UIMenuTableUI();
	}

	private void initComponents() {
		setShowGrid(false);
		setRowHeight(20);
		setDragEnabled(false);
		setDefaultRenderer(UITable.class, new UITableRender());
		setUI(getUI());
	}

	@Override
	public TableCellRenderer getDefaultRenderer(Class<?> columnClass) {
		return super.getDefaultRenderer(UITable.class);
	}

	private class UITableRender implements TableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			JPanel pane = new JPanel(new BorderLayout(4,0));
			Color back =  getRenderBackground(row);
			pane.setBackground(back);

			if(draggingIndex == row) {
				return pane;
			}
			pane.add(getRenderCompoment(value, row, column), BorderLayout.CENTER);
			return pane;
		}
	}

	protected void setRollOverRowIndex(int rowIndex) {
		this.rollOverRowIndex = rowIndex;
	}

	protected void setDraggingRowIndex(int rowIndex) {
		this.draggingIndex = rowIndex;
	}

	public void fireTargetChanged() {
		repaint();
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeListener.class) {
				((ChangeListener)listeners[i + 1]).stateChanged(new ChangeEvent(this));
			}
		}
	}

	public void addChangeListener(ChangeListener l) {
		this.listenerList.add(ChangeListener.class, l);
	}

	public void removeChangeListener(ChangeListener l) {
		this.listenerList.remove(ChangeListener.class, l);
	}

	public static void main(String... args) {
		JFrame jf = new JFrame("test");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel content = (JPanel)jf.getContentPane();
		content.setLayout(new BorderLayout());
		List<UIMenuNameableCreator> data = new ArrayList<UIMenuNameableCreator>();
		UIMenuNameableCreator reportlet = new UIMenuNameableCreator(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Reportlet"),
				new ReportletHyperlink(), ReportletHyperlinkPane.class);

		UIMenuNameableCreator email = new UIMenuNameableCreator(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Email"),
				new EmailJavaScript(), EmailPane.class);

		UIMenuNameableCreator web = new UIMenuNameableCreator(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Hyperlink_Web_Link"),
				new WebHyperlink(), WebHyperlinkPane.class );
		data.add(reportlet);
		data.add(email);
		data.add(web);
		UIMenuTable pane = new UIMenuTable();
		pane.populateBean(data);
		content.add(pane, BorderLayout.CENTER);
		GUICoreUtils.centerWindow(jf);
		jf.setSize(400, 400);
		jf.setVisible(true);
	}
}
