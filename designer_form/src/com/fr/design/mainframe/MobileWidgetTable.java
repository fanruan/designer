package com.fr.design.mainframe;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itable.HeaderRenderer;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWParameterLayout;
import com.fr.form.ui.Label;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WParameterLayout;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: zx
 * Date: 14-7-9
 * Time: 上午11:26
 */
public class MobileWidgetTable extends JTable {

    private FormDesigner designer;
    protected TableModel defaultmodel;
    private String[][] cellData  ;
    private String[] headers = {Inter.getLocText("FR-Utils_Label"),Inter.getLocText("Form-Widget_Name")};
    public static final int WIDGET_TABLE_ROW_HEIGHT = 22;
    private UILabel moveComponent = new UILabel();
    private int selectedRow = -1;
    private int GAP = 10;
    private boolean draging = false;

    public MobileWidgetTable(FormDesigner designer) {
        this.designer = designer;
        this.cellData = getData();
        this.setRowHeight(WIDGET_TABLE_ROW_HEIGHT);
        JTableHeader header = getTableHeader();
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(0, WIDGET_TABLE_ROW_HEIGHT));
        header.setDefaultRenderer(new HeaderRenderer());
        this.setGridColor(new Color(212, 208, 200));
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.setColumnSelectionAllowed(false);
        this.setRowSelectionAllowed(false);
        this.setFillsViewportHeight(true);
        this.setDefaultEditor(Object.class,new MobileCellEditor());
        defaultmodel = new BeanTableModel();
        this.setModel(defaultmodel);
        this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        TableColumn tc = this.getColumn(this.getColumnName(0));
        tc.setPreferredWidth(30);
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
            if(getSelectedRow() != -1 && getSelectedColumn() == 1){
                String widgetName = cellData[getSelectedRow()][getSelectedColumn()];
                if (StringUtils.isNotEmpty(widgetName)){
                    int count = getEditingDesigner().getParaComponent().getComponentCount();
                    for (int i = 0;i < count ;i++){
                        XCreator xCreator = (XCreator)getEditingDesigner().getParaComponent().getComponent(i);
                        Widget widget = xCreator.toData();
                        if (!widget.acceptType(Label.class) && ComparatorUtils.equals(widgetName,widget.getWidgetName())) {
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
            int overColumn = e.getX() < getColumnModel().getColumn(0).getWidth() ? 0 : 1;
            int overRow = -1;
            for (int i = 0;i < getRowCount();i++) {
                if (e.getY() > i * WIDGET_TABLE_ROW_HEIGHT && e.getY() <= (i + 1) * WIDGET_TABLE_ROW_HEIGHT){
                    overRow = i;
                }
            }
            if (overRow == getSelectedRow() && overColumn == getSelectedColumn() && overColumn !=0) {
                setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            } else {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        }

         @Override
        public void mouseDragged(MouseEvent e) {
             if (e.getX() < getColumnModel().getColumn(0).getWidth()) {
                 draging = false;
                 moveComponent.setVisible(false);
                 setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
             }
             int width = getColumnModel().getColumn(1).getWidth();
             if (getCursor().getType() == Cursor.MOVE_CURSOR){
                 draging = true;
                //下面这句话太重要了，拖拽过程中选中的不变
                 getInstance().setRowSelectionInterval(selectedRow,selectedRow);
                 moveComponent.setText(getValueAt(getSelectedRow(), getSelectedColumn()).toString());
                 moveComponent.setLocation(getColumnModel().getColumn(0).getWidth(), e.getY() - GAP);
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
            int toIndex = e.getY() < GAP ? 0 : (int)Math.rint((e.getY() - GAP)/WIDGET_TABLE_ROW_HEIGHT) + 1;
            ((WParameterLayout) designer.getParaComponent().toData()).adjustOrder(getSelectedRow(), toIndex);
            getInstance().setRowSelectionInterval(0,getRowCount() - 1);
            refresh();
            getInstance().repaint();
            designer.fireTargetModified();
        }
    };

    public MobileWidgetTable getInstance(){
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
         int column = -1;
         for (int i =0; i < cellData.length;i++){
             if(ComparatorUtils.equals(widgetName, cellData[i][0])){
                 row = i;
                 column = 0;
                 break;
             }
             if(ComparatorUtils.equals(widgetName, cellData[i][1])){
                 row = i;
                 column = 1;
                 break;
             }
         }
         selectedRow = row;
         changeSelection(row,column,false,false);
         if(row == -1){
            this.clearSelection();
         }
        }
    }

    private String[][] getData(){
    	XLayoutContainer paraContainer = designer.getParaComponent();
    	if(paraContainer == null || !paraContainer.acceptType(XWParameterLayout.class)){
    		return new String[0][0];
    	}
    	
    	WParameterLayout para = (WParameterLayout) (paraContainer.toData());
        return para.getWidgetNameTag();
    }

    private class MobileWidgetTableCellRenderer extends DefaultTableCellRenderer{
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            if (getCursor().getType() == Cursor.MOVE_CURSOR){
                if(selectedRow  > -1 && selectedRow < getRowCount()){
                    //拖拽过程中选中的不变
                    getInstance().setRowSelectionInterval(selectedRow,selectedRow);
                }
            }
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (column == 0){
                UITextField uiTableTextField;
                if (getSelectedColumn() == column && getSelectedRow() == row){
                     uiTableTextField = new UITableTextField(value.toString());
                } else {
                     uiTableTextField = new UITextField(value.toString());
                }
                return uiTableTextField;
            }
            return this;
        }

    }
    private class MobileCellEditor extends AbstractCellEditor implements TableCellEditor {
        UITableTextField uiTableTextField;
        MobileCellEditor(){
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
        public void firePropertyChange(){
            ((WParameterLayout) designer.getParaComponent().toData()).add2NameTagMap(uiTableTextField.getText(),
                    cellData[getSelectedRow()][1]);
        }

        public Object getCellEditorValue(){
            return uiTableTextField.getText();
        }

        /*
		 * 双击以编辑
		 */
        public boolean isCellEditable(EventObject anEvent) {
            if (anEvent instanceof MouseEvent) {
                return ((MouseEvent)anEvent).getClickCount() >= 2;
            }
            return true;
        }

        public Component getTableCellEditorComponent( JTable table,Object value,
                                                      boolean isSelected,int row,int column){
            uiTableTextField.setText(value.toString());
            return uiTableTextField;
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
            return 2;
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
         *  是否可编辑
         * @param row 行号
         * @param column    列号
         * @return 是否可编辑
         */
        public boolean isCellEditable(int row, int column) {
            if(column ==1){
                return false;
            }
            return true;
        }

    }

    private class UITableTextField extends UITextField {
        public UITableTextField(){
            super();
        }

        public UITableTextField(String string){
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