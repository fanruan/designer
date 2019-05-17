package com.fr.design.actions.community;

import com.fr.design.menu.MenuKeySet;
import com.fr.design.utils.BrowseUtils;
import com.fr.general.CloudCenter;
import com.fr.general.IOUtils;

import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;

public class FacebookFansAction extends UpAction {

    public FacebookFansAction() {
        this.setMenuKeySet(FACEBOOKFANS);
        this.setName(getMenuKeySet().getMenuName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(IOUtils.readIcon("/com/fr/design/images/bbs/facebook.png"));
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        BrowseUtils.browser(CloudCenter.getInstance().acquireUrlByKind("facebook.fans.tw"));
    }

    public static final MenuKeySet FACEBOOKFANS = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'F';
        }

        @Override
        public String getMenuName() {
            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Community_FaceBook_Fans");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };
}
