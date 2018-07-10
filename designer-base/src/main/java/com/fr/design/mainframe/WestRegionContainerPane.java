package com.fr.design.mainframe;

import com.fr.design.DesignModelAdapter;
import com.fr.design.DesignerEnvManager;
import com.fr.design.constants.UIConstants;
import com.fr.design.data.datapane.TableDataTreePane;
import com.fr.design.gui.icontainer.UIResizableContainer;
import com.fr.general.GeneralContext;
import com.fr.plugin.context.PluginContext;
import com.fr.plugin.injectable.PluginModule;
import com.fr.plugin.manage.PluginFilter;
import com.fr.plugin.observer.PluginEvent;
import com.fr.plugin.observer.PluginEventListener;
import com.fr.stable.Constants;

public class WestRegionContainerPane extends UIResizableContainer {

    private static WestRegionContainerPane THIS;

    /**
     * 得到实例
     * @return
     */
    public static final WestRegionContainerPane getInstance() {
        if (THIS == null) {
            THIS = new WestRegionContainerPane();
            THIS.setLastToolPaneY(DesignerEnvManager.getEnvManager().getLastWestRegionToolPaneY());
            THIS.setLastContainerWidth(DesignerEnvManager.getEnvManager().getLastWestRegionContainerWidth());
        }
        return THIS;
    }

    public WestRegionContainerPane() {
        super(DesignerFrameFileDealerPane.getInstance(), Constants.RIGHT);
    
        setDownPane(TableDataTreePane.getInstance(DesignModelAdapter.getCurrentModelAdapter()));
    
        GeneralContext.listenPluginRunningChanged(new PluginEventListener(TableDataTreePane.PLUGIN_LISTENER_PRIORITY - 1) {
        
            @Override
            public void on(PluginEvent event) {
            
                setDownPane(TableDataTreePane.getInstance(DesignModelAdapter.getCurrentModelAdapter()));
            }
        }, new PluginFilter() {
        
            @Override
            public boolean accept(PluginContext context) {
    
                return context.contain(PluginModule.ExtraDesign);
            }
        });
    
        setContainerWidth(165);
        setBackground(UIConstants.TREE_BACKGROUND);
    }
}