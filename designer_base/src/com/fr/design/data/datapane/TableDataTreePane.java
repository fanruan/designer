package com.fr.design.data.datapane;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.TableData;
import com.fr.data.TableDataSource;
import com.fr.data.impl.storeproc.StoreProcedure;
import com.fr.design.DesignModelAdapter;
import com.fr.design.actions.UpdateAction;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.data.tabledata.ResponseDataSourceChange;
import com.fr.design.data.tabledata.StoreProcedureWorkerListener;
import com.fr.design.data.tabledata.tabledatapane.AbstractTableDataPane;
import com.fr.design.data.tabledata.wrapper.AbstractTableDataWrapper;
import com.fr.design.data.tabledata.wrapper.StoreProcedureDataWrapper;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.data.tabledata.wrapper.TemplateTableDataWrapper;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.ibutton.UIHeadGroup;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.gui.itoolbar.UIToolbar;
import com.fr.design.gui.itree.refreshabletree.ExpandMutableTreeNode;
import com.fr.design.icon.IconPathConstants;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DockingView;
import com.fr.design.menu.LineSeparator;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.SeparatorDef;
import com.fr.design.menu.ToolBarDef;
import com.fr.general.ComparatorUtils;
import com.fr.general.GeneralContext;
import com.fr.general.Inter;
import com.fr.general.NameObject;
import com.fr.stable.StringUtils;
import com.fr.stable.core.PropertyChangeAdapter;
import com.fr.stable.plugin.PluginReadListener;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.TreeCellEditor;
import java.awt.*;
import java.awt.dnd.DnDConstants;
import java.awt.event.*;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

public class TableDataTreePane extends DockingView implements ResponseDataSourceChange {

    private static final int PROCEDURE_NAME_INDEX = 4;
    private static final int TEMPLATE_TABLE_DATA = 0;
    private static final int SERVER_TABLE_DATA = 1;
    private static final long serialVersionUID = -12168467370000617L;
    private static TableDataTreePane singleton = new TableDataTreePane();
    private String type = "";

