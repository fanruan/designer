package com.fr.design.mainframe.toolbar;

import com.fr.base.BaseUtils;
import com.fr.design.actions.file.ExitDesignerAction;
import com.fr.design.actions.file.PreferenceAction;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.SeparatorDef;
import com.fr.design.menu.ShortCut;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;

/**
 * Created by hzzz on 2017/12/28.
 */
public class VcsScene {

    public static MenuDef createFileMenuDef(ToolBarMenuDockPlus plus) {
        MenuDef menuDef = new MenuDef(Inter.getLocText("FR-Designer_File"), 'F');

        ShortCut[] scs = new ShortCut[0];
        if (!ArrayUtils.isEmpty(scs)) {
            menuDef.addShortCut(scs);
        }

        scs = plus.shortcut4FileMenu();
        if (!ArrayUtils.isEmpty(scs)) {
            menuDef.addShortCut(SeparatorDef.DEFAULT);
            menuDef.addShortCut(scs);
            menuDef.addShortCut(SeparatorDef.DEFAULT);
        }

        if (!BaseUtils.isAuthorityEditing()) {
            menuDef.addShortCut(new PreferenceAction());
        }

        menuDef.addShortCut(new ExitDesignerAction());
        return menuDef;
    }
}
