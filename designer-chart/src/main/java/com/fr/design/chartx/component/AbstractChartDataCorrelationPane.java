package com.fr.design.chartx.component;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.frpane.UICorrelationPane;
import com.fr.design.gui.itable.UITable;
import com.fr.design.gui.itable.UITableEditor;
import com.fr.stable.StringUtils;

import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.List;

/**
 * Created by shine on 2019/6/4.
 */
public abstract class AbstractChartDataCorrelationPane<T> extends BasicBeanPane<T> {
    private FieldEditorComponentWrapper[] editorComponents;

    private UICorrelationPane correlationPane;

    public AbstractChartDataCorrelationPane() {

        this.editorComponents = fieldEditorComponentWrappers();

        String[] headers = new String[editorComponents.length];

        for (int i = 0, len = editorComponents.length; i < len; i++) {
            headers[i] = editorComponents[i].headerName();
        }

        initComps(headers);
    }

    protected abstract FieldEditorComponentWrapper[] fieldEditorComponentWrappers();

    protected List<Object[]> update() {
        return correlationPane.updateBean();
    }

    protected void populate(List<Object[]> list) {
        correlationPane.populateBean(list);
    }

    @Override
    public T updateBean() {
        return null;
    }

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
        };

        this.setLayout(new BorderLayout());
        this.add(correlationPane, BorderLayout.CENTER);
    }

    private class Editor extends UITableEditor {

        private Component currentComponent;
        private FieldEditorComponentWrapper currentEditorWrapper;

        public Object getCellEditorValue() {
            return currentEditorWrapper.value(currentComponent);
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (column == table.getModel().getColumnCount()) {
                return null;
            }

            correlationPane.stopCellEditing();

            currentEditorWrapper = AbstractChartDataCorrelationPane.this.editorComponents[column];

            currentComponent = currentEditorWrapper.createEditorComponent(correlationPane);
            currentEditorWrapper.setValue(currentComponent, value);

            return currentComponent;
        }

    }

    @Override
    protected String title4PopupWindow() {
        return StringUtils.EMPTY;
    }

}
