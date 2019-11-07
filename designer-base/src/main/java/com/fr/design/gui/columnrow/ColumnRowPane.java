package com.fr.design.gui.columnrow;

import com.fr.design.designer.IntervalConstants;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.ispinner.ColumnRowSpinner;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.gui.ispinner.UpperCaseSpinner;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.stable.ColumnRow;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;

import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.SpinnerListModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

/**
 * the component to edit ColumnRow
 *
 * @editor zhou 2012-3-22下午3:55:53
 */
public class ColumnRowPane extends JPanel implements UIObserver {

    private static final long serialVersionUID = 1L;
    private static String[] columnarray = new String[1000];
    private static String[] rowarray = new String[5000];


    protected UpperCaseSpinner columnSpinner;
    protected UIBasicSpinner rowSpinner;

    static {
        for (int i = 1; i <= 1000; i++) {
            columnarray[i - 1] = StableUtils.convertIntToABC(i);
        }
        for (int i = 1; i <= 5000; i++) {
            rowarray[i - 1] = StringUtils.EMPTY + i;
        }
    }

    protected DocumentListener d = new DocumentListener() {


        @Override
        public void removeUpdate(DocumentEvent e) {
            String rolText = ((DefaultEditor) columnSpinner.getEditor()).getTextField().getText();
            String rowText = ((DefaultEditor) rowSpinner.getEditor()).getTextField().getText();
            if (rolText == null || rolText.length() == 0 || rowText == null || rowText.length() == 0) {
                return;
            }
            int col = StableUtils.convertABCToInt(rolText) - 1;
            int row = Integer.parseInt(rowText) - 1;
            setColumnRow(ColumnRow.valueOf(col, row));
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            int col = StableUtils.convertABCToInt(((DefaultEditor) columnSpinner.getEditor()).getTextField().getText()) - 1;
            int row = Integer.parseInt(((DefaultEditor) rowSpinner.getEditor()).getTextField().getText()) - 1;
            setColumnRow(ColumnRow.valueOf(col, row));
        }

        @Override
        public void changedUpdate(DocumentEvent e) {

        }
    };


    private UIObserverListener uiObserverListener;
    private ColumnRow cr;


    public ColumnRowPane() {
        this.initComponents();
        iniListener();
    }


    /**
     * 初始化元素
     */
    public void initComponents() {
        this.setLayout(new GridLayout(0, 2, IntervalConstants.INTERVAL_L6, 0));
        initColSpinner();
        this.add(columnSpinner, BorderLayout.WEST);
        initRowSpinner();
        this.add(rowSpinner);
        this.addDocumentListener(d);
    }

    private void iniListener() {
        if (shouldResponseChangeListener()) {
            this.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    if (uiObserverListener == null) {
                        return;
                    }
                    uiObserverListener.doChange();
                }
            });
        }
    }


    protected void initRowSpinner() {
        rowSpinner = new ColumnRowSpinner((new SpinnerListModel(rowarray))) {
            public boolean shouldResponseChangeListener() {
                return false;
            }

        };
        rowSpinner.setPreferredSize(new Dimension(60, 24));
        JFormattedTextField rftf = GUICoreUtils.getSpinnerTextField(rowSpinner);
        if (rftf != null) {
            rftf.setColumns(4); // specify more width than we need
            rftf.setHorizontalAlignment(UITextField.LEFT);
        }
    }

    protected void initColSpinner() {
        SpinnerListModel columnSpinnerListModel = new SpinnerListModel(columnarray);
        columnSpinner = new UpperCaseSpinner(columnSpinnerListModel) {
            public boolean shouldResponseChangeListener() {
                return false;
            }
        };
        columnSpinner.setPreferredSize(new Dimension(60, 24));
        JFormattedTextField cftf = GUICoreUtils.getSpinnerTextField(columnSpinner);
        if (cftf != null) {
            cftf.setColumns(3); // specify more width than we need
            cftf.setHorizontalAlignment(UITextField.LEFT);
        }else {
            throw new IllegalArgumentException("Spinner'Editor can not be null!");
        }

        ((AbstractDocument) cftf.getDocument()).setDocumentFilter(new DocumentFilter() {
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                fb.insertString(offset, string.toUpperCase(), attr);
            }

            public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attr) throws BadLocationException {
                if (string != null) {
                    string = string.toUpperCase();
                }
                fb.replace(offset, length, string, attr);
            }
        });
    }

    protected void addDocumentListener(DocumentListener l) {
        ((DefaultEditor) columnSpinner.getEditor()).getTextField().getDocument().addDocumentListener(l);
        ((DefaultEditor) rowSpinner.getEditor()).getTextField().getDocument().addDocumentListener(l);
    }

    private void removeDocumentListener(DocumentListener l) {
        ((DefaultEditor) columnSpinner.getEditor()).getTextField().getDocument().removeDocumentListener(l);
        ((DefaultEditor) rowSpinner.getEditor()).getTextField().getDocument().removeDocumentListener(l);
    }

    /**
     * columnSpinner与rowSpinner应该整体考虑，设置ColumnRow应该只触发一次事件
     *
     * @param columnRow
     */
    public void setColumnRow(ColumnRow columnRow) {
        if (!ComparatorUtils.equals(cr, columnRow)) {
            setGlobalName();
            removeDocumentListener(d);
            cr = columnRow;
            addDocumentListener(d);
            fireChanged();
        }
    }

    public void setGlobalName() {

    }

    public void populate(ColumnRow columnRow) {
        cr = columnRow;
        int column = columnRow.getColumn();
        if (column < 0) {
            column = 0;
        }
        removeDocumentListener(d);
        columnSpinner.setValue(StableUtils.convertIntToABC(column + 1));
        int row = columnRow.getRow();
        // shoc 小于0就蹦了
        if (row < 0) {
            row = 0;
        }
        rowSpinner.setValue("" + (row + 1));
        addDocumentListener(d);
        fireChanged();
    }


    public ColumnRow update() {
        return cr;
    }

    /**
     * 添加一个listener到listenerList中
     *
     * @param l 监听
     */
    public void addChangeListener(ChangeListener l) {
        this.listenerList.add(ChangeListener.class, l);
    }

    /**
     * 从listenerList中移除一个listener
     *
     * @param l 监听
     */
    public void removeChangeListener(ChangeListener l) {
        this.listenerList.remove(ChangeListener.class, l);
    }

    // august: Process the listeners last to first
    protected void fireChanged() {
        Object[] listeners = listenerList.getListenerList();

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                ((ChangeListener) listeners[i + 1]).stateChanged(new ChangeEvent(this));
            }
        }
    }

    /**
     * 给组件登记一个观察者监听事件
     *
     * @param listener 监听
     */
    public void registerChangeListener(UIObserverListener listener) {
        uiObserverListener = listener;
    }

    /**
     * 组件是否需要响应添加的观察者事件
     *
     * @return 如果需要响应观察者事件则返回true，否则返回false
     */
    public boolean shouldResponseChangeListener() {
        return true;
    }
}