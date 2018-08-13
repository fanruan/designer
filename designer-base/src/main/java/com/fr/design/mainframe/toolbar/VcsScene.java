package com.fr.design.mainframe.toolbar;

import com.fr.base.vcs.DesignerMode;
import com.fr.design.actions.edit.RedoAction;
import com.fr.design.actions.edit.UndoAction;
import com.fr.design.actions.file.ExitDesignerAction;
import com.fr.design.actions.file.PreferenceAction;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.SeparatorDef;
import com.fr.design.menu.ShortCut;
import com.fr.stable.ArrayUtils;

/**
 * Created by hzzz on 2017/12/28.
 */
public class VcsScene {

    public static MenuDef createFileMenuDef(ToolBarMenuDockPlus plus) {
        MenuDef menuDef = new MenuDef(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_File"), 'F');

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

        if (!DesignerMode.isAuthorityEditing()) {
            menuDef.addShortCut(new PreferenceAction());
        }

        menuDef.addShortCut(new ExitDesignerAction());
        return menuDef;
    }

    public  static ShortCut[] shortcut4FileMenu(JTemplate jTemplate) {
        return new ShortCut[]{new UndoAction(jTemplate), new RedoAction(jTemplate)};
    }
}
