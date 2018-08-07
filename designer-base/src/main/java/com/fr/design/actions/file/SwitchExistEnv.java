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
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.SeparatorDef;
import com.fr.design.utils.DesignUtils;
import com.fr.license.exception.RegistEditionException;
import com.fr.log.FineLoggerFactory;
import com.fr.workspace.WorkContext;
import com.fr.workspace.WorkContextCallback;
import com.fr.workspace.Workspace;
import com.fr.workspace.connect.AuthException;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
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
            Workspace workspace;
            try {
                workspace = DesignerWorkspaceGenerator.generate(selectedEnv);
                boolean checkValid = workspace == null ? false : selectedEnv.checkValid();
                if (!checkValid) {
                    JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Switch_Workspace_Failed"),
                            null, 0, UIManager.getIcon("OptionPane.errorIcon"));
                    return;
                }
                WorkContext.switchTo(workspace, new WorkContextCallback() {

                    @Override
                    public void done() {

                        DesignerEnvManager.getEnvManager().setCurEnvName(envName);
                        DesignUtils.refreshDesignerFrame();
                        HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().refreshToolArea();
                        fireDSChanged();
                    }
                });
            } catch (AuthException exception) {
                JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), com.fr.design.i18n.Toolkit.i18nText("Fine-Designer_Basic_Remote_Connect_Auth_Failed"),
                        null, 0, UIManager.getIcon("OptionPane.errorIcon"));
            } catch (RegistEditionException exception) {
                JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), com.fr.design.i18n.Toolkit.i18nText("FR-Lic_does_not_Support_Remote"),
                        null, 0, UIManager.getIcon("OptionPane.errorIcon"));
            } catch (Exception exception) {
                FineLoggerFactory.getLogger().error(exception.getMessage(), exception);
            }
        }
    }
}
