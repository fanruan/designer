package com.fr.design.gui.core;

import com.fr.base.BaseUtils;
import com.fr.form.ui.*;
import com.fr.general.ComparatorUtils;


import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class WidgetOption implements Serializable {

    private static final long serialVersionUID = -5213215153654191184L;

    private static final java.util.List<WidgetOption> STATIC_WIDGET_OPTION_LIST = new java.util.ArrayList<WidgetOption>();

    public WidgetOption() {
        STATIC_WIDGET_OPTION_LIST.add(this);
    }

    /**
     * 名字
     *
     * @return 名字
     */
    public abstract String optionName();

    /**
     * 图标
     *
     * @return 图标
     */
    public abstract Icon optionIcon();

    /**
     * 类
     *
     * @return 类
     */
    public abstract Class<? extends Widget> widgetClass();

    /**
     * 创建控件
     *
     * @return 控件
     */
    public abstract Widget createWidget();

    public boolean equals(Object obj) {
        return obj instanceof WidgetOption && ComparatorUtils.equals(((WidgetOption) obj).optionName(), optionName());
    }

    /**
     * 这个name的hashcode
     *
     * @return 这个name的hashcode
     */
    public int hashCode() {
        return optionName().hashCode();
    }

    public static WidgetOption getToolBarButton(Class cls) {
        for (int i = 0, len = STATIC_WIDGET_OPTION_LIST.size(); i < len; i++) {
            if (STATIC_WIDGET_OPTION_LIST.get(i).widgetClass() == cls) {
                return STATIC_WIDGET_OPTION_LIST.get(i);
            }
        }
        return null;
    }

    public static String getWidgetType(Widget widget) {
        WidgetOption wo = getToolBarButton(widget.getClass());
        return wo == null ? null : wo.optionName();
    }

    public static Icon getWidgetIcon(Widget widget) {
        WidgetOption wo = getToolBarButton(widget.getClass());
        return wo == null ? null : wo.optionIcon();
    }

    /*
     * 预定义控件
     */
    public static WidgetOption[] getPredefinedWidget() {
        java.util.List<WidgetOption> list = new ArrayList<WidgetOption>();
        WidgetInfoConfig mgr = WidgetInfoConfig.getInstance();
        java.util.Iterator<String> nameIt = mgr.getWidgetConfigNameIterator();
        while (nameIt.hasNext()) {
            String name = nameIt.next();
            WidgetConfig widgetConfig = mgr.getWidgetConfig(name);
            if (widgetConfig instanceof UserDefinedWidgetConfig) {
                list.add(WidgetOptionFactory.createByWidgetClass(name, BaseUtils.readIcon("/com/fr/design/images/data/user_widget.png"),
                        ((UserDefinedWidgetConfig) widgetConfig).getWidget().getClass()));
            }
        }
        return list.toArray(new WidgetOption[list.size()]);
    }

    /*
     * 应用与报表设计的控件
     */
    public static WidgetOption[] getReportWidgetInstance() {
        return new WidgetOption[]{TEXTEDITOR, TEXTAREA, NUMBEREDITOR, PASSWORD, BUTTON, CHECKBOX, RADIOGROUP, CHECKBOXGROUP, COMBOBOX,
                COMBOCHECKBOX, DATEEDITOR, MULTI_FILEEDITOR, LIST, IFRAMEDITOR, TREECOMBOBOX, TREE};
    }


    /**
     * 报表工具栏的控件
     *
     * @return 报表工具栏控件
     */
    public static WidgetOption[] getReportParaWidgetIntance() {
        return new WidgetOption[]{TEXTEDITOR, LABEL, FREEBUTTON, COMBOBOX, COMBOCHECKBOX, DATEEDITOR,
                NUMBEREDITOR, TREECOMBOBOX, RADIOGROUP, CHECKBOXGROUP, TEXTAREA, PASSWORD, CHECKBOX, TREE};
    }

    /**
     * 表单工具栏控件
     *
     * @return 表单工具栏控件
     */
    public static WidgetOption[] getFormWidgetIntance() {
        return new WidgetOption[]{TEXTEDITOR, LABEL, FREEBUTTON, COMBOBOX, COMBOCHECKBOX, DATEEDITOR,
                NUMBEREDITOR, TREECOMBOBOX, RADIOGROUP, CHECKBOXGROUP, TEXTAREA, PASSWORD, CHECKBOX, TREE, MULTI_FILEEDITOR};
    }

    public static final WidgetOption DATEEDITOR = WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Widget_Type_Date"),
            BaseUtils.readIcon("/com/fr/design/images/buttonicon/widget/date_16.png"),
            DateEditor.class);

    public static final WidgetOption TREE = WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Form-View_Tree"),
            BaseUtils.readIcon("/com/fr/design/images/buttonicon/widget/tree_16.png"), TreeEditor.class);

    public static final WidgetOption TREECOMBOBOX = WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Tree-ComboBox"), BaseUtils.readIcon("/com/fr/design/images/buttonicon/widget/comboboxtree.png"),
            TreeComboBoxEditor.class);

    public static final WidgetOption CHECKBOXGROUP = WidgetOptionFactory.createByWidgetClass(
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Widget_Type_Checkbox_Group"), BaseUtils.readIcon("/com/fr/design/images/buttonicon/widget/checkbox_group_16.png"), CheckBoxGroup.class);

    public static final WidgetOption RADIOGROUP = WidgetOptionFactory.createByWidgetClass(
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Widget_Type_Radio_Group"), BaseUtils.readIcon("/com/fr/design/images/buttonicon/widget/button_group_16.png"),
            RadioGroup.class);

    public static final WidgetOption NUMBEREDITOR = WidgetOptionFactory.createByWidgetClass(
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Widget_Type_Number"), BaseUtils.readIcon("/com/fr/design/images/buttonicon/widget/number_field_16.png"), NumberEditor.class);

    public static final WidgetOption LABEL = WidgetOptionFactory.createByWidgetClass(
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Widget_Type_Label"), BaseUtils.readIcon("/com/fr/design/images/buttonicon/widget/label_16.png"),
            Label.class);

    public static final WidgetOption BUTTON = WidgetOptionFactory.createByWidgetClass(
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Widget_Type_Button"), BaseUtils.readIcon("/com/fr/web/images/form/resources/button_16.png"),
            Button.class);

    public static final WidgetOption FREEBUTTON = WidgetOptionFactory.createByWidgetClass(
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Widget_Type_Free_Button"), BaseUtils.readIcon("/com/fr/design/images/buttonicon/widget/button_16.png"),
            FreeButton.class);

    public static final WidgetOption MULTI_FILEEDITOR = WidgetOptionFactory.createByWidgetClass(
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Widget_Type_File"), BaseUtils.readIcon("/com/fr/design/images/buttonicon/widget/files_up.png"),
            MultiFileEditor.class);

    public static final WidgetOption COMBOBOX = WidgetOptionFactory.createByWidgetClass(
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Widget_Type_Combo_Box"), BaseUtils.readIcon("/com/fr/design/images/buttonicon/widget/combo_box_16.png"),
            ComboBox.class);

    public static final WidgetOption COMBOCHECKBOX = WidgetOptionFactory.createByWidgetClass(
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Widget_Type_Combo_Checkbox"), BaseUtils.readIcon("/com/fr/design/images/buttonicon/widget/combo_check_16.png"),
            ComboCheckBox.class);

    public static final WidgetOption CHECKBOX = WidgetOptionFactory.createByWidgetClass(
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Widget_Type_Checkbox"), BaseUtils.readIcon("/com/fr/design/images/buttonicon/widget/check_box_16.png"),
            CheckBox.class);

    public static final WidgetOption LIST = WidgetOptionFactory.createByWidgetClass(
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Widget_Type_List"), BaseUtils.readIcon("/com/fr/web/images/form/resources/list_16.png"),
            ListEditor.class);


    public static final WidgetOption TEXTEDITOR = WidgetOptionFactory.createByWidgetClass(
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Widget_Type_Text")
            , BaseUtils.readIcon("/com/fr/design/images/buttonicon/widget/text_field_16.png"),
            TextEditor.class);

    public static final WidgetOption TEXTAREA = WidgetOptionFactory.createByWidgetClass(
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Widget_Type_Textarea"), BaseUtils.readIcon("/com/fr/design/images/buttonicon/widget/text_area_16.png"),
            TextArea.class);

    public static final WidgetOption PASSWORD = WidgetOptionFactory.createByWidgetClass(
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Widget_Type_Password"),
            BaseUtils.readIcon("/com/fr/design/images/buttonicon/widget/password_field_16.png"), Password.class);

    public static final WidgetOption IFRAMEDITOR = WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Form-Iframe"), BaseUtils.readIcon("/com/fr/web/images/form/resources/iframe_16.png"),
            IframeEditor.class);

}