package com.fr.van.chart.custom.component;

import com.fr.base.BaseFormula;
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
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.hyperlink.ReportletHyperlinkPane;
import com.fr.design.hyperlink.WebHyperlinkPane;
import com.fr.design.i18n.Toolkit;
import com.fr.design.javascript.JavaScriptImplPane;
import com.fr.design.javascript.ParameterJavaScriptPane;
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
import com.fr.log.FineLoggerFactory;
import com.fr.stable.Nameable;
import com.fr.stable.bridge.StableFactory;
import com.fr.van.chart.designer.component.VanChartUIListControlPane;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

        List<NameableCreator> creators = new ArrayList<NameableCreator>();

        creators.add(new NameObjectCreator(Toolkit.i18nText("Fine-Design_Chart_Link_Reportlet"),
                ReportletHyperlink.class, ReportletHyperlinkPane.class));
        creators.add(new NameObjectCreator(Toolkit.i18nText("Fine-Design_Chart_Link_Mail"), EmailJavaScript.class, VanChartEmailPane.class));
        creators.add(new NameObjectCreator(Toolkit.i18nText("Fine-Design_Chart_Link_Web"),
                WebHyperlink.class, WebHyperlinkPane.class));
        creators.add(new NameObjectCreator(Toolkit.i18nText("Fine-Design_Chart_Link_Dynamic_Parameters"),
                ParameterJavaScript.class, ParameterJavaScriptPane.class));
        creators.add(new NameObjectCreator("JavaScript", JavaScriptImpl.class, JavaScriptImplPane.class));

        creators.add(new NameObjectCreator(Toolkit.i18nText("Fine-Design_Chart_Float_Chart"),
                ChartHyperPoplink.class, ChartHyperPoplinkPane.class));
        creators.add(new NameObjectCreator(Toolkit.i18nText("Fine-Design_Chart_Link_Cell"),
                ChartHyperRelateCellLink.class, ChartHyperRelateCellLinkPane.class));
        creators.add(new NameObjectCreator(Toolkit.i18nText("Fine-Design_Chart_Link_Float"),
                ChartHyperRelateFloatLink.class, ChartHyperRelateFloatLinkPane.class));

        FormHyperlinkProvider hyperlink = StableFactory.getMarkedInstanceObjectFromClass(FormHyperlinkProvider.XML_TAG, FormHyperlinkProvider.class);
        creators.add(new NameObjectCreator(Toolkit.i18nText("Fine-Design_Chart_Link_Form"),
                hyperlink.getClass(), FormHyperlinkPane.class));

        Set<HyperlinkProvider> providers = ExtraDesignClassManager.getInstance().getArray(HyperlinkProvider.XML_TAG);
        for (HyperlinkProvider provider : providers) {
            NameableCreator nc = provider.createHyperlinkCreator();
            creators.add(nc);
        }

        return creators.toArray(new NameableCreator[creators.size()]);
    }


    public BasicBeanPane createPaneByCreators(NameableCreator creator) {
        Constructor<? extends BasicBeanPane> constructor = null;
        try {
            constructor = creator.getUpdatePane().getConstructor(HashMap.class, boolean.class);
            return constructor.newInstance(getHyperLinkEditorMap(), false);

        } catch (InstantiationException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            return super.createPaneByCreators(creator);
        } catch (InvocationTargetException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        return null;
    }

    protected Map<String, BaseFormula> getHyperLinkEditorMap() {
        return getPlot().getHyperLinkEditorMap();
    }

    /**
     * 弹出列表的标题.
     *
     * @return 返回标题字符串.
     */
    public String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Hyperlink");
    }

    @Override
    protected String getAddItemText() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Add_Hyperlink");
    }

    @Override
    protected void initShortCutFactory() {
        this.shortCutFactory = VanChartShortCutFactory.newInstance(this);
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
        setPlot(plot);

        java.util.List<NameObject> nameObjects = new ArrayList<NameObject>();

        NameJavaScriptGroup nameGroup = populateHotHyperLink(plot);
        for (int i = 0; nameGroup != null && i < nameGroup.size(); i++) {
            NameJavaScript javaScript = nameGroup.getNameHyperlink(i);
            if (javaScript != null && javaScript.getJavaScript() != null) {
                JavaScript script = javaScript.getJavaScript();
                nameObjects.add(new NameObject(javaScript.getName(), script));
            }
        }

        this.populate(nameObjects.toArray(new NameObject[nameObjects.size()]));
        doLayout();
    }

    protected NameJavaScriptGroup populateHotHyperLink(Plot plot) {
        return plot.getHotHyperLink();
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

    //邮箱
    public static class VanChartEmailPane extends ChartEmailPane {
        @Override
        protected boolean needRenamePane() {
            return false;
        }
    }


}
