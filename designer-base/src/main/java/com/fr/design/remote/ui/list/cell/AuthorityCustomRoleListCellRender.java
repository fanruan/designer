package com.fr.design.remote.ui.list.cell;

import com.fr.base.BaseUtils;
import com.fr.design.remote.constants.MemberIcon;
import com.fr.design.remote.ui.list.AuthorityListCellRenderer;
import com.fr.report.DesignAuthority;

import javax.swing.Icon;

/**
 * @author Lucian.Chen
 * @version 10.0
 * Created by Lucian.Chen on 2019/9/19
 */
public class AuthorityCustomRoleListCellRender extends AuthorityListCellRenderer {
    @Override
    protected Icon getMemberIcon() {
        return BaseUtils.readIcon(MemberIcon.CUSTOM_ROLE_ICON);
    }

    @Override
    protected String getMemberName(DesignAuthority authority) {
        return authority.getUsername();
    }
}
