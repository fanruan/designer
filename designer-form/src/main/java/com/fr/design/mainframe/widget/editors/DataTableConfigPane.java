package com.fr.design.mainframe.widget.editors;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.gui.ilable.UILabel;
import com.fr.form.data.DataTableConfig;

import com.fr.stable.core.PropertyChangeListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static com.fr.design.gui.syntax.ui.rtextarea.RTADefaultInputMap.DEFAULT_MODIFIER;

public class DataTableConfigPane extends JComponent implements PropertyChangeListener {

    private DataEditingTable table;

    public DataTableConfigPane() {
        table = new DataEditingTable();
        JScrollPane scrollPane = new JScrollPane(table);
        this.setLayout(new DataTableLayout());
        this.add(scrollPane, BorderLayout.CENTER);
    }

    public void populate(DataTableConfig config) {
        table.populate(config);
    }

    public DataTableConfig update() {
        return table.update();
    }

    class DataTableLayout extends BorderLayout {
        public void layoutContainer(Container target) {
            super.layoutContainer(target);
            table.doLayout();
        }
    }

    class DataEditingTable extends JTable {

        private DataTableConfig config;
        private BeanTableModel model;
        private TableColumnModelListener modeListener;

        public DataEditingTable() {
            this.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210), 1));
            this.setColumnSelectionAllowed(true);
            this.setRowSelectionAllowed(true);
            MouseAdapterListener l = new MouseAdapterListener(this);
            this.addMouseListener(l);
            this.addMouseMotionListener(l);
            model = new BeanTableModel();
            modeListener = new TableColumnModelListener() {

                @Override
                public void columnAdded(TableColumnModelEvent e) {

                }

                @Override
                public void columnMarginChanged(ChangeEvent e) {
                    DataTableConfigPane.this.propertyChange();
                }

                @Override
                public void columnMoved(TableColumnModelEvent e) {
                    DataTableConfigPane.this.propertyChange();
                }

                @Override
                public void columnRemoved(TableColumnModelEvent e) {

                }

                @Override
                public void columnSelectionChanged(ListSelectionEvent e) {

                }

            };
        }

        public TableCellRenderer getCellRenderer(int row, int column) {
            TableCellRenderer renderer = super.getCellRenderer(row, column);
            if (renderer instanceof UILabel) {
                ((UILabel) renderer).setHorizontalAlignment(UILabel.CENTER);
            }
            return renderer;
        }

        public void populate(DataTableConfig config) {
            this.getTableHeader().getColumnModel().removeColumnModelListener(modeListener);
            if (config == null) {
                config = DataTableConfig.DEFAULT_TABLE_DATA_CONFIG;
            }
            this.config = config;

            model = new BeanTableModel();
            this.setModel(model);
            this.setRowHeight(0, config.getRowHeight());
            for (int i = 0; i < config.getColumnCount(); i++) {
                this.getColumn(this.getColumnName(i)).setPreferredWidth(config.getColumnWidth(i));
            }
            this.getTableHeader().getColumnModel().addColumnModelListener(modeListener);
            this.doLayout();
            this.repaint();
        }

        public DataTableConfig update() {
            config.setRowHeight(this.getRowHeight(0));
            model = new BeanTableModel();
            String[] columns = new String[this.getColumnCount()];
            for (int i = 0; i < this.getColumnCount(); i++) {
                config.setColumnWidth(i, this.getColumn(this.getColumnName(i)).getWidth());
                columns[i] = this.getColumnName(i);
            }

            config.setColumns(columns);
            return config;
        }

        public class BeanTableModel extends AbstractTableModel {

            @Override
            public int getColumnCount() {
                return config.getColumnCount();
            }

            @Override
            public int getRowCount() {
                return 1;
            }

            @Override
            public String getColumnName(int column) {
                return config.getColumnName(column);
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                return config.getTableDataName() + "." + config.getColumnName(columnIndex);
            }

        }

        class MouseAdapterListener extends MouseAdapter {
            private final static int DIS = 30;
            private final static int SMALL_DIS = 3;
            private JTable table;
            int oldY = 0;
            int newY = 0;
            int row = 0;
            int oldHeight = 0;
            boolean drag = false;
            int increase = 0;
            JPopupMenu popupMenu;

            public MouseAdapterListener(JTable table) {
                this.table = table;
                popupMenu = new JPopupMenu();

                popupMenu.add(new CutAction());
                popupMenu.add(new CutAction());
                popupMenu.add(new CutAction());
                popupMenu.add(new CutAction());
            }

            class CutAction extends UpdateAction {

                /**
                 * Constructor
                 */
                public CutAction() {
                    this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_M_Edit_Cut"));
                    this.setMnemonic('T');
                    this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_edit/cut.png"));
                    this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, DEFAULT_MODIFIER));
                }

                @Override
                public void actionPerformed(ActionEvent e) {
                    int row = table.getSelectedRow();
                    int column = table.getSelectedColumn();
                    table.getColumnModel().removeColumn(table.getColumn(table.getColumnName(column)));
                    DataTableConfigPane.this.propertyChange();
                }
            }

            public void mouseMoved(MouseEvent e) {
                int onRow = table.rowAtPoint(e.getPoint());

                int height = 0;
                for (int i = 0; i <= onRow; i++) {
                    height = height + table.getRowHeight(i);
                }

                if (height - e.getY() < SMALL_DIS) {
                    drag = true;
                    table.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
                } else {
                    drag = false;
                    table.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }

            }

            private void trigger_popup(MouseEvent e) {

                popupMenu.show(table, e.getX(), e.getY());
            }

            public void mouseDragged(MouseEvent e) {
                if (drag) {
                    int value = oldHeight + e.getY() - oldY;
                    if (value < DIS) {
                        table.setRowHeight(row, DIS);
                    } else {
                        table.setRowHeight(row, oldHeight + e.getY() - oldY);
                    }
                    DataTableConfigPane.this.propertyChange();
                }
            }

            public void mousePressed(MouseEvent e) {
                oldY = e.getY();
                row = table.rowAtPoint(e.getPoint());
                oldHeight = table.getRowHeight(row);
                if (e.getButton() == MouseEvent.BUTTON3) {
                    trigger_popup(e);
                }
            }

            public void mouseReleased(MouseEvent e) {
                newY = e.getY();
                table.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }

    private ArrayList<PropertyChangeListener> changetList = new ArrayList<PropertyChangeListener>();

    public void addpropertyChangeListener(PropertyChangeListener l) {
        changetList.add(l);
    }

    @Override
    public void propertyChange() {
        for (PropertyChangeListener l : changetList) {
            l.propertyChange();
        }
    }

    @Override
    public void propertyChange(Object mark) {

    }

    @Override
    public void propertyChange(Object[] marks) {

    }
}