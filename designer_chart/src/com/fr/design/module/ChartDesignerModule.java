package com.fr.design.module;

import com.fr.chart.base.ChartInternationalNameContentBean;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.charttypes.ChartTypeManager;
import com.fr.design.ChartTypeInterfaceManager;
import com.fr.design.actions.core.ActionFactory;
import com.fr.design.chart.ChartDialog;
import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.chart.gui.ChartWidgetOption;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.mainframe.App;
import com.fr.design.mainframe.ChartPropertyPane;
import com.fr.form.ui.ChartEditor;
import com.fr.general.IOUtils;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.bridge.StableFactory;
import com.fr.stable.plugin.ExtraChartDesignClassManagerProvider;
import com.fr.van.chart.map.server.ChartMapEditorAction;

import javax.swing.Icon;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 7.0.3
 * Date: 13-7-8
 * Time: 上午9:13
 */
public class ChartDesignerModule extends DesignModule {
    public void start() {
        super.start();
        dealBeforeRegister();
        register();
        registerFloatEditor();
    }

    protected void dealBeforeRegister(){
        StableFactory.registerMarkedClass(ExtraChartDesignClassManagerProvider.XML_TAG, ChartTypeInterfaceManager.class);
        StableFactory.getStaticMarkedInstanceObjectFromClass(ExtraChartDesignClassManagerProvider.XML_TAG, ExtraChartDesignClassManagerProvider.class);
    }

    private void register(){
        DesignModuleFactory.registerHyperlinkGroupType(new ChartHyperlinkGroup());

        DesignModuleFactory.registerChartEditorClass(ChartEditor.class);
        DesignModuleFactory.registerChartComponentClass(ChartComponent.class);

        DesignModuleFactory.registerChartDialogClass(ChartDialog.class);

        DesignModuleFactory.registerChartPropertyPaneClass(ChartPropertyPane.class);

        ActionFactory.registerChartPreStyleAction(new ChartPreStyleAction());
        ActionFactory.registerChartMapEditorAction(new ChartMapEditorAction());
    }

    protected void registerFloatEditor() {
        ActionFactory.registerChartCollection(ChartCollection.class);
    }

    /**
     * 返回设计器能打开的模板类型的一个数组列表
     *
     * @return 可以打开的模板类型的数组
     */
    public App<?>[] apps4TemplateOpener() {
        return new App[0];
    }
    protected WidgetOption[] options4Show() {
        ChartInternationalNameContentBean[] typeName = ChartTypeManager.getInstance().getAllChartBaseNames();
        ChartWidgetOption[] child = new ChartWidgetOption[typeName.length];
        for (int i = 0; i < typeName.length; i++) {
            String plotID = typeName[i].getPlotID();
            Chart[] rowChart = ChartTypeManager.getInstance().getChartTypes(plotID);
            if (ArrayUtils.isEmpty(rowChart)) {
                continue;
            }
            String iconPath = ChartTypeInterfaceManager.getInstance().getIconPath(plotID);
            Icon icon = IOUtils.readIcon(iconPath);
            child[i] = new ChartWidgetOption(Inter.getLocText(typeName[i].getName()), icon, ChartEditor.class, rowChart[0]);
        }
        return child;
    }

    public String getInterNationalName() {
        return Inter.getLocText("FR-Chart-Design_ChartModule");
    }


}
