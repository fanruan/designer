package com.fr.env;

import com.fr.base.Env;
import com.fr.core.env.EnvConfig;
import com.fr.dav.LocalEnv;
import com.fr.design.DesignerEnvManager;
import com.fr.design.gui.controlpane.JListControlPane;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.general.NameObject;
import com.fr.stable.Nameable;
import com.fr.stable.StringUtils;
import com.fr.stable.core.PropertyChangeAdapter;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
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
        addEditingListner(new PropertyChangeAdapter() {
            @Override
            public void propertyChange() {
                String tempName = getEditingName();
                String[] allListNames = nameableList.getAllNames();
                allListNames[nameableList.getSelectedIndex()] = StringUtils.EMPTY;
                if (StringUtils.isEmpty(tempName)) {
                    String[] warning = new String[]{"NOT_NULL_Des", "Please_Rename"};
                    String[] sign = new String[]{",", "!"};
                    nameableList.stopEditing();
                    JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(EnvListPane.this), Inter.getLocText(warning, sign));
                    setWarnigText(editingIndex);
                    return;
                }
                if (!ComparatorUtils.equals(tempName, selectedName) && isNameRepeted(new List[]{Arrays.asList(allListNames)}, tempName)) {
                    String[] waning = new String[]{"already_exists", "Utils-Report_Runtime_Env", "Please_Rename"};
                    String[] sign = new String[]{"", tempName + ",", "!"};
                    nameableList.stopEditing();
                    JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(EnvListPane.this), Inter.getLocText(waning, sign));
                    setWarnigText(editingIndex);
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
        NameableCreator local = new NameObjectCreator(Inter.getLocText("Env-Local_Directory"), "com/fr/design/images/data/bind/localconnect.png",
                LocalEnv.class, LocalEnvPane.class);
        NameableCreator remote = new NameObjectCreator(Inter.getLocText("Env-Remote_Server"), "com/fr/design/images/data/bind/distanceconnect.png",
                RemoteEnv.class, RemoteEnvPane.class);
        return new NameableCreator[]{local, remote};
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("Env-Configure_Workspace");
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
            nameObjectList.add(new NameObject(name, mgr.getEnv(name)));
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
        mgr.clearAllEnv();
        Nameable[] res = this.update();
        for (Nameable re : res) {
            NameObject nameObject = (NameObject) re;
            mgr.putEnv(nameObject.getName(), (EnvConfig) nameObject.getObject());
        }
        return this.getSelectedName();
    }
}