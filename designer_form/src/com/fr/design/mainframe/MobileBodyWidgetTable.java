package com.fr.design.mainframe;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itable.HeaderRenderer;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WFitLayout;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: zx
 * Date: 14-9-15
 * Time: 下午4:52
 */
public class MobileBodyWidgetTable extends JTable {

    private FormDesigner designer;
    protected TableModel defaultmodel;
    private String[][] cellData;
    private String[] headers = {Inter.getLocText("Form-Widget_Name")};
    public static final int WIDGET_TABLE_ROW_HEIGHT = 22;
    private UILabel moveComponent = new UILabel();
    private int selectedRow = -1;
    private int GAP = 10;
    private boolean draging = false;


    public MobileBodyWidgetTable(FormDesigner designer) {
        this.designer = designer;
        cellData = getData();
        this.setRowHeight(WIDGET_TABLE_ROW_HEIGHT);
        JTableHeader header = getTableHeader();
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(0, WIDGET_TABLE_ROW_HEIGHT));
        HeaderRenderer headerRenderer = new HeaderRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        header.setDefaultRenderer(headerRenderer);
        this.setGridColor(new Color(212, 208, 200));
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.setColumnSelectionAllowed(false);
        this.setRowSelectionAllowed(false);
        this.setFillsViewportHeight(true);
        defaultmodel = new BeanTableModel();
        this.setModel(defaultmodel);
        this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        this.repaint();
        this.setDefaultRenderer(Object.class,new MobileWidgetTableCellRenderer());
        refresh();
        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);
        add(moveComponent);
    }

    private MouseAdapter mouseAdapter = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if(getSelectedRow() != -1){
                String widgetName = cellData[getSelectedRow()][0];
                if (StringUtils.isNotEmpty(widgetName)){
                	XLayoutContainer root = getEditingDesigner().getRootComponent();
                    int count = root.getXCreatorCount();
                    for (int i = 0;i < count ;i++){
                        XCreator xCreator = root.getXCreator(i).getEditingChildCreator();
                        Widget widget = xCreator.toData();
                        if (ComparatorUtils.equals(widgetName, widget.getWidgetName())) {
                            getEditingDesigner().getSelectionModel().setSelectedCreator(xCreator);
                            setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                            selectedRow = getSelectedRow();
                        }
                    }
                }
            }
        }
        public void mouseExited(MouseEvent e) {
            draging = false;
            moveComponent.setVisible(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            int overRow = 0;
            for (int i = 0;i < getRowCount();i++) {
                if (e.getY() > i * WIDGET_TABLE_ROW_HEIGHT && e.getY() <= (i + 1) * WIDGET_TABLE_ROW_HEIGHT){
                    overRow = i;
                }
            }
            if (overRow == getSelectedRow()) {
                setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            } else {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            int width = getColumnModel().getColumn(0).getWidth();
            if (getCursor().getType() == Cursor.MOVE_CURSOR){
                draging = true;
                //下面这句话太重要了，拖拽过程中选中的不变
                getInstance().setRowSelectionInterval(selectedRow,selectedRow);
                moveComponent.setText(getValueAt(getSelectedRow(), getSelectedColumn()).toString());
                moveComponent.setLocation(0, e.getY() - GAP);
                moveComponent.setPreferredSize(new Dimension(width, WIDGET_TABLE_ROW_HEIGHT));
                moveComponent.setSize(new Dimension(width, WIDGET_TABLE_ROW_HEIGHT));
                moveComponent.setVisible(true);
                moveComponent.setForeground(Color.lightGray);
                moveComponent.setBorder(BorderFactory.createLineBorder(Color.lightGray));
            }
        }
        @Override
        public void mouseReleased(MouseEvent e) {
            if(!draging){
                return;
            }
            draging = false;
            moveComponent.setVisible(false);
            int toIndex =  e.getY() < GAP ? 0 : (int)Math.rint((e.getY() - GAP)/WIDGET_TABLE_ROW_HEIGHT) + 1;
            ((WFitLayout) designer.getRootComponent().toData()).adjustOrder(getSelectedRow(), toIndex);
            getInstance().setRowSelectionInterval(0,getRowCount() - 1);
            refresh();
            getInstance().repaint();
            designer.fireTargetModified();
        }
    };

    public MobileBodyWidgetTable getInstance(){
        return this;
    }

    public FormDesigner getEditingDesigner(){
        return  designer;
    }

    /**
     * 刷新
     */
    public void refresh(){
        XCreator creator = designer.getSelectionModel().getSelection().getSelectedCreator();
        cellData = getData();
        if(creator != null){
            String widgetName =creator.toData().getWidgetName();
            int row = -1;
            for (int i =0; i < cellData.length;i++){
                if(ComparatorUtils.equals(widgetName, cellData[i][0])){
                    row = i;
                    break;
                }
            }
            selectedRow = row;
            changeSelection(row,0,false,false);
            if(row == -1){
                this.clearSelection();
            }
        }
    }

    private String[][] getData(){
        if(designer.isFormParaDesigner()){
            return new String[0][0];
        }
        XLayoutContainer paraContainer = designer.getRootComponent();
        if(paraContainer == null || !paraContainer.acceptType(WFitLayout.class)){
            return new String[0][0];
        }

        WFitLayout fitLayout = (WFitLayout) (paraContainer.toData());
        ArrayList<String> strings = fitLayout.getMobileWidgetList();
        String[][] widgetName = new String[strings.size()][2];
        for(int i = 0;i < strings.size();i++){
            widgetName[i][0] = strings.get(i);
        }
        return widgetName;
    }

    private class MobileWidgetTableCellRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            if (getCursor().getType() == Cursor.MOVE_CURSOR){
                if(selectedRow  > -1 && selectedRow < getRowCount()){
                    //拖拽过程中选中的不变
                    getInstance().setRowSelectionInterval(selectedRow,selectedRow);
                }
            }
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            return this;
        }

    }

    public class BeanTableModel extends DefaultTableModel {
        public BeanTableModel() {
            super(cellData,headers);
        }

        @Override
        public int getRowCount() {
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
         *  是否可编辑
         * @param row 行号
         * @param column    列号
         * @return 是否可编辑
         */
        public boolean isCellEditable(int row, int column) {
                return false;
        }

    }

}