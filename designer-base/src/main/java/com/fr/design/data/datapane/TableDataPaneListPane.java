package com.fr.design.data.datapane;

import com.fr.base.TableData;
import com.fr.data.TableDataSource;
import com.fr.data.api.StoreProcedureAssist;
import com.fr.data.impl.storeproc.StoreProcedure;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.gui.controlpane.JListControlPane;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.ilist.ListModelElement;
import com.fr.design.i18n.Toolkit;
import com.fr.file.ProcedureConfig;
import com.fr.file.TableDataConfig;
import com.fr.general.ComparatorUtils;
import com.fr.general.NameObject;
import com.fr.stable.Nameable;
import com.fr.stable.StringUtils;
import com.fr.stable.core.PropertyChangeAdapter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TableDataList Pane.
 */
public class TableDataPaneListPane extends JListControlPane implements TableDataPaneController {
    private boolean isNamePermitted = true;
    private Map<String, String> dsNameChangedMap = new HashMap<String, String>();

    public TableDataPaneListPane() {
        super();
        dsNameChangedMap.clear();
        this.addEditingListener(new PropertyChangeAdapter() {
            @Override
            public void propertyChange() {
                isNamePermitted = true;
                TableDataSource source = DesignTableDataManager.getEditingTableDataSource();
                String[] allDSNames = DesignTableDataManager.getAllDSNames(source);
                String[] allListNames = nameableList.getAllNames();
                allListNames[nameableList.getSelectedIndex()] = StringUtils.EMPTY;
                String tempName = getEditingName();
                Object editingType = getEditingType();
                if (StringUtils.isEmpty(tempName)) {
                    isNamePermitted = false;
                    nameableList.stopEditing();
                    FineJOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(TableDataPaneListPane.this), Toolkit.i18nText("Fine-Design_Basic_Table_Data_Empty_Name_Tips"));
                    setIllegalIndex(editingIndex);
                    return;
                }

                if (!ComparatorUtils.equals(tempName, selectedName)
                        && isNameRepeated(new List[]{Arrays.asList(allDSNames), Arrays.asList(allListNames)}, tempName)) {
                    isNamePermitted = false;
                    nameableList.stopEditing();
                    FineJOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(TableDataPaneListPane.this), Toolkit.i18nText("Fine-Design_Basic_Table_Data_Duplicate_Name_Tips", tempName));
                    setIllegalIndex(editingIndex);
                } else if (editingType instanceof StoreProcedure && isIncludeUnderline(tempName)) {
                    isNamePermitted = false;
                    nameableList.stopEditing();
                    FineJOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(TableDataPaneListPane.this), Toolkit.i18nText("Fine-Design_Basic_Stored_Procedure_Name_Tips"));
                    setIllegalIndex(editingIndex);
                }
                if (nameableList.getSelectedValue() instanceof ListModelElement) {
                    Nameable selected = ((ListModelElement) nameableList.getSelectedValue()).wrapper;
                    if (!ComparatorUtils.equals(tempName, selected.getName())) {
                        rename(selected.getName(), tempName);

                    }
                }
            }
        });
    }

    @Override
    public void rename(String oldName, String newName) {
        dsNameChangedMap.put(oldName, newName);
    }

    /**
     * 名字是否允许
     *
     * @return 是/否
     */
    @Override
    public boolean isNamePermitted() {
        return isNamePermitted;
    }


    /**
     * 检查按钮可用状态 Check button enabled.
     */
    @Override
    public void checkButtonEnabled() {
        super.checkButtonEnabled();
        isNamePermitted = !isContainsRename();
    }

    private boolean isIncludeUnderline(String name) {
        return name.contains(StoreProcedureAssist.GROUP_MARKER);
    }

    /**
     * 创建服务器数据集所需要的NameableCreator数组
     *
     * @return 数组
     */
    @Override
    public NameableCreator[] createNameableCreators() {
        return TableDataCreatorProducer.getInstance().createServerTableDataCreator();
    }


    // 只能保证当前模板不重名了

    /**
     * 新建不重名的面板
     *
     * @param prefix 前缀字符
     * @return 生成的名字
     */
    @Override
    public String createUnrepeatedName(String prefix) {
        TableDataSource source = DesignTableDataManager.getEditingTableDataSource();
        if (source == null) {
            return super.createUnrepeatedName(prefix);
        }
        String[] allDsNames = DesignTableDataManager.getAllDSNames(source);
        DefaultListModel model = this.getModel();
        Nameable[] all = new Nameable[model.getSize()];
        for (int i = 0; i < model.size(); i++) {
            all[i] = ((ListModelElement) model.get(i)).wrapper;
        }
        // richer:生成的名字从1开始. kunsnat: 添加属性从0开始.
        int count = all.length + 1;
        while (true) {
            String name_test = prefix + count;
            boolean repeated = false;
            for (int i = 0, len = model.size(); i < len; i++) {
                Nameable nameable = all[i];
                if (ComparatorUtils.equals(nameable.getName(), name_test)) {
                    repeated = true;
                    break;
                }
            }
            for (String dsname : allDsNames) {
                if (ComparatorUtils.equals(dsname, name_test)) {
                    repeated = true;
                    break;
                }
            }

            if (!repeated) {
                return name_test;
            }

            count++;
        }
    }

    @Override
    protected String title4PopupWindow() {
        return "TableData";
    }

    /**
     * Populate.
     */
    @Override
    public void populate(TableDataSource tds) {
        List<NameObject> nameObjectList = new ArrayList<NameObject>();

        Iterator tableDataNameIterator = tds.getTableDataNameIterator();
        while (tableDataNameIterator.hasNext()) {
            String tableDataName = (String) tableDataNameIterator.next();
            TableData tableData = tds.getTableData(tableDataName);

            if (tableData != null) {
                nameObjectList.add(new NameObject(tableDataName, tableData));
            }
        }

        populate(nameObjectList.toArray(new NameObject[nameObjectList.size()]));
    }

    /**
     * Populate.
     */
    @Override
    public void populate(TableDataConfig tableDataConfig) {
        Iterator<String> nameIt = tableDataConfig.getTableDatas().keySet().iterator();
        Iterator<String> procedurenameIt = ProcedureConfig.getInstance().getProcedures().keySet().iterator();
        List<NameObject> nameObjectList = new ArrayList<NameObject>();
        while (nameIt.hasNext()) {
            String name = nameIt.next();
            nameObjectList.add(new NameObject(name, tableDataConfig.getTableData(name)));
        }
        while (procedurenameIt.hasNext()) {
            String name = procedurenameIt.next();
            nameObjectList.add(new NameObject(name,  ProcedureConfig.getInstance().getProcedure(name)));
        }

        populate(nameObjectList.toArray(new NameObject[nameObjectList.size()]));
    }

    @Override
    public void update(TableDataConfig tableDataConfig) {
        tableDataConfig.removeAllTableData();
        ProcedureConfig.getInstance().removeAllProcedure();
        Nameable[] tableDataArray = this.update();
        Map<String,TableData> tableDataMap = new LinkedHashMap<String,TableData>();
        for (int i = 0; i < tableDataArray.length; i++) {
            NameObject nameObject = (NameObject) tableDataArray[i];
            tableDataMap.put(nameObject.getName(), (TableData) nameObject.getObject());
        }
        tableDataConfig.setTableDatas(tableDataMap);
    }

    @Override
    public void update(TableDataSource tds) {
        tds.clearAllTableData();

        Nameable[] tableDataArray = this.update();
        for (int i = 0; i < tableDataArray.length; i++) {
            NameObject nameObject = (NameObject) tableDataArray[i];
            tds.putTableData(nameObject.getName(), (TableData) nameObject.getObject());
        }
    }

    /**
     * 判断数据集是否重名
     */
    @Override
    public void checkValid() throws Exception {
        List<String> exsitTableDataNameList = new ArrayList<String>();
        // _denny: 判断是否有重复的数据集名
        checkRepeatedDSName(exsitTableDataNameList);

        Nameable[] tableDataArray = this.update();
        for (int i = 0; i < tableDataArray.length; i++) {
            NameObject nameObject = (NameObject) tableDataArray[i];

            if (exsitTableDataNameList.contains(nameObject.getName())) {
                throw new Exception(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Table_Data_Duplicate_Name_Tips", nameObject.getName()));
            }

            exsitTableDataNameList.add(nameObject.getName());
        }
    }

    protected void checkRepeatedDSName(List<String> exsitTableDataNameList) {
        // Do nothing
    }

    /**
     * 在JJControlPane的左侧Tree里面选中某一Item
     *
     * @param name 被选择的Item名称
     */
    public void selectName(String name) {
        this.setSelectedName(name);
    }

    @Override
    public Map<String, String> getDsNameChangedMap() {
        return this.dsNameChangedMap;
    }

    @Override
    public JPanel getPanel() {
        return this;
    }
}
