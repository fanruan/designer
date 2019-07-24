package com.fr.design.chartx.component.correlation;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.frpane.UICorrelationPane;
import com.fr.design.gui.itable.UITable;
import com.fr.design.gui.itable.UITableEditor;
import com.fr.stable.StringUtils;

import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created by shine on 2019/6/4.
 * 自定义editorComponent + 支持多种数据格式
 */
public abstract class AbstractCorrelationPane<T> extends BasicBeanPane<T> {
    private FieldEditorComponentWrapper[] editorComponents;

    private UICorrelationPane correlationPane;

    public AbstractCorrelationPane() {

        this.editorComponents = createFieldEditorComponentWrappers();

        String[] headers = new String[editorComponents.length];

        for (int i = 0, len = editorComponents.length; i < len; i++) {
            headers[i] = editorComponents[i].headerName();
        }

        initComps(headers);
    }

    protected abstract FieldEditorComponentWrapper[] createFieldEditorComponentWrappers();

    @Override
    public void populateBean(T ob) {
        correlationPane.populateBean(covertTBeanToTableModelList(ob));
    }

    @Override
    public void updateBean(T ob) {
        setTableModelListToTBean(correlationPane.updateBean(), ob);
    }

    @Override
    public T updateBean() {
        return null;
    }

    protected abstract List<Object[]> covertTBeanToTableModelList(T t);

    protected abstract void setTableModelListToTBean(List<Object[]> tableValues, T t);

    private void initComps(String[] headers) {
        correlationPane = new UICorrelationPane(headers) {
            public UITableEditor createUITableEditor() {
                return new Editor();
            }

            protected UITable initUITable() {
                return new UITable(columnCount) {

                    public UITableEditor createTableEditor() {
                        return createUITableEditor();
                    }

                    public void tableCellEditingStopped(ChangeEvent e) {
                        stopPaneEditing(e);
                    }
                };
            }

            protected ActionListener getAddButtonListener() {
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        tablePane.addLine(createLine());
                        fireTargetChanged();
                    }
                };
            }
        };

        this.setLayout(new BorderLayout());
        this.add(correlationPane, BorderLayout.CENTER);
    }

    protected Object[] createLine() {
        return new Object[this.editorComponents.length];
    }


    private class Editor extends UITableEditor {

        private Component currentComponent;
        private FieldEditorComponentWrapper currentEditorWrapper;

        public Object getCellEditorValue() {
            return currentEditorWrapper.getValue(currentComponent);
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (column == table.getModel().getColumnCount()) {
                return null;
            }

            correlationPane.stopCellEditing();

            currentEditorWrapper = AbstractCorrelationPane.this.editorComponents[column];

            currentComponent = currentEditorWrapper.getTableCellEditorComponent(correlationPane, table, isSelected, row, column);
            currentEditorWrapper.setValue(currentComponent, value);

            return currentComponent;
        }

    }

    @Override
    protected String title4PopupWindow() {
        return StringUtils.EMPTY;
    }

}
