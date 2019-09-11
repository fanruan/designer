package com.fr.design.widget.ui.designer.component;

import com.fr.design.constants.LayoutConstants;
import com.fr.design.editor.editor.BooleanEditor;
import com.fr.design.editor.editor.DateEditor;
import com.fr.design.editor.editor.DoubleEditor;
import com.fr.design.editor.editor.Editor;
import com.fr.design.editor.editor.FormulaEditor;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.mainframe.widget.editors.DataBindingEditor;
import com.fr.design.mainframe.widget.editors.DataTableEditor;
import com.fr.design.mainframe.widget.editors.ServerDataBindingEditor;
import com.fr.design.mainframe.widget.editors.ServerDataTableEditor;
import com.fr.form.ui.DataControl;
import com.fr.form.ui.WidgetValue;
import com.fr.form.ui.concept.data.ValueInitializer;
import com.fr.general.ComparatorUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Created by ibm on 2017/7/27.
 */
public class FormWidgetValuePane extends JPanel {
    private UIButtonGroup widgetValueHead;
    private Editor[] editor;
    private JPanel customPane;
    private CardLayout cardLayout;


    public FormWidgetValuePane(Object o, boolean onlyServer) {
        DataControl widget = (DataControl) o;
        editor = createWidgetValueEditor(widget, onlyServer);
        this.setLayout(new BorderLayout(0, LayoutConstants.VGAP_SMALL));
        cardLayout = new CardLayout();
        customPane = new JPanel(cardLayout);
        final String[] tabTitles = new String[editor.length];
        for (int i = 0; i < editor.length; i++) {
            customPane.add(editor[i], editor[i].getName());
            tabTitles[i] = editor[i].getName();
        }
        widgetValueHead = new UIButtonGroup(tabTitles);
        this.add(widgetValueHead, BorderLayout.NORTH);
        this.add(customPane, BorderLayout.CENTER);

    }

    public void attributeChange() {
        int index = widgetValueHead.getSelectedIndex();
        if (index == -1) {
            index = 0;
            widgetValueHead.setSelectedIndex(index);
        }
        if (ComparatorUtils.equals(editor[index].getName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Widget_Field"))) {
            customPane.setPreferredSize(new Dimension(100, 47));
        } else {
            customPane.setPreferredSize(new Dimension(100, 20));
        }
        cardLayout.show(customPane, editor[index].getName());
    }


    /**
     * 根据类型创建
     *
     * @param type       类型
     * @param onlyServer 是否是服务器
     * @return 编辑器
     */
    public static Editor createWidgetValueEditorByType(int type, boolean onlyServer) {
        switch (type) {
            case DataControl.TYPE_NUMBER:
                return new DoubleEditor();
            case DataControl.TYPE_FORMULA:
                return new FormulaEditor(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Parameter_Formula"));
            case DataControl.TYPE_DATABINDING:
                return onlyServer ? new ServerDataBindingEditor() : new DataBindingEditor();
            case DataControl.TYPE_STRING:
                return new com.fr.design.editor.editor.TextEditor();
            case DataControl.TYPE_BOOLEAN:
                return new BooleanEditor(false);
            case DataControl.TYPE_DATE:
                return new DateEditor(true, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Date"));
            case DataControl.TYPE_TABLEDATA:
                return onlyServer ? new ServerDataTableEditor() : new DataTableEditor();
            default:
                return null;
        }
    }


    /**
     * 用DataControl构建
     *
     * @param data       数据
     * @param onlyServer 是否是服务器
     * @return 编辑器
     */
    public static Editor[] createWidgetValueEditor(DataControl data, boolean onlyServer) {
        int types[] = data.getValueType();
        Editor[] editor = new Editor[types.length];
        for (int i = 0; i < types.length; i++) {
            editor[i] = createWidgetValueEditorByType(types[i], onlyServer);

        }
        return editor;
    }

    public void update(DataControl ob) {
        attributeChange();
        int index = widgetValueHead.getSelectedIndex();
        Editor e = editor[index];
        Object value = e.getValue();
        ob.setWidgetValue(new WidgetValue(value));
    }

    public void populate(DataControl ob) {
        ValueInitializer widgetValue = ob.getWidgetValue();
        if (widgetValue != null) {
            for (int i = 0; i < editor.length; i++) {
                if (editor[i].accept(widgetValue.getValue())) {
                    setCardValue(i, widgetValue.getValue());
                    break;
                }
            }
        }
        attributeChange();
    }

    private void setCardValue(int i, Object object) {
        widgetValueHead.setSelectedIndex(i);
        editor[i].setValue(object);
        // kunsnat: bug7861 所有的Editor值都要跟随改变, 因为populate的editor 从""
        // 一定是最后的Editor哦.
        for (int j = 0; j < editor.length; j++) {
            if (i == j) {
                continue;
            }
            this.editor[j].setValue(null);
        }
    }

}
