package com.fr.design.actions.community;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.utils.BrowseUtils;
import com.fr.general.CloudCenter;


import javax.swing.KeyStroke;

import java.awt.event.ActionEvent;


/**
 * Created by XINZAI on 2018/8/23.
 */
public class TechSolutionAction extends UpdateAction{
    public TechSolutionAction()
    {
        this.setMenuKeySet(TSO);
        this.setName(getMenuKeySet().getMenuName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images//bbs/solotion.png"));

    }

    @Override
    public void actionPerformed(ActionEvent arg0)
    {
        String url = CloudCenter.getInstance().acquireUrlByKind("bbs.solution");
        BrowseUtils.browser(url);

    }
    public static final MenuKeySet TSO = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'T';
        }

        @Override
        public String getMenuName() {
            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Commuinity_Solution");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

}
