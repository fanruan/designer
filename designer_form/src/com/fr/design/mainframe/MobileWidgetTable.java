package com.fr.design.mainframe;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.*;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itable.GroupRenderer;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.*;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

/**
 * MobileWidgetTable类主要显示各种容器的控件列表（body，tab，绝对布局快，不包括参数面板）
 * Created with IntelliJ IDEA.
 * User: zx
 * Date: 14-9-15
 * Time: 下午4:52
 * Modified by fanglei at 2017/01/23
 */
public class MobileWidgetTable extends JTable {

    private FormDesigner designer;
    private String[][] cellData;
    private String[] headers = {Inter.getLocText("Form-Widget_Name")};
    private static final int WIDGET_TABLE_ROW_HEIGHT = 22;
    private UILabel moveComponent = new UILabel(); // 作为拖动时候随鼠标移动的那个半透明控件
    private int selectedRow = -1;
    private static final int GAP = 11;
    private boolean draging = false;
    private boolean collapsed = false; // 控件列表是否折叠

    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        //第一行渲染成为标题的样子
        if (row == 0) {
            return new GroupRenderer();
        }
        return super.getCellRenderer(row, column);
    }


    public MobileWidgetTable(FormDesigner designer) {
        this.designer = designer;
        cellData = getData();
        this.setTableProperties();
        TableModel defaultModel = new BeanTableModel();
        this.setModel(defaultModel);
        this.repaint();
        this.setDefaultRenderer(Object.class, new DefaultTableCellRenderer());
        refreshData();
        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);
        add(moveComponent);
    }

    private void setTableProperties() {
        JTableHeader header = getTableHeader();
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(0, 0)); // 隐藏表头
        GroupRenderer headerRenderer = new GroupRenderer();
        headerRenderer.setPreferredSize(new Dimension(0, 0)); //这行代码隐藏表头。因为要实现折叠效果，表头不好监听事件
        headerRenderer.setHorizontalAlignment(JLabel.LEFT);
        header.setDefaultRenderer(headerRenderer);

        this.setRowHeight(WIDGET_TABLE_ROW_HEIGHT);
        this.setGridColor(new Color(212, 208, 200));
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.setColumnSelectionAllowed(false);
        this.setRowSelectionAllowed(false);
        this.setFillsViewportHeight(false);
        this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    private MouseAdapter mouseAdapter = new MouseAdapter() {
        /**
         * 鼠标按下时处理的事件（设置当前选中的行列）
         * @param e
         */
        @Override
        public void mousePressed(MouseEvent e) {
            getInstance().setCellSelected();
            if (selectedRow == 0 && !e.isPopupTrigger() && e.getClickCount() == 1 && e.getX() < WIDGET_TABLE_ROW_HEIGHT / 2) { // 如果是点击在第一行
                toggleCollapse();
            }
        }

        /**
         * 鼠标放开时处理的事件（如果是正在拖动则执行换位操作，重新绘制属性表，如果不是则什么也不做）
         * 所谓的换行就是简单的重新拿到一次表格数据然后重新绘制表格
         * @param e
         */
        @Override
        public void mouseReleased(MouseEvent e) {
            if (!draging) {
                return;
            }
            draging = false;
            moveComponent.setVisible(false);
            int toIndex = e.getY() < GAP ? 0 : (int) Math.rint((e.getY() - GAP) / WIDGET_TABLE_ROW_HEIGHT) + 1;
            //当鼠标放开时，将选中的容器调整至新的顺序
            ((WSortLayout) designer.getSelectionModel().getSelection().getSelectedCreator().toData()).adjustOrder(selectedRow - 1, toIndex - 1);
            //拿取排序后表格数据，然后重绘表格
            getInstance().refreshData();
            getInstance().repaint();
            designer.fireTargetModified();
            getInstance().setCellSelected();
        }

        /**
         * 设置鼠标在属性表区域移动时候的事件
         * @param e
         */
        @Override
        public void mouseMoved(MouseEvent e) {
            int overRow = 0;
            for (int i = 0; i < getRowCount(); i++) {
                if (e.getY() > i * WIDGET_TABLE_ROW_HEIGHT && e.getY() <= (i + 1) * WIDGET_TABLE_ROW_HEIGHT) {
                    overRow = i; //判断鼠标在哪一行
                }
            }
            //如果鼠标移动到当前选中的行上面的时候
            if (overRow == selectedRow && selectedRow > 0) {
                //把当前选中的那一行的光标改成(除了第一列)移动样式MOVE_CURSOR
                setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            } else {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        }

        /**
         * 鼠标拖动事件（如果鼠标当前是<code>MOVE_CURSOR</code>状态则执行开始拖动的代码，
         * 绘制一个<code>moveComponent</code>来跟随鼠标移动）
         * @param e
         */
        @Override
        public void mouseDragged(MouseEvent e) {
            int width = getColumnModel().getColumn(0).getWidth();
            //如果当前选中的行的范围是合理的话，就可以拖动
            if (selectedRow < getRowCount() && selectedRow > 0) {
                setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                draging = true;
                moveComponent.setText(getValueAt(selectedRow, 0).toString());
                moveComponent.setLocation(0, e.getY() - GAP);
                moveComponent.setSize(new Dimension(width, WIDGET_TABLE_ROW_HEIGHT));
                moveComponent.setVisible(true);
                moveComponent.setForeground(Color.lightGray);
                moveComponent.setBorder(BorderFactory.createLineBorder(Color.lightGray));
            }
        }

        /**
         * 设置鼠标单击时处理的事件（单击控件列表进入控件属性表）
         * @param e
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            if (selectedRow > 0) {
                //当前点击的控件的名字
                String widgetName = cellData[selectedRow][0];
                if (StringUtils.isNotEmpty(widgetName)) {
                    //当前选择的容器
                    XCreator selectedContainer = designer.getSelectionModel().getSelection().getSelectedCreator();
                    WLayout selectedWidget = (WLayout) selectedContainer.toData();
                    //当前选择的容器中的控件数量
                    int count = selectedWidget.getWidgetCount();
                    for (int i = 0; i < count; i++) {
                        XCreator xCreator = (XCreator) selectedContainer.getComponent(i);
                        Widget widget = xCreator.toData();
                        if (ComparatorUtils.equals(widgetName, widget.getWidgetName())) {
                            getEditingDesigner().getSelectionModel().setSelectedCreator(xCreator);
                        }
                    }
                }
            } else if (selectedRow == 0) { // 如果是点击在第一行
                if (!e.isPopupTrigger() && e.getClickCount() > 1) {
                    toggleCollapse();
                }
            }
        }

        /**
         * 鼠标离开属性表区域事件
         * @param e
         */
        public void mouseExited(MouseEvent e) {
            draging = false;
            moveComponent.setVisible(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    };

    public MobileWidgetTable getInstance() {
        return this;
    }

    public FormDesigner getEditingDesigner() {
        return designer;
    }

    /**
     * 设置当前get到的行列的单元格为选中状态
     */
    private void setCellSelected() {
        selectedRow = getSelectedRow();
        if (selectedRow != -1) {
            this.setRowSelectionInterval(selectedRow, selectedRow);
            this.setColumnSelectionInterval(0, 0);
        }
    }

    /**
     * 切换属性组折叠属性true/false
     */
    private void toggleCollapse() {
        this.setCollapsed(!this.isCollapsed());
        //这里获取表格的父控件是为了当表格被折叠了后，装表格的父控件也要相应的重新布局一下
        //比如折叠之后表格行数应该比原来的少，占用父容器空间应该小点，不重新布局父容器，表格大小不会改变
        Container parent = MobileWidgetTable.this.getParent();
        if (parent != null) {
            parent.revalidate();
        }
        repaint();
    }

    /**
     * 重新get排序后的数据
     */
    public void refreshData() {
        cellData = getData();
    }

    /**
     * 获取选中控件的控件列表
     *
     * @return String[][] 二维数组，[0][0]widgetName
     */
    private String[][] getData() {
        if (designer.isFormParaDesigner()) {
            return new String[0][0];
        }

        //选择的控件
        XCreator selectedCreator = designer.getSelectionModel().getSelection().getSelectedCreator();
        Widget selectedModel = selectedCreator != null ? selectedCreator.toData() : null;

        if (selectedModel == null) {
            return new String[0][0];
        }

        // 选择的控件有两种类型，一种是WLayout，代表容器，一种是Widget，代表控件
        if (selectedModel.acceptType(WSortLayout.class)) {
            List<String> mobileWidgetList = ((WSortLayout) selectedModel).getOrderedMobileWidgetList();
            String[][] widgetName = new String[mobileWidgetList.size() + 1][1];
            widgetName[0][0] = Inter.getLocText("FR-Designer_WidgetOrder");
            for (int i = 0; i < mobileWidgetList.size(); i++) {
                widgetName[i + 1][0] = mobileWidgetList.get(i);
            }
            return widgetName;
        } else {
            return new String[0][0];
        }
    }

    public boolean isCollapsed() {
        return collapsed;
    }

    public void setCollapsed(boolean collapsed) {
        this.collapsed = collapsed;
    }

    /**
     * 自定义的tableEditor类
     */
    public class BeanTableModel extends DefaultTableModel {
        public BeanTableModel() {
            super(cellData, headers);
        }

        @Override
        public int getRowCount() {
            if (isCollapsed()) {
                return 1;
            }
            return cellData.length;
        }

        @Override
        public int getColumnCount() {
            return 1;
        }


        @Override
        public Object getValueAt(int row, int column) {
            if (row >= getRowCount() || column >= getColumnCount()) {
                return null;
            }
            if (row == 0) {
                return (isCollapsed() ? "+" : "-") + cellData[row][0];
            }

            return cellData[row][0];
        }

        @Override
        public String getColumnName(int column) {
            return headers[0];
        }


        @Override
        public void setValueAt(Object aValue, int row, int column) {
            if (row >= getRowCount() || column >= getColumnCount()) {
                return;
            }
            if (aValue == null) {
                cellData[row] = null;
                return;
            }
            cellData[row][0] = aValue.toString();
        }

        /**
         * 是否可编辑
         *
         * @param row    行号
         * @param column 列号
         * @return 是否可编辑
         */
        public boolean isCellEditable(int row, int column) {
            return false;
        }

    }

}