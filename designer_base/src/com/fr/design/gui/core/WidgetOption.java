package com.fr.design.gui.core;

import com.fr.base.BaseUtils;
import com.fr.form.ui.*;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;

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
        WidgetManagerProvider mgr = WidgetManager.getProviderInstance();
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

    public static final WidgetOption DATEEDITOR = WidgetOptionFactory.createByWidgetClass(Inter.getLocText(new String[]{"Date", "Widget"}),
            BaseUtils.readIcon("/com/fr/design/images/buttonicon/widget/date_16.png"),
            DateEditor.class);

    public static final WidgetOption TREE = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("Form-View_Tree"),
            BaseUtils.readIcon("/com/fr/design/images/buttonicon/widget/tree_16.png"), TreeEditor.class);

    public static final WidgetOption TREECOMBOBOX = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Designer_Tree-ComboBox"), BaseUtils.readIcon("/com/fr/design/images/buttonicon/widget/comboboxtree.png"),
            TreeComboBoxEditor.class);

    public static final WidgetOption CHECKBOXGROUP = WidgetOptionFactory.createByWidgetClass(
            Inter.getLocText(new String[]{"Form-CheckBoxGroup", "Widget"}), BaseUtils.readIcon("/com/fr/design/images/buttonicon/widget/checkbox_group_16.png"), CheckBoxGroup.class);

    public static final WidgetOption RADIOGROUP = WidgetOptionFactory.createByWidgetClass(
            Inter.getLocText(new String[]{"Form-RadioGroup", "Widget"}), BaseUtils.readIcon("/com/fr/design/images/buttonicon/widget/button_group_16.png"),
            RadioGroup.class);

    public static final WidgetOption NUMBEREDITOR = WidgetOptionFactory.createByWidgetClass(
            Inter.getLocText(new String[]{"Number", "Widget"}), BaseUtils.readIcon("/com/fr/design/images/buttonicon/widget/number_field_16.png"), NumberEditor.class);

    public static final WidgetOption LABEL = WidgetOptionFactory.createByWidgetClass(
            Inter.getLocText(new String[]{"Label", "Widget"}), BaseUtils.readIcon("/com/fr/design/images/buttonicon/widget/label_16.png"),
            Label.class);

    public static final WidgetOption BUTTON = WidgetOptionFactory.createByWidgetClass(
            Inter.getLocText(new String[]{"Form-Button", "Widget"}), BaseUtils.readIcon("/com/fr/web/images/form/resources/button_16.png"),
            Button.class);

    public static final WidgetOption FREEBUTTON = WidgetOptionFactory.createByWidgetClass(
            Inter.getLocText(new String[]{"Form-Button", "Widget"}), BaseUtils.readIcon("/com/fr/design/images/buttonicon/widget/button_16.png"),
            FreeButton.class);

    public static final WidgetOption MULTI_FILEEDITOR = WidgetOptionFactory.createByWidgetClass(
            Inter.getLocText(new String[]{"File", "Widget"}), BaseUtils.readIcon("/com/fr/design/images/buttonicon/widget/files_up.png"),
            MultiFileEditor.class);

    public static final WidgetOption COMBOBOX = WidgetOptionFactory.createByWidgetClass(
            Inter.getLocText(new String[]{"Form-ComboBox", "Widget"}), BaseUtils.readIcon("/com/fr/design/images/buttonicon/widget/combo_box_16.png"),
            ComboBox.class);

    public static final WidgetOption COMBOCHECKBOX = WidgetOptionFactory.createByWidgetClass(
            Inter.getLocText(new String[]{"Form-ComboCheckBox", "Widget"}), BaseUtils.readIcon("/com/fr/design/images/buttonicon/widget/combo_check_16.png"),
            ComboCheckBox.class);

    public static final WidgetOption CHECKBOX = WidgetOptionFactory.createByWidgetClass(
            Inter.getLocText(new String[]{"Form-CheckBox", "Widget"}), BaseUtils.readIcon("/com/fr/design/images/buttonicon/widget/check_box_16.png"),
            CheckBox.class);

    public static final WidgetOption LIST = WidgetOptionFactory.createByWidgetClass(
            Inter.getLocText(new String[]{"Form-List", "Widget"}), BaseUtils.readIcon("/com/fr/web/images/form/resources/list_16.png"),
            ListEditor.class);

    public static final WidgetOption TABLETREE = WidgetOptionFactory.createByWidgetClass(
            Inter.getLocText(new String[]{"Form-TableTree", "Widget"}), BaseUtils.readIcon("/com/fr/web/images/form/resources/list_16.png"),
            TableTree.class);

    public static final WidgetOption TEXTEDITOR = WidgetOptionFactory.createByWidgetClass(Inter.getLocText(new String[]{"Text", "Widget"})
            , BaseUtils.readIcon("/com/fr/design/images/buttonicon/widget/text_field_16.png"),
            TextEditor.class);

    public static final WidgetOption TEXTAREA = WidgetOptionFactory.createByWidgetClass(
            Inter.getLocText(new String[]{"Form-TextArea", "Widget"}), BaseUtils.readIcon("/com/fr/design/images/buttonicon/widget/text_area_16.png"),
            TextArea.class);

    public static final WidgetOption PASSWORD = WidgetOptionFactory.createByWidgetClass(
            Inter.getLocText(new String[]{"Form-Password", "Widget"}),
            BaseUtils.readIcon("/com/fr/design/images/buttonicon/widget/password_field_16.png"), Password.class);

    public static final WidgetOption IFRAMEDITOR = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Designer_Form-Iframe"), BaseUtils.readIcon("/com/fr/web/images/form/resources/iframe_16.png"),
            IframeEditor.class);

//    public static final WidgetOption DATATABLE = WidgetOptionFactory.createByWidgetClass(Inter
//                    .getLocText("Form-DataTable"), BaseUtils.readIcon("/com/fr/web/images/form/resources/table_16.png"),
//            DataTable.class);

//	public static final WidgetOption CHARTCONTAINER = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("Chart")
//			+ Inter.getLocText("Widget"), ChartEditor.class);


//	public static final WidgetOption ABSOLUTELAYOUTCONTAINER = WidgetOptionFactory.createByWidgetClass(Inter
//			.getLocText("AbsoluteLayout"), BaseUtils.readIcon("/com/fr/web/images/form/resources/layout_absolute.png"),
//			WAbsoluteLayout.class);
//
//	public static final WidgetOption BORDERLAYOUTCONTAINER = WidgetOptionFactory.createByWidgetClass(Inter
//			.getLocText("BorderLayout"), BaseUtils.readIcon("/com/fr/web/images/form/resources/layout_border.png"),
//			WBorderLayout.class);
//
//	public static final WidgetOption CARDLAYOUTCONTAINER = WidgetOptionFactory.createByWidgetClass(Inter
//			.getLocText("CardLayout"), BaseUtils.readIcon("/com/fr/web/images/form/resources/card_layout_16.png"),
//			WCardLayout.class);
//
//	public static final WidgetOption HORIZONTALBOXLAYOUTCONTAINER = WidgetOptionFactory.createByWidgetClass(Inter
//			.getLocText("Layout-HBox"), BaseUtils.readIcon("/com/fr/web/images/form/resources/boxlayout_h_16.png"),
//			WHorizontalBoxLayout.class);
//
//	public static final WidgetOption VERTICALBOXLAYOUTCONTAINER = WidgetOptionFactory.createByWidgetClass(Inter
//			.getLocText("VerticalBoxLayout"), BaseUtils.readIcon("/com/fr/web/images/form/resources/boxlayout_v_16.png"),
//			WVerticalBoxLayout.class);

}