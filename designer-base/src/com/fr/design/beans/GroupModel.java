package com.fr.design.beans;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * richer:属性和事件分组
 * @since 6.5.3
 */
public interface GroupModel {
    /**
     * 该属性所属的分类,普通属性分为基本属性和其它,事件属性根据事件名称不同进行分类
     */
    String getGroupName();

    /**
     * 总共的属性行数
     */
    int getRowCount();
    
    /**
     * 获取属性表中第row行的渲染器
     * @param row
     */
    TableCellRenderer getRenderer(int row);

    /**
     * 获取属性表中第row行的编辑器
     * @param row
     */
    TableCellEditor getEditor(int row);

    /**
     * 获取属性表中第row行第column列的值
     * @param row
     * @param column
     * @return 表格中的值
     */
    Object getValue(int row, int column);

    /**
     * 设置属性表中第row行第column列的值为value
     * @param value
     * @param row
     * @param column
     * @return 设置成功则返回true，否则返回false
     */
    boolean setValue(Object value, int row, int column);

    /**
     * 改行是否可编辑
     * @param row
     * @return 第row行可编辑返回true，否则返回false
     */
    boolean isEditable(int row);
}