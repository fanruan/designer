package com.fr.design.chart.series.PlotSeries;

import com.fr.chart.base.MapSvgXMLHelper;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.constants.UIConstants;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.dialog.UIDialog;
import com.fr.design.event.ChangeEvent;
import com.fr.design.event.ChangeListener;
import com.fr.design.gui.frpane.UIExtensionPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UISearchTextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.general.GeneralUtils;

import com.fr.stable.ArrayUtils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 7.0.3
 * Date: 12-12-29
 * Time: 上午11:14
 * 可收缩的面板，只有两层
 */
public class UIGroupExtensionPane extends BasicPane {
	public static final String EDIT = "edit";
	public static final String DELETE = "delete";
	private static final int BUTTONWIDTH = 16;
	private static final int DIALOG_WIDTH = 140;
	private static final int DIALOG_HEIGHT = 100;

	private UISearchTextField searchTextFiled;
	private JList[] contentViews;
	private Component[][] components;
	private String[] titles;
	private java.util.List<ChangeListener> selectionListeners = new ArrayList<ChangeListener>();
	private java.util.List<ChangeListener> editListeners = new ArrayList<ChangeListener>();
	private List<ChangeListener> deleteListeners = new ArrayList<ChangeListener>();
	private boolean isPressOnDelete = false; // 是否点击在删除按钮上

	@Override
	protected String title4PopupWindow() {
		return "group";
	}

	public UIGroupExtensionPane(String[] titles) {
		this.titles = titles;
		if (ArrayUtils.isEmpty(titles)) {
			return;
		}
		initComponents(titles);
	}

	private void initComponents(String[] titles) {
		setBackground(null);
		searchTextFiled = initSearchTextField();
		int count = titles.length;
		contentViews = new JList[count];
		components = new Component[count + 1][];
		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] rowSize = new double[count + 1];
		double[] columnSize = {f};
		for (int i = 0; i < count + 1; i++) {
			rowSize[i] = p;
			if (i == 0) {
				components[i] = new Component[]{searchTextFiled};
			} else if (i > 0 && i < count + 1) {
				JList li = new JList(new DefaultListModel());
				li.addListSelectionListener(listSelectionListener);
				li.addMouseListener(mouseListener);
				li.setCellRenderer(listCellRenderer);

				li.setBackground(null);
				contentViews[i - 1] = li;
				components[i] = new UIExtensionPane[]{new UIExtensionPane(titles[i - 1], li, false)};
			}
		}

