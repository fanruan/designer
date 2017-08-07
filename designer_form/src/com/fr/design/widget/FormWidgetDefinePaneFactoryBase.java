package com.fr.design.widget;

import com.fr.base.FRContext;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.gui.core.WidgetConstants;
import com.fr.design.parameter.RootDesignDefinePane;
import com.fr.design.widget.ui.designer.*;
import com.fr.design.widget.ui.designer.layout.*;
import com.fr.form.ui.*;
import com.fr.form.ui.container.*;
import com.fr.form.ui.container.cardlayout.WCardMainBorderLayout;
import com.fr.form.ui.container.cardlayout.WTabFitLayout;
import com.fr.general.Inter;
import com.fr.stable.bridge.BridgeMark;
import com.fr.stable.bridge.StableFactory;

import java.lang.reflect.Constructor;
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
        defineMap.put(ComboCheckBox.class, new Appearance(ComboCheckBoxDefinePane.class, WidgetConstants.COMBOCHECKBOX + ""));
        defineMap.put(ComboBox.class, new Appearance(ComboBoxDefinePane.class, WidgetConstants.COMBOBOX + ""));
        defineMap.put(RadioGroup.class, new Appearance(RadioGroupDefinePane.class, WidgetConstants.RADIOGROUP + ""));
        defineMap.put(CheckBoxGroup.class, new Appearance(CheckBoxGroupDefinePane.class, WidgetConstants.CHECKBOXGROUP + ""));

        defineMap.put(NoneWidget.class, new Appearance(NoneWidgetDefinePane.class, WidgetConstants.NONE + ""));
        defineMap.put(Button.class, new Appearance(FreeButtonDefinePane.class, WidgetConstants.BUTTON + ""));
        defineMap.put(FreeButton.class, new Appearance(FreeButtonDefinePane.class, WidgetConstants.BUTTON + ""));
        defineMap.put(WFitLayout.class, new Appearance(FRFitLayoutDefinePane.class, Inter.getLocText("FR-Designer-Layout_Adaptive_Layout")));
        defineMap.put(WCardMainBorderLayout.class, new Appearance(WCardMainLayoutDefinePane.class, Inter.getLocText("WLayout-Card-ToolTips")));
//        if (StableFactory.getMarkedClass(BridgeMark.SUBMIT_BUTTON, Widget.class) != null) {
//            defineMap.put(StableFactory.getMarkedClass(BridgeMark.SUBMIT_BUTTON, Widget.class), new Appearance(ButtonDefinePane.class, WidgetConstants.BUTTON + ""));
//        }

        defineMap.put(WAbsoluteLayout.class, new Appearance(FRAbsoluteLayoutDefinePane.class, Inter.getLocText("FR-Designer_AbsoluteLayout")));
        defineMap.put(WAbsoluteBodyLayout.class, new Appearance(FRAbsoluteBodyLayoutDefinePane.class, Inter.getLocText("FR-Designer-Layout_Adaptive_Layout")));
        defineMap.put(WParameterLayout.class, new Appearance(RootDesignDefinePane.class, Inter.getLocText("FR-Designer_Para-Body")));
        defineMap.put(WCardMainBorderLayout.class, new Appearance(WCardMainLayoutDefinePane.class, "tab"));
        defineMap.put(WTitleLayout.class, new Appearance(WTitleLayoutDefinePane.class, "tab"));
        defineMap.put(Label.class, new Appearance(LabelDefinePane.class, "label"));
        defineMap.put(WTabFitLayout.class, new Appearance(WTabFitLayoutDefinePane.class, "label"));
        defineMap.putAll(ExtraDesignClassManager.getInstance().getCellWidgetOptionsMap());
    }

    private FormWidgetDefinePaneFactoryBase() {

    }

    public static RN createWidgetDefinePane(XCreator creator, Widget widget, Operator operator) {
        Appearance dn = defineMap.get(widget.getClass());
        DataModify<Widget> definePane = null;
        try {
            Constructor con =  dn.getDefineClass().getConstructor(XCreator.class);
            definePane  = (DataModify)con.newInstance(creator);
            operator.did(definePane.dataUI(), dn.getDisplayName());
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage(), e);
        }
        return new RN(definePane, dn.getDisplayName());
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