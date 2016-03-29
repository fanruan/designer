/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.gui.itable;

import java.awt.*;
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
import javax.swing.table.TableModel;

import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;

/**
 * @author richer
 * @since 6.5.3
 */
public abstract class AbstractPropertyTable extends JTable {

    // 属性分组
    protected ArrayList<PropertyGroup> groups;
    protected TableModel default_table_model;
	// 属性表被选中的行加一个浅蓝色的背景
	public static final Color PROPERTY_SELECTION_BACKGROUND = new Color(153, 204, 255);
	// 属性表的行高
	public static final int PROPERTY_TABLE_ROW_HEIGHT = 22;

    public AbstractPropertyTable() {
        this.setRowHeight(PROPERTY_TABLE_ROW_HEIGHT);
        JTableHeader header = getTableHeader();
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(0, PROPERTY_TABLE_ROW_HEIGHT));
        header.setDefaultRenderer(new HeaderRenderer());
        this.setGridColor(new Color(212, 208, 200));
        this.setSelectionBackground(PROPERTY_SELECTION_BACKGROUND);
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.setColumnSelectionAllowed(false);
        this.setRowSelectionAllowed(true);
        this.setFillsViewportHeight(true);
        this.initPopup();
        default_table_model = new DefaultTableModel();
        this.setModel(default_table_model);
        this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    public abstract void initPropertyGroups(Object source);

    public void fireValueChanged(Object old_value, boolean success, Object newValue) {
        if (success && !ComparatorUtils.equals(old_value, newValue)) {
            firePropertyEdit();
        }
    }

    public abstract void firePropertyEdit();

    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        if (groups != null) {
            Point pIndex = getGroupIndex(row);
            PropertyGroup group = groups.get(pIndex.x);
            if (pIndex.y == 0) {
                if (column == 0) {
                    return group.getFirstRenderer();
                } else {
                    return group.getSecondRenderer();
                }
            } else {
                if (column == 0) {
                	return super.getCellRenderer(row, column);
				} else {
					TableCellRenderer renderer = group.getModel().getRenderer(pIndex.y - 1);
					if (renderer instanceof Component) {
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
        if (groups != null) {
            Point pIndex = getGroupIndex(row);
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

            @Override
            public void mouseClicked(MouseEvent e) {
                if (!e.isPopupTrigger() && groups != null) {
                    int row = AbstractPropertyTable.super.rowAtPoint(e.getPoint());
                    if (row != -1) {
                        Point pIndex = getGroupIndex(row);
                        if (pIndex.y == 0 && e.getClickCount() > 1) {
                            toggleCollapse(pIndex.x);
                        }
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (!e.isPopupTrigger() && groups != null) {
                    int row = AbstractPropertyTable.super.rowAtPoint(e.getPoint());
                    if (row != -1) {
                        Point pIndex = getGroupIndex(row);
                        if (pIndex.y == 0 && e.getClickCount() == 1 && e.getX() < 10) {
                            toggleCollapse(pIndex.x);
                        }
                    }
                }
            }
        });
    }

    private void toggleCollapse(int groupIndex) {
        PropertyGroup group = groups.get(groupIndex);
        group.setCollapsed(!group.isCollapsed());
        Container parent = AbstractPropertyTable.this.getParent();
        if (parent != null) {
            parent.doLayout();
        }
        repaint();
    }

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
                return Inter.getLocText("Form-Widget_Property");
            } else {
                return Inter.getLocText("Form-Widget_Property_Value");
            }
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            Point pIndex = getGroupIndex(row);
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
            PropertyGroup group = groups.get(pIndex.x);
            if (pIndex.y == 0) {
                if (column == 0) {
                    return false;
                } else {
                    return false;
                }
            } else {
                return column == 1 && group.getModel().isEditable(pIndex.y - 1);
            }
        }
    }
}