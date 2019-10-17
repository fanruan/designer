package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.base.vcs.DesignerMode;
import com.fr.cluster.ClusterBridge;
import com.fr.cluster.engine.base.FineClusterConfig;
import com.fr.design.DesignModelAdapter;
import com.fr.design.DesignerEnvManager;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.actions.UpdateAction;
import com.fr.design.constants.UIConstants;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.data.datapane.TableDataTreePane;
import com.fr.design.data.tabledata.ResponseDataSourceChange;
import com.fr.design.file.FileOperations;
import com.fr.design.file.FileToolbarStateChangeListener;
import com.fr.design.file.HistoryTemplateListCache;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.file.MutilTempalteTabPane;
import com.fr.design.file.SaveSomeTemplatePane;
import com.fr.design.file.TemplateTreePane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.imenu.UIMenuHighLight;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.gui.itoolbar.UIToolbar;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.vcs.common.VcsHelper;
import com.fr.design.mainframe.vcs.ui.FileVersionsPanel;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.design.roleAuthority.RolesAlreadyEditedPane;
import com.fr.design.utils.DesignUtils;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.file.FileNodeFILE;
import com.fr.file.filetree.FileNode;
import com.fr.general.ComparatorUtils;
import com.fr.general.GeneralContext;
import com.fr.log.FineLoggerFactory;
import com.fr.plugin.context.PluginContext;
import com.fr.plugin.injectable.PluginModule;
import com.fr.plugin.manage.PluginFilter;
import com.fr.plugin.observer.PluginEvent;
import com.fr.plugin.observer.PluginEventListener;
import com.fr.stable.CoreConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.project.ProjectConstants;
import com.fr.third.org.apache.commons.io.FilenameUtils;
import com.fr.workspace.WorkContext;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static javax.swing.JOptionPane.WARNING_MESSAGE;

public class DesignerFrameFileDealerPane extends JPanel implements FileToolbarStateChangeListener, ResponseDataSourceChange {

    private static final String FILE = "file";
    private static volatile DesignerFrameFileDealerPane THIS;

    static {
        GeneralContext.listenPluginRunningChanged(new PluginEventListener() {

            @Override
            public void on(PluginEvent event) {
                DesignUtils.refreshDesignerFrame();
            }
        }, new PluginFilter() {

            @Override
            public boolean accept(PluginContext context) {
                return context.contain(PluginModule.ExtraDesign, ShortCut.TEMPLATE_TREE);
            }
        });
    }

    private List<FileToolbarStateChangeListener> otherToolbarStateChangeListeners = new ArrayList<>();

    private FileOperations selectedOperation;

    private UIToolbar toolBar;

    private NewFolderAction newFolderAction = new NewFolderAction();

    private RefreshTreeAction refreshTreeAction = new RefreshTreeAction();

    private ShowInExplorerAction showInExplorerAction = new ShowInExplorerAction();

    private RenameAction renameAction = new RenameAction();

    private DelFileAction delFileAction = new DelFileAction();

    private VcsAction vcsAction = new VcsAction();


