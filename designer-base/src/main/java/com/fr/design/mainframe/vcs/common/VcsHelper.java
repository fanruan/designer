package com.fr.design.mainframe.vcs.common;

import com.fr.cluster.ClusterBridge;
import com.fr.concurrent.NamedThreadFactory;
import com.fr.design.DesignerEnvManager;
import com.fr.design.file.HistoryTemplateListCache;
import com.fr.design.file.TemplateTreePane;
import com.fr.design.gui.itree.filetree.TemplateFileTree;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.DesignerFrameFileDealerPane;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.mainframe.JTemplateActionListener;
import com.fr.design.mainframe.vcs.VcsConfigManager;
import com.fr.design.mainframe.vcs.ui.FileVersionTable;
import com.fr.general.IOUtils;
import com.fr.plugin.context.PluginContext;
import com.fr.plugin.manage.PluginManager;
import com.fr.report.entity.VcsEntity;
import com.fr.stable.StringUtils;
import com.fr.stable.project.ProjectConstants;
import com.fr.workspace.WorkContext;
import com.fr.workspace.server.vcs.VcsOperator;
import com.fr.workspace.server.vcs.filesystem.VcsFileSystem;
import com.fr.workspace.server.vcs.git.config.GcConfig;

import javax.swing.Icon;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.util.Date;

import static com.fr.stable.StableUtils.pathJoin;

/**
 * Created by XiaXiang on 2019/4/17.
 */
public class VcsHelper implements JTemplateActionListener {

    public final static Color TABLE_SELECT_BACKGROUND = new Color(0xD8F2FD);
    public final static Color COPY_VERSION_BTN_COLOR = new Color(0x419BF9);
    public final static EmptyBorder EMPTY_BORDER = new EmptyBorder(10, 10, 0, 10);
    public final static EmptyBorder EMPTY_BORDER_MEDIUM = new EmptyBorder(5, 10, 0, 10);
    public final static EmptyBorder EMPTY_BORDER_BOTTOM = new EmptyBorder(10, 10, 10, 10);
    public final static Icon VCS_LIST_PNG = IOUtils.readIcon("/com/fr/design/images/vcs/vcs_list.png");
    public final static Icon VCS_BACK_PNG = IOUtils.readIcon("/com/fr/design/images/vcs/vcs_back.png");
    public final static Icon VCS_FILTER_PNG = IOUtils.readIcon("/com/fr/design/images/vcs/icon_filter@1x.png");
    public final static Icon VCS_EDIT_PNG = IOUtils.readIcon("/com/fr/design/images/vcs/icon_edit.png");
    public final static Icon VCS_DELETE_PNG = IOUtils.readIcon("/com/fr/design/images/vcs/icon_delete.png");
    public final static Icon VCS_USER_PNG = IOUtils.readIcon("/com/fr/design/images/vcs/icon_user@1x.png");
    public final static Icon VCS_REVERT = IOUtils.readIcon("/com/fr/design/images/vcs/icon_revert.png");
    public final static int OFFSET = 2;
    private static final int MINUTE = 60 * 1000;
    private final static String VCS_PLUGIN_ID = "com.fr.plugin.vcs.v10";
    private static final VcsHelper INSTANCE = new VcsHelper();

    public static VcsHelper getInstance() {
        return INSTANCE;
    }

    private int containsFolderCounts() {
        TemplateFileTree fileTree = TemplateTreePane.getInstance().getTemplateFileTree();
        if (fileTree.getSelectionPaths() == null) {
            return 0;
        }

        //选择的包含文件和文件夹的数目
        if (fileTree.getSelectionPaths().length == 0) {
            return 0;
        }
        //所有的num减去模板的count，得到文件夹的count
        return fileTree.getSelectionPaths().length - fileTree.getSelectedTemplatePaths().length;
    }

    public String getCurrentUsername() {
        return WorkContext.getCurrent().isLocal()
                ? Toolkit.i18nText("Fine-Design_Vcs_Local_User")
                : WorkContext.getCurrent().getConnection().getUserName();
    }

