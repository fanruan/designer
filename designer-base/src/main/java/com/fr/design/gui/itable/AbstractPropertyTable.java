/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.gui.itable;

import com.fr.general.ComparatorUtils;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * @author richer
 * @since 6.5.3
 */
public abstract class AbstractPropertyTable extends JTable {

    // // 所有数据组, 把数据分组，一个可折叠的项里面的所有行 为一组
    protected ArrayList<PropertyGroup> groups;
	// 属性表被选中的行加一个浅蓝色的背景
	public static final Color PROPERTY_SELECTION_BACKGROUND = new Color(153, 204, 255);
	// 属性表的行高
	public static final int PROPERTY_TABLE_ROW_HEIGHT = 22;
    private static final int PROPERTY_ICON_WIDTH = 10;

    public AbstractPropertyTable() {
        this.setTableProperties();
        this.initPopup();
        this.setModel(new DefaultTableModel());
    }

    private void setTableProperties() {
        JTableHeader header = getTableHeader();
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(0, PROPERTY_TABLE_ROW_HEIGHT));
        header.setDefaultRenderer(new HeaderRenderer());
        this.setRowHeight(PROPERTY_TABLE_ROW_HEIGHT);
        this.setGridColor(new Color(212, 208, 200));
        this.setSelectionBackground(PROPERTY_SELECTION_BACKGROUND);
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.setColumnSelectionAllowed(false);
        this.setRowSelectionAllowed(true);
        this.setFillsViewportHeight(true);
        this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    /**
     * 在这个函数里面初始化表格数据再repaint
     * @param source
     */
    public abstract void initPropertyGroups(Object source);

    public void fireValueChanged(Object old_value, boolean success, Object newValue) {
        if (success && !ComparatorUtils.equals(old_value, newValue)) {
            firePropertyEdit();
        }
    }

