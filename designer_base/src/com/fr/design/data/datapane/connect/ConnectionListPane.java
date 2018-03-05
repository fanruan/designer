package com.fr.design.data.datapane.connect;

import com.fr.data.impl.Connection;
import com.fr.data.impl.JDBCDatabaseConnection;
import com.fr.data.impl.JNDIDatabaseConnection;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.fun.ConnectionProvider;
import com.fr.design.gui.controlpane.JListControlPane;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.file.ConnectionConfig;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.general.NameObject;
import com.fr.stable.ArrayUtils;
import com.fr.stable.Nameable;
import com.fr.stable.StringUtils;
import com.fr.stable.core.PropertyChangeAdapter;

import javax.swing.*;
import java.util.*;

/**
 * Connection List Pane.
 */
public class ConnectionListPane extends JListControlPane implements ConnectionShowPane {
    public static final String TITLE_NAME = Inter.getLocText("Server-Define_Data_Connection");
    private boolean isNamePermitted = true;
    private HashMap<String, String> renameMap = new HashMap<String, String>();

    public ConnectionListPane() {
        renameMap.clear();
        this.addEditingListner(new PropertyChangeAdapter() {
            public void propertyChange() {
                isNamePermitted = true;
                String[] allListNames = nameableList.getAllNames();
                allListNames[nameableList.getSelectedIndex()] = StringUtils.EMPTY;
                String tempName = getEditingName();
                if (StringUtils.isEmpty(tempName)) {
                    String[] warning = new String[]{"NOT_NULL_Des", "Please_Rename"};
                    String[] sign = new String[]{",", "!"};
                    nameableList.stopEditing();
                    JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(ConnectionListPane.this), Inter.getLocText(warning, sign));
                    setWarnigText(editingIndex);
                    isNamePermitted = false;
                    return;
                }
                if (!ComparatorUtils.equals(tempName, selectedName)
                        && isNameRepeted(new List[]{Arrays.asList(allListNames)}, tempName)) {
                    isNamePermitted = false;
                    nameableList.stopEditing();
                    String message = Inter.getLocText(new String[]{"Utils-has_been_existed", "DashBoard-ConnectionList", "Please_Rename"}, new String[]{"", tempName + ",", "!"});
                    JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(ConnectionListPane.this), message);
                    setWarnigText(editingIndex);
                }
                if (isNamePermitted && !ComparatorUtils.equals(tempName, selectedName)) {
                    rename(selectedName, tempName);
                }

            }
        });
    }


    protected void rename(String oldName, String newName) {
        if (renameMap.containsKey(selectedName)) {
            renameMap.remove(selectedName);
        }
        renameMap.put(selectedName, newName);
    }

    /**
     * 名字是否允许
     *
     * @return 是/否
     */
    public boolean isNamePermitted() {
        return isNamePermitted;
    }

    /**
     * 检查按钮可用状态 Check button enabled.
     */
    public void checkButtonEnabled() {
        super.checkButtonEnabled();
        isNamePermitted = !isContainsRename();
    }


    public HashMap<String, String> getRenameMap() {
        return renameMap;
    }

    /**
     * 创建菜单项
     *
     * @return 菜单项
     */
    public NameableCreator[] createNameableCreators() {
        NameableCreator[] creators = new NameableCreator[]{new NameObjectCreator(
                "JDBC",
                "/com/fr/design/images/data/source/jdbcTableData.png",
                JDBCDatabaseConnection.class,
                DatabaseConnectionPane.JDBC.class
        ), new NameObjectCreator(
                "JNDI",
                "/com/fr/design/images/data/source/jdbcTableData.png",
                JNDIDatabaseConnection.class,
                DatabaseConnectionPane.JNDI.class
        )};
        Set<ConnectionProvider> pluginCreators = ExtraDesignClassManager.getInstance().getArray(ConnectionProvider.XML_TAG);
        for (ConnectionProvider provider : pluginCreators) {
            NameObjectCreator creator = new NameObjectCreator(
                    provider.nameForConnection(),
                    provider.iconPathForConnection(),
                    provider.classForConnection(),
                    provider.appearanceForConnection()
            );
            creators = ArrayUtils.add(creators, creator);
        }

        return creators;
    }

    @Override
    protected String title4PopupWindow() {
        return TITLE_NAME;
    }

    /**
     * Populate.
     *
     * @param connectionConfig the new datasourceManager.
     */
    public void populate(ConnectionConfig connectionConfig) {
        Iterator<String> nameIt = connectionConfig.getConnectionNameIterator();

        List<NameObject> nameObjectList = new ArrayList<NameObject>();
        while (nameIt.hasNext()) {
            String name = nameIt.next();
            nameObjectList.add(new NameObject(name, connectionConfig.getConnection(name)));
        }
        this.populate(nameObjectList.toArray(new NameObject[nameObjectList.size()]));

    }

    /**
     * Update.
     */
    public void update(ConnectionConfig connectionConfig) {
        // Nameable[]居然不能强转成NameObject[],一定要这么写...
        Nameable[] res = this.update();
        NameObject[] res_array = new NameObject[res.length];
        java.util.Arrays.asList(res).toArray(res_array);

        connectionConfig.clearAllConnection();

        for (int i = 0; i < res_array.length; i++) {
            NameObject nameObject = res_array[i];
            connectionConfig.putConnection(nameObject.getName(), (Connection) nameObject.getObject());
        }
    }
}