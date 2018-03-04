package com.fr.design.webattr;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.report.web.ToolBarManager;
import com.fr.report.web.WebView;
import com.fr.web.attr.ReportWebConfig;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ViewWebSettingPane extends WebSettingPane<WebView> {
    private UICheckBox sortCheckBox;
    private UICheckBox conditionFilterBox;
    private UICheckBox listFilterBox;

    public ViewWebSettingPane() {
        super();
    }

    @Override
    protected JPanel createOtherSetPane() {
        sortCheckBox = new UICheckBox(Inter.getLocText("FR-Engine-Sort_Sort"));
        conditionFilterBox = new UICheckBox(Inter.getLocText("FR-Engine-Selection_Filter"));
        listFilterBox = new UICheckBox(Inter.getLocText("FR-Engine-List_Filter"));

        sortCheckBox.setSelected(true);
        conditionFilterBox.setSelected(true);
        listFilterBox.setSelected(true);
        return GUICoreUtils.createFlowPane(new Component[]{new UILabel(Inter.getLocText("FR-Engine_ViewPreview") + ":"),
                sortCheckBox, conditionFilterBox, listFilterBox}, FlowLayout.LEFT, 6);
    }

    @Override
    protected void populateSubWebSettingrBean(WebView ob) {
        if (ob == null) {
            ob = new WebView();
        }
        listFilterBox.setSelected(ob.isListFuncCheck());
        conditionFilterBox.setSelected(ob.isConditionFuncCheck());
        sortCheckBox.setSelected(ob.isSortFuncCheck());
    }

    @Override
    protected WebView updateSubWebSettingBean() {
        WebView webView = new WebView();
        webView.setListFuncCheck(listFilterBox.isSelected());
        webView.setConditionFuncCheck(conditionFilterBox.isSelected());
        webView.setSortFuncCheck(sortCheckBox.isSelected());
        return webView;
    }

    @Override
    protected WidgetOption[] getToolBarInstance() {
        List<WidgetOption> defaultOptions = Arrays.asList(ReportWebWidgetConstants.getViewToolBarInstance());
        List<WidgetOption> extraOptions = Arrays.asList(ExtraDesignClassManager.getInstance().getWebWidgetOptions());
        List<WidgetOption> options = new ArrayList<WidgetOption>();
        options.addAll(defaultOptions);
        options.addAll(extraOptions);
        return options.toArray(new WidgetOption[options.size()]);
    }

    @Override
    protected ToolBarManager getDefaultToolBarManager() {
        return ToolBarManager.createDefaultViewToolBar();
    }

    @Override
    protected WebView getWebContent(ReportWebConfig reportWebAttr) {
        return reportWebAttr == null ? null : reportWebAttr.getWebView();
    }

    @Override
    protected String[] getEventNames() {
        return new WebView().supportedEvents();
    }

    @Override
    protected void setWebContent(ReportWebConfig reportWebAttr, WebView webContent) {
        reportWebAttr.setWebView(webContent);
    }
    @Override
    protected void checkEnabled(boolean isSelected) {
        super.checkEnabled(isSelected);
        sortCheckBox.setEnabled(isSelected);
        conditionFilterBox.setEnabled(isSelected);
        listFilterBox.setEnabled(isSelected);
    }
    protected void setDefault(){
        super.setDefault();
        sortCheckBox.setSelected(true);
        conditionFilterBox.setSelected(true);
        listFilterBox.setSelected(true);
    }
}