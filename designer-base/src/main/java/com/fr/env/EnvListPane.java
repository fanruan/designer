package com.fr.env;

import com.fr.design.DesignerEnvManager;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.env.DesignerWorkspaceInfo;
import com.fr.design.env.LocalDesignerWorkspaceInfo;
import com.fr.design.env.RemoteDesignerWorkspaceInfo;
import com.fr.design.gui.controlpane.JListControlPane;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.general.ComparatorUtils;
import com.fr.general.NameObject;
import com.fr.stable.Nameable;
import com.fr.stable.StringUtils;
import com.fr.stable.core.PropertyChangeAdapter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author yaohwu
 */
public class EnvListPane extends JListControlPane {
    public EnvListPane() {
        super();
        addEditingListener(new PropertyChangeAdapter() {
            @Override
            public void propertyChange() {
                String tempName = getEditingName();
                String[] allListNames = nameableList.getAllNames();
                allListNames[nameableList.getSelectedIndex()] = StringUtils.EMPTY;
                if (StringUtils.isEmpty(tempName)) {
                    nameableList.stopEditing();
                    FineJOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(EnvListPane.this), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Workspace_Empty_Name_Warn_Text"));
                    setIllegalIndex(editingIndex);
                    return;
                }
                if (!ComparatorUtils.equals(tempName, selectedName) && isNameRepeated(new List[]{Arrays.asList(allListNames)}, tempName)) {
                    nameableList.stopEditing();
                    FineJOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(EnvListPane.this), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Workspace_Duplicate_Name_Warn_Text", tempName));
                    setIllegalIndex(editingIndex);
                }
            }
        });
    }

    /**
     * 生成添加按钮的NameableCreator
     *
     * @return 返回添加按钮的NameableCreator
     */
    @Override
    public NameableCreator[] createNameableCreators() {
        NameableCreator local = new NameObjectCreator(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Local_Workspace"), "com/fr/design/images/data/bind/localconnect.png",
                LocalDesignerWorkspaceInfo.class, LocalEnvPane.class);
        NameableCreator remote = new NameObjectCreator(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Env_Remote_Server"), "com/fr/design/images/data/bind/distanceconnect.png",
                RemoteDesignerWorkspaceInfo.class, RemoteEnvPane.class);
        return new NameableCreator[]{local, remote};
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Env_Configure_Workspace");
    }

    /**
     * 弹出选中环境的面板
     *
     * @param selectedEnv 选中的环境
     */
    public void populateEnvManager(String selectedEnv) {
        DesignerEnvManager mgr = DesignerEnvManager.getEnvManager();
        Iterator<String> nameIt = mgr.getEnvNameIterator();
        List<NameObject> nameObjectList = new ArrayList<>();
        while (nameIt.hasNext()) {
            String name = nameIt.next();
            nameObjectList.add(new NameObject(name, mgr.getWorkspaceInfo(name)));
        }

        this.populate(nameObjectList.toArray(new NameObject[0]));

        if (StringUtils.isBlank(selectedEnv)) {
            selectedEnv = mgr.getCurEnvName();
        }
        this.setSelectedName(selectedEnv);
    }


    /**
     * 更新designerEnvManager里面所有的Env
     *
     * @return 返回选中的环境的名字
     */
    public String updateEnvManager() {
        DesignerEnvManager mgr = DesignerEnvManager.getEnvManager();
        //这里代码时序换一下，因为update中需要借助mgr来获取提醒时间，已确认mgr对res无依赖
        Nameable[] res = this.update();
        mgr.clearAllEnv();
        for (Nameable re : res) {
            NameObject nameObject = (NameObject) re;
            mgr.putEnv(nameObject.getName(), (DesignerWorkspaceInfo) nameObject.getObject());
        }
        return this.getSelectedName();
    }
}
