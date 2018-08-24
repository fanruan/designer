package com.fr.design.actions.community;

import com.fr.base.BaseUtils;

import com.fr.design.menu.MenuKeySet;
import com.fr.design.utils.BrowseUtils;
import com.fr.general.CloudCenter;
import javax.swing.KeyStroke;

import java.awt.event.ActionEvent;

/**
 * Created by XINZAI on 2018/8/23.
 */
public class CusDemandAction extends  UpAction{
    public CusDemandAction()
    {
        this.setMenuKeySet(DEMAND);
        this.setName(getMenuKeySet().getMenuName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/bbs/demand.png"));

    }

    @Override
    public void actionPerformed(ActionEvent arg0)
    {
        String url = CloudCenter.getInstance().acquireUrlByKind("bbs.demand");
        BrowseUtils.browser(url);

    }
    public static final MenuKeySet DEMAND = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'D';
        }

        @Override
        public String getMenuName() {
            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Commuinity_Demand");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };
}