    private int selectedTemplateCounts() {
        TemplateFileTree fileTree = TemplateTreePane.getInstance().getTemplateFileTree();
        if (fileTree.getSelectionPaths() == null) {
            return 0;
        }

        return fileTree.getSelectedTemplatePaths().length;
    }

    public boolean isUnSelectedTemplate() {
        return containsFolderCounts() + selectedTemplateCounts() != 1;
    }

    private String getEditingFilename() {
        String vcsCacheDir = VcsFileSystem.getInstance().getVcsCacheRelativePath();
        JTemplate jt = HistoryTemplateListCache.getInstance().getCurrentEditingTemplate();
        String editingFilePath = jt.getEditingFILE().getPath();
        if (editingFilePath.startsWith(ProjectConstants.REPORTLETS_NAME)) {
            editingFilePath = editingFilePath.replaceFirst(ProjectConstants.REPORTLETS_NAME, StringUtils.EMPTY);
        } else if (editingFilePath.startsWith(vcsCacheDir)) {
            editingFilePath = editingFilePath.replaceFirst(vcsCacheDir, StringUtils.EMPTY);
        }
        if (editingFilePath.startsWith("/")) {
            editingFilePath = editingFilePath.substring(1);
        }
        return editingFilePath;
    }

    private boolean needDeleteVersion(VcsEntity entity) {
        VcsConfigManager configManager = DesignerEnvManager.getEnvManager().getVcsConfigManager();
        if (entity == null || !configManager.isUseInterval()) {
            return false;
        }
        if (configManager.isSaveCommit() && StringUtils.isNotBlank(entity.getCommitMsg())) {
            return false;
        }
        return System.currentTimeMillis() - entity.getTime().getTime() < DesignerEnvManager.getEnvManager().getVcsConfigManager().getSaveInterval() * MINUTE;
    }

    public boolean needInit() {
        PluginContext context = PluginManager.getContext(VCS_PLUGIN_ID);
        return context == null || !context.isRunning();
    }

    /**
     * 版本控制
     *
     * @param jt
     */
    public void fireVcs(final JTemplate jt) {
        ExecutorService fireVcs = Executors.newSingleThreadExecutor(new NamedThreadFactory("fireVcs"));
        fireVcs.execute(new Runnable() {
            @Override
            public void run() {

                String fileName = getEditingFilename();
                VcsOperator operator = WorkContext.getCurrent().get(VcsOperator.class);
                VcsEntity entity = operator.getFileVersionByIndex(fileName, 0);
                int latestFileVersion = 0;
                if (entity != null) {
                    latestFileVersion = entity.getVersion();
                }
                if (jt.getEditingFILE() instanceof VcsCacheFileNodeFile) {
                    operator.saveVersionFromCache(getCurrentUsername(), fileName, StringUtils.EMPTY, latestFileVersion + 1);
                    String path = DesignerFrameFileDealerPane.getInstance().getSelectedOperation().getFilePath();
                    FileVersionTable.getInstance().updateModel(1, WorkContext.getCurrent().get(VcsOperator.class).getVersions(path.replaceFirst("/", "")));
                } else {
                    operator.saveVersion(getCurrentUsername(), fileName, StringUtils.EMPTY, latestFileVersion + 1);
                }
                VcsEntity oldEntity = WorkContext.getCurrent().get(VcsOperator.class).getFileVersionByIndexAndUsername(fileName, getCurrentUsername(), 1);
                if (needDeleteVersion(oldEntity)) {
                    operator.deleteVersion(oldEntity.getFilename(), oldEntity.getVersion());
                }
                if (GcConfig.getInstance().isGcEnable()) {
                    operator.gc();
                }

            }
        });
        fireVcs.shutdown();
    }


    @Override
    public void templateOpened(JTemplate<?, ?> jt) {

    }

    /**
     * 模板保存时 处理.
     *
     * @param jt 模板
     */
    @Override
    public void templateSaved(JTemplate<?, ?> jt) {
        if (needInit()
                && DesignerEnvManager.getEnvManager().getVcsConfigManager().isVcsEnable()
                && !ClusterBridge.isClusterMode()) {
            fireVcs(jt);
        }
    }

    @Override
    public void templateClosed(JTemplate<?, ?> jt) {

    }
}
