package com.fr.design.gui.ilist;

import com.fr.general.NameObject;
import com.fr.base.Utils;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.general.Inter;
import com.fr.stable.Nameable;
import com.fr.stable.StringUtils;
import com.fr.stable.core.PropertyChangeAdapter;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Vector;

public class JNameEdList extends UIList implements CellEditorListener {
    private static final int TEST_LIST_LENTH = 20;
    private static final int ICON_WIDTH = 20;
    private boolean editable = true;

    // kunsnat: 是否强制ListName是数字 (int型)
    private boolean isNameShouldNumber = false;

    transient protected ListCellEditor cellEditor;
    transient protected Component editorComp;
    transient protected int editingIndex;
    private PropertyChangeAdapter editingListner;
    private java.util.List<ModNameActionListener> ll = new ArrayList<ModNameActionListener>();

    public JNameEdList(ListModel dataModel) {
        super(dataModel);
    }

    public JNameEdList(final Object[] listData) {
        super(listData);
    }

    public JNameEdList(final Vector<?> listData) {
        super(listData);
    }

    public JNameEdList() {
        super();
    }

    /*
     * Sets是否可编辑
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    /**
     * 是否可编辑
     *
     * @return 是则返回true
     */
    public boolean isEditable() {
        return this.editable;
    }

    public void setNameShouldNumber(boolean isNameShouldNumber) {
        this.isNameShouldNumber = isNameShouldNumber;
    }

    /**
     * 是否强制ListName是数字 (int型)
     *
     * @return 是则返回true
     */
    public boolean isNameShouldNumber() {
        return isNameShouldNumber;
    }

    /**
     * 添加名字改变时的listener
     *
     * @param l 监听器
     */
    public void addModNameActionListener(ModNameActionListener l) {
        ll.add(l);
    }

    /**
     * 编辑时的监听器
     *
     * @param l 监听器
     */
    public void addEditingListner(PropertyChangeAdapter l) {
        this.editingListner = l;
    }

    /**
     * 移除某名字改变时的listener
     *
     * @param l 监听器
     */
    public void removeModNameActionListener(ModNameActionListener l) {
        ll.remove(l);
    }

    public ListCellEditor getCellEditor() {
        if (cellEditor == null) {
            UITextField editField = new UITextField();
            if (editingListner != null) {
                editField.addFocusListener(new FocusListener() {

                    @Override
                    public void focusLost(FocusEvent e) {
                        editingListner.propertyChange();

                    }

                    @Override
                    public void focusGained(FocusEvent e) {
                        // TODO Auto-generated method stub

                    }
                });
            }
            cellEditor = new DefaultListCellEditor(editField) {
                public boolean stopCellEditing() {
                    boolean isTrue = super.stopCellEditing();
                    stopEditing();

                    return isTrue;
                }
            };
            cellEditor.addCellEditorListener(this);
        }

        return cellEditor;
    }

    protected void doAfterLostFocus() {

    }

    public void setCellEditor(ListCellEditor editor) {
        this.cellEditor = editor;
    }

    /*
     * 取得index节点的名字
     */
    public String getNameAt(int index) {
        Nameable nameable = ((ListModelElement) getModel().getElementAt(index)).wrapper;
        if (nameable != null) {
            return nameable.getName();
        }

        return null;
    }

    public Object getType(int index) {
        Nameable nameable = ((ListModelElement) getModel().getElementAt(index)).wrapper;
        if (nameable != null && nameable instanceof NameObject) {
            return ((NameObject) nameable).getObject();
        }
        return null;
    }


    public void setWarnigText() {
        setWarnigText(this.getSelectedIndex());
    }

    public void setWarnigText(int index) {
        setNameAt(Inter.getLocText("Please_Rename") + "!", index);
        this.repaint();
    }

    /*
     * 设置index节点的名字
     */
    // b:edit改变name的时候怎么办？
    public void setNameAt(String name, int index) {
        Nameable nameable = ((ListModelElement) getModel().getElementAt(index)).wrapper;
        if (nameable != null) {
            String oldName = nameable.getName();

            if (isNameShouldNumber()) {
                // kunsnat: 限制只能是数字(int型)
                Number number = Utils.string2Number(name);
                if (number == null) {
                    nameable.setName(oldName);
                } else {
                    int newName = number.intValue();
                    nameable.setName(String.valueOf(newName));
                }
            } else {
                nameable.setName(name);
            }

            for (int i = 0, len = ll.size(); i < len; i++) {
                ll.get(i).nameModed(index, oldName, name);
            }
        }
    }

    /*
     * 编辑第index个item
     */
    private String oldName;