    public abstract void firePropertyEdit();

    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        //如果数据组不为空
        if (groups != null) {
            Point pIndex = getGroupIndex(row);
            if (pIndex == null){
                return super.getCellRenderer(row, column);
            }
            //拿出当前行所在的那个属性组
            PropertyGroup group = groups.get(pIndex.x);
            //如果是标题行
            if (pIndex.y == 0) {
                if (column == 0) {
                    return group.getFirstRenderer();
                } else {
                    return group.getSecondRenderer();
                }
            } else {
                //如果是非标题行第一列，采用默认渲染器
                if (column == 0) {
                	return super.getCellRenderer(row, column);
				} else {
					TableCellRenderer renderer = group.getModel().getRenderer(pIndex.y - 1);
					if (renderer instanceof Component) {
                        //如果这个渲染器是继承自Component，根据当前行列是否可编辑决定该控件是否可用
						((Component) renderer).setEnabled(isCellEditable(row, column));
					}
					return renderer;
				}
            }
        } else {
            return super.getCellRenderer(row, column);
        }
    }

	public String getToolTipText(MouseEvent event) {
		return null;
	}

    @Override
    public TableCellEditor getCellEditor(int row, int column) {
        Point pIndex = getGroupIndex(row);
        if (groups != null && pIndex != null) {
            PropertyGroup group = groups.get(pIndex.x);
            if (pIndex.y == 0) {
                return super.getCellEditor(row, column);
            } else {
                if (column == 0) {
                    return super.getCellEditor(row, column);
                } else {
                    return group.getModel().getEditor(pIndex.y - 1);
                }
            }
        } else {
            return super.getCellEditor(row, column);
        }
    }

    /**
     * 看懂了。。写个注释先
     * 获取当前row行的Point(x, y), x代表当前row行是属于groups中的第x个组，y代表当前row行所在的第x组里面的第y行
     * @param row
     * @return
     */
    private Point getGroupIndex(int row) {
        int count = 0;
        for (int i = 0; i < groups.size(); i++) {
            PropertyGroup group = groups.get(i);
            int groupRowCount = 1;
            if (!group.isCollapsed()) {
                groupRowCount += group.getModel().getRowCount();
            }
            if (count + groupRowCount > row) {
                return new Point(i, row - count);
            }
            count += groupRowCount;
        }
        return null;
    }

    private void initPopup() {

        this.addMouseListener(new MouseAdapter() {

            /**
             * 如果点到标题行就要触发折叠事件
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!e.isPopupTrigger() && groups != null) {
                    int row = AbstractPropertyTable.super.rowAtPoint(e.getPoint());
                    if (row != -1) {
                        Point pIndex = getGroupIndex(row);
                        if (pIndex != null && pIndex.y == 0 && e.getClickCount() > 1) {
                            toggleCollapse(pIndex.x);
                        }
                    }
                }
            }

            /**
             * 这个mousePressed和上面的mouseClicked唯一不同的地方是单双击和e.getX() < 10 的判断
             * 这个意思应该就是说点到图标（加号减号），立即触发折叠效果，否则点其他处要双击才能触发
             * @param e
             */
            @Override
            public void mousePressed(MouseEvent e) {
                if (!e.isPopupTrigger() && groups != null) {
                    int row = AbstractPropertyTable.super.rowAtPoint(e.getPoint());
                    if (row != -1) {
                        Point pIndex = getGroupIndex(row);
                        if (pIndex != null && pIndex.y == 0 && e.getClickCount() == 1 && e.getX() < PROPERTY_ICON_WIDTH) {
                            toggleCollapse(pIndex.x);
                        }
                    }
                }
            }
        });
    }

    /**
     * 切换属性组折叠属性true/false
     * @param groupIndex
     */
    private void toggleCollapse(int groupIndex) {
        PropertyGroup group = groups.get(groupIndex);
        group.setCollapsed(!group.isCollapsed());
        //这里获取表格的父控件是为了当表格被折叠了后，装表格的父控件也要相应的重新布局一下
        //比如折叠之后表格行数应该比原来的少，占用父容器空间应该小点，不重新布局父容器，表格大小不会改变
        Container parent = AbstractPropertyTable.this.getParent();
        if (parent != null) {
//            parent.doLayout(); // 这里还是用revalidate吧。。daLayout有时候会失效不知道为什么
            parent.revalidate();
        }
        repaint();
    }

    /**
     * BeanTableModel类，提供表格数据
     * 它的所有数据来源均来自PropertyGroup中的AbstractPropertyGroupModel中的descriptor
     */
    public class BeanTableModel extends AbstractTableModel {

        @Override
        public int getRowCount() {
            int row = 0;
            for (PropertyGroup group : groups) {
                row++;
                if (!group.isCollapsed()) {
                    row += group.getModel().getRowCount();
                }
            }
            return row;
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

		@Override
		public Object getValueAt(int row, int column) {
			Point pIndex = getGroupIndex(row);
            if (pIndex == null){
                return null;
            }
			PropertyGroup group = groups.get(pIndex.x);
			if (pIndex.y == 0) {
				if (column == 0) {
					return (group.isCollapsed() ? "+" : "-") + group.getName();
				} else {
					return null;
				}
			} else {
				Object value = group.getModel().getValue(pIndex.y - 1, column);
				if (column == 0) {
					value = "  " + value;
				}
				return value;
			}
		}

        @Override
        public String getColumnName(int column) {
            if (column == 0) {
                return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Widget_Property");
            } else {
                return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Widget_Property_Value");
            }
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            Point pIndex = getGroupIndex(row);
            if (pIndex == null) {
                return;
            }
            PropertyGroup group = groups.get(pIndex.x);
            if (pIndex.y != 0) {
                Object old_value = getValueAt(row, column);
                boolean success = group.getModel().setValue(aValue, pIndex.y - 1, column);
                fireValueChanged(old_value, success, aValue);
            } else {
                group.setCollapsed((Boolean) aValue);
            }
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            Point pIndex = getGroupIndex(row);
            if (pIndex == null) {
                return false;
            }
            PropertyGroup group = groups.get(pIndex.x);
            return pIndex.y != 0 && (column == 1 && group.getModel().isEditable(pIndex.y - 1));
        }
    }
}
