package com.fr.design.actions.community;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.design.actions.UpdateAction;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.CloudCenter;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.StringUtils;

import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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
        if (StringUtils.isEmpty(url)) {
            FRContext.getLogger().info("The URL is empty!");
            return;
        }
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException exp) {
            JOptionPane.showMessageDialog(null, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Set_Default_Browser"));
            FineLoggerFactory.getLogger().error(exp.getMessage(), exp);
        } catch (URISyntaxException exp) {
            FineLoggerFactory.getLogger().error(exp.getMessage(), exp);
        } catch (Exception exp) {
            FineLoggerFactory.getLogger().error(exp.getMessage(), exp);
            FineLoggerFactory.getLogger().error("Can not open the browser for URL:  " + url);
        }

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
