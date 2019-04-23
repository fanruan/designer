package com.fr.design.mainframe.vcs.common;

import com.fr.base.BaseUtils;
import com.fr.design.file.HistoryTemplateListCache;
import com.fr.design.file.TemplateTreePane;
import com.fr.design.gui.itree.filetree.TemplateFileTree;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.JTemplate;
import com.fr.stable.StringUtils;
import com.fr.stable.project.ProjectConstants;
import com.fr.workspace.WorkContext;

import javax.swing.Icon;
import javax.swing.border.EmptyBorder;
import java.awt.Color;

import static com.fr.stable.StableUtils.pathJoin;


public class VcsHelper {

    public final static String VCS_DIR = "vcs";
    public final static String VCS_CACHE_DIR = pathJoin(VCS_DIR, "cache");

    public final static String CURRENT_USERNAME = WorkContext.getCurrent().isLocal()
            ? Toolkit.i18nText("Fine-Design_Vcs_Local_User")
            : WorkContext.getCurrent().getConnection().getUserName();

    public final static Color TABLE_SELECT_BACKGROUND = new Color(0xD8F2FD);

    public final static EmptyBorder EMPTY_BORDER = new EmptyBorder(5, 10, 0, 10);

    public final static EmptyBorder EMPTY_BORDER_BOTTOM = new EmptyBorder(10, 10, 10, 10);


    public final static Icon VCS_LIST_PNG = BaseUtils.readIcon("/com/fr/design/mainframe/vcs/images/vcs_list.png");
    public final static Icon VCS_BACK_PNG = BaseUtils.readIcon("/com/fr/design/mainframe/vcs/images/vcs_back.png");
    public final static Icon VCS_FILTER_PNG = BaseUtils.readIcon("/com/fr/design/mainframe/vcs/images/icon_filter@1x.png");
    public final static Icon VCS_EDIT_PNG = BaseUtils.readIcon("/com/fr/design/mainframe/vcs/images/icon_edit.png");
    public final static Icon VCS_DELETE_PNG = BaseUtils.readIcon("/com/fr/design/mainframe/vcs/images/icon_delete.png");
    public final static Icon VCS_USER_PNG = BaseUtils.readIcon("/com/fr/design/mainframe/vcs/images/icon_user@1x.png");
    public final static Icon VCS_REVERT = BaseUtils.readIcon("/com/fr/design/mainframe/vcs/images/icon_revert.png");

    private static int containsFolderCounts() {
        TemplateFileTree fileTree = TemplateTreePane.getInstance().getTemplateFileTree();
        if (fileTree.getSelectionPaths() == null) {
            return 0;
        }

        //选择的包含文件和文件夹的数目
        if (fileTree.getSelectionPaths().length == 0) {
            return 0;
        }
        //所有的num减去模板的num，得到文件夹的num
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
        return VcsHelper.containsFolderCounts() + VcsHelper.selectedTemplateCounts() > 1;
    }

    public static String getEditingFilename() {
        JTemplate<?, ?> jt = HistoryTemplateListCache.getInstance().getCurrentEditingTemplate();
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


}
