package com.fr.design.mainframe.actions;

import com.fr.design.ChartEnvManager;
import com.fr.design.actions.UpdateAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.chart.UpdateOnLinePane;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.json.JSONObject;
import com.fr.stable.ProductConstants;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 7.1.1
 * 图表设计器在线更新
 */
public class UpdateOnlineAction extends UpdateAction {

    public UpdateOnlineAction() {
        this.setMenuKeySet(getKeySet());
        this.setName(getMenuKeySet().getMenuKeySetName() + "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
    }

    private MenuKeySet getKeySet() {
        return new MenuKeySet() {
            @Override
            public char getMnemonic() {
                return 'U';
            }

            @Override
            public String getMenuName() {
                return Inter.getLocText("FR-Chart-Help_UpdateOnline");
            }

            @Override
            public KeyStroke getKeyStroke() {
                return null;
            }
        };
    }


    /**
     *动作
     * @param e 事件
     */
    public void actionPerformed(ActionEvent e) {
        new UpdateVersion(){
            protected void done() {
                try {
                    ChartEnvManager.getEnvManager().resetCheckDate();
                    JSONObject serverVersion = get();
                    String version = serverVersion.getString(UpdateVersion.VERSION);
                    UpdateOnLinePane updateOnLinePane = new UpdateOnLinePane(StringUtils.isEmpty(version)? ProductConstants.RELEASE_VERSION:version);
                    BasicDialog dg = updateOnLinePane.showWindow4UpdateOnline(DesignerContext.getDesignerFrame());
                    updateOnLinePane.setParentDialog(dg);
                    dg.setVisible(true);
                }catch (Exception e){
                    FRLogger.getLogger().error(e.getMessage());
                }
            }
        }.execute();
    }

}