    /**
     * 编辑第index项
     *
     * @param index 序号
     * @return 成功返回true
     */
    public boolean editItemAt(int index) {
        // 如果不可编辑,返回
        if (!this.editable) {
            return false;
        }

        if (cellEditor != null && !cellEditor.stopCellEditing()) {
            return false;
        }
        if (index < 0 || index >= this.getModel().getSize()) {
            return false;
        }

        ListCellEditor editor = getCellEditor();
        Object value = editor.getCellEditorValue();
        if (!StringUtils.isBlank(value.toString())) {
            oldName = value.toString();
        }
        editorComp = prepareEditor(editor, index);
        if (editorComp == null) {
            return false;
        }
        Rectangle rect = this.getCellBounds(index, index);
        // alex:所有的JNameEdList都有Icon,空出前面20 * 20的位置就是放的Icon
        rect.setRect(createRect(rect, ICON_WIDTH));

        editorComp.setBounds(rect);
        add(editorComp);
        editorComp.validate();
        editorComp.requestFocus();
        if (editorComp instanceof UITextField) {
            ((UITextField) editorComp).selectAll();
        }

        setEditingIndex(index);

        return true;
    }

    public Rectangle createRect(Rectangle rect, int iconWidth) {
        return new Rectangle(rect.x + iconWidth, rect.y, rect.width - iconWidth, rect.height);
    }

    public String getEditingName() {
        return (String) getCellEditor().getCellEditorValue();
    }

    /*
     * 根据ListCellEditor取得编辑器的Component
     */
    private Component prepareEditor(ListCellEditor cellEditor, int index) {
        String name = getNameAt(index);
        boolean isSelected = this.isSelectedIndex(index);
        Component comp = cellEditor.getListCellEditorComponent(this, name, isSelected, index);

        return comp;
    }

    /*
     * 记录正在编辑的index
     */
    private void setEditingIndex(int idx) {
        editingIndex = idx;
    }

    /**
     * 编辑取消
     *
     * @param e 事件
     */
    public void editingCanceled(ChangeEvent e) {
        removeComp();
    }

    /**
     * 编辑结束
     *
     * @param e 事件
     */
    public void editingStopped(ChangeEvent e) {
        doAfterLostFocus();
        stopEditing();
    }

    /**
     * 停止编辑事件
     */
    public void stopEditing() {
        ListCellEditor editor = getCellEditor();
        if (editor != null && editorComp != null) {
            Object value = editor.getCellEditorValue();
            String name = StringUtils.isBlank(value.toString()) ? oldName : value.toString();
            setNameAt(name, editingIndex);
            removeComp();
        }
    }

    public String[] getAllNames() {
        int length = this.getModel().getSize();
        String[] names = new String[length];
        for (int i = 0; i < length; i++) {
            names[i] = getNameAt(i);
        }
        return names;
    }

    public Object[] getAllTypes() {
        int length = this.getModel().getSize();
        Object[] types = new Object[length];
        for (int i = 0; i < length; i++) {
            types[i] = getType(i);
        }
        return types;
    }


    /*
     * 移除编辑器的Component
     */
    private void removeComp() {
        if (editorComp != null) {
            remove(editorComp);
        }
        Rectangle cellRect = this.getCellBounds(editingIndex, editingIndex);
        setEditingIndex(-1);
        editorComp = null;
        repaint(cellRect);
    }

    /**
     * 主函数
     *
     * @param args 参数
     */
    public static void main(String... args) {
        JFrame f = new JFrame();
        JPanel c = (JPanel) f.getContentPane();
        c.setLayout(new BorderLayout());
        ListModelElement[] data = new ListModelElement[TEST_LIST_LENTH];
        for (int i = 0; i < TEST_LIST_LENTH; i++) {
            data[i] = new ListModelElement(new NameObject(i + 1 + "", i));
        }
        final JNameEdList list = new JNameEdList(data);
        list.setEditable(true);
        list.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent evt) {
                list.stopEditing();
                if (evt.getClickCount() >= 2
                        && SwingUtilities.isLeftMouseButton(evt)) {
                    list.editItemAt(list.getSelectedIndex());
                }
            }
        })
        ;

        list.setCellEditor(new DefaultListCellEditor(new UITextField()));
        list.setCellRenderer(new NameableListCellRenderer());
        c.add(list, BorderLayout.CENTER);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setSize(400, 600);
        f.setVisible(true);
    }

    private static class NameableListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                                                      boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof Nameable) {
                Nameable wrappee = (Nameable) value;
                this.setText(wrappee.getName());
            }
            return this;
        }
    }

}