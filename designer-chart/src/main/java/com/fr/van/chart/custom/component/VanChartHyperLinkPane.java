package com.fr.van.chart.custom.component;

import com.fr.base.BaseFormula;
import com.fr.chart.chartattr.Plot;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chart.javascript.ChartEmailPane;
import com.fr.design.designer.TargetComponent;
import com.fr.design.fun.HyperlinkProvider;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.i18n.Toolkit;
import com.fr.design.module.DesignModuleFactory;
import com.fr.general.NameObject;
import com.fr.js.EmailJavaScript;
import com.fr.js.JavaScript;
import com.fr.js.NameJavaScript;
import com.fr.js.NameJavaScriptGroup;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.ListMap;
import com.fr.stable.Nameable;
import com.fr.van.chart.designer.component.VanChartUIListControlPane;

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
        //面板初始化
        Map<String, NameableCreator> nameCreators = new ListMap<>();
        NameableCreator[] creators = DesignModuleFactory.getHyperlinkGroupType().getHyperlinkCreators();
        for (NameableCreator creator : creators) {
            nameCreators.put(creator.menuName(), creator);
        }
        //使用chart的email替换掉报表的email
        String email = Toolkit.i18nText("Fine-Design_Basic_Email");
        nameCreators.put(email, new NameObjectCreator(email, EmailJavaScript.class, ChartEmailPane.NoRename.class));

        Set<HyperlinkProvider> providers = ExtraDesignClassManager.getInstance().getArray(HyperlinkProvider.XML_TAG);
        for (HyperlinkProvider provider : providers) {
            NameableCreator nc = provider.createHyperlinkCreator();
            nameCreators.put(nc.menuName(), nc);
        }
        return nameCreators.values().toArray(new NameableCreator[nameCreators.size()]);
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
}
