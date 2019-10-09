package com.fr.design.designer.properties;

import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.controlpane.UIListControlPane;
import com.fr.design.gui.frpane.ListenerUpdatePane;
import com.fr.design.javascript.EmailPane;
import com.fr.design.javascript.JavaScriptActionPane;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.widget.EventCreator;
import com.fr.design.write.submit.DBManipulationPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.designer.creator.XCreator;
import com.fr.form.event.Listener;
import com.fr.design.form.javascript.FormEmailPane;
import com.fr.form.ui.Widget;

import com.fr.general.NameObject;
import com.fr.report.web.util.ReportEngineEventMapping;
import com.fr.stable.Nameable;

import javax.swing.*;
import java.util.ArrayList;

public class EventPropertyTable extends UIListControlPane {

    private XCreator creator;
    private FormDesigner designer;

    public EventPropertyTable(FormDesigner designer) {
        super();
        this.setNameListEditable(false);
        this.designer = designer;
    }

    @Override
    public String getAddItemText() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Add_Event");
    }

    public static class WidgetEventListenerUpdatePane extends ListenerUpdatePane {

        @Override
        protected JavaScriptActionPane createJavaScriptActionPane() {
            return new JavaScriptActionPane() {
                @Override
                protected DBManipulationPane createDBManipulationPane() {
                    return new DBManipulationPane(ValueEditorPaneFactory.formEditors());
                }

                @Override
                protected String title4PopupWindow() {
                    return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Set_Callback_Function");
                }

                @Override
                protected EmailPane initEmaiPane() {
                    return new FormEmailPane();
                }

                @Override
                public boolean isForm() {
                    return true;
                }

                protected String[] getDefaultArgs() {
                    return new String[0];
                }

            };
        }

        @Override
        protected boolean supportCellAction() {
            return false;
        }
    }

    private String switchLang(String eventName) {
        // 在 properties 文件中找到相应的 key 值
        String localeKey = ReportEngineEventMapping.getLocaleName(eventName);
        return com.fr.design.i18n.Toolkit.i18nText(localeKey);
    }

    /**
     * 刷新
     */
    public void refresh() {
        int selectionSize = designer.getSelectionModel().getSelection().size();
        if (selectionSize == 0 || selectionSize == 1) {
            this.creator = selectionSize == 0 ? designer.getRootComponent() : designer.getSelectionModel()
                    .getSelection().getSelectedCreator();
        } else {
            this.creator = null;
            ((DefaultListModel) nameableList.getModel()).removeAllElements();
            checkButtonEnabled();
            return;
        }
        Widget widget = creator.toData();

        refreshNameableCreator(EventCreator.createEventCreator(widget.supportedEvents(), WidgetEventListenerUpdatePane.class));
    }

    public void populateNameObjects() {
        Widget widget = creator.toData();

        ArrayList<NameObject> nameObjectList = new ArrayList<>();
        for (int i = 0, size = widget.getListenerSize(); i < size; i++) {
            Listener listener = widget.getListener(i);
            if (!listener.isDefault()) {
                nameObjectList.add(i, new NameObject(switchLang(listener.getEventName()) + (i + 1), listener));
            }
        }
        populate(nameObjectList.toArray(new NameObject[widget.getListenerSize()]));
        checkButtonEnabled();
        this.repaint();
    }

    /**
     * 更新控件事件
     *
     * @param creator 控件
     */
    public void updateWidgetListener(XCreator creator) {
        (creator.toData()).clearListeners();
        Nameable[] res = this.update();
        for (int i = 0; i < res.length; i++) {
            NameObject nameObject = (NameObject) res[i];
            (creator.toData()).addListener((Listener) nameObject.getObject());
        }

        designer.fireTargetModified();
        checkButtonEnabled();
    }

    @Override
    protected String title4PopupWindow() {
        return "Event";
    }

    @Override
    public NameableCreator[] createNameableCreators() {
        return new NameableCreator[]{
                new EventCreator(Widget.EVENT_STATECHANGE, WidgetEventListenerUpdatePane.class)
        };
    }

    @Override
    public void saveSettings() {
        if (isPopulating) {
            return;
        }
        updateWidgetListener(creator);
    }
}
