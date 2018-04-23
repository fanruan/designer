package com.fr.design.actions.file;

import java.awt.event.ActionEvent;
import java.util.List;

import com.fr.base.BaseUtils;
import com.fr.design.DesignerEnvManager;
import com.fr.design.actions.UpdateAction;
import com.fr.design.gui.imenu.UIMenu;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuDef;
import com.fr.file.FILEFactory;

/**
 * Open Resent MenuDef.
 */
public class OpenRecentReportMenuDef extends MenuDef {
    public OpenRecentReportMenuDef() {
        this.setMenuKeySet(KeySetUtils.RECENT_OPEN);
        this.setName(getMenuKeySet().getMenuKeySetName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setIconPath("/com/fr/base/images/cell/blank.gif");
        initMenuDef();
    }

    private void initMenuDef() {
        DesignerEnvManager designerEnvManager = DesignerEnvManager.getEnvManager();
        List<String> list = designerEnvManager.getRecentOpenedFilePathList();
        for (int i = 0; i < list.size(); i++) {
            this.addShortCut(new OpenRecentReportAction(list.get(i)));
        }
    }

    /**
     * 更新菜单
     */
    public void updateMenu() {
        UIMenu createdMenu = this.createJMenu();
        createdMenu.removeAll();
        this.clearShortCuts();
        initMenuDef();
        int menuCount = this.getShortCutCount();
        for (int i = 0; i < menuCount; i++) {
            Object object = this.getShortCut(i);
            if (!(object instanceof OpenRecentReportAction)) {
                return;
            }
            OpenRecentReportAction openResentReportAction = (OpenRecentReportAction) object;
            openResentReportAction.update();
            if (openResentReportAction.isEnabled()) {
                createdMenu.add(openResentReportAction.createMenuItem());
            }
        }
    }

    public static class OpenRecentReportAction extends UpdateAction {
        public OpenRecentReportAction() {
        }

        public OpenRecentReportAction(String cptName) {
            this.setName(cptName);
            this.setSmallIcon(BaseUtils.readIcon("/com/fr/base/images/oem/logo.png"));
        }

        /**
         * 动作
         * @param e 事件
         */
        public void actionPerformed(ActionEvent e) {
            DesignerContext.getDesignerFrame().openTemplate(FILEFactory.createFILE(this.getPath()));
        }

        public String getPath() {
            return this.getName();
        }
    }
}