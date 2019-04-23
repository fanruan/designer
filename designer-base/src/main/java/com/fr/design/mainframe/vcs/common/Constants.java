package com.fr.workspace.server.vcs.common;

import com.fr.base.BaseUtils;
import com.fr.locale.InterProviderFactory;
import com.fr.workspace.WorkContext;

import javax.swing.Icon;
import javax.swing.border.EmptyBorder;
import java.awt.Color;

import static com.fr.stable.StableUtils.pathJoin;


public class Constants {

    public final static String VCS_DIR = "vcs";

    // 如果用其他方式实现vcs，未必需要这个cache
    //TODO 要不要放到其他地方
    public final static String VCS_CACHE_DIR = pathJoin(VCS_DIR, "cache");

    public final static String CURRENT_USERSNAME = WorkContext.getCurrent().isLocal()
            ? InterProviderFactory.getProvider().getLocText("本地用户")
            : WorkContext.getCurrent().getConnection().getUserName();

    public final static Color TABLE_SELECT_BACKGROUND = new Color(0xD8F2FD);

    public final static Color COPY_VERSION_BTN_COLOR = new Color(0x419BF9);

    public final static Color DELETE_VERSION_BTN_COLOR = new Color(0xEB1D1F);

    public final static EmptyBorder EMPTY_BORDER = new EmptyBorder(5, 10, 0, 10);

    public final static EmptyBorder EMPTY_BORDER_BOTTOM = new EmptyBorder(10, 10, 10, 10);


    public final static Icon VCS_LIST_PNG = BaseUtils.readIcon("/com/fr/design/mainframe/vcs/images/vcs_list.png");
    public final static Icon VCS_BACK_PNG = BaseUtils.readIcon("/com/fr/design/mainframe/vcs/images/vcs_back.png");
    public final static Icon VCS_SAVE_PNG = BaseUtils.readIcon("/com/fr/design/mainframe/vcs/images/vcs_save.png");
    public final static Icon VCS_FILTER_PNG = BaseUtils.readIcon("/com/fr/design/mainframe/vcs/images/icon_filter@1x.png");
    public final static Icon VCS_EDIT_PNG = BaseUtils.readIcon("/com/fr/design/mainframe/vcs/images/icon_edit.png");
    public final static Icon VCS_DELETE_PNG = BaseUtils.readIcon("/com/fr/design/mainframe/vcs/images/icon_delete.png");
    public final static Icon VCS_USER_PNG = BaseUtils.readIcon("/com/fr/design/mainframe/vcs/images/icon_user@1x.png");
    public final static Icon VCS_REVERT = BaseUtils.readIcon("/com/fr/design/mainframe/vcs/images/icon_revert.png");


}
