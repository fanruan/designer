/**
 *
 */
package com.fr.design.mainframe.bbs;

import com.fr.base.ConfigManager;
import com.fr.base.FRContext;
import com.fr.design.DesignerEnvManager;
import com.fr.design.bbs.BBSLoginUtils;
import com.fr.design.extra.LoginContextListener;
import com.fr.design.extra.LoginWebBridge;
import com.fr.design.extra.PluginWebBridge;
import com.fr.design.extra.UserLoginContext;
import com.fr.design.extra.WebViewDlgHelper;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.imenu.UIMenuItem;
import com.fr.design.gui.imenu.UIPopupMenu;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.utils.concurrent.ThreadFactoryBuilder;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.DateUtils;
import com.fr.general.Inter;
import com.fr.general.SiteCenter;
import com.fr.general.http.HttpClient;
import com.fr.stable.EncodeConstants;
import com.fr.stable.OperatingSystem;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;

import javax.swing.SwingConstants;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author neil
 * @date: 2015-3-4-上午9:05:52
 */
public class UserInfoLabel extends UILabel {

    //默认查询消息时间, 30s
    private static final long CHECK_MESSAGE_TIME = 30 * 1000L;
    //默认论坛检测到更新后的弹出延迟时间
    private static final long DELAY_TIME = 2 * 1000L;
    private static final String MESSAGE_KEY = "messageCount";

    private static final int MIN_MESSAGE_COUNT = 1;
    private static final int MENU_HEIGHT = 20;

    private static final int DEFAULT_BBS_UID = 0;

    //用户名
    private String userName;
    //消息条数
    private int messageCount;

    private UserInfoPane userInfoPane;
    private BBSLoginDialog bbsLoginDialog;

    public UserInfoPane getUserInfoPane() {
        return userInfoPane;
    }

    public void setUserInfoPane(UserInfoPane userInfoPane) {
        this.userInfoPane = userInfoPane;
    }

    public BBSLoginDialog getBbsLoginDialog() {
        return bbsLoginDialog;
    }

    public void setBbsLoginDialog(BBSLoginDialog bbsLoginDialog) {
        this.bbsLoginDialog = bbsLoginDialog;
    }

    public UserInfoLabel(UserInfoPane userInfoPane) {
        init(userInfoPane);
    }

    private void init(UserInfoPane userInfoPane) {
        this.userInfoPane = userInfoPane;

        String userName = ConfigManager.getProviderInstance().getBbsUsername();
        this.addMouseListener(userInfoAdapter);
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setText(userName);

        if (StableUtils.getMajorJavaVersion() == 8) {
            LoginWebBridge.getHelper().setUILabel(UserInfoLabel.this);
            PluginWebBridge.getHelper().setUILabel(UserInfoLabel.this);
        }

        UserLoginContext.addLoginContextListener(new LoginContextListener() {
            @Override
            public void showLoginContext() {
                WebViewDlgHelper.createLoginDialog();
                clearLoginInformation();
                updateInfoPane();
            }
        });
    }

    private void clearLoginInformation() {
        BBSLoginUtils.bbsLogout();
    }

    private void updateInfoPane() {
        userInfoPane.markUnSignIn();
    }

