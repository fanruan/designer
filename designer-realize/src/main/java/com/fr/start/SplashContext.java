package com.fr.start;

import com.fr.concurrent.NamedThreadFactory;
import com.fr.design.i18n.Toolkit;
import com.fr.design.locale.impl.SplashMark;
import com.fr.design.mainframe.bbs.BBSConstants;
import com.fr.event.Event;
import com.fr.event.EventDispatcher;
import com.fr.event.Listener;
import com.fr.general.GeneralContext;
import com.fr.general.locale.LocaleCenter;
import com.fr.general.locale.LocaleMark;
import com.fr.module.ModuleEvent;
import com.fr.stable.StringUtils;

import java.util.Locale;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 启动动画策略
 *
 * @author vito
 * @date 2018/6/5
 */
public class SplashContext {

    public static final String SPLASH_PATH = getSplashPath();
    public static final String SPLASH_CACHE_NAME = SPLASH_PATH.substring(SPLASH_PATH.lastIndexOf("/") + 1);
    private static final int FETCH_ONLINE_MAX_TIMES = 50;
    private static final String THANKS = Toolkit.i18nText("Fine-Design_Report_Thanks_To");

    private static final SplashContext SPLASH_CONTEXT = new SplashContext();

    private SplashStrategy splashStrategy;

    private String moduleId = "";
    private int loadingIndex = 0;
    private String[] loading = new String[]{"..", "....", "......"};

    private int fetchOnlineTimes = 0;
    private String guest = StringUtils.EMPTY;
    private boolean hasShowThanks = false;

    private ScheduledExecutorService scheduler = Executors
            .newScheduledThreadPool(1, new NamedThreadFactory("SplashContext"));

    private Listener<String> listener;

    public static SplashContext getInstance() {
        return SPLASH_CONTEXT;
    }

    private SplashContext() {

    }

    /**
     * 注册具体的启动动画
     */
    public void registerSplash(SplashStrategy splashStrategy) {
        this.splashStrategy = splashStrategy;
    }

    /**
     * 展示启动动画
     */
    public void show() {
        splashStrategy.show();
        //监听
        initListener();
    }

    /**
     * 隐藏启动动画
     */
    public void hide() {
        if (splashStrategy != null) {
            // 窗口关闭后取消定时获取模块信息的timer
            scheduler.shutdown();
            //取消监听
            EventDispatcher.stopListen(listener);
            splashStrategy.hide();
            // 一次性
            splashStrategy = null;
        }
    }

    private void initListener() {
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                showThanks();
                loadingIndex++;
                updateModuleLog(moduleId.isEmpty() ? StringUtils.EMPTY : moduleId + loading[loadingIndex % 3]);
            }
        }, 0, 300, TimeUnit.MILLISECONDS);

        listener = new Listener<String>() {

            @Override
            public void on(Event event, String i18n) {
                moduleId = i18n;
                loadingIndex++;
                updateModuleLog(moduleId.isEmpty() ? StringUtils.EMPTY : moduleId + loading[loadingIndex % 3]);
            }
        };
        EventDispatcher.listen(ModuleEvent.MajorModuleStarting, listener);
    }

    private void updateModuleLog(String text) {
        splashStrategy.updateModuleLog(text);
    }

    private void updateThanksLog(String text) {
        splashStrategy.updateThanksLog(text);
    }

    /**
     * 获取随机感谢人员
     */
    private String getRandomUser(String[] allGuest) {
        int num = new Random().nextInt(allGuest.length);
        return StringUtils.BLANK + allGuest[num];
    }

    /**
     * 尝试获取在线资源，达到尝试上限之后使用默认值
     */
    private void tryFetchOnline() {
        if (StringUtils.isNotEmpty(guest)) {
            return;
        }
        String[] allGuest;
        if (fetchOnlineTimes < FETCH_ONLINE_MAX_TIMES) {
            allGuest = BBSConstants.getAllGuestManual(true);
            if (allGuest.length == 0) {
                fetchOnlineTimes++;
                return;
            }
        } else {
            allGuest = BBSConstants.getAllGuestManual(false);
        }
        guest = getRandomUser(allGuest);
    }

    /**
     * 展示感谢信息。这里场景是优先使用在线名单，
     * 甚至可以因此可以延迟几秒显示。目前是尝试
     * 获取10次在线资源，最大时间3秒
     */
    private void showThanks() {
        if (shouldShowThanks() && !hasShowThanks) {
            tryFetchOnline();
            if (StringUtils.isNotEmpty(guest)) {
                updateThanksLog(THANKS + guest);
                hasShowThanks = true;
            }
        }
    }

    /**
     * 是否显示鸣谢面板
     */
    private boolean shouldShowThanks() {
        Locale[] hideLocales = {Locale.CHINA, Locale.TAIWAN};
        for (Locale loc : hideLocales) {
            if (GeneralContext.getLocale().equals(loc)) {
                return true;
            }
        }
        return false;
    }

    private static String getSplashPath() {
        LocaleMark<String> localeMark = LocaleCenter.getMark(SplashMark.class);
        return localeMark.getValue();
    }
}