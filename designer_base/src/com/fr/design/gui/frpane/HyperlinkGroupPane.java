package com.fr.design.gui.frpane;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.actions.HyperlinkPluginAction;
import com.fr.design.actions.UpdateAction;
import com.fr.design.fun.HyperlinkProvider;
import com.fr.design.gui.controlpane.JListControlPane;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.module.DesignModuleFactory;
import com.fr.general.Inter;
import com.fr.general.NameObject;
import com.fr.js.JavaScript;
import com.fr.js.NameJavaScript;
import com.fr.js.NameJavaScriptGroup;
import com.fr.plugin.PluginManager;
import com.fr.stable.ArrayUtils;
import com.fr.stable.Nameable;

import java.util.ArrayList;
import java.util.List;

/**
 * 超级链接 界面.
 *
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-6-25 上午11:17:57
 */
public class HyperlinkGroupPane extends JListControlPane {

    /**
     * 生成添加按钮的NameableCreator
     *
     * @return 返回Nameable按钮数组.
     */
    public NameableCreator[] createNameableCreators() {
        NameableCreator[] creators = DesignModuleFactory.getHyperlinkGroupType().getHyperlinkCreators();
        PluginManager.getInstance().setExtensionPoint(HyperlinkPluginAction.XML_TAG);
        ArrayList<UpdateAction> templateArrayLisy = PluginManager.getInstance().getResultList();
//        if (templateArrayLisy.isEmpty()) {
//            return creators;
//        }
        NameableCreator[] pluginCreators = new NameableCreator[templateArrayLisy.size()];
        for (int i = 0; i < templateArrayLisy.size(); i++) {
            pluginCreators[i] = ((HyperlinkPluginAction) templateArrayLisy.get(i)).getHyperlinkCreator();
        }
        HyperlinkProvider[] providers = ExtraDesignClassManager.getInstance().getHyperlinkProvider();
        List<NameableCreator> creatorList = new ArrayList<NameableCreator>();
        for (HyperlinkProvider provider : providers) {
            NameableCreator nc = provider.createHyperlinkCreator();
            creatorList.add(nc);
        }
        return (NameableCreator[]) ArrayUtils.addAll(creatorList.toArray(new NameableCreator[creatorList.size()]), ArrayUtils.addAll(creators, pluginCreators));
    }

    /**
     * 弹出列表的标题.
     *
     * @return 返回标题字符串.
     */
    public String title4PopupWindow() {
        return Inter.getLocText("FR-Designer_Hyperlink");
    }

    public void populate(NameJavaScriptGroup nameHyperlink_array) {
        java.util.List<NameObject> list = new ArrayList<NameObject>();
        if (nameHyperlink_array != null) {
            for (int i = 0; i < nameHyperlink_array.size(); i++) {
                list.add(new NameObject(nameHyperlink_array.getNameHyperlink(i).getName(), nameHyperlink_array.getNameHyperlink(i).getJavaScript()));
            }
        }

        this.populate(list.toArray(new NameObject[list.size()]));
    }

    /**
     * updateJs的Group
     *
     * @return 返回NameJavaScriptGroup
     */
    public NameJavaScriptGroup updateJSGroup() {
        Nameable[] res = this.update();
        NameJavaScript[] res_array = new NameJavaScript[res.length];
        for (int i = 0; i < res.length; i++) {
            NameObject no = (NameObject) res[i];
            res_array[i] = new NameJavaScript(no.getName(), (JavaScript) no.getObject());
        }

        return new NameJavaScriptGroup(res_array);
    }
}