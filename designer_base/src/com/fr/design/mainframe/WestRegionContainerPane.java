package com.fr.design.mainframe;

import com.fr.design.DesignModelAdapter;
import com.fr.design.DesignerEnvManager;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.data.datapane.TableDataTreePane;
import com.fr.design.fun.TableDataTreePaneProcessor;
import com.fr.design.gui.icontainer.UIResizableContainer;
import com.fr.stable.Constants;

import javax.swing.*;

public class WestRegionContainerPane extends UIResizableContainer {

    private static WestRegionContainerPane THIS;

    /**
     * 得到实例
     * @return
     */
    public static final WestRegionContainerPane getInstance() {
        if (THIS == null) {
            TableDataTreePane tableDataTreePane = null;
            TableDataTreePaneProcessor treePaneProcessor = ExtraDesignClassManager.getInstance().getTableDataTreePaneProcessor();
            if (treePaneProcessor != null) {
                tableDataTreePane = treePaneProcessor.createTableDataTreePane();
            } else {
                tableDataTreePane = TableDataTreePane.getInstance(DesignModelAdapter.getCurrentModelAdapter());
            }
            THIS = new WestRegionContainerPane(tableDataTreePane);
            THIS.setLastToolPaneY(DesignerEnvManager.getEnvManager().getLastWestRegionToolPaneY());
            THIS.setLastContainerWidth(DesignerEnvManager.getEnvManager().getLastWestRegionContainerWidth());
        }
        return THIS;
    }

    public WestRegionContainerPane(JComponent pane) {
        super(DesignerFrameFileDealerPane.getInstance(), pane, Constants.RIGHT);
        setContainerWidth(165);
    }

}