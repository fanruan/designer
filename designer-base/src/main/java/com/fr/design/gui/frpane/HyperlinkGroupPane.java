package com.fr.design.gui.frpane;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.designer.TargetComponent;
import com.fr.design.file.HistoryTemplateListCache;
import com.fr.design.fun.HyperlinkProvider;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.controlpane.UIListControlPane;
import com.fr.design.module.DesignModuleFactory;
import com.fr.general.GeneralContext;

import com.fr.general.NameObject;
import com.fr.js.JavaScript;
import com.fr.js.NameJavaScript;
import com.fr.js.NameJavaScriptGroup;
import com.fr.plugin.context.PluginContext;
import com.fr.plugin.manage.PluginFilter;
import com.fr.plugin.observer.PluginEvent;
import com.fr.plugin.observer.PluginEventListener;
import com.fr.stable.ListMap;
import com.fr.stable.Nameable;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * 超级链接 界面.
 *
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-6-25 上午11:17:57
 */
public abstract class HyperlinkGroupPane extends UIListControlPane {
    protected HyperlinkGroupPaneActionProvider hyperlinkGroupPaneActionProvider;

    public HyperlinkGroupPane(HyperlinkGroupPaneActionProvider hyperlinkGroupPaneActionProvider) {
        super();
        this.hyperlinkGroupPaneActionProvider = hyperlinkGroupPaneActionProvider;
    }

    @Override
    protected void initComponentPane() {
        super.initComponentPane();
        GeneralContext.listenPluginRunningChanged(new PluginEventListener() {

            @Override
            public void on(PluginEvent event) {
                refreshNameableCreator(createNameableCreators());
                HistoryTemplateListCache.getInstance().reloadCurrentTemplate();
            }
        }, new PluginFilter() {

            @Override
            public boolean accept(PluginContext context) {
                return context.contain(HyperlinkProvider.XML_TAG);
            }
        });
    }

    /**
     * 生成添加按钮的NameableCreator
     *
     * @return 返回Nameable按钮数组.
     */
    @Override
    public NameableCreator[] createNameableCreators() {
        Map<String, NameableCreator> nameCreators = new ListMap<>();
        NameableCreator[] creators = DesignModuleFactory.getHyperlinkGroupType().getHyperlinkCreators();
        for (NameableCreator creator : creators) {
            nameCreators.put(creator.menuName(), creator);
        }
        Set<HyperlinkProvider> providers = ExtraDesignClassManager.getInstance().getArray(HyperlinkProvider.XML_TAG);
        for (HyperlinkProvider provider : providers) {
            NameableCreator nc = provider.createHyperlinkCreator();
            nameCreators.put(nc.menuName(), nc);
        }
        return nameCreators.values().toArray(new NameableCreator[nameCreators.size()]);
    }

    /**
     * 弹出列表的标题.
     *
     * @return 返回标题字符串.
     */
    @Override
    public String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Hyperlink");
    }

    @Override
    protected String getAddItemText() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Add_Hyperlink");
    }

    public void populate(NameJavaScriptGroup hyperlinkArray) {
        java.util.List<NameObject> list = new ArrayList<>();
        if (hyperlinkArray != null) {
            for (int i = 0; i < hyperlinkArray.size(); i++) {
                list.add(new NameObject(hyperlinkArray.getNameHyperlink(i).getName(), hyperlinkArray.getNameHyperlink(i).getJavaScript()));
            }
        }

        this.populate(list.toArray(new NameObject[list.size()]));
    }

    public void populate(TargetComponent elementCasePane) {
        hyperlinkGroupPaneActionProvider.populate(this, elementCasePane);
    }

    /**
     * updateJs的Group
     *
     * @return 返回NameJavaScriptGroup
     */
    public NameJavaScriptGroup updateJSGroup() {
        Nameable[] res = this.update();
        NameJavaScript[] resArray = new NameJavaScript[res.length];
        for (int i = 0; i < res.length; i++) {
            NameObject no = (NameObject) res[i];
            resArray[i] = new NameJavaScript(no.getName(), (JavaScript) no.getObject());
        }

        return new NameJavaScriptGroup(resArray);
    }

    @Override
    public void saveSettings() {
        if (isPopulating || !needAutoSave()) {
            return;
        }
        hyperlinkGroupPaneActionProvider.saveSettings(this);
    }

    /**
     * 是否需要自动保存到超级链接属性中
     *
     * @return 是否需要自动保存
     */
    public boolean needAutoSave() {
        return true;
    }
}