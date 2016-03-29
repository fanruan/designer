package com.fr.design.mainframe;

import com.fr.design.data.datapane.TableDataTreePane;
import com.fr.design.DesignModelAdapter;
import com.fr.design.DesignerEnvManager;
import com.fr.design.gui.icontainer.UIResizableContainer;
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
        super(DesignerFrameFileDealerPane.getInstance(), TableDataTreePane.getInstance(DesignModelAdapter.getCurrentModelAdapter()), Constants.RIGHT);
        setContainerWidth(165);
    }
}