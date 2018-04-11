package com.fr.start;

import com.bulenkov.iconloader.IconLoader;
import com.bulenkov.iconloader.util.JBUI;
import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.GraphHelper;
import com.fr.design.mainframe.bbs.BBSConstants;
import com.fr.general.GeneralContext;
import com.fr.general.Inter;
import com.fr.general.ModuleContext;
import com.fr.stable.OperatingSystem;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.module.ModuleAdapter;
import com.fr.stable.module.ModuleListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Locale;
import java.util.Random;
import java.util.TimerTask;

/**
 * @author neil
 * @date: 2015-3-13-上午9:47:58
 */
public class ReportSplashPane extends SplashPane {

    private static final String OEM_PATH = "/com/fr/base/images/oem";
    private static final String SPLASH_MAC_CN = "splash_chinese_mac.png";
    private static final String SPLASH_MAC_EN = "splash_english_mac.png";

    private static float JBUI_INIT_SCALE = JBUI.scale(1f);

    private static final Color MODULE_COLOR = new Color(255, 255, 255);
    private static final int MODULE_INFO_X = uiScale(54);
    private static final int MODULE_INFO_Y = uiScale(340);

    private static final Color THANK_COLOR = new Color(255, 255, 255, (int) (0.6 * 255 + 0.5));
    private static final int THANK_INFO_Y = uiScale(382);

    private static final String ARIAL_FONT_NAME = "Arial";
    private static final String YAHEI_FONT_NAME = "Microsoft YaHei";

    private static final String GUEST = getRandomUser();

    private String showText = "";

    private String moduleID = "";
    private int loadingIndex = 0;
    private String[] loading = new String[]{"..", "....", "......"};
    private java.util.Timer timer = new java.util.Timer();

    private static float uiScale(float f) {
        return f * JBUI_INIT_SCALE;
    }

    private static int uiScale(int i) {
        return (int) (i * JBUI_INIT_SCALE);
    }

    public ReportSplashPane() {
        init();
    }

    private void init() {
        this.setBackground(null);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                loadingIndex++;
                ReportSplashPane.this.setShowText(moduleID.isEmpty() ? StringUtils.EMPTY : moduleID + loading[loadingIndex % 3]);
                ReportSplashPane.this.repaint();
            }
        }, 0, 300);

        ModuleListener moduleListener = new ModuleAdapter() {
            @Override
            public void onStartBefore(String moduleName, String moduleI18nName) {
                moduleID = moduleI18nName;
                loadingIndex++;
                ReportSplashPane.this.setShowText(moduleID.isEmpty() ? StringUtils.EMPTY : moduleID + loading[loadingIndex % 3]);
                ReportSplashPane.this.repaint();
            }
        };
        ModuleContext.registerModuleListener(moduleListener);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Icon icon = IconLoader.getIcon(StableUtils.pathJoin(OEM_PATH, getImageName()));
        icon.paintIcon(null, g, 0, 0);
        paintShowText((Graphics2D) g);
        g.dispose();
    }

    @Override
    public void setShowText(String text) {
        this.showText = text;
    }

    @Override
    public Image getSplashImage() {
        Icon icon = IconLoader.getIcon(StableUtils.pathJoin(OEM_PATH, getImageName()));
        return ((ImageIcon) IconLoader.getIconSnapshot(icon)).getImage();
    }

    protected void paintShowText(Graphics2D splashG2d) {
        GraphHelper.applyRenderingHints(splashG2d);

        splashG2d.setPaint(MODULE_COLOR);

        Font font = null;
        if (OperatingSystem.isWindows()) {
            font = new Font(YAHEI_FONT_NAME, Font.PLAIN, uiScale(12));
        }

        if (font == null || isDialogFont(font)) {
            font = createFont(ARIAL_FONT_NAME);
        }
        splashG2d.setFont(font);

        //加载模块信息
        GraphHelper.drawString(splashG2d, showText, MODULE_INFO_X, MODULE_INFO_Y);

        //每次随机感谢一位论坛用户
        if (shouldShowThanks()) {
            splashG2d.setPaint(THANK_COLOR);
            String content = Inter.getLocText("FR-Designer_Thanks-To") + GUEST;
            GraphHelper.drawString(splashG2d, content, MODULE_INFO_X, THANK_INFO_Y);
        }
    }

    private boolean isDialogFont(Font font) {
        return Font.DIALOG.equals(font.getFamily(Locale.US));
    }

    private Font createFont(String fontName) {
        return new Font(fontName, Font.PLAIN, uiScale(12));
    }

    // 是否显示鸣谢文字
    protected boolean shouldShowThanks() {
        Locale[] hideLocales = {Locale.US, Locale.KOREA, Locale.JAPAN};
        for (Locale loc : hideLocales) {
            if (FRContext.getLocale().equals(loc)) {
                return false;
            }
        }
        return true;
    }

    private static String getRandomUser() {
        String[] allGuest = BBSConstants.getAllGuest();
        if (allGuest.length == 0) {
            return StringUtils.EMPTY;
        }
        int num = new Random().nextInt(allGuest.length);
        return StringUtils.BLANK + allGuest[num];
    }

    /**
     * 窗口关闭后取消定时获取模块信息的timer
     */
    @Override
    public void releaseTimer() {
        timer.cancel();
    }

    /**
     * 创建启动画面的背景图片
     *
     * @return 背景图片
     */
    @Override
    public Image createSplashBackground() {
        String fileName = getImageName();
        return BaseUtils.readImage(StableUtils.pathJoin(OEM_PATH, fileName));
    }

    //获取图片文件名
    private String getImageName() {
        boolean isChina = GeneralContext.isChineseEnv();
        //jdk1.8下透明有bug, 设置了setWindowTransparent后, JFrame直接最小化了, 先用mac下的加载图片
        return isChina ? SPLASH_MAC_CN : SPLASH_MAC_EN;
    }
}
