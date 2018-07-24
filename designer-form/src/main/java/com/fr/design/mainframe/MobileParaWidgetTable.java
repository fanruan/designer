package com.fr.design.mainframe;


import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWParameterLayout;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itable.HeaderRenderer;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.form.ui.Label;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WParameterLayout;
import com.fr.general.ComparatorUtils;

import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.EventObject;

/**
 * MobileParaWidgetTable主要显示参数面板容器的控件列表，与MobileWidgetTable的区别就是该表多了UITextField这一列
 * Created with IntelliJ IDEA.
 * User: zx
 * Date: 14-7-9
 * Time: 上午11:26
 * Modified by fanglei during 2017/1/22 - 2017/2/
 */
class MobileParaWidgetTable extends JTable {

    private FormDesigner designer;
    private String[][] cellData;
    private String[] headers = {com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Label"), com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Widgetname")};
    private static final int WIDGET_TABLE_ROW_HEIGHT = 22;
    private static final int UITEXTFIELD_WIDTH = 0;
    private static final int GAP = 11;
    private UILabel moveComponent = new UILabel(); // 作为拖动时候随鼠标移动的那个半透明控件
    private int selectedRow = -1;
    private int selectedColumn = -1;
    private boolean draging = false;

    private void init(FormDesigner designer) {
        this.designer = designer;
        this.cellData = getData();
        this.setTableProperties();
        this.setDefaultEditor(Object.class, new MobileCellEditor());
        this.setModel(new BeanTableModel());
        this.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        TableColumn tc = this.getColumn(this.getColumnName(0));
        tc.setMinWidth(UITEXTFIELD_WIDTH);
        tc.setMaxWidth(UITEXTFIELD_WIDTH);
        tc.setPreferredWidth(UITEXTFIELD_WIDTH);
        this.repaint();
        this.setDefaultRenderer(Object.class, new MobileWidgetTableCellRenderer());
        refreshData();
        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);
    }

    MobileParaWidgetTable(FormDesigner designer) {
        init(designer);
        add(moveComponent);
    }

    private void setTableProperties() {
        JTableHeader header = getTableHeader();
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(0, WIDGET_TABLE_ROW_HEIGHT));
        header.setDefaultRenderer(new HeaderRenderer());

        this.setRowHeight(WIDGET_TABLE_ROW_HEIGHT);
        this.setGridColor(new Color(212, 208, 200));
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.setColumnSelectionAllowed(false);
        this.setRowSelectionAllowed(false);
        this.setFillsViewportHeight(true);
    }

    private MouseAdapter mouseAdapter = new MouseAdapter() {
        /**
         * 鼠标按下时处理的事件（设置当前选中的行列）
         * @param e
         */
        @Override
        public void mousePressed(MouseEvent e) {
            getInstance().setCellSelected();
        }

        /**
         * 鼠标放开时处理的事件（如果是正在拖动则执行换位操作，重新绘制属性表，如果不是则什么也不做）
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
            ((WParameterLayout) designer.getParaComponent().toData()).adjustOrder(selectedRow, toIndex);
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
            int overColumn = e.getX() < getColumnModel().getColumn(0).getWidth() ? 0 : 1; // 判断当前鼠标在哪一列
            int overRow = -1;
            for (int i = 0; i < getRowCount(); i++) {
                if (e.getY() > i * WIDGET_TABLE_ROW_HEIGHT && e.getY() <= (i + 1) * WIDGET_TABLE_ROW_HEIGHT) {
                    overRow = i; //判断当前鼠标在哪一行
                }
            }
            //如果鼠标移动到当前选中的行列上面的时候，并且不能在第一列
            if (overRow == selectedRow && overColumn == selectedColumn && overColumn != 0) {
                //把当前选中的那一列行的光标改成(除了第一列)移动样式MOVE_CURSOR
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
            int width = getColumnModel().getColumn(1).getWidth();
            //如果点击选中的是第二列，就可以拖动
            if (selectedColumn == 1) {
                setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                draging = true;
                moveComponent.setText(getValueAt(selectedRow, selectedColumn).toString());
                moveComponent.setLocation(getColumnModel().getColumn(0).getWidth(), e.getY() - GAP);
                moveComponent.setSize(new Dimension(width, WIDGET_TABLE_ROW_HEIGHT));
                moveComponent.setVisible(true);
                moveComponent.setForeground(Color.lightGray);
                moveComponent.setBorder(BorderFactory.createLineBorder(Color.lightGray));
            }
        }

        /**
         * 设置鼠标单击时处理的事件（单击第二列的控件列表进入控件属性表）
         * @param e
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            if (getSelectedRow() != -1 && getSelectedColumn() == 1) {
                String widgetName = cellData[getSelectedRow()][getSelectedColumn()];
                if (StringUtils.isNotEmpty(widgetName)) {
                    int count = getEditingDesigner().getParaComponent().getComponentCount();
                    for (int i = 0; i < count; i++) {
                        XCreator xCreator = (XCreator) getEditingDesigner().getParaComponent().getComponent(i);
                        Widget widget = xCreator.toData();
                        if (!widget.acceptType(Label.class) && ComparatorUtils.equals(widgetName, widget.getWidgetName())) {
                            //设置选中的component，这句代码控制点击之后跳转到相应component属性表
                            getEditingDesigner().getSelectionModel().setSelectedCreator(xCreator);
                        }
                    }
                }
            }
        }

        /**
         * 鼠标离开属性表区域事件
         * @param e
         */
        @Override
        public void mouseExited(MouseEvent e) {
            draging = false;
            moveComponent.setVisible(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    };

    public MobileParaWidgetTable getInstance() {
        return this;
    }

    private FormDesigner getEditingDesigner() {
        return designer;
    }

    /**
     * 设置当前get到的行列的单元格为选中状态
     */
    private void setCellSelected() {
        selectedRow = getSelectedRow();
        selectedColumn = getSelectedColumn();
        if (selectedRow != -1) {
            this.setRowSelectionInterval(selectedRow, selectedRow);
        }
        if (selectedColumn != -1) {
            this.setColumnSelectionInterval(selectedColumn, selectedColumn);
        }
    }

    /**
     * 重新get排序后的数据
     */
    public void refreshData() {
        cellData = getData();
    }

    /**
     * 获取参数面板的控件列表
     *
     * @return String[][] 二维数组，[0][0]widgetTag, [0][1]widgetName
     */
    private String[][] getData() {
        XLayoutContainer paraContainer = designer.getParaComponent();
        if (paraContainer == null || !paraContainer.acceptType(XWParameterLayout.class)) {
            return new String[0][0];
        }

        WParameterLayout para = (WParameterLayout) (paraContainer.toData());
        return para.getWidgetNameTag();
    }

    /**
     * 自定义的tableRender类
     */
    private class MobileWidgetTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (column == 0) {
                UITextField uiTableTextField;
                if (getSelectedColumn() == column && getSelectedRow() == row) {
                    uiTableTextField = new UITableTextField(value.toString());
                } else {
                    uiTableTextField = new UITextField(value.toString());
                }
                return uiTableTextField;
            }
            return this;
        }

    }

    /**
     * 自定义的tableEditor类
     */
    private class MobileCellEditor extends AbstractCellEditor implements TableCellEditor {
        UITableTextField uiTableTextField;

        MobileCellEditor() {
            uiTableTextField = new UITableTextField();
            uiTableTextField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent evt) {
                    stopCellEditing();
                    designer.fireTargetModified();
                }
            });
            uiTableTextField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    firePropertyChange();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    firePropertyChange();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    firePropertyChange();
                }
            });
        }

        /**
         * cell改变，相应的nametag改变
         */

        private void firePropertyChange() {
            ((WParameterLayout) designer.getParaComponent().toData()).add2NameTagMap(uiTableTextField.getText(),
                    cellData[getSelectedRow()][1]);
            ((WParameterLayout) designer.getParaComponent().toData()).setNameTagModified(cellData[getSelectedRow()][1],
                    true);
        }

        @Override
        public Object getCellEditorValue() {
            return uiTableTextField.getText();
        }

        /**
         * 双击以编辑, 表示只有双击的情况下才可以编辑
         */
        @Override
        public boolean isCellEditable(EventObject anEvent) {
            return !(anEvent instanceof MouseEvent) || ((MouseEvent) anEvent).getClickCount() >= 2;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            uiTableTextField.setText(value.toString());
            return uiTableTextField;
        }
    }

    /**
     * BeanTableModel类继承DefaultTableModel类，指定了表格的表头和内容
     */
    private class BeanTableModel extends DefaultTableModel {
        BeanTableModel() {
            super(cellData, headers);
        }

        @Override
        public int getRowCount() {
            return cellData.length;
        }

        @Override
        public int getColumnCount() {
            return headers.length;
        }


        @Override
        public Object getValueAt(int row, int column) {
            if (row >= getRowCount() || column >= getColumnCount()) {
                return null;
            }
            Object[] rowValue = cellData[row];
            if (column > -1 && column < rowValue.length) {
                return cellData[row][column];
            }
            return null;
        }

        @Override
        public String getColumnName(int column) {
            if (column == 0) {
                return headers[0];
            } else {
                return headers[1];
            }
        }


        @Override
        public void setValueAt(Object aValue, int row, int column) {
            if (row >= getRowCount() || column >= getColumnCount()) {
                return;
            }
            if (aValue == null) {
                cellData[row][column] = null;
                return;
            }
            cellData[row][column] = aValue.toString();
        }

        /**
         * 是否可编辑 控件标签列可以编辑，控件名不可编辑
         *
         * @param row    行号
         * @param column 列号
         * @return 是否可编辑
         */
        public boolean isCellEditable(int row, int column) {
            return column != 1;
        }

    }

    /**
     * 继承自JTextField类，重写了编辑框的样式
     */
    private class UITableTextField extends UITextField {
        public UITableTextField() {
            super();
        }

        public UITableTextField(String string) {
            super(string);
        }

        protected void paintBorder(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (this.isFocusOwner()) {
                g2d.setStroke(new BasicStroke(1.5f));
            } else {
                g2d.setStroke(new BasicStroke(1f));
            }
            RoundRectangle2D.Double rect = new RoundRectangle2D.Double(0, 0, this.getWidth() - 2, this.getHeight() - 2, 4, 4);
            g2d.setColor(Color.orange);
            g2d.draw(rect);
        }

    }
}