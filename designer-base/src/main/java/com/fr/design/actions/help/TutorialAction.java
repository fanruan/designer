package com.fr.design.actions.help;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.CloudCenter;
import com.fr.general.GeneralContext;
import com.fr.general.http.HttpToolbox;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.CommonUtils;
import com.fr.stable.ProductConstants;
import com.fr.stable.StringUtils;
import com.fr.third.org.apache.http.HttpStatus;
import com.fr.third.org.apache.http.StatusLine;
import com.fr.third.org.apache.http.client.methods.HttpGet;

import javax.swing.KeyStroke;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URI;

public class TutorialAction extends UpdateAction {

    public TutorialAction() {
        this.setMenuKeySet(HELP_TUTORIAL);
        this.setName(getMenuKeySet().getMenuName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/bbs/help.png"));
        this.setAccelerator(getMenuKeySet().getKeyStroke());
    }

    /**
     * 动作
     * @param evt 事件
     */
    public void actionPerformed(ActionEvent evt) {
        String helpURL = CloudCenter.getInstance().acquireUrlByKind(createDocKey());
        if (isServerOnline(helpURL)) {
            try {
                Desktop.getDesktop().browse(new URI(helpURL));
                return;
            } catch (Exception e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
        }
        FineLoggerFactory.getLogger().warn(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Offline_Helptutorial_Msg"));
    }

    // 生成帮助文档 sitecenter key, help.zh_CN.10
    protected String createDocKey() {
        String locale = GeneralContext.getLocale().toString();
        return CommonUtils.join(new String[]{ "help", locale, ProductConstants.MAIN_VERSION }, ".");
    }

    // 判断是否可以访问在线文档
    protected boolean isServerOnline(String url) {
        if (StringUtils.isEmpty(url)) {
            return false;
        }

        HttpGet getHelp = new HttpGet(url);
        try {
            StatusLine statusLine = HttpToolbox.getHttpClient(url).execute(getHelp).getStatusLine();
            return statusLine.getStatusCode() == HttpStatus.SC_OK;
        } catch (Exception ignore) {
            // 网络异常
            return false;
        }
    }

    public static final MenuKeySet HELP_TUTORIAL = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'T';
        }

        @Override
        public String getMenuName() {
            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Community_Help");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0);
        }
    };

}
