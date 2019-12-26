package com.fr.design.data.datapane;

import com.fr.base.BaseUtils;
import com.fr.base.TableData;
import com.fr.data.TableDataSource;
import com.fr.data.impl.TableDataSourceDependent;
import com.fr.design.DesignModelAdapter;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.actions.UpdateAction;
import com.fr.design.constants.UIConstants;
import com.fr.design.data.BasicTableDataTreePane;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.data.tabledata.StoreProcedureWorkerListener;
import com.fr.design.data.tabledata.tabledatapane.AbstractTableDataPane;
import com.fr.design.data.tabledata.wrapper.AbstractTableDataWrapper;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.file.HistoryTemplateListCache;
import com.fr.design.fun.TableDataDefineProvider;
import com.fr.design.fun.TableDataPaneProcessor;
import com.fr.design.gui.ibutton.UIHeadGroup;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.gui.itoolbar.UIToolbar;
import com.fr.design.icon.IconPathConstants;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.SeparatorDef;
import com.fr.design.menu.ToolBarDef;
import com.fr.general.ComparatorUtils;
import com.fr.general.GeneralContext;
import com.fr.general.NameObject;
import com.fr.plugin.context.PluginContext;
import com.fr.plugin.injectable.PluginModule;
import com.fr.plugin.manage.PluginFilter;
import com.fr.plugin.observer.PluginEvent;
import com.fr.plugin.observer.PluginEventListener;
import com.fr.stable.core.PropertyChangeAdapter;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.dnd.DnDConstants;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TableDataTreePane extends BasicTableDataTreePane {
    private static TableDataTreePane singleton = new TableDataTreePane();
    
    public static final int PLUGIN_LISTENER_PRIORITY = 1;

    public synchronized static BasicTableDataTreePane getInstance(DesignModelAdapter<?, ?> tc) {

        TableDataPaneProcessor treePaneProcessor = ExtraDesignClassManager.getInstance().getSingle(TableDataPaneProcessor.XML_TAG);
        if (treePaneProcessor != null) {
            return treePaneProcessor.createTableDataTreePane(tc);
        }
        if (singleton.tc == null) {
            singleton.addMenuDef.clearShortCuts();
            singleton.createAddMenuDef();
        }
        singleton.tc = tc;
        singleton.op = new TableDataSourceOP(tc);
        singleton.op.setDataMode(singleton.buttonGroup.getSelectedIndex() == 0 ? TEMPLATE_TABLE_DATA : SERVER_TABLE_DATA);
        singleton.refreshDockingView();
        return singleton;
    }

    private TableDataSourceOP op;
    private TableDataTree dataTree;
    private EditAction editAction;
    private RemoveAction removeAction;
    private PreviewTableDataAction previewTableDataAction;

    private TableDataTreePane() {
        this.setLayout(new BorderLayout(4, 0));
        this.setBorder(null);
        dataTree = new TableDataTree();
        ToolTipManager.sharedInstance().registerComponent(dataTree);
        ToolTipManager.sharedInstance().setDismissDelay(3000);
        ToolTipManager.sharedInstance().setInitialDelay(0);
        addMenuDef = new MenuDef(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Action_Add"));
        addMenuDef.setIconPath(IconPathConstants.ADD_POPMENU_ICON_PATH);

        createAddMenuDef();
    
        // 创建插件监听
        createPluginListener();
    
        editAction = new EditAction();
        removeAction = new RemoveAction();
        previewTableDataAction = new PreviewTableDataAction(dataTree);
        connectionTableAction = new ConnectionTableAction();
        ToolBarDef toolbarDef = new ToolBarDef();
        toolbarDef.addShortCut(addMenuDef, SeparatorDef.DEFAULT, editAction, removeAction, SeparatorDef.DEFAULT, previewTableDataAction, connectionTableAction);
        UIToolbar toolBar = ToolBarDef.createJToolBar();
        toolBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIConstants.TOOLBAR_BORDER_COLOR));
        toolBar.setBorderPainted(true);
        toolbarDef.updateToolBar(toolBar);

        JPanel toolbarPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        toolbarPane.add(toolBar, BorderLayout.CENTER);
        this.add(toolbarPane, BorderLayout.NORTH);

        UIScrollPane scrollPane = new UIScrollPane(dataTree);
        scrollPane.setBorder(null);
        initbuttonGroup();
        JPanel jPanel = new JPanel(new BorderLayout(0, 0));
        JPanel buttonPane = new JPanel(new GridLayout());
        buttonPane.add(buttonGroup, BorderLayout.CENTER);
        jPanel.add(buttonPane, BorderLayout.NORTH);
        jPanel.add(scrollPane, BorderLayout.CENTER);
        this.add(jPanel, BorderLayout.CENTER);
        dataTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                checkButtonEnabled();
            }
        });
        dataTree.addKeyListener(getTableTreeNodeListener(editAction, previewTableDataAction, removeAction, op, dataTree));
        // TreeCellEditor
        dataTree.setEditable(true);
        TableDataTreeCellEditor treeCellEditor = new TableDataTreeCellEditor(new UITextField(), dataTree, this);
        treeCellEditor.addCellEditorListener(treeCellEditor);
        dataTree.setCellEditor(treeCellEditor);
        new TableDataTreeDragSource(dataTree, DnDConstants.ACTION_COPY);
        checkButtonEnabled();
    }
    
    private void createPluginListener() {
        
        //菜单栏监听
        GeneralContext.listenPluginRunningChanged(new PluginEventListener(PLUGIN_LISTENER_PRIORITY) {
        
            @Override
            public void on(PluginEvent event) {
            
                addMenuDef.clearShortCuts();
                createAddMenuDef();
            }
        }, new PluginFilter() {
        
            @Override
            public boolean accept(PluginContext context) {
    
                return context.contain(PluginModule.ExtraDesign);
            }
        });
        
        //监听数据集插件
        GeneralContext.listenPluginRunningChanged(new PluginEventListener() {
            @Override
            public void on(PluginEvent event) {
                //REPORT-25577
                //如果数据集插件禁用或启用。需要清空当前模板中的缓存
                reloadCurrTemplate();
            }
    
            private void reloadCurrTemplate() {
                JTemplate<?, ?> jt = HistoryTemplateListCache.getInstance().getCurrentEditingTemplate();
                HistoryTemplateListCache.getInstance().closeSelectedReport(jt);
                DesignerContext.getDesignerFrame().openTemplate(jt.getEditingFILE());
            }
        }, new PluginFilter() {
            @Override
            public boolean accept(PluginContext pluginContext) {
                
                return pluginContext.contain(TableDataDefineProvider.XML_TAG);
            }
        });
    }
    
    
    protected void checkButtonEnabled() {
        super.checkButtonEnabled(editAction, previewTableDataAction, removeAction, op, dataTree);
    }

    /**
     * 刷新
     */
    public void refreshDockingView() {
        populate(new TableDataSourceOP(tc));
        this.checkButtonEnabled();
    }

    protected void initbuttonGroup() {
//        Icon[] iconArray = {BaseUtils.readIcon("/com/fr/design/images/data/datasource.png"), BaseUtils.readIcon("/com/fr/design/images/data/dock/serverdatabase.png")};
        final Integer[] modeArray = {TEMPLATE_TABLE_DATA, SERVER_TABLE_DATA};
        String[] textArray = {com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Tabledata_Source_Type_Template"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_DS_Server_TableData")};
        buttonGroup = new UIHeadGroup(textArray) {
            public void tabChanged(int index) {
                if (op != null) {
                    op.setDataMode(modeArray[buttonGroup.getSelectedIndex()]);
                    addMenuDef.setEnabled(modeArray[buttonGroup.getSelectedIndex()] == TEMPLATE_TABLE_DATA ? true : false);
                    refreshDockingView();
                }
            }
        };
        buttonGroup.setNeedLeftRightOutLine(false);
    }

    /**
     * 编辑面板
     *
     * @param uPanel       面板
     * @param originalName 原始名字
     */
    public void dgEdit(final AbstractTableDataPane<?> uPanel, String originalName, boolean isUpdate) {
        uPanel.addStoreProcedureWorkerListener(new StoreProcedureWorkerListener() {
            public void fireDoneAction() {
                if (dataTree.getSelectionPath() == null) {
                    dataTree.refresh();
                } else {
                    Object object = dataTree.getSelectionPath().getLastPathComponent();
                    int[] rows = dataTree.getSelectionRows();
                    dataTree.refreshChildByName(object.toString());
                    dataTree.setSelectionRows(rows);
                }
            }
        });
        final BasicPane.NamePane nPanel = uPanel.asNamePane();
        nPanel.setObjectName(originalName);
        final String oldName = originalName;
        final BasicDialog dg;
        allDSNames = DesignTableDataManager.getAllDSNames(tc.getBook());
        dg = nPanel.showLargeWindow(SwingUtilities.getWindowAncestor(TableDataTreePane.this), new DialogActionAdapter() {
            public void doOk() {
                DesignTableDataManager.setThreadLocal(DesignTableDataManager.NO_PARAMETER);
                tc.renameTableData(oldName, nPanel.getObjectName(), false);
                TableDataSource tds = tc.getBook();
                TableData td = uPanel.updateBean();
                if (td instanceof TableDataSourceDependent) {
                    ((TableDataSourceDependent) td).setTableDataSource(tds);
                }
                String tdName = nPanel.getObjectName();
                tds.putTableData(tdName, td);
                Map<String, String> map = new HashMap<String, String>();
                if (!ComparatorUtils.equals(oldName, tdName)) {
                    map.put(oldName, tdName);
                }
                fireDSChanged(map);
                tc.fireTargetModified();
                tc.parameterChanged();
                int[] rows = dataTree.getSelectionRows();
                dataTree.refreshChildByName(tdName);
                dataTree.setSelectionRows(rows);
            }
        });
        nPanel.addPropertyChangeListener(new PropertyChangeAdapter() {
            @Override
            public void propertyChange() {
                doPropertyChange(dg, nPanel, oldName);
            }
        });
        dg.setVisible(true);
    }

    private class EditAction extends UpdateAction {
        public EditAction() {
            this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Edit"));
            this.setMnemonic('E');
            this.setSmallIcon(BaseUtils.readIcon(IconPathConstants.TD_EDIT_ICON_PATH));
        }

        public void actionPerformed(ActionEvent e) {
            final NameObject selectedNO = dataTree.getSelectedNameObject();
            if (selectedNO == null) {
                return;
            }
            dgEdit(((AbstractTableDataWrapper) selectedNO.getObject()).creatTableDataPane(), selectedNO.getName(), false);
        }
    }

    private class RemoveAction extends UpdateAction {

        public RemoveAction() {
            this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Remove"));
            this.setMnemonic('R');
            this.setSmallIcon(BaseUtils.readIcon(IconPathConstants.TD_REMOVE_ICON_PATH));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            NameObject selectedNO = dataTree.getSelectedNameObject();

            if (selectedNO == null) {
                return;
            }

            int returnVal = JOptionPane.showConfirmDialog(DesignerContext.getDesignerFrame(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Utils_Are_You_Sure_To_Remove_The_Selected_Item") + ":" + selectedNO.getName() + "?",
                    com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Remove"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (returnVal == JOptionPane.OK_OPTION) {
                // richer:这个地方为什么要在DataSourceTree里面去remove呢？多此一举吧
                op.removeAction(selectedNO.getName());
                dataTree.refresh();
                // Richie:默认最后一行获得焦点.
                dataTree.requestFocus();
                dataTree.setSelectionRow(dataTree.getRowCount() - 1);
                fireDSChanged();
                checkButtonEnabled();
            }
        }
    }

    private void populate(TableDataSourceOP op) {
        this.op = op;
        dataTree.populate(op);
        checkButtonEnabled();
    }


    /**
     * @return
     */
    public TableDataTree getDataTree() {
        return dataTree;
    }

    /**
     * 合并数据集
     * @param srcName 数据集来源(比如报表块，就是报表块的名称)
     * @param tableDataSource 数据集
     */
    public Map<String, String> addTableData(String srcName, TableDataSource tableDataSource) {
        Map<String, String> tdNameMap = new HashMap<>();
        allDSNames = DesignTableDataManager.getAllDSNames(tc.getBook());
        DesignTableDataManager.setThreadLocal(DesignTableDataManager.NO_PARAMETER);
        TableDataSource tds = tc.getBook();
        Iterator tdIterator = tableDataSource.getTableDataNameIterator();
        while (tdIterator.hasNext()) {
            String tdName = (String) tdIterator.next();
            String oldName = tdName;
            TableData td = tableDataSource.getTableData(tdName);
            if (tds.getTableData(tdName) != null || isDsNameRepeaded(tdName)) {//如果有同名的就拼上来源名称
                tdName = srcName + tdName;
            }
            int i = 0;
            while (tds.getTableData(tdName) != null) {
                i++;//如果拼上名字后依然已经存在就加编号
                tdName += i;
            }
            tds.putTableData(tdName, td);
            if (!ComparatorUtils.equals(oldName, tdName)) {
                tdNameMap.put(oldName, tdName);
            }
        }
        tc.parameterChanged();
        dataTree.refresh();
        return  Collections.unmodifiableMap(tdNameMap);
    }
    public void addDataPane(final AbstractTableDataPane<?> uPanel, String paneName) {
        final NamePane nPanel = uPanel.asNamePane();
        nPanel.setObjectName(paneName);
        final String oldName = paneName;

        allDSNames = DesignTableDataManager.getAllDSNames(tc.getBook());

        DesignTableDataManager.setThreadLocal(DesignTableDataManager.NO_PARAMETER);
        tc.renameTableData(oldName, nPanel.getObjectName(), false);
        TableDataSource tds = tc.getBook();
        TableData td = uPanel.updateBean();
        if (td instanceof TableDataSourceDependent) {
            ((TableDataSourceDependent) td).setTableDataSource(tds);
        }
        String tdName = nPanel.getObjectName();
        tds.putTableData(tdName, td);
        Map<String, String> map = new HashMap<String, String>();
        if (!ComparatorUtils.equals(oldName, tdName)) {
            map.put(oldName, tdName);
        }
        fireDSChanged(map);
        tc.fireTargetModified();
        tc.parameterChanged();
        int[] rows = dataTree.getSelectionRows();
        dataTree.refreshChildByName(tdName);
        dataTree.setSelectionRows(rows);
    }
}
