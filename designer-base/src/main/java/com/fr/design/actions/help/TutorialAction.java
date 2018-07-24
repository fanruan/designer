package com.fr.design.actions.help;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;

import javax.swing.KeyStroke;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.Utils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.CloudCenter;

import com.fr.general.http.HttpClient;
import com.fr.stable.OperatingSystem;
import com.fr.stable.StableUtils;

public class TutorialAction extends UpdateAction {
	
    public TutorialAction() {
        this.setMenuKeySet(HELP_TUTORIAL);
        this.setName(getMenuKeySet().getMenuName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/help.png"));
        this.setAccelerator(getMenuKeySet().getKeyStroke());
    }

    private void nativeExcuteMacInstallHomePrograms(String appName) {
        String installHome = StableUtils.getInstallHome();
        if(installHome == null) {
            FRContext.getLogger().error("Can not find the install home, please check it.");
        } else {
            String appPath = StableUtils.pathJoin(new String[]{installHome, "bin", appName});
            if(!(new File(appPath)).exists()) {
                FRContext.getLogger().error(appPath + " can not be found.");
            }

            String cmd = "open " + appPath;
            Runtime runtime = Runtime.getRuntime();

            try {
                runtime.exec(cmd);
            } catch (IOException e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }

        }
    }
    /**
     * 动作
     * @param evt 事件
     */
    public void actionPerformed(ActionEvent evt) {
        String helpURL = CloudCenter.getInstance().acquireUrlByKind("help." + FRContext.getLocale());

        if (helpURL != null) {
            HttpClient client = new HttpClient(helpURL);
            if(client.getResponseCode() != -1) {
                try {
                	 Desktop.getDesktop().browse(new URI(helpURL));
                    return;
                } catch (Exception e) {
                    //出了异常的话, 依然打开本地教程
                }
            }
        }

        if (OperatingSystem.isMacOS()) {
            nativeExcuteMacInstallHomePrograms("helptutorial.app");
        }
        else {
            Utils.nativeExcuteInstallHomePrograms("helptutorial.exe");
        }
    }

    public static final MenuKeySet HELP_TUTORIAL = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'T';
        }

        @Override
        public String getMenuName() {
            return com.fr.design.i18n.Toolkit.i18nText("FR-Designer_COMMUNITY_HELP");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0);
        }
    };

}