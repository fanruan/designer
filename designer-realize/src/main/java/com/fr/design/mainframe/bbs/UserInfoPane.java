/**
 *
 */
package com.fr.design.mainframe.bbs;

import com.fr.base.FRContext;
import com.fr.concurrent.NamedThreadFactory;
import com.fr.config.MarketConfig;
import com.fr.design.DesignerEnvManager;
import com.fr.design.constants.UIConstants;
import com.fr.design.dialog.BasicPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.general.DateUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.StringUtils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author neil
 * @date: 2015-3-5-上午11:19:50
 */
public class UserInfoPane extends BasicPane {

    /**
     * 默认未登录颜色
     */
    private static final Color UN_LOGIN_BACKGROUND = UIConstants.TEMPLATE_TAB_PANE_BACKGROUND;
    private static final Color LOGIN_BACKGROUND = new Color(184, 220, 242);
    private static final int WIDTH = 104;
    private static final int HEIGHT = 24;

    /**
     * 登录成功
     */
    private static final String LOGININ = "0";

    /**
     * 登录框弹出间隔时间
     */
    private static final int LOGIN_DIFF_DAY = 7;
    /**
     * 等待国际化等相关初始化工作完成之后再弹出登录框
     */
    private static final int WAIT_TIME = 10000;

    private UserInfoLabel userInfoLabel;


    private static UserInfoPane instance = new UserInfoPane();


    public static UserInfoPane getInstance() {
        return instance;
    }

    /**
     * 构造函数
     */
    private UserInfoPane() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setLayout(new BorderLayout());

        this.userInfoLabel = new UserInfoLabel(this);

        this.markUnSignIn();
        autoPushLoginDialog();

        this.add(userInfoLabel, BorderLayout.CENTER);
    }

    public UserInfoLabel getUserInfoLabel() {
        return userInfoLabel;
    }

    public void setUserInfoLabel(UserInfoLabel userInfoLabel) {
        this.userInfoLabel = userInfoLabel;
    }


    /**
     * 标志未登录状态, 面板设置为灰色
     */
    public void markUnSignIn() {
        this.userInfoLabel.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Base_UnSignIn"));
        this.userInfoLabel.setOpaque(true);
        this.userInfoLabel.setBackground(UN_LOGIN_BACKGROUND);
        this.userInfoLabel.resetUserName();
    }

    /**
     * 标志登陆状态, 面包设置为蓝色
     *
     * @param userName 用户名
     */
    public void markSignIn(String userName) {
        this.userInfoLabel.setText(userName);
        this.userInfoLabel.setUserName(userName);
        this.userInfoLabel.setOpaque(true);
        this.userInfoLabel.setBackground(LOGIN_BACKGROUND);
    }

    @Override
    protected String title4PopupWindow() {
        return StringUtils.EMPTY;
    }

    public void updateBBSUserInfo() {
        String username = MarketConfig.getInstance().getBbsUsername();
        if (StringUtils.isEmpty(username)) {
            markUnSignIn();
        } else {
            markSignIn(username);
        }
    }


    /**
     * 计算xml保存的上次弹框时间和当前时间的时间差
     *
     * @return 时间差
     */
    private int getDiffFromLastLogin() {
        String lastBBSTime = DesignerEnvManager.getEnvManager().getLastShowBBSTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date lastBBSDate;
        try {
            if (lastBBSTime != null) {
                synchronized (this) {
                    lastBBSDate = sdf.parse(lastBBSTime);
                }
                Calendar calender = Calendar.getInstance();
                calender.setTime(lastBBSDate);
                int dayOld = calender.get(Calendar.DAY_OF_YEAR);
                calender.setTime(new Date());
                int dayNew = calender.get(Calendar.DAY_OF_YEAR);
                return dayNew - dayOld;
            }
        } catch (ParseException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        return 1;
    }

    private void autoPushLoginDialog() {
        ExecutorService executorService = Executors.newSingleThreadExecutor(new NamedThreadFactory("BBSAutoPushLogin"));
        executorService.execute(new Runnable() {

            @Override
            public void run() {
                try {
                    // 等国际化等加载完毕再启动线程弹出登录框
                    Thread.sleep(WAIT_TIME);
                    if (!FRContext.isChineseEnv()) {
                        return;
                    }

                    //七天弹一次, 如果xml中和当前时间相差小于7天, 就不弹了
                    if (getDiffFromLastLogin() < LOGIN_DIFF_DAY) {
                        return;
                    }

                } catch (InterruptedException e) {
                    FineLoggerFactory.getLogger().error(e.getMessage(), e);
                }

                String userName = MarketConfig.getInstance().getBbsUsername();
                if (StringUtils.isNotEmpty(userName)) {
                    return;
                }

                BBSLoginDialog bbsLoginDialog = userInfoLabel.getBbsLoginDialog();
                if (bbsLoginDialog == null) {
                    bbsLoginDialog = new BBSLoginDialog(DesignerContext.getDesignerFrame(), userInfoLabel);
                    userInfoLabel.setBbsLoginDialog(bbsLoginDialog);
                }

                bbsLoginDialog.showWindow();
                DesignerEnvManager.getEnvManager().setLastShowBBSTime(DateUtils.DATEFORMAT2.format(new Date()));
            }

        });
        executorService.shutdown();
    }


}