    /**
     * showBBSDialog 弹出BBS资讯框
     */
    public static void showBBSDialog() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("bbs-dlg-thread-%s").build();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1), namedThreadFactory);
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                // vito:最新mac10.12和javafx弹出框初始化时会有大几率卡死在native方法，这里先屏蔽一下。
                if (!FRContext.isChineseEnv() || OperatingSystem.isMacOS()) {
                    return;
                }
                String lastBBSNewsTime = DesignerEnvManager.getEnvManager().getLastShowBBSNewsTime();
                try {
                    String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                    if (ComparatorUtils.equals(lastBBSNewsTime, today)) {
                        return;
                    }
                    Thread.sleep(DELAY_TIME);
                } catch (InterruptedException e) {
                    FRContext.getLogger().error(e.getMessage());
                }
                HttpClient hc = new HttpClient(SiteCenter.getInstance().acquireUrlByKind("bbs.popup"));
                if (!hc.isServerAlive()) {
                    return;
                }
                String res = hc.getResponseText();
                if (!res.contains(BBSConstants.UPDATE_KEY)) {
                    return;
                }
                try {
                    Class<?> clazz = Class.forName("com.fr.design.mainframe.bbs.BBSDialog");
                    Constructor constructor = clazz.getConstructor(Frame.class);
                    Object instance = constructor.newInstance(DesignerContext.getDesignerFrame());
                    Method showWindow = clazz.getMethod("showWindow", String.class);
                    showWindow.invoke(instance, SiteCenter.getInstance().acquireUrlByKind("bbs.popup"));
                    DesignerEnvManager.getEnvManager().setLastShowBBSNewsTime(DateUtils.DATEFORMAT2.format(new Date()));
                } catch (Throwable ignored) {
                    // ignored
                }
            }
        });
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            FRContext.getLogger().error(e.getMessage());
        }
    }

    public String getUserName() {
        return userName;
    }

    /**
     * 重置当前用户名
     */
    public void resetUserName() {
        this.userName = StringUtils.EMPTY;
    }

    public void setUserName(String userName) {
        if (StringUtils.isEmpty(userName)) {
            return;
        }

        this.userName = userName;
    }

    private String encode(String str) {
        try {
            return URLEncoder.encode(str, EncodeConstants.ENCODING_UTF_8);
        } catch (UnsupportedEncodingException e) {
            return str;
        }
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        // 当只有一条消息时，阅读之后，消息面板重置为只含用户名的状态
        if (this.messageCount == MIN_MESSAGE_COUNT && messageCount < MIN_MESSAGE_COUNT) {
            this.setText(this.userName);
            return;
        }
        if (this.messageCount == messageCount || messageCount < MIN_MESSAGE_COUNT) {
            return;
        }

        this.messageCount = messageCount;
        String sb = StringUtils.BLANK + this.userName +
                "(" + this.messageCount +
                ")" + StringUtils.BLANK;
        //内容eg: aaa(11)

        //更新面板Text
        this.setText(sb);
    }

    private MouseAdapter userInfoAdapter = new MouseAdapter() {

        @Override
        public void mouseEntered(MouseEvent e) {
            UserInfoLabel.this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            userName = ConfigManager.getProviderInstance().getBbsUsername();
            if (StringUtils.isNotEmpty(userName)) {
                UIPopupMenu menu = new UIPopupMenu();
                menu.setOnlyText(true);
                menu.setPopupSize(userInfoPane.getWidth(), userInfoPane.getHeight() * 3);

                //私人消息
                UIMenuItem priviteMessage = new UIMenuItem(Inter.getLocText("FR-Designer-BBSLogin_Privite-Message"));
                priviteMessage.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (StringUtils.isNotEmpty(userName)) {
                            try {
                                String loginUrl = SiteCenter.getInstance().acquireUrlByKind("bbs.default");
                                Desktop.getDesktop().browse(new URI(loginUrl));
                            } catch (Exception exp) {
                                FRContext.getLogger().info(exp.getMessage());
                            }
                        }
                    }

                });
                //切换账号
                UIMenuItem closeOther = new UIMenuItem(Inter.getLocText("FR-Designer-BBSLogin_Switch-Account"));
                closeOther.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        BBSLoginUtils.bbsLogout();
                        UserLoginContext.fireLoginContextListener();
                    }
                });
                menu.add(priviteMessage);
                menu.add(closeOther);
                GUICoreUtils.showPopupMenu(menu, UserInfoLabel.this, 0, MENU_HEIGHT);
            } else {
                UserLoginContext.fireLoginContextListener();
            }
        }
    };
}
