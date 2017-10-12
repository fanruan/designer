package com.fr.plugin.chart.custom.component;

import com.fr.chart.chartattr.Plot;
import com.fr.chart.web.ChartHyperPoplink;
import com.fr.chart.web.ChartHyperRelateCellLink;
import com.fr.chart.web.ChartHyperRelateFloatLink;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chart.javascript.ChartEmailPane;
import com.fr.design.chart.series.SeriesCondition.impl.ChartHyperPoplinkPane;
import com.fr.design.chart.series.SeriesCondition.impl.ChartHyperRelateCellLinkPane;
import com.fr.design.chart.series.SeriesCondition.impl.ChartHyperRelateFloatLinkPane;
import com.fr.design.chart.series.SeriesCondition.impl.FormHyperlinkPane;
import com.fr.design.designer.TargetComponent;
import com.fr.design.fun.HyperlinkProvider;
import com.fr.design.gui.HyperlinkFilterHelper;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.imenutable.UIMenuNameableCreator;
import com.fr.design.hyperlink.ReportletHyperlinkPane;
import com.fr.design.hyperlink.WebHyperlinkPane;
import com.fr.design.javascript.JavaScriptImplPane;
import com.fr.design.javascript.ParameterJavaScriptPane;
import com.fr.design.module.DesignModuleFactory;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.general.NameObject;
import com.fr.js.EmailJavaScript;
import com.fr.js.FormHyperlinkProvider;
import com.fr.js.JavaScript;
import com.fr.js.JavaScriptImpl;
import com.fr.js.NameJavaScript;
import com.fr.js.NameJavaScriptGroup;
import com.fr.js.ParameterJavaScript;
import com.fr.js.ReportletHyperlink;
import com.fr.js.WebHyperlink;
import com.fr.plugin.chart.designer.component.VanChartUIListControlPane;
import com.fr.stable.ListMap;
import com.fr.stable.Nameable;
import com.fr.stable.bridge.StableFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Fangjie on 2016/4/28.
 */
public class VanChartHyperLinkPane extends VanChartUIListControlPane {

    public VanChartHyperLinkPane() {
        super();
    }

