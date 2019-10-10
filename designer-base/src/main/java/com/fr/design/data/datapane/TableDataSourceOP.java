package com.fr.design.data.datapane;

import com.fr.base.StoreProcedureParameter;
import com.fr.base.TableData;
import com.fr.data.TableDataSource;
import com.fr.data.impl.storeproc.StoreProcedure;
import com.fr.data.impl.storeproc.StoreProcedureConstants;
import com.fr.design.DesignModelAdapter;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.data.tabledata.wrapper.StoreProcedureDataWrapper;
import com.fr.design.data.tabledata.wrapper.StoreProcedureNameWrapper;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.gui.itree.refreshabletree.ExpandMutableTreeNode;
import com.fr.design.gui.itree.refreshabletree.UserObjectOP;

import com.fr.general.NameObject;

import java.util.*;
import java.util.Map.Entry;

/**
 * 数据集树的操作
 *
 * @editor zhou
 * @since 2012-3-28下午9:58:53
 */
public class TableDataSourceOP implements UserObjectOP<TableDataWrapper> {
    public static final int TEMPLATE_TABLE_DATA = 0;
    public static final int SERVER_TABLE_DATA = 1;
    public static final int STORE_PRECEDURE_DATA = 2;
    public static int dataMode = -1;

    private DesignModelAdapter<?, ?> tc;


    public TableDataSourceOP(DesignModelAdapter<?, ?> tc) {
        this.tc = tc;
    }

    public DesignModelAdapter<?, ?> getDesignModelAdapter() {
        return tc;
    }

    public void setDesignModelAdapter(DesignModelAdapter<?, ?> tc) {
        this.tc = tc;
    }

    /**
     * 初始化数据集的list
     *
     * @return
     */
    @Override
    public List<Map<String, TableDataWrapper>> init() {
        if (tc != null) {
            return DesignTableDataManager.getEditingDataSet(tc.getBook());
        }
        List<Map<String, TableDataWrapper>> empty = new ArrayList<Map<String, TableDataWrapper>>();
        //数据集
        empty.add(Collections.<String, TableDataWrapper>emptyMap());
        //服务器数据集
        empty.add(Collections.<String, TableDataWrapper>emptyMap());
        //存储过程
        empty.add(Collections.<String, TableDataWrapper>emptyMap());
        return empty;
    }

    /**
     * ButtonEnabled intercept
     *
     * @return interceptbuttonEnabled
     */
    @Override
    public boolean interceptButtonEnabled() {
        return tc == null;
    }

    /**
     * 移除名字是name的TableData
     *
     * @param name tabledata name
     */
    @Override
    public void removeAction(String name) {
        if (tc != null) {
            TableDataSource tds = tc.getBook();
            tds.removeTableData(name);

            tc.fireTargetModified();
        }
    }

    protected ExpandMutableTreeNode[] getNodeArrayFromMap(Map<String, TableDataWrapper> map) {
        List<ExpandMutableTreeNode> dataList = new ArrayList<>();
        Iterator<Entry<String, TableDataWrapper>> entryIt = map.entrySet().iterator();
        while (entryIt.hasNext()) {
            Entry<String, TableDataWrapper> entry = entryIt.next();
            String name = entry.getKey();
            TableDataWrapper t = entry.getValue();

            ExpandMutableTreeNode newChildTreeNode = new ExpandMutableTreeNode(new NameObject(name, t));
            dataList.add(newChildTreeNode);
            newChildTreeNode.add(new ExpandMutableTreeNode());
        }
        return dataList.toArray(new ExpandMutableTreeNode[0]);
    }