		JPanel centerPane = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, 0, 0);
		setLayout(new BorderLayout());
		add(new UIScrollPane(centerPane), BorderLayout.CENTER);
	}

	private UISearchTextField initSearchTextField() {
		UISearchTextField searchTextFiled = new UISearchTextField() {
			public Dimension getPreferredSize() {
				return new Dimension(120, 22);
			}
		};
		searchTextFiled.setIconPosition(SwingConstants.RIGHT);
		searchTextFiled.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				doFilter();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				doFilter();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				doFilter();
			}
		});
		return searchTextFiled;
	}

	/**
	 * 返回index列表的数据
	 */
	public Object[] getData(int index) {
		if (index < 0 || index > contentViews.length) {
			return ArrayUtils.EMPTY_OBJECT_ARRAY;
		}
		ListModel model = contentViews[index].getModel();
		Object[] items = new Object[model.getSize()];
		for (int i = 0, len = model.getSize(); i < len; i++) {
			items[i] = model.getElementAt(i);
		}
		return items;
	}

	/**
	 * 返回对应Title的列表数组
	 */
	public Object[] getData(String title) {
		int dataIndex = ArrayUtils.indexOf(titles, title);
		if (dataIndex != ArrayUtils.INDEX_NOT_FOUND) {
			return getData(dataIndex);
		}
		return ArrayUtils.EMPTY_OBJECT_ARRAY;
	}

	private void doFilter() {
		for (JList list : contentViews) {
			ListDataListener[] ls = ((DefaultListModel) list.getModel()).getListDataListeners();
			for (ListDataListener l : ls) {
				l.contentsChanged(new ListDataEvent(l, ListDataEvent.CONTENTS_CHANGED, 0, list.getModel().getSize()));
			}
		}
		
		for (int i = 1, len = components.length; i < len; i++) {
			((UIExtensionPane) components[i][0]).setExpand(true);
		}
	}

	/**
	 * 获取选中的值
	 *
	 * @return 选中的值
	 */
	public Object getSelectedObject() {
		for (JList list : contentViews) {
			if (list.getSelectedValue() != null) {
				return list.getSelectedValue();
			}
		}
		return null;
	}

    /**
     * 返回选中的类别
     * @return 类别
     */
    public String getSelectedType(){
        for (int i = 0, len = contentViews.length; i < len; i++) {
            if(contentViews[i].getSelectedValue() != null){
                return titles[i];
            }
        }
        return "";
    }

	/**
	 * 设置选中的数据
	 */
	public void setSelectedObject(Object value) {
		for (int i = 0, len = contentViews.length; i < len; i++) {
			UIExtensionPane extensionPane = (UIExtensionPane) components[i + 1][0];
			JList list = contentViews[i];
			DefaultListModel model = (DefaultListModel) list.getModel();
			extensionPane.setExpand(model.contains(value));
			if (model.contains(value)) {
				list.setSelectedValue(value, true);
			}
		}
	}

	/**
	 * 设置数据选中的序号.
	 */
	public void setValueAtCurrentSelectIndex(Object value) {
		for (JList list : contentViews) {
			if (list.getSelectedIndex() != -1) {
				((DefaultListModel) list.getModel()).setElementAt(value, list.getSelectedIndex());
			}
		}
	}

	/**
	 * 根据索引来添加数据
	 *
	 * @param data  要添加的数据
	 * @param index 要添加的数据向的索引
	 */
	public void addData(Object data, int index) {
		addData(data, index, false);
	}

	/**
	 * 根据索引来添加数据
	 *
	 * @param data        要添加的数据
	 * @param index       要添加的数据向的索引
	 * @param checkRepeat 是否检查名字重复
	 */
	public void addData(Object data, int index, boolean checkRepeat) {
		if (contentViews == null || index < 0 || index > contentViews.length - 1) {
			return;
		}
		JList list = contentViews[index];
		DefaultListModel model = (DefaultListModel) list.getModel();
		if (data instanceof String) {
			model.addElement(createUnrepeatedName(model, (String) data, checkRepeat));
		}
		if (checkRepeat) {
			// 将添加类型以外的其他类型都收起来
			for (int i = 1, len = components.length; i < len; i++) {
				((UIExtensionPane) components[i][0]).setExpand(false);
			}
			((UIExtensionPane) components[index + 1][0]).setExpand(true);
			int selectedIndex = list.getModel().getSize() - 1;
			list.setSelectedIndex(selectedIndex);
			dealNewAddedDataIndex(((DefaultListModel) list.getModel()).elementAt(selectedIndex));
		}
	}

	/**
	 * 新添加的数据的序号
	 * @param data 数据
	 */
	protected void dealNewAddedDataIndex(Object data){

	}

	/**
	 * 根据标题来添加数据
	 *
	 * @param data  要添加的数据
	 * @param title 要添加数据的项的标题文字
	 */
	public void addData(Object data, String title) {
		addData(data, title, false);
	}


	/**
	 * 根据标题来添加数据
	 *
	 * @param data        要添加的数据
	 * @param title       要添加数据的项的标题文字
	 * @param checkRepeat 是否检查名字重复
	 */
	public void addData(Object data, String title, boolean checkRepeat) {
		int addIndex = ArrayUtils.indexOf(titles, title);
		if (addIndex != ArrayUtils.INDEX_NOT_FOUND) {
			addData(data, addIndex, checkRepeat);
		}
	}

	/**
	 * 清除所有的数据
	 */
	public void clearData() {
		for (JList list : contentViews) {
			((DefaultListModel) list.getModel()).clear();
		}
	}


	private String createUnrepeatedName(DefaultListModel model, String name, boolean checkRepeat) {
		if (!checkRepeat) {
			return name;
		}
		int count = model.getSize();
		int extra = 1;
		String newName = name + (count + extra);
		boolean hasRepeated = false;
		do {
			hasRepeated = false;
			newName = name + (count + extra);
			for (int i = 0; i < count; i++) {
				if (ComparatorUtils.equals(model.getElementAt(i), newName)) {
					hasRepeated = true;
					extra++;
				}
			}
		} while (hasRepeated);

		return name + (count + extra);
	}

	/**
	 * 判断该控件是否应该有编辑操作
	 *
	 * @param list 列表组件
	 * @return 如果有编辑操作则需要显示相应的图标
	 */
	private boolean hasEditOperation(JList list) {
		return true;
	}

	private DefaultListCellRenderer listCellRenderer = new DefaultListCellRenderer() {
		public Component getListCellRendererComponent(
				JList list,
				Object value,
				int index,
				boolean isSelected,
				boolean cellHasFocus) {
			JComponent c = (JComponent) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			Border border = null;
			c.setBorder(border);
			UILabel editLabel = new UILabel(UIConstants.EDIT_ICON);
			UILabel deleteLabel = new UILabel(UIConstants.DELETE_ICON);

			JPanel editPane = GUICoreUtils.createFlowPane(new Component[]{editLabel, deleteLabel}, FlowLayout.LEFT, LayoutConstants.HGAP_LARGE);
			editPane.setBackground(isSelected ? c.getBackground() : null);
			editPane.setBorder(border);
			JPanel renderPane = GUICoreUtils.createBorderLayoutPane(c, BorderLayout.CENTER,
					editPane, BorderLayout.EAST);
			renderPane.setPreferredSize(new Dimension((int) renderPane.getPreferredSize().getWidth(), 20));
			if (shouldFilter(value)) {
				renderPane.setPreferredSize(new Dimension(0, 0));
			}
			return renderPane;
		}
	};

	//事件发生的顺序是ListSelection、MousePressed、ListSelection、MouseReleased
	private ListSelectionListener listSelectionListener = new ListSelectionListener() {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			//在鼠标按下的时候的值改变事件,重置属性
			if(e.getValueIsAdjusting()){
				isPressOnDelete = false;
			}

			if(!isRespondToValueChange(e)){
				return;
			}
			fireSelectionChangeListener(new ChangeEvent(e.getSource()));
			if (((JList) e.getSource()).getSelectedIndex() == -1) {
				return;
			}
			for (JList list : contentViews) {
				if (list.getSelectedIndex() != -1 && !ComparatorUtils.equals(list, e.getSource())) {
					try {
						list.setSelectedIndices(null);
					} catch (Exception ee) {

					}
				}
			}
		}
	};

	/**
	 * 是否响应list值改变
	 * @return 响应
	 */
	protected boolean isRespondToValueChange(ListSelectionEvent e){
		return true;
	}

	private boolean shouldFilter(Object value) {
		return !GeneralUtils.objectToString(value).toLowerCase().contains(searchTextFiled.getText().toLowerCase());
	}

	private MouseListener mouseListener = new MouseAdapter() {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void mousePressed(final MouseEvent e) {
			isPressOnDelete = false;
			final JList list = (JList) e.getSource();
			Point point = e.getPoint();
			final int index = list.locationToIndex(point);
			int width = list.getWidth();

			if (hasEditOperation(list)) {
				// 删除按钮
				if (point.x > width - (BUTTONWIDTH + LayoutConstants.HGAP_LARGE)) {
					BasicPane bp = new BasicPane() {
						protected String title4PopupWindow() {
							return "";
						}
					};
					isPressOnDelete = true;
					bp.setLayout(new BorderLayout());
					bp.add(new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Delete_Map") + "?", SwingConstants.CENTER));

                    clearLastListSelection(list);

					final String selectedType = UIGroupExtensionPane.this.getSelectedType();
                    UIDialog dialog = bp.showUnsizedWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {
						public void doOk() {
							Object name = getSelectedObject();
							((DefaultListModel) list.getModel()).removeElementAt(index);
                            MapSvgXMLHelper.getInstance().removeMapAttr(GeneralUtils.objectToString(name));
							MapSvgXMLHelper.getInstance().removeCateNames(selectedType,GeneralUtils.objectToString(name));
							fireDeleteListener(new ChangeEvent(e));
						}
					});
					
					dialog.setResizable(true);
					dialog.setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
					dialog.setResizable(false);
					GUICoreUtils.centerWindow(dialog);
					
					dialog.setVisible(true);
				}
				// 编辑按钮
				else if (point.x > width - (BUTTONWIDTH * 2 + 2 * LayoutConstants.HGAP_LARGE)
						&& point.x < width - (BUTTONWIDTH + LayoutConstants.HGAP_LARGE)) {
					fireItemEditListener(new ChangeEvent(e));
				}
			}
		}

		public void mouseReleased(MouseEvent e) {
			isPressOnDelete = false;
		}

	};

    /**
     * 直接跨list点击删除按钮，要置之前list的选中项为空（因为删除操作不触发更新）
     * @param currentList 当前选中的list
     */
    public void clearLastListSelection (JList currentList) {
        for (JList list : contentViews) {
            if (list.getSelectedIndex() != -1 && !ComparatorUtils.equals(list, currentList)) {
                try {
                    list.setSelectedIndices(null);
                } catch (Exception e) {
                    FineLoggerFactory.getLogger().error(e.getMessage(), e);
                }
            }
        }
    }

	/**
	 * 是否点击在删除按钮上
	 * @return 是则返回true
	 */
	public boolean isPressOnDelete(){
		return isPressOnDelete;
	}



	/**
	 * 添加选中变化监听事件
     * @param listener 监听
	 */
	public void addSelectionChangeListener(ChangeListener listener) {
		selectionListeners.add(listener);
	}

	private void fireSelectionChangeListener(ChangeEvent e) {
		for (int i = selectionListeners.size(); i > 0; i--) {
			selectionListeners.get(i - 1).fireChanged(e);
		}
	}

	/**
	 * 添加Item的监听事件
     * @param listener 监听器
	 */
	public void addItemEditListener(ChangeListener listener) {
		editListeners.add(listener);
	}

	private void fireItemEditListener(ChangeEvent e) {
		for (int i = editListeners.size(); i > 0; i--) {
			editListeners.get(i - 1).fireChanged(e);
		}
	}
	
	/**
	 * 添加删除事件的监听事件
     * @param listener 监听
	 */
	public void addDeleteListener(ChangeListener listener) {
		deleteListeners.add(listener);
	}
	
	private void fireDeleteListener(ChangeEvent e) {
		for(int i = deleteListeners.size(); i > 0; i--) {
			deleteListeners.get(i - 1).fireChanged(e);
		}
	}

    public void setEnabled(boolean isEnabled){
        super.setEnabled(isEnabled);

        if(searchTextFiled != null){
            searchTextFiled.setEnabled(isEnabled);
        }

        if(this.components != null){
            for(int i = 0; i < this.components.length; i++){
                Component[] comp = this.components[i];
                for(int j = 0; j < comp.length; j++){
                    comp[j].setEnabled(isEnabled);
                }
            }
        }
    }

	/**
	 * 测试程序
     * @param args 参数
	 */
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = f.getContentPane();
		c.setBackground(Color.WHITE);
		c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
		final UIGroupExtensionPane g = new UIGroupExtensionPane(new String[]{"title1", "title2", "title3"});
		c.add(g, BorderLayout.CENTER);
		JPanel pp = new JPanel(new FlowLayout());
		c.add(pp, BorderLayout.SOUTH);
		UIButton test = new UIButton("add1");

		test.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				g.addData("test111", 0);
			}
		});
		pp.add(test);
		UIButton test2 = new UIButton("add2");
		test2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				g.addData("test222", 1);
			}
		});
		pp.add(test2);
		f.setSize(360, 500);
		f.setLocation(200, 100);
		f.setVisible(true);
	}
}