    @Override
    public NameableCreator[] createNameableCreators() {

        //面板初始化，需要在populate的时候更新
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


    protected BasicBeanPane createPaneByCreators(NameableCreator creator) {
        Constructor<? extends BasicBeanPane> constructor = null;
        try {
            constructor = creator.getUpdatePane().getConstructor(HashMap.class, boolean.class);
            return constructor.newInstance(plot.getHyperLinkEditorMap(), false);

        } catch (InstantiationException e) {
            FRLogger.getLogger().error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            FRLogger.getLogger().error(e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            return super.createPaneByCreators(creator);
        } catch (InvocationTargetException e) {
            FRLogger.getLogger().error(e.getMessage(), e);
        }
        return null;
    }


    /**
     * 弹出列表的标题.
     *
     * @return 返回标题字符串.
     */
    public String title4PopupWindow() {
        return Inter.getLocText("FR-Designer_Hyperlink");
    }

    @Override
    protected String getAddItemText() {
        return Inter.getLocText("FR-Designer_Add_Hyperlink");
    }

    @Override
    protected AddItemMenuDef getAddItemMenuDef (NameableCreator[] creators) {
        return new AddVanChartItemMenuDef(creators);
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

    public void populate(TargetComponent elementCasePane) {
        //populate
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

    public void populate(Plot plot) {
        this.plot = plot;
        HashMap paneMap = getHyperlinkMap(plot);

        //安装平台内打开插件时,添加相应按钮
        Set<HyperlinkProvider> providers = ExtraDesignClassManager.getInstance().getArray(HyperlinkProvider.XML_TAG);
        for (HyperlinkProvider provider : providers) {
            NameableCreator nc = provider.createHyperlinkCreator();
                paneMap.put(nc.getHyperlink(), nc.getUpdatePane());
        }

        java.util.List<UIMenuNameableCreator> list = refreshList(paneMap);
        NameObjectCreator[] creators = new NameObjectCreator[list.size()];
        for (int i = 0; list != null && i < list.size(); i++) {
            UIMenuNameableCreator uiMenuNameableCreator = list.get(i);
            creators[i] = new NameObjectCreator(uiMenuNameableCreator.getName(), uiMenuNameableCreator.getObj().getClass(), uiMenuNameableCreator.getPaneClazz());

        }

        refreshNameableCreator(creators);

        java.util.List<NameObject> nameObjects = new ArrayList<NameObject>();

        NameJavaScriptGroup nameGroup = populateHotHyperLink(plot);
        for (int i = 0; nameGroup != null && i < nameGroup.size(); i++) {
            NameJavaScript javaScript = nameGroup.getNameHyperlink(i);
            if (javaScript != null && javaScript.getJavaScript() != null) {
                JavaScript script = javaScript.getJavaScript();
                UIMenuNameableCreator uiMenuNameableCreator = new UIMenuNameableCreator(javaScript.getName(), script, getUseMap(paneMap, script.getClass()));
                nameObjects.add(new NameObject(uiMenuNameableCreator.getName(), uiMenuNameableCreator.getObj()));

            }
        }

        this.populate(nameObjects.toArray(new NameObject[nameObjects.size()]));
        doLayout();
    }

    protected NameJavaScriptGroup populateHotHyperLink(Plot plot) {
        return plot.getHotHyperLink();
    }

    protected HashMap getHyperlinkMap(Plot plot) {
        HashMap<Class, Class> map = new HashMap<Class, Class>();

        map.put(ReportletHyperlink.class, ReportletHyperlinkPane.class);
        map.put(EmailJavaScript.class, ChartEmailPane.class);
        map.put(WebHyperlink.class, WebHyperlinkPane.class);
        map.put(ParameterJavaScript.class, ParameterJavaScriptPane.class);

        map.put(JavaScriptImpl.class, JavaScriptImplPane.class);
        map.put(ChartHyperPoplink.class, ChartHyperPoplinkPane.class);
        map.put(ChartHyperRelateCellLink.class, ChartHyperRelateCellLinkPane.class);
        map.put(ChartHyperRelateFloatLink.class, ChartHyperRelateFloatLinkPane.class);

        map.put(FormHyperlinkProvider.class, FormHyperlinkPane.class);
        return map;
    }

    public void update(Plot plot) {

        NameJavaScriptGroup nameGroup = updateNameGroup();

        updateHotHyperLink(plot, nameGroup);
    }

    protected void updateHotHyperLink(Plot plot, NameJavaScriptGroup nameGroup) {
        plot.setHotHyperLink(nameGroup);
    }

    private NameJavaScriptGroup updateNameGroup() {
        Nameable[] nameables = update();

        NameJavaScriptGroup nameGroup = new NameJavaScriptGroup();
        nameGroup.clear();

        for (int i = 0; i < nameables.length; i++) {
            JavaScript javaScript = (JavaScript) ((NameObject) nameables[i]).getObject();
            String name = nameables[i].getName();
            NameJavaScript nameJava = new NameJavaScript(name, javaScript);
            nameGroup.addNameHyperlink(nameJava);
        }

        return nameGroup;
    }


    protected java.util.List<UIMenuNameableCreator> refreshList(HashMap map) {
        java.util.List<UIMenuNameableCreator> list = new ArrayList<UIMenuNameableCreator>();

        list.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Link_Reportlet"),
                new ReportletHyperlink(), getUseMap(map, ReportletHyperlink.class)));
        list.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Link_Mail"), new EmailJavaScript(), VanChartEmailPane.class));
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
        if (map.get(key) != null) {
            return (Class<? extends BasicBeanPane>) map.get(key);
        }
        //引擎在这边放了个provider,当前表单对象
        for (Object tempKey : map.keySet()) {
            if (((Class) tempKey).isAssignableFrom((Class) key)) {
                return (Class<? extends BasicBeanPane>) map.get(tempKey);
            }
        }
        return null;
    }

    protected class AddVanChartItemMenuDef extends AddItemMenuDef {

        public AddVanChartItemMenuDef(NameableCreator[] creators) {
            super(creators);
        }

        @Override
        protected boolean whetherAdd(String itemName) {
            return HyperlinkFilterHelper.whetherAddHyperlink4Chart(itemName);
        }
    }

    //邮箱
    public static class VanChartEmailPane extends ChartEmailPane {
        @Override
        protected boolean needRenamePane() {
            return false;
        }
    }


}
