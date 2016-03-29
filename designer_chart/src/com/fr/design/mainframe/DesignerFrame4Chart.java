package com.fr.design.mainframe;

import com.fr.design.ChartEnvManager;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.mainframe.actions.UpdateVersion;
import com.fr.design.mainframe.chart.UpdateOnLinePane;
import com.fr.design.mainframe.toolbar.ToolBarMenuDock;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.json.JSONObject;
import com.fr.stable.ProductConstants;
import com.fr.stable.StableUtils;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 7.1.1
 */
public class DesignerFrame4Chart extends DesignerFrame {

    /**
     * Constructor.
     *
     * @param ad
     */
    public DesignerFrame4Chart(ToolBarMenuDock ad) {
        super(ad);
    }





    protected ArrayList<WindowListener> getFrameListeners(){
        ArrayList<WindowListener> listeners = super.getFrameListeners();
        listeners.add(0, new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                super.windowOpened(e);
                judgeFirstUseWhenStart();
            }
        });
        return listeners;
   	}

    /**
     * 退出
     */
    public void exit() {
        ChartEnvManager.getEnvManager().saveXMLFile();
        super.exit();
    }

    //不需要西侧的文件树面板
    protected void laoyoutWestPane(){

    }

    protected void judgeFirstUseWhenStart(){
        boolean isNeed2Check =ChartEnvManager.getEnvManager().isPushUpdateAuto() || ChartEnvManager.getEnvManager().isOverOneMonth();
        if(!StableUtils.checkDesignerActive(ChartEnvManager.getEnvManager().getActivationKey())
                || isNeed2Check){
            ChartEnvManager.getEnvManager().setActivationKey(ChartEnvManager.ACTIVE_KEY);
            checkVersion();
            if(ChartEnvManager.getEnvManager().isOverOneMonth()){
                ChartEnvManager.getEnvManager().resetCheckDate();
            }
        }
    }

    private void checkVersion(){

        new UpdateVersion(){
            protected void done() {
                try {
                    JSONObject serverVersion = get();
                    String version = serverVersion.getString(UpdateVersion.VERSION);
                    if(!ComparatorUtils.equals(ProductConstants.RELEASE_VERSION, version)){
                        UpdateOnLinePane updateOnLinePane = new UpdateOnLinePane(version);
                        BasicDialog dg = updateOnLinePane.showWindow4UpdateOnline(DesignerContext.getDesignerFrame());
                        updateOnLinePane.setParentDialog(dg);
                        dg.setVisible(true);
                    }
                }catch (Exception e){
                    FRLogger.getLogger().error(e.getMessage());
                }
            }
        }.execute();
    }



}