package com.fr.van.chart.map.server;

import com.fr.base.Env;
import com.fr.base.FRContext;
import com.fr.base.ServerConfig;
import com.fr.design.DesignerEnvManager;
import com.fr.design.actions.UpdateAction;
import com.fr.general.GeneralContext;
import com.fr.general.IOUtils;
import com.fr.general.Inter;
import com.fr.start.StartServer;

import java.awt.event.ActionEvent;

/**
 * Created by eason on 2017/5/23.
 */
public class ChartMapEditorAction extends UpdateAction {

    public ChartMapEditorAction(){
        this.setSmallIcon(IOUtils.readIcon("/com/fr/van/chart/map/images/mapData.png"));
        this.setName(Inter.getLocText("Plugin-ChartF_Map_Data"));
    }

    public void actionPerformed(ActionEvent evt) {
        int port = DesignerEnvManager.getEnvManager().getJettyServerPort();
        String web = GeneralContext.getCurrentAppNameOfEnv();
        String serverlet = ServerConfig.getInstance().getReportServletName();
        Env env = FRContext.getCurrentEnv();
        StartServer.browserURLWithLocalEnv(env.isLocalEnv() ? String.format("http://localhost:%d/%s/%s?op=map", port, web, serverlet) : env.getPath() + "?op=map");
    }

}