    private DesignerFrameFileDealerPane() {

        setLayout(new BorderLayout());
        toolBar = ToolBarDef.createJToolBar();
        toolBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIConstants.TOOLBAR_BORDER_COLOR));
        toolBar.setBorderPainted(true);
        JPanel tooBarPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        JPanel parent = new JPanel(new BorderLayout());
        parent.add(toolBar, BorderLayout.CENTER);
        parent.setBorder(BorderFactory.createEmptyBorder(3, 0, 4, 0));
        tooBarPane.add(parent, BorderLayout.CENTER);
        tooBarPane.add(new UIMenuHighLight(), BorderLayout.SOUTH);

        add(tooBarPane, BorderLayout.NORTH);
        CardLayout card;
        JPanel cardPane = new JPanel(card = new CardLayout());
        cardPane.add(TemplateTreePane.getInstance(), FILE);

        selectedOperation = TemplateTreePane.getInstance();
        card.show(cardPane, FILE);

        TemplateTreePane.getInstance().setToolbarStateChangeListener(this);

        add(cardPane, BorderLayout.CENTER);
        stateChange();
    }

    public static DesignerFrameFileDealerPane getInstance() {

        if (THIS == null) {
            synchronized (DesignerFrameFileDealerPane.class) {
                if (THIS == null) {
                    THIS = new DesignerFrameFileDealerPane();
                }
            }
        }
        return THIS;
    }

    /**
     * 刷新
     */
    public void refresh() {
        selectedOperation.refresh();
    }

    public final void setCurrentEditingTemplate(JTemplate<?, ?> jt) {

        DesignModelAdapter.setCurrentModelAdapter(jt.getModel());
        fireDSChanged();
        TableDataTreePane.getInstance(DesignModelAdapter.getCurrentModelAdapter());
        HistoryTemplateListPane.getInstance().setCurrentEditingTemplate(jt);
        //处理自动新建的模板
        MutilTempalteTabPane.getInstance().doWithtemTemplate();
        if (DesignerMode.isAuthorityEditing()) {
            RolesAlreadyEditedPane.getInstance().refreshDockingView();
        }

        jt.setComposite();
        jt.refreshToolArea();
        jt.fireJTemplateOpened();
        jt.requestFocus();
        jt.revert();

        FineLoggerFactory.getLogger().info(
                "\"" + jt.getEditingFILE().getName() + "\""
                        + Toolkit.i18nText("Fine-Design_Basic_LOG_Has_Been_Openned") + "!");
    }

    /**
     * 刷新菜单
     */
    public void refreshDockingView() {
        ToolBarDef toolbarDef = new ToolBarDef();
        toolbarDef.addShortCut(newFolderAction, refreshTreeAction);
        if (WorkContext.getCurrent().isLocal()) {
            toolbarDef.addShortCut(showInExplorerAction);
        }
        toolbarDef.addShortCut(renameAction, delFileAction);
        Set<ShortCut> extraShortCuts = ExtraDesignClassManager.getInstance().getExtraShortCuts();
        for (ShortCut shortCut : extraShortCuts) {
            toolbarDef.addShortCut(shortCut);
        }
        addVcsAction(toolbarDef);
        toolbarDef.updateToolBar(toolBar);
        resetActionStatus();
        refresh();
    }


    /**
     * 添加VcsAction
     *
     * @param toolbarDef
     */
    private void addVcsAction(ToolBarDef toolbarDef) {
        if (VcsHelper.getInstance().needInit()) {
            vcsAction = new VcsAction();

            if (WorkContext.getCurrent().isLocal()) {
                vcsAction.setName(Toolkit.i18nText("Fine-Design_Vcs_Title"));
            } else {
                vcsAction.setName(Toolkit.i18nText("Fine-Design_Vcs_NotSupportRemote"));
            }
            toolbarDef.addShortCut(vcsAction);

        }
    }


    private void resetActionStatus() {

        newFolderAction.setEnabled(false);
        refreshTreeAction.setEnabled(true);
        showInExplorerAction.setEnabled(false);
        renameAction.setEnabled(false);
        delFileAction.setEnabled(false);
        vcsAction.setEnabled(false);
        this.repaint();
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
     * @param map 改变名字的数据集
     */
    @Override
    public void fireDSChanged(Map<String, String> map) {

        DesignTableDataManager.fireDSChanged(map);
    }


    public void addToolbarStateChangeListener(FileToolbarStateChangeListener listener) {
        this.otherToolbarStateChangeListeners.add(listener);
    }

    public void removeToolbarStateChangeListener(FileToolbarStateChangeListener listener) {
        this.otherToolbarStateChangeListeners.remove(listener);
    }

    private void otherStateChange() {
        for (FileToolbarStateChangeListener listener : otherToolbarStateChangeListeners) {
            listener.stateChange();
        }
    }

    private boolean isCurrentEditing(String path) {
        JTemplate<?, ?> jt = HistoryTemplateListCache.getInstance().getCurrentEditingTemplate();
        String editing = jt.getEditingFILE().getPath();
        return ComparatorUtils.equals(editing, path);
    }

    /**
     * 按钮状态改变
     */
    @Override
    public void stateChange() {

        int selectedPathNum = TemplateTreePane.getInstance().countSelectedPath();

        // 新建文件夹，重命名操作，在explorer中打开三个操作在选中单个文件夹或者文件时可用，其他情况不可用
        boolean singleSelected = selectedPathNum == 1;
        newFolderAction.setEnabled(singleSelected);
        renameAction.setEnabled(singleSelected);
        showInExplorerAction.setEnabled(singleSelected);
        // 删除操作在至少选中一个时可用
        boolean selected = selectedPathNum > 0;
        delFileAction.setEnabled(selected);
        // 刷新操作始终可用
        refreshTreeAction.setEnabled(true);
        //触发vcsAction变化
        vcsAction.fireVcsActionChange();

        // 其他状态
        otherStateChange();
    }

    public FileOperations getSelectedOperation() {
        return selectedOperation;
    }

    /**
     * 新建文件夹
     */
    private class NewFolderAction extends UpdateAction {

        public NewFolderAction() {

            this.setName(KeySetUtils.NEW_FOLDER.getMenuKeySetName());
            this.setSmallIcon(BaseUtils.readIcon("com/fr/design/images/icon_NewFolderIcon_normal.png"));
        }

        @Override
        public void actionPerformed(ActionEvent evt) {

            if (!selectedOperation.access()) {
                JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(),
                        Toolkit.i18nText("Fine-Design_Basic_Template_Permission_Denied"),
                        Toolkit.i18nText("Fine-Design_Basic_Tool_Tips"),
                        WARNING_MESSAGE);
                return;
            }

            new MkdirDialog();
            stateChange();
        }

    }

    /**
     * 版本管理
     */
    private class VcsAction extends UpdateAction {

        public VcsAction() {
            this.setSmallIcon(VcsHelper.VCS_LIST_PNG);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String path = DesignerFrameFileDealerPane.getInstance().getSelectedOperation().getFilePath();
            path = StableUtils.pathJoin(ProjectConstants.REPORTLETS_NAME, path);

            boolean isCurrentEditing = isCurrentEditing(path);

            // 如果模板已经打开了，关掉，避免出现2个同名tab（1个是模板，1个是版本）
            closeOpenedTemplate(path, isCurrentEditing);
            FileVersionsPanel fileVersionTablePanel = FileVersionsPanel.getInstance();
            fileVersionTablePanel.showFileVersionsPane();
            stateChange();

        }

        /**
         * 版本管理可用状态的监控
         */
        private void fireVcsActionChange() {
            if (!DesignerEnvManager.getEnvManager().getVcsConfigManager().isVcsEnable()
                    || VcsHelper.getInstance().isUnSelectedTemplate()
                    || FineClusterConfig.getInstance().isCluster()) {
                setEnabled(false);
                return;
            }


            if (WorkContext.getCurrent() != null) {
                if (!WorkContext.getCurrent().isLocal()) {
                    //当前环境为远程环境时
                    FileNode node = TemplateTreePane.getInstance().getTemplateFileTree().getSelectedFileNode();
                    if (selectedOperation.getFilePath() != null) {
                        if (node.getLock() != null && !ComparatorUtils.equals(node.getUserID(), node.getLock())) {
                            setEnabled(false);
                        } else {
                            setEnabled(true);
                        }
                    } else {
                        setEnabled(false);
                    }
                } else {
                    //当前环境为本地环境时
                    setEnabled(selectedOperation.getFilePath() != null);
                }
            }
        }

        private void closeOpenedTemplate(String path, boolean isCurrentEditing) {
            for (JTemplate jTemplate : HistoryTemplateListCache.getInstance().getHistoryList()) {
                if (ComparatorUtils.equals(jTemplate.getEditingFILE().getPath(), path)) {
                    if (isCurrentEditing) {
                        MutilTempalteTabPane.getInstance().setIsCloseCurrent(true);
                    }
                    MutilTempalteTabPane.getInstance().closeFormat(jTemplate);
                    MutilTempalteTabPane.getInstance().closeSpecifiedTemplate(jTemplate);
                    return;
                }
            }
        }


    }

    /**
     * 在系统资源管理器中打开
     */
    private class ShowInExplorerAction extends UpdateAction {

        public ShowInExplorerAction() {

            this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Show_In_Containing_Folder"));
            this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_file/view_folder.png"));
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            selectedOperation.showInExplorer();
        }
    }

    /*
     * 刷新ReportletsTree
     */
    private class RefreshTreeAction extends UpdateAction {

        public RefreshTreeAction() {

            this.setName(Toolkit.i18nText("Fine-Design_Basic_Refresh"));
            this.setSmallIcon(UIConstants.REFRESH_ICON);
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            selectedOperation.refresh();
            stateChange();
        }
    }

    /*
     * 重命名文件
     */
    private class RenameAction extends UpdateAction {

        public RenameAction() {

            this.setName(Toolkit.i18nText("Fine-Design_Basic_Rename"));
            this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/data/source/rename.png"));
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            if (!selectedOperation.access()) {
                JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(),
                        Toolkit.i18nText("Fine-Design_Basic_Template_Permission_Denied"),
                        Toolkit.i18nText("Fine-Design_Basic_Tool_Tips"),
                        WARNING_MESSAGE);
                return;
            }

            FileNode node = selectedOperation.getFileNode();
            String lock = node.getLock();
            if (lock != null && !lock.equals(node.getUserID())) {
                // 提醒被锁定模板无法重命名
                JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(),
                        Toolkit.i18nText("Fine-Design_Basic_Unable_Rename_Locked_File"),
                        Toolkit.i18nText("Fine-Design_Basic_Tool_Tips"),
                        WARNING_MESSAGE);
                return;
            }

            new FileRenameDialog(node);
            MutilTempalteTabPane.getInstance().repaint();
            stateChange();
        }

    }

    /*
     * 删除指定文件
     */
    private class DelFileAction extends UpdateAction {

        public DelFileAction() {

            this.setName(Toolkit.i18nText("Fine-Design_Basic_Remove"));
            this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/data/source/delete.png"));
        }

        @Override
        public void actionPerformed(ActionEvent evt) {

            if (!selectedOperation.access()) {
                JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(),
                        Toolkit.i18nText("Fine-Design_Basic_Template_Permission_Denied"),
                        Toolkit.i18nText("Fine-Design_Basic_Tool_Tips"),
                        WARNING_MESSAGE);
                return;
            }
            selectedOperation.deleteFile();
            stateChange();
            DesignerContext.getDesignerFrame().setTitle();
        }
    }

    /**
     * 重命名对话框
     * 支持快捷键Enter，ESC
     */
    private class FileRenameDialog extends JDialog {

        private UITextField nameField;

        private UILabel warnLabel;

        private UIButton confirmButton;

        /**
         * 操作的节点
         */
        private FileNodeFILE fnf;


        private FileRenameDialog(FileNode node) {
            if (node == null) {
                return;
            }
            fnf = new FileNodeFILE(node);

            String oldName = fnf.getName();
            String suffix = fnf.isDirectory() ? StringUtils.EMPTY : oldName.substring(oldName.lastIndexOf(CoreConstants.DOT), oldName.length());
            oldName = StringUtils.replaceLast(oldName, suffix, StringUtils.EMPTY);
            this.setLayout(new BorderLayout());
            this.setModal(true);

            // 输入框前提示
            UILabel newNameLabel = new UILabel(Toolkit.i18nText(
                    fnf.isDirectory() ?
                            "Fine-Design_Basic_Enter_New_Folder_Name" : "Fine-Design_Basic_Enter_New_File_Name")
            );
            newNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            newNameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
            newNameLabel.setPreferredSize(new Dimension(118, 15));

            // 重命名输入框
            nameField = new UITextField(oldName);
            nameField.getDocument().addDocumentListener(new DocumentListener() {

                @Override
                public void changedUpdate(DocumentEvent e) {
                    validInput();
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                    validInput();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    validInput();
                }
            });
            nameField.selectAll();
            nameField.setPreferredSize(new Dimension(180, 20));

            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
            topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 15));
            topPanel.add(newNameLabel);
            topPanel.add(nameField);

            // 增加enter以及esc快捷键的支持
            nameField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        dispose();
                    } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        if (confirmButton.isEnabled()) {
                            confirmClose();
                        }
                    }
                }
            });
            // 重名提示
            warnLabel = new UILabel();
            warnLabel.setPreferredSize(new Dimension(300, 30));
            warnLabel.setHorizontalAlignment(SwingConstants.LEFT);
            warnLabel.setForeground(Color.RED);
            warnLabel.setVisible(false);

            JPanel midPanel = new JPanel(new BorderLayout());
            midPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
            midPanel.add(warnLabel, BorderLayout.WEST);

            // 确认按钮
            confirmButton = new UIButton(Toolkit.i18nText("Fine-Design_Basic_Confirm"));
            confirmButton.setPreferredSize(new Dimension(60, 25));
            confirmButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    confirmClose();
                }
            });

            // 取消按钮
            UIButton cancelButton = new UIButton(Toolkit.i18nText("Fine-Design_Basic_Cancel"));
            cancelButton.setPreferredSize(new Dimension(60, 25));

            cancelButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });

            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
            bottomPanel.add(confirmButton);
            bottomPanel.add(cancelButton);

            this.add(
                    TableLayoutHelper.createTableLayoutPane(
                            new Component[][]{
                                    new Component[]{topPanel},
                                    new Component[]{midPanel},
                                    new Component[]{bottomPanel}
                            },
                            new double[]{TableLayout.FILL, TableLayout.FILL, TableLayout.FILL},
                            new double[]{TableLayout.FILL}
                    ),
                    BorderLayout.CENTER);


            this.setSize(340, 180);
            this.setTitle(Toolkit.i18nText("Fine-Design_Basic_Rename"));
            this.setResizable(false);
            this.setAlwaysOnTop(true);
            this.setIconImage(BaseUtils.readImage("/com/fr/base/images/oem/logo.png"));
            this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            GUICoreUtils.centerWindow(this);
            this.setVisible(true);
        }

        private void confirmClose() {

            String userInput = nameField.getText().trim();
            // 处理不合法的文件夹名称
            userInput = userInput.replaceAll("[\\\\/:*?\"<>|]", StringUtils.EMPTY);

            String path = FilenameUtils.standard(fnf.getPath());

            String oldName = fnf.getName();
            String suffix = fnf.isDirectory() ? StringUtils.EMPTY : oldName.substring(oldName.lastIndexOf(CoreConstants.DOT), oldName.length());
            oldName = StringUtils.replaceLast(oldName, suffix, StringUtils.EMPTY);

            // 输入为空或者没有修改
            if (ComparatorUtils.equals(userInput, oldName)) {
                this.dispose();
                return;
            }

            String parentPath = FilenameUtils.standard(fnf.getParent().getPath());

            // 简单执行old new 替换是不可行的，例如 /abc/abc/abc/abc/
            String newPath = parentPath + CoreConstants.SEPARATOR + userInput + suffix;
            this.dispose();

            //模版重命名
            boolean success = false;

            // 提醒保存文件
            SaveSomeTemplatePane saveSomeTempaltePane = new SaveSomeTemplatePane(true);
            // 只有一个文件未保存时
            if (HistoryTemplateListCache.getInstance().getHistoryCount() == 1) {
                int choose = saveSomeTempaltePane.saveLastOneTemplate();
                if (choose != JOptionPane.CANCEL_OPTION) {
                    success = selectedOperation.rename(fnf, path, newPath);
                }
            } else {
                if (saveSomeTempaltePane.showSavePane()) {
                    success = selectedOperation.rename(fnf, path, newPath);
                }
            }

            if (success) {
                HistoryTemplateListCache.getInstance().rename(fnf, path, newPath);
                DesignerEnvManager.getEnvManager().replaceRecentOpenedFilePath(fnf.isDirectory(), path, newPath);
                selectedOperation.refresh();
                DesignerContext.getDesignerFrame().setTitle();
            } else {
                JOptionPane.showConfirmDialog(DesignerContext.getDesignerFrame(),
                        Toolkit.i18nText("Fine-Design_Basic_Rename_Failure"),
                        UIManager.getString("OptionPane.messageDialogTitle"),
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.ERROR_MESSAGE);
            }
        }


        private void validInput() {

            String userInput = nameField.getText().trim();

            String oldName = fnf.getName();
            String suffix = fnf.isDirectory() ? StringUtils.EMPTY : oldName.substring(oldName.lastIndexOf(CoreConstants.DOT), oldName.length());
            oldName = oldName.replaceAll(suffix, StringUtils.EMPTY);

            if (StringUtils.isEmpty(userInput)) {
                confirmButton.setEnabled(false);
                return;
            }

            if (ComparatorUtils.equals(userInput, oldName)) {
                warnLabel.setVisible(false);
                confirmButton.setEnabled(true);
                return;
            }

            if (selectedOperation.duplicated(userInput, suffix)) {
                nameField.selectAll();
                // 如果文件名已存在，则灰掉确认按钮
                warnLabel.setText(
                        Toolkit.i18nText(fnf.isDirectory() ?
                                        "Fine-Design_Basic_Folder_Name_Duplicate" :
                                        "Fine-Design_Basic_Template_File_Name_Duplicate",
                                userInput));
                warnLabel.setVisible(true);
                confirmButton.setEnabled(false);
            } else {
                warnLabel.setVisible(false);
                confirmButton.setEnabled(true);
            }
        }
    }


    /**
     * 新建文件夹对话框
     * 支持快捷键Enter，ESC
     */
    private class MkdirDialog extends JDialog {

        private UITextField nameField;

        private UILabel warnLabel;

        private UIButton confirmButton;


        private MkdirDialog() {

            this.setLayout(new BorderLayout());
            this.setModal(true);

            // 输入框前提示
            UILabel newNameLabel = new UILabel(Toolkit.i18nText(
                    "Fine-Design_Basic_Enter_New_Folder_Name")
            );
            newNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            newNameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
            newNameLabel.setPreferredSize(new Dimension(118, 15));

            // 文件名输入框
            nameField = new UITextField();
            nameField.getDocument().addDocumentListener(new DocumentListener() {

                @Override
                public void changedUpdate(DocumentEvent e) {
                    validInput();
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                    validInput();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    validInput();
                }
            });
            nameField.selectAll();
            nameField.setPreferredSize(new Dimension(180, 20));

            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
            topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 15));
            topPanel.add(newNameLabel);
            topPanel.add(nameField);

            // 增加enter以及esc快捷键的支持
            nameField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        dispose();
                    } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        if (confirmButton.isEnabled()) {
                            confirmClose();
                        }
                    }
                }
            });
            // 重名提示
            warnLabel = new UILabel();
            warnLabel.setPreferredSize(new Dimension(300, 30));
            warnLabel.setHorizontalAlignment(SwingConstants.LEFT);
            warnLabel.setForeground(Color.RED);
            warnLabel.setVisible(false);

            JPanel midPanel = new JPanel(new BorderLayout());
            midPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
            midPanel.add(warnLabel, BorderLayout.WEST);

            // 确认按钮
            confirmButton = new UIButton(Toolkit.i18nText("Fine-Design_Basic_Confirm"));
            confirmButton.setPreferredSize(new Dimension(60, 25));
            confirmButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    confirmClose();
                }
            });
            confirmButton.setEnabled(false);

            // 取消按钮
            UIButton cancelButton = new UIButton(Toolkit.i18nText("Fine-Design_Basic_Cancel"));
            cancelButton.setPreferredSize(new Dimension(60, 25));

            cancelButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });

            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
            bottomPanel.add(confirmButton);
            bottomPanel.add(cancelButton);

            this.add(
                    TableLayoutHelper.createTableLayoutPane(
                            new Component[][]{
                                    new Component[]{topPanel},
                                    new Component[]{midPanel},
                                    new Component[]{bottomPanel}
                            },
                            new double[]{TableLayout.FILL, TableLayout.FILL, TableLayout.FILL},
                            new double[]{TableLayout.FILL}
                    ),
                    BorderLayout.CENTER);


            this.setSize(340, 180);
            this.setTitle(Toolkit.i18nText("Fine-Design_Basic_Mkdir"));
            this.setResizable(false);
            this.setAlwaysOnTop(true);
            this.setIconImage(BaseUtils.readImage("/com/fr/base/images/oem/logo.png"));
            this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            GUICoreUtils.setWindowCenter(DesignerContext.getDesignerFrame(), this);
            this.setVisible(true);
        }

        private void confirmClose() {
            String userInput = nameField.getText().trim();

            // 处理不合法的文件夹名称
            userInput = userInput.replaceAll("[\\\\/:*?\"<>|]", StringUtils.EMPTY);

            if (StringUtils.isEmpty(userInput)) {
                return;
            }

            //新建文件夹
            boolean success = selectedOperation.mkdir(
                    FilenameUtils.standard(selectedOperation.getFileNode().getParent() + CoreConstants.SEPARATOR + userInput)
            );
            selectedOperation.refresh();
            this.dispose();
            if (!success) {
                JOptionPane.showConfirmDialog(DesignerContext.getDesignerFrame(),
                        Toolkit.i18nText("Fine-Design_Basic_Make_Failure"),
                        UIManager.getString("OptionPane.messageDialogTitle"),
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.ERROR_MESSAGE);
            }
        }


        private void validInput() {
            String userInput = nameField.getText().trim();


            if (StringUtils.isEmpty(userInput)) {
                confirmButton.setEnabled(false);
                return;
            }

            if (selectedOperation.duplicated(userInput, StringUtils.EMPTY)) {
                nameField.selectAll();
                // 如果文件名已存在，则灰掉确认按钮
                warnLabel.setText(
                        Toolkit.i18nText(
                                "Fine-Design_Basic_Folder_Name_Duplicate",
                                userInput));
                warnLabel.setVisible(true);
                confirmButton.setEnabled(false);
            } else {
                warnLabel.setVisible(false);
                confirmButton.setEnabled(true);
            }
        }
    }


}