    /**
     * 得到实例
     *
     * @param tc
     * @return
     */
    public synchronized static TableDataTreePane getInstance(DesignModelAdapter<?, ?> tc) {
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

    private static TableDataTree dataTree;
    private TableDataSourceOP op;

    private MenuDef addMenuDef;
    private EditAction editAction;
    private RemoveAction removeAction;
    private DesignModelAdapter<?, ?> tc;
    private PreviewTableDataAction previewTableDataAction;
    private ConnectionTableAction connectionTableAction;
    private UIHeadGroup buttonGroup;
    private String[] allDSNames;

    private TableDataTreePane() {
        this.setLayout(new BorderLayout(4, 0));
        this.setBorder(null);
        dataTree = new TableDataTree();
        ToolTipManager.sharedInstance().registerComponent(dataTree);
        ToolTipManager.sharedInstance().setDismissDelay(3000);
        ToolTipManager.sharedInstance().setInitialDelay(0);
        addMenuDef = new MenuDef(Inter.getLocText("FR-Action_Add"));
        addMenuDef.setIconPath(IconPathConstants.ADD_POPMENU_ICON_PATH);

        createAddMenuDef();

        GeneralContext.addPluginReadListener(new PluginReadListener() {
            @Override
            public void success() {
                addMenuDef.clearShortCuts();
                createAddMenuDef();
            }
        });

        editAction = new EditAction();
        removeAction = new RemoveAction();
        previewTableDataAction = new PreviewTableDataAction();
        connectionTableAction = new ConnectionTableAction();
        ToolBarDef toolbarDef = new ToolBarDef();
        toolbarDef.addShortCut(addMenuDef, SeparatorDef.DEFAULT, editAction, removeAction, SeparatorDef.DEFAULT, previewTableDataAction, connectionTableAction);
        UIToolbar toolBar = ToolBarDef.createJToolBar();
        toolbarDef.updateToolBar(toolBar);

        JPanel toolbarPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        toolbarPane.add(toolBar, BorderLayout.CENTER);
        this.add(toolbarPane, BorderLayout.NORTH);

        UIScrollPane scrollPane = new UIScrollPane(dataTree);
        scrollPane.setBorder(null);
        initbuttonGroup();
        JPanel jPanel = new JPanel(new BorderLayout(4, 4));
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
        dataTree.addKeyListener(getTableTreeNodeListener());
        // TreeCellEditor
        dataTree.setEditable(true);
        TableDataTreeCellEditor treeCellEditor = new TableDataTreeCellEditor(new UITextField());
        treeCellEditor.addCellEditorListener(treeCellEditor);
        dataTree.setCellEditor(treeCellEditor);
        new TableDataTreeDragSource(dataTree, DnDConstants.ACTION_COPY);
        checkButtonEnabled();
    }

    private KeyAdapter getTableTreeNodeListener() {
        return new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                //F2重命名先屏蔽了, 有bug没时间弄
                if (e.getKeyCode() == KeyEvent.VK_F2){
                    return;
                }

                super.keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                checkButtonEnabled();
            }
        };
    }

    private void createAddMenuDef() {
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

    private void initbuttonGroup() {
        Icon[] iconArray = {BaseUtils.readIcon("/com/fr/design/images/data/datasource.png"), BaseUtils.readIcon("/com/fr/design/images/data/dock/serverdatabase.png")};
        final Integer[] modeArray = {TEMPLATE_TABLE_DATA, SERVER_TABLE_DATA};
        String[] textArray = {Inter.getLocText(new String[]{"Template", "DS-TableData"}), Inter.getLocText("DS-Server_TableData")};
        buttonGroup = new UIHeadGroup(iconArray, textArray) {
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
     * getViewIcon
     *
     * @return
     */
    public Icon getViewIcon() {
        return BaseUtils.readIcon(IconPathConstants.DS_ICON_PATH);
    }

    /**
     * 刷新
     */
    public static void refresh() {
        dataTree.refresh();
    }

    private void checkButtonEnabled() {
        // august:BUG 9344
        addMenuDef.setEnabled(true);
        connectionTableAction.setEnabled(FRContext.getCurrentEnv() != null && FRContext.getCurrentEnv().isRoot());
        if (this.op == null || this.op.interceptButtonEnabled()) {
            addMenuDef.setEnabled(false);
            editAction.setEnabled(false);
            removeAction.setEnabled(false);
            previewTableDataAction.setEnabled(false);
            return;
        }
        if (this.op.getDataMode() == SERVER_TABLE_DATA) {
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
                boolean istmp = dataTree.getSelectedNameObject().getObject() instanceof TemplateTableDataWrapper;
                editAction.setEnabled(istmp);
                removeAction.setEnabled(istmp);
                previewTableDataAction.setEnabled(true);
                break;
            default:
                editAction.setEnabled(false);
                previewTableDataAction.setEnabled(false);

        }
        if (dataTree.getSelectionPath() != null) {
            if (((ExpandMutableTreeNode) dataTree.getSelectionPath().getLastPathComponent()).getUserObject() instanceof String) {
                previewTableDataAction.setEnabled(false);
                editAction.setEnabled(false);
                removeAction.setEnabled(false);
            }
        }
    }


    /**
     * 刷新
     */
    public void refreshDockingView() {
        populate(new TableDataSourceOP(tc));
        this.checkButtonEnabled();
    }

    /**
     * getViewTitle()
     *
     * @return
     */
    public String getViewTitle() {
        return Inter.getLocText("FR-Designer_TableData");
    }


    /**
     * 最佳位置
     *
     * @return 返回位置
     */
    public Location preferredLocation() {
        return Location.WEST_ABOVE;
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
     * @param map 数据集变化Map
     */
    public void fireDSChanged(Map<String, String> map) {
        DesignTableDataManager.fireDSChanged(map);
    }

    private class TableDataTreeCellEditor extends DefaultCellEditor implements TreeCellEditor, CellEditorListener {

        private NameObject editingNO;
        private String oldName;
        private String newName;
        private UITextField jTextField;

        public TableDataTreeCellEditor(final UITextField textField) {
            super(textField);
            this.jTextField = textField;
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

            editorComponent.setPreferredSize(new java.awt.Dimension(TableDataTreePane.this.getPreferredSize().width, editorComponent.getPreferredSize().height));

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
        }

        @Override
        public void editingStopped(ChangeEvent e) {
        }
    }


    /**
     * 编辑面板
     *
     * @param uPanel       面板
     * @param originalName 原始名字
     */
    public void dgEdit(final AbstractTableDataPane<?> uPanel, String originalName) {
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

    private void doPropertyChange(BasicDialog dg, BasicPane.NamePane nPanel, final String oldName) {
        type = dg.getTitle();
        nPanel.setShowText(StringUtils.BLANK);
        dg.setButtonEnabled(true);
        String tempName = nPanel.getObjectName();
        if (StringUtils.isBlank(tempName)) {
            nPanel.setShowText(Inter.getLocText(new String[]{"DS-TableData", "ISEMPTY", "PLEASE", "GIVE-NAME"}, new String[]{"", "，", "", "！"}));
            dg.setButtonEnabled(false);
        } else if (!ComparatorUtils.equals(oldName, tempName) && isDsNameRepeaded(tempName, allDSNames)) {
            String[] waring = new String[]{"DS-TableData", "Utils-has_been_existed", "PLEASE", "Rename"};
            String[] sign = new String[]{tempName, "，", "", "！"};
            nPanel.setShowText(Inter.getLocText(waring, sign));
            dg.setButtonEnabled(false);
        } else if (isProcedureName(oldName)) {
            if (isIncludeUnderline(tempName)) {
                String[] datasource_underline = new String[]{"Datasource-Stored_Procedure", "Name", "can_not_include_underline"};
                String[] sign = new String[]{"", "", "!"};
                nPanel.setShowText(Inter.getLocText(datasource_underline, sign));
                dg.setButtonEnabled(false);
            }
        } else {
            nPanel.setShowText(StringUtils.BLANK);
            dg.setButtonEnabled(true);
        }
    }


    private boolean isProcedureName(String oldName) {
        return oldName.length() >= PROCEDURE_NAME_INDEX && ComparatorUtils.equals(type, Inter.getLocText("Datasource-Stored_Procedure"));
    }


    private boolean isDsNameRepeaded(String name, String[] names) {
        boolean repeat = false;
        for (int i = 0; i < names.length; i++) {
            if (ComparatorUtils.equals(name, names[i])) {
                repeat = true;
            }
        }
        return repeat;
    }


    private boolean isIncludeUnderline(String name) {
        return ComparatorUtils.equals(name.indexOf(StoreProcedure.SPLIT), -1) ? false : true;
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

        public void actionPerformed(ActionEvent e) {
            dgEdit(getTableDataInstance().creatTableDataPane(), createDsName(getNamePrefix()));
        }
    }

    private String createDsName(String prefix) {
        int count = 1;
        allDSNames = DesignTableDataManager.getAllDSNames(tc.getBook());
        while (isDsNameRepeaded(prefix + count, allDSNames)) {
            count++;
        }
        return prefix + count;
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

    private class PreviewTableDataAction extends UpdateAction {

        public PreviewTableDataAction() {
            this.setName(Inter.getLocText("FR-Designer_Preview"));
            this.setMnemonic('p');
            this.setSmallIcon(BaseUtils.readIcon(IconPathConstants.PREVIEW_ICON_PATH));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            NameObject selectedNO = dataTree.getRealSelectedNameObject();
            Object data = null;
            if (selectedNO != null) {
                data = selectedNO.getObject();
            }
            try {
                if (((TableDataWrapper) data).getTableData() instanceof StoreProcedure) {
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
                FRContext.getLogger().error(ex.getMessage(), ex);
            }

        }
    }

    private class EditAction extends UpdateAction {
        public EditAction() {
            this.setName(Inter.getLocText("FR-Designer_Edit"));
            this.setMnemonic('E');
            this.setSmallIcon(BaseUtils.readIcon(IconPathConstants.TD_EDIT_ICON_PATH));
        }

        public void actionPerformed(ActionEvent e) {
            final NameObject selectedNO = dataTree.getSelectedNameObject();
            if (selectedNO == null) {
                return;
            }

            dgEdit(((AbstractTableDataWrapper) selectedNO.getObject()).creatTableDataPane(), selectedNO.getName());
        }
    }

    private class RemoveAction extends UpdateAction {

        public RemoveAction() {
            this.setName(Inter.getLocText("FR-Designer_Remove"));
            this.setMnemonic('R');
            this.setSmallIcon(BaseUtils.readIcon(IconPathConstants.TD_REMOVE_ICON_PATH));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            NameObject selectedNO = dataTree.getSelectedNameObject();

            if (selectedNO == null) {
                return;
            }

            int returnVal = JOptionPane.showConfirmDialog(DesignerContext.getDesignerFrame(), Inter.getLocText("Utils-Are_you_sure_to_remove_the_selected_item") + ":" + selectedNO.getName() + "?",
                    Inter.getLocText("FR-Designer_Remove"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
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

    private class ConnectionTableAction extends com.fr.design.actions.server.ConnectionListAction {

        public ConnectionTableAction() {
            this.setName(Inter.getLocText("Server-Define_Data_Connection"));
            this.setMnemonic('D');
            this.setSmallIcon(BaseUtils.readIcon(IconPathConstants.TD_CONNECTION_ICON_PATH));
        }
    }

    /**
     * @return
     */
    public TableDataTree getDataTree() {
        return dataTree;
    }
}