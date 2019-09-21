package com.fr.design.remote.ui.list.cell;

import com.fr.base.BaseUtils;
import com.fr.design.remote.ui.list.AddingMemberListCellRender;
import com.fr.workspace.server.authority.RemoteDesignMember;

import javax.swing.Icon;

/**
 * @author Lucian.Chen
 * @version 10.0
 * Created by Lucian.Chen on 2019/9/19
 */
public class AddingUserListCellRender extends AddingMemberListCellRender {
    @Override
    protected Icon getMemberIcon() {
        return BaseUtils.readIcon("com/fr/design/remote/images/icon_Member_normal@1x.png");
    }

    @Override
    protected String getMemberName(RemoteDesignMember member) {
        return member.getRealName() + "(" + member.getUsername() + ")";
    }
}
