package com.fr.design.data;

import com.fr.base.BaseUtils;
import com.fr.base.TableData;
import com.fr.data.TableDataSource;
import com.fr.data.api.StoreProcedureAssist;
import com.fr.data.impl.storeproc.StoreProcedure;
import com.fr.design.DesignModelAdapter;
import com.fr.design.actions.UpdateAction;
import com.fr.design.data.datapane.TableDataCreatorProducer;
import com.fr.design.data.datapane.TableDataNameObjectCreator;
import com.fr.design.data.datapane.TableDataSourceOP;
import com.fr.design.data.datapane.TableDataTree;
import com.fr.design.data.tabledata.ResponseDataSourceChange;
import com.fr.design.data.tabledata.tabledatapane.AbstractTableDataPane;
import com.fr.design.data.tabledata.wrapper.StoreProcedureDataWrapper;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.data.tabledata.wrapper.TemplateTableDataWrapper;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIHeadGroup;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.gui.itree.refreshabletree.ExpandMutableTreeNode;
import com.fr.design.icon.IconPathConstants;
import com.fr.design.mainframe.DockingView;
import com.fr.design.menu.LineSeparator;
import com.fr.design.menu.MenuDef;
import com.fr.general.ComparatorUtils;
import com.fr.general.NameObject;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.StringUtils;
import com.fr.workspace.WorkContext;

import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.TreeCellEditor;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Coder: zack
 * Date: 2016/4/22
 * Time: 16:23
 */
public abstract class BasicTableDataTreePane extends DockingView implements ResponseDataSourceChange {
    protected static final int PROCEDURE_NAME_INDEX = 4;
    protected static final int TEMPLATE_TABLE_DATA = 0;
    protected static final int SERVER_TABLE_DATA = 1;
    protected MenuDef addMenuDef;
    protected DesignModelAdapter<?, ?> tc;
    protected UIHeadGroup buttonGroup;
    protected String[] allDSNames;
    protected ConnectionTableAction connectionTableAction;


    private String type = "";

    /**
     * 最佳位置
     *
     * @return 返回位置
     */
    @Override
    public Location preferredLocation() {
        return Location.WEST_ABOVE;
    }

