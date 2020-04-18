package com.fr.design.widget;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.gui.core.WidgetConstants;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.parameter.RootDesignDefinePane;
import com.fr.design.widget.ui.designer.CheckBoxDefinePane;
import com.fr.design.widget.ui.designer.CheckBoxGroupDefinePane;
import com.fr.design.widget.ui.designer.ComboBoxDefinePane;
import com.fr.design.widget.ui.designer.ComboCheckBoxDefinePane;
import com.fr.design.widget.ui.designer.DateEditorDefinePane;
import com.fr.design.widget.ui.designer.FreeButtonDefinePane;
import com.fr.design.widget.ui.designer.IframeEditorDefinePane;
import com.fr.design.widget.ui.designer.LabelDefinePane;
import com.fr.design.widget.ui.designer.MultiFileEditorPane;
import com.fr.design.widget.ui.designer.NoneWidgetDefinePane;
import com.fr.design.widget.ui.designer.NumberEditorDefinePane;
import com.fr.design.widget.ui.designer.PasswordDefinePane;
import com.fr.design.widget.ui.designer.RadioDefinePane;
import com.fr.design.widget.ui.designer.RadioGroupDefinePane;
import com.fr.design.widget.ui.designer.TextAreaDefinePane;
import com.fr.design.widget.ui.designer.TextFieldEditorDefinePane;
import com.fr.design.widget.ui.designer.TreeComboBoxEditorDefinePane;
import com.fr.design.widget.ui.designer.TreeEditorDefinePane;
import com.fr.design.widget.ui.designer.UserEditorDefinePane;
import com.fr.design.widget.ui.designer.WidgetDefinePane;
import com.fr.design.widget.ui.designer.layout.BorderStyleWidgetDefinePane;
import com.fr.design.widget.ui.designer.layout.ElementEditorDefinePane;
import com.fr.design.widget.ui.designer.layout.FRAbsoluteBodyLayoutDefinePane;
import com.fr.design.widget.ui.designer.layout.FRAbsoluteLayoutDefinePane;
import com.fr.design.widget.ui.designer.layout.FRFitLayoutDefinePane;
import com.fr.design.widget.ui.designer.layout.WCardLayoutDefinePane;
import com.fr.design.widget.ui.designer.layout.WCardMainLayoutDefinePane;
import com.fr.design.widget.ui.designer.layout.WCardTagLayoutDefinePane;
import com.fr.design.widget.ui.designer.layout.WTabFitLayoutDefinePane;
import com.fr.form.parameter.FormSubmitButton;
import com.fr.form.ui.AbstractBorderStyleWidget;
import com.fr.form.ui.Button;
import com.fr.form.ui.CheckBox;
import com.fr.form.ui.CheckBoxGroup;
import com.fr.form.ui.ComboBox;
import com.fr.form.ui.ComboCheckBox;
import com.fr.form.ui.DateEditor;
import com.fr.form.ui.ElementCaseEditor;
import com.fr.form.ui.FreeButton;
import com.fr.form.ui.IframeEditor;
import com.fr.form.ui.Label;
import com.fr.form.ui.MultiFileEditor;
import com.fr.form.ui.NameWidget;
import com.fr.form.ui.NoneWidget;
import com.fr.form.ui.NumberEditor;
import com.fr.form.ui.Password;
import com.fr.form.ui.Radio;
import com.fr.form.ui.RadioGroup;
import com.fr.form.ui.TextArea;
import com.fr.form.ui.TextEditor;
import com.fr.form.ui.TreeComboBoxEditor;
import com.fr.form.ui.TreeEditor;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WAbsoluteBodyLayout;
import com.fr.form.ui.container.WAbsoluteLayout;
import com.fr.form.ui.container.WCardLayout;
import com.fr.form.ui.container.WFitLayout;
import com.fr.form.ui.container.WParameterLayout;
import com.fr.form.ui.container.cardlayout.WCardMainBorderLayout;
import com.fr.form.ui.container.cardlayout.WCardTagLayout;
import com.fr.form.ui.container.cardlayout.WTabFitLayout;
import com.fr.invoke.Reflect;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.bridge.BridgeMark;
import com.fr.stable.bridge.StableFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-14
 * Time   : 上午11:17
 */
public class FormWidgetDefinePaneFactoryBase {
    private static Map<Class<? extends Widget>, Appearance> defineMap = new HashMap<Class<? extends Widget>, Appearance>();

