package com.fr.design.remote.ui.list.cell;

import com.fr.base.BaseUtils;
import com.fr.design.remote.constants.MemberIcon;
import com.fr.design.remote.ui.list.AddedMemberListCellRender;
import com.fr.workspace.server.authority.RemoteDesignMember;

import javax.swing.Icon;

/**
 * @author Lucian.Chen
 * @version 10.0
 * Created by Lucian.Chen on 2019/9/19
 */
public class AddedCustomRoleListCellRender extends AddedMemberListCellRender {
    @Override
    protected Icon getMemberIcon() {
        return BaseUtils.readIcon(MemberIcon.CUSTOM_ROLE_ICON);
    }

    @Override
    protected String getMemberName(RemoteDesignMember member) {
        return member.getUsername();
    }
}