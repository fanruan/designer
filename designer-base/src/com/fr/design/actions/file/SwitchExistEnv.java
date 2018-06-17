package com.fr.design.actions.file;

import com.fr.base.BaseUtils;
import com.fr.design.DesignerEnvManager;
import com.fr.design.actions.UpdateAction;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.data.tabledata.ResponseDataSourceChange;
import com.fr.design.env.DesignerWorkspaceGenerator;
import com.fr.design.env.DesignerWorkspaceInfo;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.mainframe.TemplatePane;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.SeparatorDef;
import com.fr.design.utils.DesignUtils;
import com.fr.general.GeneralContext;
import com.fr.general.Inter;
import com.fr.stable.EnvChangedListener;
import com.fr.workspace.WorkContext;
import com.fr.workspace.WorkContextCallback;

import javax.swing.JOptionPane;
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
            DesignerWorkspaceInfo env = DesignerEnvManager.getEnvManager().getWorkspaceInfo(envName);
            switch (env.getType()) {
                case Local: {
                    this.setSmallIcon(BaseUtils.readIcon("com/fr/design/images/data/bind/localconnect.png"));
                    break;
                }
                case Remote: {
                    this.setSmallIcon(BaseUtils.readIcon("com/fr/design/images/data/bind/distanceconnect.png"));
                    break;
                }
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
            final String envName = getName();
            DesignerWorkspaceInfo selectedEnv = envManager.getWorkspaceInfo(envName);
            WorkContext.switchTo(DesignerWorkspaceGenerator.generate(selectedEnv), new WorkContextCallback() {
                @Override
                public void success() {
                    DesignerEnvManager.getEnvManager().setCurEnvName(envName);
                    DesignUtils.refreshDesignerFrame();
                    HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().refreshToolArea();
                    fireDSChanged();
                }

                @Override
                public void fail() {
                    TemplatePane.getInstance().editItems();
                    JOptionPane.showMessageDialog(
                            DesignerContext.getDesignerFrame(),
                            Inter.getLocText(new String[]{"M-SwitchWorkspace", "Failed"}));
                }
            });
        }
    }
}
