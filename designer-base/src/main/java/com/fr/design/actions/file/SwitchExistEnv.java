package com.fr.design.actions.file;

import com.fr.base.BaseUtils;
import com.fr.design.DesignerEnvManager;
import com.fr.design.EnvChangeEntrance;
import com.fr.design.actions.UpdateAction;
import com.fr.design.env.DesignerWorkspaceInfo;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.SeparatorDef;

import java.awt.event.ActionEvent;
import java.util.Iterator;

public class SwitchExistEnv extends MenuDef {

    public SwitchExistEnv() {
        this.setMenuKeySet(KeySetUtils.SWITCH_ENV);
        this.setName(getMenuKeySet().getMenuName());
        this.setHasScrollSubMenu(true);
        initMenuDef();
    }

    private void initMenuDef() { 
        Iterator<String> nameIt = DesignerEnvManager.getEnvManager().getEnvNameIterator();
        while (nameIt.hasNext()) {
            String name = nameIt.next();
            this.setIconPath("com/fr/design/images/m_file/switch.png");
            this.addShortCut(new GetExistEnvAction(name));
        }
        this.addShortCut(SeparatorDef.DEFAULT);
        this.addShortCut(new EditEnvAction());
    }

    public static class GetExistEnvAction extends UpdateAction {

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
         * 动作
         *
         * @param e 事件
         */
        public void actionPerformed(ActionEvent e) {
            final String envName = getName();
            EnvChangeEntrance.getInstance().switch2Env(envName);
        }
    }
}
