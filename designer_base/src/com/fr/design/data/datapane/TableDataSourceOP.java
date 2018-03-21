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
import com.fr.general.Inter;
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
        empty.add(Collections.<String, TableDataWrapper>emptyMap());//数据集
        empty.add(Collections.<String, TableDataWrapper>emptyMap());//服务器数据集
        empty.add(Collections.<String, TableDataWrapper>emptyMap());//存储过程
        return empty;
    }

    /**
     * ButtonEnabled intercept
     * @return interceptbuttonEnabled
     */
    @Override
    public boolean interceptButtonEnabled() {
        return tc == null;
    }

    /**
     * 移除名字是name的TableData
     * @param name tabledata name
     */
    public void removeAction(String name) {
        if (tc != null) {
            TableDataSource tds = tc.getBook();
            tds.removeTableData(name);

            tc.fireTargetModified();
        }
    }

    protected ExpandMutableTreeNode[] getNodeArrayFromMap(Map<String, TableDataWrapper> map) {
        List<ExpandMutableTreeNode> dataList = new ArrayList<ExpandMutableTreeNode>();
        Iterator<Entry<String, TableDataWrapper>> entryIt = map.entrySet().iterator();
        while (entryIt.hasNext()) {
            Entry<String, TableDataWrapper> entry = entryIt.next();
            String name = entry.getKey();
            TableDataWrapper t = entry.getValue();

            ExpandMutableTreeNode newChildTreeNode = new ExpandMutableTreeNode(new NameObject(name, t));
            dataList.add(newChildTreeNode);
            newChildTreeNode.add(new ExpandMutableTreeNode());
        }
        return dataList.toArray(new ExpandMutableTreeNode[dataList.size()]);
    }

    private ExpandMutableTreeNode initTemplateDataNode(Map<String, TableDataWrapper> templateDataMap) {
        ExpandMutableTreeNode templateNode = new ExpandMutableTreeNode(new NameObject(Inter.getLocText("DS-TableData"), TEMPLATE_TABLE_DATA), true);
        templateNode.addChildTreeNodes(getNodeArrayFromMap(templateDataMap));
        return templateNode;
    }

    private ExpandMutableTreeNode initServerDataNode(Map<String, TableDataWrapper> serverDataMap) {
        ExpandMutableTreeNode templateNode = new ExpandMutableTreeNode(new NameObject(Inter.getLocText("DS-Server_TableData"), SERVER_TABLE_DATA), false);
        templateNode.addChildTreeNodes(getNodeArrayFromMap(serverDataMap));
        return templateNode;
    }

    private ExpandMutableTreeNode initStoreProcedureNode(Map<String, TableDataWrapper> storeProcedureMap) {
        ExpandMutableTreeNode templateNode = new ExpandMutableTreeNode(new NameObject(Inter.getLocText("Datasource-Stored_Procedure"), STORE_PRECEDURE_DATA), false);
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
        Map<String, TableDataWrapper> templateDataMap = null;
        Map<String, TableDataWrapper> serverDataMap = null;
        Map<String, TableDataWrapper> storeProcedureMap = null;

        if (this != null) {
            templateDataMap = this.init().get(0);
            serverDataMap = this.init().get(1);
            storeProcedureMap = this.init().get(2);
        } else {
            templateDataMap = Collections.emptyMap();
            serverDataMap = Collections.emptyMap();
            storeProcedureMap = Collections.emptyMap();
        }
        List<ExpandMutableTreeNode> list = new ArrayList<ExpandMutableTreeNode>(); //所有的数据集
        List<ExpandMutableTreeNode> templist = new ArrayList<ExpandMutableTreeNode>(); //模板数据集
        List<ExpandMutableTreeNode> serverlist = new ArrayList<ExpandMutableTreeNode>();   //服务器数据集

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
                return templist.toArray(new ExpandMutableTreeNode[templist.size()]);
            case SERVER_TABLE_DATA:
                return serverlist.toArray(new ExpandMutableTreeNode[serverlist.size()]);
            default:
                return list.toArray(new ExpandMutableTreeNode[list.size()]);
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
        ArrayList<String> nodeName = new ArrayList<String>();
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
            for (int i = 0; i < resultNames.size(); i++) {
                if (!nodeName.contains(resultNames.get(i))) {
                    nodeName.add(resultNames.get(i));
                    hasChild = true;
                    String parameterName = name + "_" + resultNames.get(i);
                    TableDataWrapper newTwd = new StoreProcedureDataWrapper(storeProcedure, name, parameterName, false);
                    ExpandMutableTreeNode newChildNode = new ExpandMutableTreeNode(new NameObject(resultNames.get(i), newTwd));
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
        this.dataMode = i;
    }

    public int getDataMode() {
        return dataMode;
    }

}