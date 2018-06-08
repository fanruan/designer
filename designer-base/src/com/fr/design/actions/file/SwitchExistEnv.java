package com.fr.design.actions.file;

import com.fr.base.BaseUtils;
import com.fr.base.env.EnvUpdater;
import com.fr.core.env.EnvConfig;
import com.fr.core.env.impl.LocalEnvConfig;
import com.fr.design.DesignerEnvManager;
import com.fr.design.actions.UpdateAction;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.data.tabledata.ResponseDataSourceChange;
import com.fr.design.env.EnvGenerator;
import com.fr.design.env.RemoteEnvConfig;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.mainframe.TemplatePane;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.SeparatorDef;
import com.fr.env.RemoteEnv;
import com.fr.general.GeneralContext;
import com.fr.general.Inter;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.EnvChangedListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SwitchExistEnv extends MenuDef {

    public SwitchExistEnv() {
        this.setMenuKeySet(KeySetUtils.SWITCH_ENV);
        this.setName(getMenuKeySet().getMenuName());
        this.setHasScrollSubMenu(true);
        initMenuDef();
        JTemplate<?, ?> t = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        if (t != null) {
            GeneralContext.addEnvWillChangedListener(
                    t.getFullPathName(),
                    new EnvChangedListener() {
                        public void envChanged() {
                            SwitchExistEnv.this.clearShortCuts();
                            initMenuDef();
                        }
                    });
        }
    }

    private void initMenuDef() {
        // ButtonGroup group = new ButtonGroup();
        Iterator<String> nameIt = DesignerEnvManager.getEnvManager().getEnvNameIterator();
        while (nameIt.hasNext()) {
            String name = nameIt.next();
            this.setIconPath("com/fr/design/images/m_file/switch.png");
            this.addShortCut(new GetExistEnvAction(name));
        }
        this.addShortCut(SeparatorDef.DEFAULT);
        this.addShortCut(new EditEnvAction());
    }

    public static class GetExistEnvAction extends UpdateAction implements ResponseDataSourceChange {
        public GetExistEnvAction() {
        }

        public GetExistEnvAction(String envName) {
            this.setName(envName);
            EnvConfig env = DesignerEnvManager.getEnvManager().getEnv(envName);
            if (env instanceof LocalEnvConfig) {
                this.setSmallIcon(BaseUtils.readIcon("com/fr/design/images/data/bind/localconnect.png"));
            } else if (env instanceof RemoteEnvConfig) {
                this.setSmallIcon(BaseUtils.readIcon("com/fr/design/images/data/bind/distanceconnect.png"));
            }
        }

        /**
         * 响应数据集改变
         */
        public void fireDSChanged() {
            fireDSChanged(new HashMap<String, String>());
        }

        /**
         * 响应数据集改变
         *
         * @param map 数据集
         */
        public void fireDSChanged(Map<String, String> map) {
            DesignTableDataManager.fireDSChanged(map);
        }

        /**
         * 动作
         *
         * @param e 事件
         */
        public void actionPerformed(ActionEvent e) {
            DesignerEnvManager envManager = DesignerEnvManager.getEnvManager();
            EnvConfig selectedEnv = envManager.getEnv(this.getName());
            try {
                if (selectedEnv instanceof RemoteEnv && !((RemoteEnv) selectedEnv).testServerConnection()) {
                    JOptionPane.showMessageDialog(
                            DesignerContext.getDesignerFrame(),
                            Inter.getLocText(new String[]{"M-SwitchWorkspace", "Failed"}));
                    return;
                }
                EnvUpdater.updateEnv(EnvGenerator.generate(selectedEnv));
                HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().refreshToolArea();
                fireDSChanged();
            } catch (Exception em) {
                FineLoggerFactory.getLogger().error(em.getMessage(), em);
                JOptionPane.showMessageDialog(
                        DesignerContext.getDesignerFrame(),
                        Inter.getLocText(new String[]{"M-SwitchWorkspace", "Failed"}));
                TemplatePane.getInstance().editItems();
            }
        }
    }
}
