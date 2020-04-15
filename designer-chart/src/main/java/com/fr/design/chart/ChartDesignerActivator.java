package com.fr.design.chart;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.decision.webservice.v10.map.geojson.helper.GEOJSONHelper;
import com.fr.design.ChartTypeInterfaceManager;
import com.fr.design.actions.core.ActionFactory;
import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.mainframe.ChartPropertyPane;
import com.fr.design.module.ChartEmptyDataStyleAction;
import com.fr.design.module.ChartHyperlinkGroup;
import com.fr.design.module.ChartPreStyleAction;
import com.fr.design.module.DesignModuleFactory;
import com.fr.form.ui.ChartEditor;
import com.fr.locale.InterMutableKey;
import com.fr.locale.LocaleMarker;
import com.fr.locale.LocaleScope;
import com.fr.module.Activator;
import com.fr.module.extension.Prepare;
import com.fr.stable.bridge.StableFactory;
import com.fr.stable.plugin.ExtraChartDesignClassManagerProvider;
import com.fr.van.chart.DownloadOnlineSourcesHelper;
import com.fr.van.chart.map.server.ChartMapEditorAction;

/**
 * Created by juhaoyu on 2018/6/27.
 */
public class ChartDesignerActivator extends Activator implements Prepare {

    @Override
    public void start() {

        StableFactory.registerMarkedClass(ExtraChartDesignClassManagerProvider.XML_TAG, ChartTypeInterfaceManager.class);
        StableFactory.getStaticMarkedInstanceObjectFromClass(ExtraChartDesignClassManagerProvider.XML_TAG, ExtraChartDesignClassManagerProvider.class);

        DesignModuleFactory.registerHyperlinkGroupType(new ChartHyperlinkGroup());

        DesignModuleFactory.registerChartEditorClass(ChartEditor.class);
        DesignModuleFactory.registerChartComponentClass(ChartComponent.class);

        DesignModuleFactory.registerChartDialogClass(ChartDialog.class);

        DesignModuleFactory.registerChartPropertyPaneClass(ChartPropertyPane.class);

        ActionFactory.registerChartPreStyleAction(new ChartPreStyleAction());
        ActionFactory.registerChartEmptyDataStyleAction(new ChartEmptyDataStyleAction());
        ActionFactory.registerChartMapEditorAction(new ChartMapEditorAction());

        ActionFactory.registerChartCollection(ChartCollection.class);

        DesignModuleFactory.registerExtraWidgetOptions(ChartTypeInterfaceManager.initWidgetOption());

        GEOJSONHelper.registerDownloadSourcesEvent(new DownloadOnlineSourcesHelper());

        ChartTypeInterfaceManager.addPluginChangedListener();
    }

    @Override
    public void prepare() {
        addMutable(InterMutableKey.Path, LocaleMarker.create("com/fr/design/i18n/chart", LocaleScope.DESIGN));
    }

    @Override
    public void stop() {

    }
}
