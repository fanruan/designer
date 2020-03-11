package com.fr.design.widget;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.fun.CellWidgetOptionProvider;
import com.fr.design.gui.core.WidgetConstants;
import com.fr.design.widget.ui.ButtonDefinePane;
import com.fr.design.widget.ui.CheckBoxDefinePane;
import com.fr.design.widget.ui.CheckBoxGroupDefinePane;
import com.fr.design.widget.ui.ComboBoxDefinePane;
import com.fr.design.widget.ui.ComboCheckBoxDefinePane;
import com.fr.design.widget.ui.DateEditorDefinePane;
import com.fr.design.widget.ui.IframeEditorDefinePane;
import com.fr.design.widget.ui.ListEditorDefinePane;
import com.fr.design.widget.ui.MultiFileEditorPane;
import com.fr.design.widget.ui.NoneWidgetDefinePane;
import com.fr.design.widget.ui.NumberEditorDefinePane;
import com.fr.design.widget.ui.PasswordDefinePane;
import com.fr.design.widget.ui.RadioDefinePane;
import com.fr.design.widget.ui.RadioGroupDefinePane;
import com.fr.design.widget.ui.TextAreaDefinePane;
import com.fr.design.widget.ui.TextFieldEditorDefinePane;
import com.fr.design.widget.ui.TreeComboBoxEditorDefinePane;
import com.fr.design.widget.ui.TreeEditorDefinePane;
import com.fr.design.widget.ui.UserEditorDefinePane;
import com.fr.form.ui.Button;
import com.fr.form.ui.CheckBox;
import com.fr.form.ui.CheckBoxGroup;
import com.fr.form.ui.ComboBox;
import com.fr.form.ui.ComboCheckBox;
import com.fr.form.ui.DateEditor;
import com.fr.form.ui.FreeButton;
import com.fr.form.ui.IframeEditor;
import com.fr.form.ui.ListEditor;
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
import com.fr.general.GeneralContext;
import com.fr.log.FineLoggerFactory;
import com.fr.plugin.context.PluginContext;
import com.fr.plugin.injectable.PluginModule;
import com.fr.plugin.manage.PluginFilter;
import com.fr.plugin.observer.PluginEvent;
import com.fr.plugin.observer.PluginEventListener;
import com.fr.report.web.button.form.TreeNodeToggleButton;
import com.fr.report.web.button.write.AppendRowButton;
import com.fr.report.web.button.write.DeleteRowButton;
import com.fr.stable.bridge.BridgeMark;
import com.fr.stable.bridge.StableFactory;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-14
 * Time   : 上午11:17
 */
public class WidgetDefinePaneFactory {
    
    private static Map<Class<? extends Widget>, Appearance> defineMap = new HashMap<Class<? extends Widget>, Appearance>();
    private static Map<Class<? extends Widget>, Appearance> pluginDefineMap = ExtraDesignClassManager.getInstance().getCellWidgetOptionsMap();

    static {
        putDefault();

        GeneralContext.listenPluginRunningChanged(new PluginEventListener() {
            @Override
            public void on(PluginEvent event) {
                refreshPluginMap();
            }
        }, new PluginFilter() {
            @Override
            public boolean accept(PluginContext context) {
                return context.contain(PluginModule.ExtraDesign, CellWidgetOptionProvider.XML_TAG);
            }
        });
    }

    private WidgetDefinePaneFactory() {

    }

    private static void putDefault() {
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
        defineMap.put(ListEditor.class, new Appearance(ListEditorDefinePane.class, WidgetConstants.LIST + ""));
        defineMap.put(ComboBox.class, new Appearance(ComboBoxDefinePane.class, WidgetConstants.COMBOBOX + ""));
        defineMap.put(RadioGroup.class, new Appearance(RadioGroupDefinePane.class, WidgetConstants.RADIOGROUP + ""));
        defineMap.put(CheckBoxGroup.class, new Appearance(CheckBoxGroupDefinePane.class, WidgetConstants.CHECKBOXGROUP + ""));

        defineMap.put(NoneWidget.class, new Appearance(NoneWidgetDefinePane.class, WidgetConstants.NONE + ""));
        defineMap.put(Button.class, new Appearance(ButtonDefinePane.class, WidgetConstants.BUTTON + ""));
        defineMap.put(FreeButton.class, new Appearance(ButtonDefinePane.class, WidgetConstants.BUTTON + ""));
        if (StableFactory.getMarkedClass(BridgeMark.SUBMIT_BUTTON, Widget.class) != null) {
            defineMap.put(StableFactory.getMarkedClass(BridgeMark.SUBMIT_BUTTON, Widget.class), new Appearance(ButtonDefinePane.class, WidgetConstants.BUTTON + ""));
        }
        defineMap.put(AppendRowButton.class, new Appearance(ButtonDefinePane.class, WidgetConstants.BUTTON + ""));
        defineMap.put(DeleteRowButton.class, new Appearance(ButtonDefinePane.class, WidgetConstants.BUTTON + ""));
        defineMap.put(TreeNodeToggleButton.class, new Appearance(ButtonDefinePane.class, WidgetConstants.BUTTON + ""));
    }

    private static void refreshPluginMap() {
        pluginDefineMap.clear();
        pluginDefineMap.putAll(ExtraDesignClassManager.getInstance().getCellWidgetOptionsMap());
    }

    @Nullable
    public static RN createWidgetDefinePane(Widget widget, Operator operator) {
    
        Appearance dn = defineMap.get(widget.getClass());
        // 再走一遍插件。
        if (dn == null) {
            dn = pluginDefineMap.get(widget.getClass());
        }
        if (dn != null) {
            DataModify<Widget> definePane = null;
            try {
                definePane = (DataModify) dn.getDefineClass().newInstance();
                definePane.populateBean(widget);
                operator.did(definePane.dataUI(), dn.getDisplayName());
            } catch (Exception e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
            return new RN(definePane, dn.getDisplayName());
        }
        return null;
    }

    public static class RN {
        private DataModify<Widget> definePane;
        private String cardName;

        public RN(DataModify<Widget> definePane, String cardName) {
            this.definePane = definePane;
            this.cardName = cardName;
        }

        public DataModify<? extends Widget> getDefinePane() {
            return definePane;
        }

        public String getCardName() {
            return cardName;
        }
    }

}