package com.fr.design.mainframe.vcs.common;

import com.fr.design.DesignerEnvManager;
import com.fr.design.file.HistoryTemplateListCache;
import com.fr.design.file.TemplateTreePane;
import com.fr.design.gui.itree.filetree.TemplateFileTree;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.DesignerFrameFileDealerPane;
import com.fr.design.mainframe.JTemplate;
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

import javax.swing.Icon;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.util.Date;

import static com.fr.stable.StableUtils.pathJoin;

/**
 * Created by XiaXiang on 2019/4/17.
 */
public class VcsHelper {

    private final static String VCS_DIR = "vcs";
    public final static String VCS_CACHE_DIR = pathJoin(VCS_DIR, "cache");
    private static final int MINUTE = 60 * 1000;
    private final static String VCS_PLUGIN_ID = "com.fr.plugin.vcs.v10";


    public final static String CURRENT_USERNAME = WorkContext.getCurrent().isLocal()
            ? Toolkit.i18nText("Fine-Design_Vcs_Local_User")
            : WorkContext.getCurrent().getConnection().getUserName();

    public final static Color TABLE_SELECT_BACKGROUND = new Color(0xD8F2FD);
    public final static Color COPY_VERSION_BTN_COLOR = new Color(0x419BF9);


    public final static EmptyBorder EMPTY_BORDER = new EmptyBorder(10, 10, 0, 10);

    public final static EmptyBorder EMPTY_BORDER_BOTTOM = new EmptyBorder(10, 10, 10, 10);


    public final static Icon VCS_LIST_PNG = IOUtils.readIcon("/com/fr/design/images/vcs/vcs_list.png");
    public final static Icon VCS_BACK_PNG = IOUtils.readIcon("/com/fr/design/images/vcs/vcs_back.png");
    public final static Icon VCS_FILTER_PNG = IOUtils.readIcon("/com/fr/design/images/vcs/icon_filter@1x.png");
    public final static Icon VCS_EDIT_PNG = IOUtils.readIcon("/com/fr/design/images/vcs/icon_edit.png");
    public final static Icon VCS_DELETE_PNG = IOUtils.readIcon("/com/fr/design/images/vcs/icon_delete.png");
    public final static Icon VCS_USER_PNG = IOUtils.readIcon("/com/fr/design/images/vcs/icon_user@1x.png");
    public final static Icon VCS_REVERT = IOUtils.readIcon("/com/fr/design/images/vcs/icon_revert.png");

    private static int containsFolderCounts() {
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

    private static int selectedTemplateCounts() {
        TemplateFileTree fileTree = TemplateTreePane.getInstance().getTemplateFileTree();
        if (fileTree.getSelectionPaths() == null) {
            return 0;
        }

        return fileTree.getSelectedTemplatePaths().length;
    }

    public static boolean isUnSelectedTemplate() {
        return VcsHelper.containsFolderCounts() + VcsHelper.selectedTemplateCounts() != 1;
    }

    public static String getEditingFilename() {
        JTemplate jt = HistoryTemplateListCache.getInstance().getCurrentEditingTemplate();
        String editingFilePath = jt.getEditingFILE().getPath();
        if (editingFilePath.startsWith(ProjectConstants.REPORTLETS_NAME)) {
            editingFilePath = editingFilePath.replaceFirst(ProjectConstants.REPORTLETS_NAME, StringUtils.EMPTY);
        } else if (editingFilePath.startsWith(VcsHelper.VCS_CACHE_DIR)) {
            editingFilePath = editingFilePath.replaceFirst(VcsHelper.VCS_CACHE_DIR, StringUtils.EMPTY);
        }
        if (editingFilePath.startsWith("/")) {
            editingFilePath = editingFilePath.substring(1);
        }
        return editingFilePath;
    }

    public static boolean needDeleteVersion(VcsEntity entity) {
        VcsConfigManager configManager = DesignerEnvManager.getEnvManager().getVcsConfigManager();
        if (entity == null || !configManager.isUseInterval()) {
            return false;
        }
        if (configManager.isSaveCommit() && StringUtils.isNotBlank(entity.getCommitMsg())) {
            return false;
        }
        return new Date().getTime() - entity.getTime().getTime() < DesignerEnvManager.getEnvManager().getVcsConfigManager().getSaveInterval() * MINUTE;
    }

    public static boolean needInit() {
        PluginContext context = PluginManager.getContext(VCS_PLUGIN_ID);
        return context == null || !context.isActive();
    }

    /**
     * 版本控制
     * @param jt
     */
    public static void dealWithVcs(final JTemplate jt) {
        new Thread(new Runnable() {
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
                    operator.saveVersionFromCache(VcsHelper.CURRENT_USERNAME, fileName, StringUtils.EMPTY, latestFileVersion + 1);
                    String path = DesignerFrameFileDealerPane.getInstance().getSelectedOperation().getFilePath();
                    FileVersionTable.getInstance().updateModel(1, WorkContext.getCurrent().get(VcsOperator.class).getVersions(path.replaceFirst("/", "")));
                } else {
                    operator.saveVersion(VcsHelper.CURRENT_USERNAME, fileName, StringUtils.EMPTY, latestFileVersion + 1);
                }
                VcsEntity oldEntity = WorkContext.getCurrent().get(VcsOperator.class).getFileVersionByIndex(fileName, 1);
                if (VcsHelper.needDeleteVersion(oldEntity)) {
                    operator.deleteVersion(oldEntity.getFilename(), oldEntity.getVersion());
                }

            }
        }).start();

    }


}