    /**
     * getViewTitle()
     *
     * @return
     */
    @Override
    public String getViewTitle() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_TableData");
    }

    /**
     * getViewIcon
     *
     * @return
     */
    @Override
    public Icon getViewIcon() {
        return BaseUtils.readIcon(IconPathConstants.DS_ICON_PATH);
    }

    /**
     * 响应数据集改变
     */
    @Override
    public void fireDSChanged() {
        fireDSChanged(new HashMap<String, String>());
    }


    /**
     * 响应数据集改变
     *
     * @param map 数据集变化Map
     */
    @Override
    public void fireDSChanged(Map<String, String> map) {
        DesignTableDataManager.fireDSChanged(map);
    }

    public void dgEdit(final AbstractTableDataPane<?> uPanel, String originalName) {
        dgEdit(uPanel, originalName, false);
    }

    public abstract void dgEdit(final AbstractTableDataPane<?> uPanel, String originalName, boolean isUpdate);

    protected void doPropertyChange(BasicDialog dg, BasicPane.NamePane nPanel, final String oldName) {
        type = dg.getTitle();
        nPanel.setShowText(StringUtils.BLANK);
        dg.setButtonEnabled(true);
        String tempName = nPanel.getObjectName();
        if (StringUtils.isBlank(tempName)) {
            nPanel.setShowText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Table_Data_Empty_Name_Tips"));
            dg.setButtonEnabled(false);
        } else if (!ComparatorUtils.equals(oldName, tempName) && isDsNameRepeaded(tempName)) {
            nPanel.setShowText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Table_Data_Duplicate_Name_Tips", tempName));
            dg.setButtonEnabled(false);
        } else if (isProcedureName(oldName)) {
            if (isIncludeUnderline(tempName)) {
                nPanel.setShowText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Stored_Procedure_Name_Tips"));
                dg.setButtonEnabled(false);
            }
        } else if (!BasicTableDataUtils.checkName(tempName)) {
            dg.setButtonEnabled(false);
        } else {
            nPanel.setShowText(StringUtils.BLANK);
            dg.setButtonEnabled(true);
        }
    }

    private boolean isProcedureName(String oldName) {
        return oldName.length() >= PROCEDURE_NAME_INDEX && ComparatorUtils.equals(type, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Datasource_Stored_Procedure"));
    }


    private boolean isIncludeUnderline(String name) {
        return ComparatorUtils.equals(name.indexOf(StoreProcedureAssist.GROUP_MARKER), -1) ? false : true;
    }
    public abstract void addDataPane(final AbstractTableDataPane<?> uPanel, String paneName);

    public abstract TableDataTree getDataTree();

    @Override
    public abstract void refreshDockingView();

    protected void checkButtonEnabled(UpdateAction editAction, UpdateAction previewTableDataAction, UpdateAction removeAction, TableDataSourceOP op, TableDataTree dataTree) {
        // august:BUG 9344
        addMenuDef.setEnabled(true);
        connectionTableAction.setEnabled(WorkContext.getCurrent() != null && WorkContext.getCurrent().isRoot());
        if (op == null || op.interceptButtonEnabled()) {
            addMenuDef.setEnabled(false);
            editAction.setEnabled(false);
            removeAction.setEnabled(false);
            previewTableDataAction.setEnabled(false);
            return;
        }
        if (op.getDataMode() == SERVER_TABLE_DATA) {
            addMenuDef.setEnabled(false);
            removeAction.setEnabled(false);
        }
        int selectioncount = dataTree.getSelectionCount();
        switch (selectioncount) {
            case 0:
                editAction.setEnabled(false);
                removeAction.setEnabled(false);
                previewTableDataAction.setEnabled(false);
                break;
            case 1:
                boolean istmp = false;
                if (dataTree.getSelectedNameObject().getObject() == null || dataTree.getSelectedNameObject().getObject() instanceof TemplateTableDataWrapper) {
                    istmp = true;
                }
                editAction.setEnabled(istmp);
                removeAction.setEnabled(istmp);
                previewTableDataAction.setEnabled(true);
                break;
            default:
                editAction.setEnabled(false);
                previewTableDataAction.setEnabled(false);

        }
        if (dataTree.getSelectionPath() != null) {
            ExpandMutableTreeNode selectedTreeNode = (ExpandMutableTreeNode) dataTree.getSelectionPath().getLastPathComponent();
            if (selectedTreeNode.getUserObject() instanceof String) {
                previewTableDataAction.setEnabled(false);
                editAction.setEnabled(false);
                removeAction.setEnabled(false);
            }
        }
    }

    protected class TableDataTreeCellEditor extends DefaultCellEditor implements TreeCellEditor, CellEditorListener {
        private NameObject editingNO;
        private String oldName;
        private String newName;
        private UITextField jTextField;
        private TableDataTree dataTree;
        private BasicTableDataTreePane pane;

        public TableDataTreeCellEditor(final UITextField textField, TableDataTree dataTree, BasicTableDataTreePane pane) {
            super(textField);
            this.jTextField = textField;
            this.dataTree = dataTree;
            this.pane = pane;
        }

        @Override
        public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
            editingNO = dataTree.getSelectedNameObject();

            oldName = editingNO.getName();

            delegate.setValue(oldName);

            if (jTextField != null) {
                jTextField.addFocusListener(new FocusAdapter() {

                    @Override
                    public void focusLost(FocusEvent e) {
                        // stopCellEditing 执行过程中会调用到 getCellEditorValue
                        stopCellEditing();
                    }
                });
            }

            editorComponent.setPreferredSize(new java.awt.Dimension(pane.getPreferredSize().width, editorComponent.getPreferredSize().height));

            return editorComponent;
        }

        @Override
        public boolean isCellEditable(EventObject anEvent) {
            NameObject no = dataTree.getSelectedNameObject();
            return no != null && anEvent == null;

        }

        @Override
        public Object getCellEditorValue() {
            newName = super.getCellEditorValue().toString();

            editingNO.setName(newName);

            // 如果没有重命名成功,则还原成原来的oldName
            if (tc != null && !tc.renameTableData(oldName, newName)) {
                editingNO.setName(oldName);
            }
            if (dataTree.isNameRepeated(newName)) {
                editingNO.setName(oldName);
            }

            return editingNO;
        }

        /*
         * 下面两个方法是CellEditorListener的
         */
        @Override
        public void editingCanceled(ChangeEvent e) {
            // Do nothing
        }

        @Override
        public void editingStopped(ChangeEvent e) {
            // Do nothing
        }
    }

    protected void createAddMenuDef() {
        TableDataNameObjectCreator[] creators = TableDataCreatorProducer.getInstance().createReportTableDataCreator();
        for (final TableDataNameObjectCreator creator : creators) {
            if (creator.shouldInsertSeparator()) {
                addMenuDef.addShortCut(new LineSeparator());
            }

            addMenuDef.addShortCut(new TDAction() {
                @Override
                protected String getTDName() {
                    return creator.menuName();
                }

                @Override
                protected Icon getTDIcon() {
                    return creator.menuIcon();
                }

                @Override
                protected String getNamePrefix() {
                    return creator.getPrefix();
                }

                @Override
                protected TemplateTableDataWrapper getTableDataInstance() {
                    return new TemplateTableDataWrapper((TableData) creator.createObject());
                }
            });
        }
    }

    private abstract class TDAction extends UpdateAction {

        protected abstract String getTDName();

        protected abstract Icon getTDIcon();

        protected abstract String getNamePrefix();

        protected abstract TemplateTableDataWrapper getTableDataInstance();

        public TDAction() {
            this.setName(this.getTDName());
            this.setSmallIcon(this.getTDIcon());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            dgEdit(getTableDataInstance().creatTableDataPane(), createDsName(getNamePrefix()), false);
        }
    }

    private String createDsName(String prefix) {
        int count = 1;
        allDSNames = DesignTableDataManager.getAllDSNames(tc.getBook());
        while (isDsNameRepeaded(prefix + count)) {
            count++;
        }
        return prefix + count;
    }

    protected boolean isDsNameRepeaded(String name) {
        if (allDSNames == null) {
            allDSNames = DesignTableDataManager.getAllDSNames(tc.getBook());
        }
        for (int i = 0; i < allDSNames.length; i++) {
            if (ComparatorUtils.equals(name, allDSNames[i])) {
                return true;
            }
        }
        return false;
    }

    protected KeyAdapter getTableTreeNodeListener(final UpdateAction editAction, final UpdateAction previewTableDataAction, final UpdateAction removeAction, final TableDataSourceOP op, final TableDataTree dataTree) {
        return new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                //F2重命名先屏蔽了, 有bug没时间弄
                if (e.getKeyCode() == KeyEvent.VK_F2) {
                    return;
                }

                super.keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                checkButtonEnabled(editAction, previewTableDataAction, removeAction, op, dataTree);
            }
        };
    }

    /**
     * 创建不重复的名字即初始的默认名
     *
     * @param tree   树
     * @param prefix 后缀
     * @return 返回名字
     */
    public static String createUnrepeatedName(TableDataTree tree, String prefix) {
        int count = 1;

        while (tree.isNameRepeated(prefix + count)) {
            count++;
        }
        return prefix + count;
    }

    protected class PreviewTableDataAction extends UpdateAction {
        private TableDataTree dataTree;

        public PreviewTableDataAction(TableDataTree dataTree) {
            this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Preview"));
            this.setMnemonic('p');
            this.setSmallIcon(BaseUtils.readIcon(IconPathConstants.PREVIEW_ICON_PATH));
            this.dataTree = dataTree;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            NameObject selectedNO = dataTree.getRealSelectedNameObject();
            Object data = null;
            if (selectedNO != null) {
                data = selectedNO.getObject();
            }
            try {
                if (((TableDataWrapper) Objects.requireNonNull(data)).getTableData() instanceof StoreProcedure) {
                    ((StoreProcedure) (((TableDataWrapper) data).getTableData())).resetDataModelList();
                    if (data instanceof StoreProcedureDataWrapper) {
                        StoreProcedureDataWrapper oldSdw = ((StoreProcedureDataWrapper) data);
                        StoreProcedureDataWrapper newSdw = new StoreProcedureDataWrapper((StoreProcedure) oldSdw.getTableData(), oldSdw.getStoreprocedureName(), oldSdw.getTableDataName());
                        newSdw.previewData(StoreProcedureDataWrapper.PREVIEW_ONE);
                    } else {
                        StoreProcedure storeProcedure = (StoreProcedure) ((TableDataWrapper) data).getTableData();
                        StoreProcedureDataWrapper storeProcedureDataWrapper = new StoreProcedureDataWrapper(storeProcedure, StringUtils.EMPTY, StringUtils.EMPTY);
                        storeProcedureDataWrapper.previewData(StoreProcedureDataWrapper.PREVIEW_ALL);
                    }
                } else {
                    ((TableDataWrapper) data).previewData();
                }

            } catch (Exception ex) {
                FineLoggerFactory.getLogger().error(ex.getMessage(), ex);
            }

        }
    }

    protected class ConnectionTableAction extends com.fr.design.actions.server.ConnectionListAction {

        public ConnectionTableAction() {
            this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Server_Define_Data_Connection"));
            this.setMnemonic('D');
            this.setSmallIcon(BaseUtils.readIcon(IconPathConstants.TD_CONNECTION_ICON_PATH));
        }
    }

    /**
     * 合并数据集并返回自动更改数据集名字的新老名字键值对
     * @param srcName 数据集来源(比如报表块，就是报表块的名称)
     * @param tableDataSource 数据集
     */
    public Map<String, String> addTableData(String srcName, TableDataSource tableDataSource) {
         return new HashMap<>(0);
    }
}