    static {
        defineMap.put(NumberEditor.class, new Appearance(NumberEditorDefinePane.class, WidgetConstants.NUMBER + ""));
        defineMap.put(DateEditor.class, new Appearance(DateEditorDefinePane.class, WidgetConstants.DATE + ""));
        defineMap.put(ComboCheckBox.class, new Appearance(ComboCheckBoxDefinePane.class, WidgetConstants.COMBOCHECKBOX + ""));
        defineMap.put(Radio.class, new Appearance(RadioDefinePane.class, WidgetConstants.RADIO + ""));
        defineMap.put(CheckBox.class, new Appearance(CheckBoxDefinePane.class, WidgetConstants.CHECKBOX + ""));
        defineMap.put(TreeComboBoxEditor.class, new Appearance(TreeComboBoxEditorDefinePane.class, WidgetConstants.TREECOMBOBOX + ""));
        defineMap.put(TreeEditor.class, new Appearance(TreeEditorDefinePane.class, WidgetConstants.TREE + ""));
        defineMap.put(MultiFileEditor.class, new Appearance(MultiFileEditorPane.class, WidgetConstants.MULTI_FILE + ""));
        defineMap.put(TextArea.class, new Appearance(TextAreaDefinePane.class, WidgetConstants.TEXTAREA + ""));
        defineMap.put(Password.class, new Appearance(PasswordDefinePane.class, WidgetConstants.PASSWORD + ""));
        defineMap.put(IframeEditor.class, new Appearance(IframeEditorDefinePane.class, WidgetConstants.IFRAME + ""));
        defineMap.put(TextEditor.class, new Appearance(TextFieldEditorDefinePane.class, WidgetConstants.TEXT + ""));
        defineMap.put(NameWidget.class, new Appearance(UserEditorDefinePane.class, "UserDefine"));
        defineMap.put(ComboBox.class, new Appearance(ComboBoxDefinePane.class, WidgetConstants.COMBOBOX + ""));
        defineMap.put(RadioGroup.class, new Appearance(RadioGroupDefinePane.class, WidgetConstants.RADIOGROUP + ""));
        defineMap.put(CheckBoxGroup.class, new Appearance(CheckBoxGroupDefinePane.class, WidgetConstants.CHECKBOXGROUP + ""));

        defineMap.put(NoneWidget.class, new Appearance(NoneWidgetDefinePane.class, WidgetConstants.NONE + ""));
        defineMap.put(Button.class, new Appearance(FreeButtonDefinePane.class, WidgetConstants.BUTTON + ""));
        defineMap.put(FreeButton.class, new Appearance(FreeButtonDefinePane.class, WidgetConstants.BUTTON + ""));
        defineMap.put(FormSubmitButton.class, new Appearance(FreeButtonDefinePane.class, WidgetConstants.BUTTON + ""));
        defineMap.put(WFitLayout.class, new Appearance(FRFitLayoutDefinePane.class, "wFitLayout"));
        if (StableFactory.getMarkedClass(BridgeMark.CHART_EDITOR, AbstractBorderStyleWidget.class) != null) {
            defineMap.put(StableFactory.getMarkedClass(BridgeMark.CHART_EDITOR, AbstractBorderStyleWidget.class), new Appearance(BorderStyleWidgetDefinePane.class, "chartEditor"));
        }

        defineMap.put(WAbsoluteLayout.class, new Appearance(FRAbsoluteLayoutDefinePane.class, "wAbsoluteLayout"));
        defineMap.put(ElementCaseEditor.class, new Appearance(ElementEditorDefinePane.class, "elementCaseEditor"));
        defineMap.put(WAbsoluteBodyLayout.class, new Appearance(FRAbsoluteBodyLayoutDefinePane.class, "wAbsoluteBodyLayout"));
        defineMap.put(WParameterLayout.class, new Appearance(RootDesignDefinePane.class, "wParameterLayout"));
        defineMap.put(WCardMainBorderLayout.class, new Appearance(WCardMainLayoutDefinePane.class, "wCardMainBorderLayout"));
        defineMap.put(WCardTagLayout.class, new Appearance(WCardTagLayoutDefinePane.class, "wCardMainBorderLayout"));
        defineMap.put(WCardLayout.class, new Appearance(WCardLayoutDefinePane.class, "wCardLayout"));
        defineMap.put(Label.class, new Appearance(LabelDefinePane.class, "label"));
        defineMap.put(WTabFitLayout.class, new Appearance(WTabFitLayoutDefinePane.class, "wTabFitLayout"));

    }


    private FormWidgetDefinePaneFactoryBase() {

    }


    public static void registerDefinePane(Class<? extends Widget> widget, Appearance appearance) {
        defineMap.put(widget, appearance);
    }

    public static RN createWidgetDefinePane(XCreator creator, FormDesigner designer, Widget widget, Operator operator) {
        if (isExtraXWidget(widget)) {
            WidgetDefinePane widgetDefinePane = new WidgetDefinePane(creator, designer);
            return new RN(widgetDefinePane, widgetDefinePane.title4PopupWindow());
        }
        Appearance dn = defineMap.get(widget.getClass());
        DataModify<Widget> definePane = null;
        try {
            definePane = Reflect.on(dn.getDefineClass()).create(creator).get();
            operator.did(definePane.dataUI(), dn.getDisplayName());
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        return new RN(definePane, dn.getDisplayName());
    }

    public static boolean isExtraXWidget(Widget widget) {
        return defineMap.get(widget.getClass()) == null;
    }

    public static class RN {
        private DataModify<Widget> definePane;
        private String cardName;

        public RN(DataModify<Widget> definePane, String cardName) {
            this.definePane = definePane;
            this.cardName = cardName;
        }

        public DataModify<Widget> getDefinePane() {
            return definePane;
        }

        public String getCardName() {
            return cardName;
        }
    }

}
