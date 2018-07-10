package com.fr.design.gui.itable;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.fr.design.event.ChangeEvent;
import com.fr.design.event.ChangeListener;
import com.fr.design.utils.gui.GUICoreUtils;

public class UISelectTable extends UITable implements MouseListener {

	protected int selectedRowIndex = -1;
	private List<ChangeListener> selectionListeners = new ArrayList<ChangeListener>();

	public UISelectTable(int columnSize) {
		super(columnSize);
	     addMouseListener(this);
	}

	/**
	 * 给表添加选择事件
	 *
	 * @param listener 选择表数据时触发的事件
	 */
	public void addSelectionChangeListener(ChangeListener listener) {
		selectionListeners.add(listener);
	}

	private void fireSelectionChangeListener(ChangeEvent e) {
		for (int i = selectionListeners.size(); i > 0; i--) {
			selectionListeners.get(i - 1).fireChanged(e);
		}
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return column != 0 && super.isCellEditable(row, column);
	}

	/**
	 * 鼠标点击事件，以触发表的选择事件
	 *
	 * @param e 鼠标事件对象
	 */
	@Override
	public final void mouseClicked(MouseEvent e) {
		selectedRowIndex = rowAtPoint(e.getPoint());
		Object selectItem = getModel().getValueAt(selectedRowIndex, columnAtPoint(e.getPoint()));
		fireSelectionChangeListener(new ChangeEvent(selectItem));
	}

	/**
	 * Invoked when a mouse button has been pressed on a component.
	 */
	@Override
	public void mousePressed(MouseEvent e) {
	}

	/**
	 * Invoked when a mouse button has been released on a component.
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
	}

	/**
	 * Invoked when the mouse enters a component.
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
	}

	/**
	 * Invoked when the mouse exits a component.
	 */
	@Override
	public void mouseExited(MouseEvent e) {
	}


	public static void main(String... args) {
		JFrame jf = new JFrame("test");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel content = (JPanel) jf.getContentPane();
		content.setLayout(new BorderLayout());
		List<Object[]> data = new ArrayList<Object[]>();
		String[] a = {"1", "11"};
		String[] b = {"2", "22"};
		String[] c = {"3", "33"};
		String[] d = {"4", "44"};
		data.add(a);
		data.add(b);
		data.add(c);
		data.add(d);
		UISelectTable pane = new UISelectTable(1);
		pane.populateBean(data);
		content.add(pane, BorderLayout.CENTER);
		GUICoreUtils.centerWindow(jf);
		jf.setSize(400, 400);
		jf.setVisible(true);
	}
}