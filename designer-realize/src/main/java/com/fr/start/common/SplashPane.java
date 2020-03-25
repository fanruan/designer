package com.fr.start.common;

import com.bulenkov.iconloader.IconLoader;
import com.bulenkov.iconloader.util.JBUI;
import com.fr.base.GraphHelper;
import com.fr.design.locale.impl.SplashMark;
import com.fr.design.ui.util.GraphicsConfig;
import com.fr.general.locale.LocaleCenter;
import com.fr.general.locale.LocaleMark;
import com.fr.stable.GraphDrawHelper;
import com.fr.stable.StringUtils;
import com.fr.stable.os.OperatingSystem;
import com.fr.value.NotNullLazyValue;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;

/**
 * 启动画面面板
 *
 * @author vito
 * @version 10.0
 * Created by vito on 2019/09/12
 */
public class SplashPane extends JPanel {

    private static String OEM_PATH = getSplashPath();
    private static float JBUI_INIT_SCALE = JBUI.scale(1f);

    private static final Color MODULE_COLOR = new Color(255, 255, 255);
    private static final int MODULE_INFO_X = uiScale(36);
    private static final int MODULE_INFO_Y = uiScale(300);

    private static final Color THANK_COLOR = new Color(255, 255, 255, (int) (0.6 * 255 + 0.5));
    private static final int THANK_INFO_Y = uiScale(340);
    private static final int FONT_SIZE = uiScale(12);

    private static final int MODULE_INFO_WIDTH = uiScale(170);
    private static final int MODULE_INFO_HEIGHT = uiScale(20);

    private static final String ARIAL_FONT_NAME = "Arial";
    private static final String YAHEI_FONT_NAME = "Microsoft YaHei";

    private String thanksLog = StringUtils.EMPTY;
    private String moduleText = StringUtils.EMPTY;

    private static int uiScale(int i) {
        return (int) (i * JBUI_INIT_SCALE);
    }

    private static String getSplashPath() {
        LocaleMark<String> localeMark = LocaleCenter.getMark(SplashMark.class);
        return localeMark.getValue();
    }

    private NotNullLazyValue<Font> fontValue = new NotNullLazyValue<Font>() {
        @NotNull
        @Override
        protected Font compute() {
            Font font = null;
            if (OperatingSystem.isWindows()) {
                font = createFont(YAHEI_FONT_NAME);
            }
            if (font == null || isDialogFont(font)) {
                font = createFont(ARIAL_FONT_NAME);
            }
            return font;
        }
    };

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Icon icon = IconLoader.getIcon(OEM_PATH);
        icon.paintIcon(null, g, 0, 0);
        paintShowText((Graphics2D) g);
        g.dispose();
    }


    protected void paintShowText(Graphics2D g) {
        GraphicsConfig config = new GraphicsConfig(g).setupAAPainting();

        g.setPaint(MODULE_COLOR);
        g.setFont(fontValue.getValue());

        //加载模块信息
        GraphDrawHelper.drawString(g, moduleText, MODULE_INFO_X, MODULE_INFO_Y);

        //感谢用户信息
        if (StringUtils.isNotEmpty(thanksLog)) {
            g.setPaint(THANK_COLOR);
            GraphHelper.drawString(g, thanksLog, MODULE_INFO_X, THANK_INFO_Y);
        }
        config.restore();
    }

    Dimension getSplashDimension() {
        Icon icon = IconLoader.getIcon(OEM_PATH);
        return new Dimension(icon.getIconWidth(), icon.getIconHeight());
    }

    private boolean isDialogFont(Font font) {
        return Font.DIALOG.equals(font.getFamily(Locale.US));
    }

    private Font createFont(String fontName) {
        return new Font(fontName, Font.PLAIN, FONT_SIZE);
    }

    /**
     * 设置在启动过程中, 动态改变的文本, 如 当前启动的模块信息
     *
     * @param text 指定的文本
     */
    void updateModuleLog(String text) {
        moduleText = text;
        repaint(MODULE_INFO_X, MODULE_INFO_Y - FONT_SIZE, MODULE_INFO_WIDTH, MODULE_INFO_HEIGHT);
    }

    void updateThanksLog(String text) {
        thanksLog = text;
        repaint(MODULE_INFO_X, THANK_INFO_Y - FONT_SIZE, MODULE_INFO_WIDTH, MODULE_INFO_HEIGHT);
    }

}
