package com.fr.plugin.chart.custom.component;

import com.fr.chart.chartattr.Plot;
import com.fr.chart.web.ChartHyperPoplink;
import com.fr.chart.web.ChartHyperRelateCellLink;
import com.fr.chart.web.ChartHyperRelateFloatLink;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chart.javascript.ChartEmailPane;
import com.fr.design.fun.HyperlinkProvider;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.frpane.UICorrelationComboBoxPane;
import com.fr.design.gui.imenutable.UIMenuNameableCreator;
import com.fr.general.Inter;
import com.fr.js.*;
import com.fr.plugin.chart.designer.other.HyperlinkMapFactory;
import com.fr.stable.bridge.StableFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Fangjie on 2016/4/28.
 */
public class VanChartHyperLinkPane extends UICorrelationComboBoxPane {

    public VanChartHyperLinkPane() {
        super();
    }

    public void populate(Plot plot) {
        HashMap paneMap = getHyperlinkMap(plot);

        //安装平台内打开插件时,添加相应按钮
        Set<HyperlinkProvider> providers = ExtraDesignClassManager.getInstance().getArray(HyperlinkProvider.XML_TAG);
        for (HyperlinkProvider provider : providers) {
            NameableCreator nc = provider.createHyperlinkCreator();
            //todo@shine9.0
           // paneMap.put(nc.getHyperlink(), nc.getUpdatePane());
        }

        java.util.List<UIMenuNameableCreator> list = refreshList(paneMap);
        refreshMenuAndAddMenuAction(list);

        java.util.List<UIMenuNameableCreator> hyperList = new ArrayList<UIMenuNameableCreator>();
        NameJavaScriptGroup nameGroup = populateHotHyperLink(plot);
        for(int i = 0; nameGroup != null &&  i < nameGroup.size(); i++) {
            NameJavaScript javaScript = nameGroup.getNameHyperlink(i);
            if(javaScript != null && javaScript.getJavaScript() != null) {
                JavaScript script = javaScript.getJavaScript();
                hyperList.add(new UIMenuNameableCreator(javaScript.getName(), script, getUseMap(paneMap, script.getClass())));
            }
        }

        populateBean(hyperList);
        doLayout();
    }

    protected NameJavaScriptGroup populateHotHyperLink(Plot plot) {
        return plot.getHotHyperLink();
    }

    protected HashMap getHyperlinkMap(Plot plot){
        return HyperlinkMapFactory.getHyperlinkMap(plot);
    }

    public void update(Plot plot) {

        NameJavaScriptGroup nameGroup = updateNameGroup();

        updateHotHyperLink(plot, nameGroup);
    }

    protected void updateHotHyperLink(Plot plot, NameJavaScriptGroup nameGroup) {
        plot.setHotHyperLink(nameGroup);
    }

    private NameJavaScriptGroup updateNameGroup() {
        NameJavaScriptGroup nameGroup = new NameJavaScriptGroup();
        nameGroup.clear();

        resetItemName();
        java.util.List list = updateBean();
        for(int i = 0; i < list.size(); i++) {
            UIMenuNameableCreator menu = (UIMenuNameableCreator)list.get(i);
            NameJavaScript nameJava = new NameJavaScript(menu.getName(), (JavaScript)menu.getObj());
            nameGroup.addNameHyperlink(nameJava);
        }

        return nameGroup;
    }


    protected java.util.List<UIMenuNameableCreator> refreshList(HashMap map) {
        java.util.List<UIMenuNameableCreator> list = new ArrayList<UIMenuNameableCreator>();

        list.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Link_Reportlet"),
                new ReportletHyperlink(), getUseMap(map, ReportletHyperlink.class)));
        list.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Link_Mail"), new EmailJavaScript(), ChartEmailPane.class));
        list.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Link_Web"),
                new WebHyperlink(), getUseMap(map, WebHyperlink.class)));
        list.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Link_Dynamic_Parameters"),
                new ParameterJavaScript(), getUseMap(map, ParameterJavaScript.class)));
        list.add(new UIMenuNameableCreator("JavaScript", new JavaScriptImpl(), getUseMap(map, JavaScriptImpl.class)));

        list.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Float_Chart"),
                new ChartHyperPoplink(), getUseMap(map, ChartHyperPoplink.class)));
        list.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Link_Cell"),
                new ChartHyperRelateCellLink(), getUseMap(map, ChartHyperRelateCellLink.class)));
        list.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Link_Float"),
                new ChartHyperRelateFloatLink(), getUseMap(map, ChartHyperRelateFloatLink.class)));

        FormHyperlinkProvider hyperlink = StableFactory.getMarkedInstanceObjectFromClass(FormHyperlinkProvider.XML_TAG, FormHyperlinkProvider.class);
        list.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Link_Form"),
                hyperlink, getUseMap(map, FormHyperlinkProvider.class)));

        return list;
    }

    protected Class<? extends BasicBeanPane> getUseMap(HashMap map, Object key) {
        if(map.get(key) != null){
            return (Class<? extends BasicBeanPane>)map.get(key);
        }
        //引擎在这边放了个provider,当前表单对象
        for(Object tempKey : map.keySet()){
            if(((Class)tempKey).isAssignableFrom((Class)key)){
                return (Class<? extends BasicBeanPane>)map.get(tempKey);
            }
        }
        return null;
    }
}