    private ExpandMutableTreeNode initTemplateDataNode(Map<String, TableDataWrapper> templateDataMap) {
        ExpandMutableTreeNode templateNode = new ExpandMutableTreeNode(new NameObject(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_DS_TableData"), TEMPLATE_TABLE_DATA), true);
        templateNode.addChildTreeNodes(getNodeArrayFromMap(templateDataMap));
        return templateNode;
    }

    private ExpandMutableTreeNode initServerDataNode(Map<String, TableDataWrapper> serverDataMap) {
        ExpandMutableTreeNode templateNode = new ExpandMutableTreeNode(new NameObject(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_DS_Server_TableData"), SERVER_TABLE_DATA), false);
        templateNode.addChildTreeNodes(getNodeArrayFromMap(serverDataMap));
        return templateNode;
    }

    private ExpandMutableTreeNode initStoreProcedureNode(Map<String, TableDataWrapper> storeProcedureMap) {
        ExpandMutableTreeNode templateNode = new ExpandMutableTreeNode(new NameObject(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Datasource_Stored_Procedure"), STORE_PRECEDURE_DATA), false);
        templateNode.addChildTreeNodes(getNodeArrayFromMap(storeProcedureMap));
        return templateNode;
    }

    /**
     * 根据不同模式生成子节点
     *
     * @return
     */
    @Override
    public ExpandMutableTreeNode[] load() {
        Map<String, TableDataWrapper> templateDataMap;
        Map<String, TableDataWrapper> serverDataMap;
        Map<String, TableDataWrapper> storeProcedureMap;

        templateDataMap = this.init().get(0);
        serverDataMap = this.init().get(1);
        storeProcedureMap = this.init().get(2);

        //所有的数据集
        List<ExpandMutableTreeNode> list = new ArrayList<>();
        //模板数据集
        List<ExpandMutableTreeNode> templist = new ArrayList<>();
        //服务器数据集
        List<ExpandMutableTreeNode> serverlist = new ArrayList<>();

        list.add(initTemplateDataNode(templateDataMap));
        addNodeToList(templateDataMap, templist);
        if (!serverDataMap.isEmpty()) {
            list.add(initServerDataNode(serverDataMap));
            addNodeToList(serverDataMap, serverlist);
        }
        if (!storeProcedureMap.isEmpty()) {
            list.add(initStoreProcedureNode(storeProcedureMap));
            for (int i = 0; i < getNodeArrayFromMap(storeProcedureMap).length; i++) {
                ExpandMutableTreeNode tmpNode = getNodeArrayFromMap(storeProcedureMap)[i];
                if (((NameObject) tmpNode.getUserObject()).getObject() instanceof StoreProcedureNameWrapper) {
                    TableData tableData = ((StoreProcedureNameWrapper) (((NameObject) tmpNode.getUserObject()).getObject())).getStoreProcedure();
                    setStoreProcedureTree(tableData, tmpNode);
                    serverlist.add(tmpNode);
                }
            }
        }
        switch (dataMode) {
            case TEMPLATE_TABLE_DATA:
                return templist.toArray(new ExpandMutableTreeNode[0]);
            case SERVER_TABLE_DATA:
                return serverlist.toArray(new ExpandMutableTreeNode[0]);
            default:
                return list.toArray(new ExpandMutableTreeNode[0]);
        }
    }

    private void addNodeToList(Map<String, TableDataWrapper> dataMap, List<ExpandMutableTreeNode> dataList) {
        for (int i = 0; i < getNodeArrayFromMap(dataMap).length; i++) {
            ExpandMutableTreeNode tmpNode = getNodeArrayFromMap(dataMap)[i];
            TableData tableData = ((TableDataWrapper) (((NameObject) tmpNode.getUserObject()).getObject())).getTableData();
            if (tableData instanceof StoreProcedure) {
                setStoreProcedureTree(tableData, tmpNode);
                dataList.add(tmpNode);
            } else {
                dataList.add(tmpNode);
            }
        }
    }

    protected void setStoreProcedureTree(TableData tableData, ExpandMutableTreeNode tmpNode) {
        ArrayList<String> nodeName = new ArrayList<>();
        StoreProcedure storeProcedure = (StoreProcedure) tableData;
        String name = ((NameObject) tmpNode.getUserObject()).getName();
        StoreProcedureParameter[] parameters = StoreProcedure.getSortPara(storeProcedure.getParameters());
        List<String> resultNames = storeProcedure.getResultNames();
        boolean hasChild = false;
        tmpNode.remove(0);
        TableDataWrapper tdw = new StoreProcedureNameWrapper(name + "_Table1", storeProcedure);
        ExpandMutableTreeNode childNode = new ExpandMutableTreeNode(new NameObject("Table", tdw));
        childNode.add(new ExpandMutableTreeNode());
        tmpNode.add(childNode);
        for (StoreProcedureParameter parameter : parameters) {
            if (parameter.getSchema() != StoreProcedureConstants.IN) {
                if (!nodeName.contains(parameter.getName())) {
                    nodeName.add(parameter.getName());
                    hasChild = true;
                    String parameterName = name + "_" + parameter.getName();
                    TableDataWrapper newTwd = new StoreProcedureDataWrapper(storeProcedure, name, parameterName, false);
                    ExpandMutableTreeNode newChildNode = new ExpandMutableTreeNode(new NameObject(parameter.getName(), newTwd));
                    newChildNode.add(new ExpandMutableTreeNode());
                    tmpNode.add(newChildNode);
                }
            }
        }

        if (!resultNames.isEmpty()) {
            for (String resultName : resultNames) {
                if (!nodeName.contains(resultName)) {
                    nodeName.add(resultName);
                    hasChild = true;
                    String parameterName = name + "_" + resultName;
                    TableDataWrapper newTwd = new StoreProcedureDataWrapper(storeProcedure, name, parameterName, false);
                    ExpandMutableTreeNode newChildNode = new ExpandMutableTreeNode(new NameObject(resultName, newTwd));
                    newChildNode.add(new ExpandMutableTreeNode());
                    tmpNode.add(newChildNode);
                }

            }

        }

        if (hasChild) {
            tmpNode.remove(0);
        }
    }


    public void setDataMode(int i) {
        dataMode = i;
    }

    public int getDataMode() {
        return dataMode;
    }

}