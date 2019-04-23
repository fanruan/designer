package com.fr.design.mainframe.vcs.common;

import com.fr.base.BaseUtils;
import com.fr.design.i18n.Toolkit;
import com.fr.workspace.WorkContext;

import javax.swing.Icon;
import javax.swing.border.EmptyBorder;
import java.awt.Color;

import static com.fr.stable.StableUtils.pathJoin;


public class Constants {

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


}
