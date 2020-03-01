package com.fr.design.gui.itable;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.TableUI;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.fr.design.constants.UIConstants;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.utils.gui.GUICoreUtils;

public class UITable extends JTable implements UIObserver {


    private static final int OFF_LEFT = 10;
    private static final int DEFAULT_ROW_HEIGHT =20;
    private UIObserverListener uiObserverListener;
    UITableEditor editor ;
    private boolean shouldResponseAwt;
    private boolean isEditingStopped;


    /**
	 * 在没有任何数据的时候，使用此构造函数，然后通过populate更新数据
	 *
	 * @param columnSize 列表的列数
	 */
	public UITable(int columnSize) {

		super(new UITableDataModel(columnSize));
		initComponents();
        iniListener();
        shouldResponseAwt = false;
        // kunsnat: 屏蔽: 对于下拉框, 无法等待选择结果之后在stop..
//        Toolkit.getDefaultToolkit().addAWTEventListener(awt, AWTEvent.MOUSE_EVENT_MASK);
	}

    public UITable (int columnSize, boolean needAWTEventListener) {
        this(columnSize);
        shouldResponseAwt = needAWTEventListener;
        isEditingStopped = true;
        if (needAWTEventListener) {
            Toolkit.getDefaultToolkit().addAWTEventListener(awt, AWTEvent.MOUSE_EVENT_MASK);
            this.addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) {
                    isEditingStopped = false;
                }
            });
        }
    }

	/**
	 * values不允许为空！
	 *
	 * @param values 一个列表，里面装有字符串数组，每个数组代表一行内容
	 */
	public UITable(List<Object[]> values) {
		super(new UITableDataModel(values));
		initComponents();
        iniListener();
	}

	public UITable() {

	}

	public void populateBean(List<Object[]> values) {
		getTableDataModel().populateBean(values);
	}



    private AWTEventListener awt = new AWTEventListener() {
    		public void eventDispatched(AWTEvent event) {
                if(!UITable.this.isShowing()){
                    return;
                }
    			doSomeInAll(event);
    		}
    	};

    	private void doSomeInAll(AWTEvent event) {
            Rectangle bounds = new Rectangle(getLocationOnScreen().x, getLocationOnScreen().y, getWidth(), getHeight());
            if (event instanceof MouseEvent) {
    			MouseEvent mv = (MouseEvent) event;
    			if (mv.getClickCount() > 0) {
    				Point point = new Point((int) (mv.getLocationOnScreen().getX()) - 2 * OFF_LEFT, (int) mv.getLocationOnScreen().getY());
    				// 判断鼠标点击是否在边界内
    				if (!bounds.contains(point) && shouldResponseAwt) {
                        if (!isEditingStopped) {
                            this.editor.stopCellEditing();
                            isEditingStopped = true;
                        }
    				}
    			}
    		}
    	}

	public List<Object[]> updateBean() {
		return getTableDataModel().updateBean();
	}

	/**
	 * 在table底部增加一空行
	 */
	public void addBlankLine() {
		getTableDataModel().addBlankLine();
	}

	/**
	 * 在table底部增加一行内容
	 * @param line 该行的内容
	 */
	public void addLine(Object[] line) {
		getTableDataModel().addLine(line);
	}

	/**
	 * @param rowIndex
	 * @return 某一行的内容
	 */
	public Object[] getLine(int rowIndex) {
		return getTableDataModel().getLine(rowIndex);
	}

	/**
	 * 删除某行内容
	 *
	 * @param rowIndex 行号
	 */
	public void removeLine(int rowIndex) {
		getTableDataModel().removeLine(rowIndex);
	}

	/**
	 * 对某一行拖动时进行排序
	 *
	 * @param rowIndex 行号
	 * @param positive 鼠标移动的距离
	 */
	public void dragSort(int rowIndex, boolean positive) {
		((UITableDataModel) dataModel).dragSort(rowIndex, positive);
	}


	/**
	 *格子是否可编辑，可置顶某一列column不可编辑
	 * @param row 行号
	 * @param column 列号
	 * @return 是否可编辑
	 */
	public boolean isCellEditable(int row, int column) {
		return true;
	}

	/**
	 * 清空数据
	 */
	public void clear() {
		getTableDataModel().clear();
	}
	
	private UITableDataModel getTableDataModel() {
		return (UITableDataModel) dataModel;
	}

	/**
	 * @param value  该行列的值(字符串)
	 * @param row
	 * @param column
	 * @return 列表中默认显示的东西，如果有很多内容，可以装载一个JPanel里再嵌进来
	 */
	protected JComponent getRenderCompoment(Object value, int row, int column) {
		UILabel text = new UILabel();
		if (value != null) {
			text.setText(value.toString());
		}
		return text;
	}

	protected void initComponents() {
		setShowGrid(false);
		setRowHeight(getRowHeight4Table());
		setDragEnabled(false);
		editor = createTableEditor();
		editor.addCellEditorListener(new CellEditorListener() {
			@Override
			public void editingStopped(ChangeEvent e) {
				tableCellEditingStopped(e);
			}

			@Override
			public void editingCanceled(ChangeEvent e) {
				tableCellEditingCanceled(e);
			}

		});
		
		setBackground(UIConstants.NORMAL_BACKGROUND);
		setDefaultEditor(UITable.class, editor);
		setDefaultRenderer(UITable.class, new UITableRender());
		setUI(getUI());
		
		TableColumn deleteTableColumn = new TableColumn(getTableDataModel().getColumnCount());
		deleteTableColumn.setCellEditor(null);
		deleteTableColumn.setCellRenderer(null);
		deleteTableColumn.setMaxWidth(20);
		getColumnModel().addColumn(deleteTableColumn);
	}

	/**
	 * 鼠标悬浮再某一行时触发的事件
	 * @param index 行号
	 */
	public void dealWithRollOver(int index){

	}

    private void iniListener(){
        if(shouldResponseChangeListener()){
            this.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    if(uiObserverListener == null){
                        return ;
                    }
                    uiObserverListener.doChange();
                }
            });
        }
    }

	protected int getRowHeight4Table() {
		return DEFAULT_ROW_HEIGHT;
	}


	/**
	 *停止编辑事件
	 * @param e 事件
	 */
	public void tableCellEditingStopped(ChangeEvent e) {

	}

    /**
     *取消编辑事件
     * @param e 事件
     */
	public void tableCellEditingCanceled(ChangeEvent e) {

	}

	/**
	 * 编辑器
	 * @return 编辑器
	 */
	public UITableEditor createTableEditor() {
		return new UIDefaultTableCellEditor(new UITextField());
	}

	@Override
    /**
     *
     */
	public TableUI getUI() {
		return new UITableUI();
	}

	@Override
    /**
     *
     */
	public TableCellEditor getDefaultEditor(Class<?> columnClass) {
		return super.getDefaultEditor(UITable.class);
	}

	@Override
    /**
     *
     */
	public TableCellRenderer getDefaultRenderer(Class<?> columnClass) {
		return super.getDefaultRenderer(UITable.class);
	}

	/**
	 * 给组件登记一个观察者监听事件
	 *
	 * @param listener 观察者监听事件
	 */
    public void registerChangeListener(UIObserverListener listener) {
        uiObserverListener = listener;
    }

	/**
	 * 组件是否需要响应添加的观察者事件
	 *
	 * @return 如果需要响应观察者事件则返回true，否则返回false
	 */
    public boolean shouldResponseChangeListener() {
        return true;
    }

    protected class UITableRender implements TableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table,
													   Object value, boolean isSelected, boolean hasFocus, int row,
													   int column) {
			JComponent comp = getRenderCompoment(value, row, column);
			comp.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
			return comp;
		}
	}

	protected void fireTargetChanged() {
		repaint();
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeListener.class) {
				((ChangeListener) listeners[i + 1]).stateChanged(new ChangeEvent(this));
			}
		}
	}

	/**
	 * 增加监听
	 * @param l 监听
	 */
	public void addChangeListener(ChangeListener l) {
		this.listenerList.add(ChangeListener.class, l);
	}

    /**
     *移除监听
     * @param l 监听
     */
	public void removeChangeListener(ChangeListener l) {
		this.listenerList.remove(ChangeListener.class, l);
	}

    /**
     *测试主函数
     * @param args 参数
     */
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
		UITable pane = new UITable(2);
		pane.populateBean(data);
		content.add(pane, BorderLayout.CENTER);
		GUICoreUtils.centerWindow(jf);
		jf.setSize(400, 400);
		jf.setVisible(true);
	}
}