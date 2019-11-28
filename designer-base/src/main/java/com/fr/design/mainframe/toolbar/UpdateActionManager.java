package com.fr.design.mainframe.toolbar;

import com.fr.concurrent.NamedThreadFactory;
import com.fr.design.actions.UpdateAction;
import com.fr.design.actions.help.alphafine.AlphaFineConfigManager;
import com.fr.design.constants.DesignerLaunchStatus;
import com.fr.design.gui.frpane.LoadingBasicPane;
import com.fr.event.Event;
import com.fr.event.EventDispatcher;
import com.fr.event.Listener;
import com.fr.event.Null;
import com.fr.general.ComparatorUtils;
import com.fr.general.GeneralUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.StableUtils;
import com.fr.value.NotNullLazyValue;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 按钮面板管理类
 *
 * @author XiaXiang
 * @date 2017/4/13
 */
public class UpdateActionManager {
    private static UpdateActionManager updateActionManager = null;

    private List<UpdateActionModel> updateActions;

    private Map<String, UpdateAction> updateActionsIndexCache = new HashMap<>(16);

    private static boolean isRegisterIndexSearchTextTask = false;

    private boolean afterStartup = false;

    private NotNullLazyValue<Boolean> isCacheValid = new NotNullLazyValue<Boolean>() {
        @NotNull
        @Override
        protected Boolean compute() {
            // 缓存是否有效。
            // 注意：开发工程版本为不是安装版本，
            // 索引只会出现在首次启动。
            return ComparatorUtils.equals(GeneralUtils.readBuildNO(), AlphaFineConfigManager.getInstance().getCacheBuildNO());
        }
    };

    private ExecutorService searchPool = Executors
            .newSingleThreadExecutor(new NamedThreadFactory("IndexAlphaFineSearchText", true));

    /**
     * 限制初始化
     */
    private UpdateActionManager() {
    }

    public synchronized static UpdateActionManager getUpdateActionManager() {
        if (updateActionManager == null) {
            updateActionManager = new UpdateActionManager();
        }
        return updateActionManager;
    }

    public List<UpdateActionModel> getUpdateActions() {
        return updateActions;
    }

    public void setUpdateActions(List<UpdateActionModel> updateActions) {
        this.updateActions = updateActions;
    }

    /**
     * 根据action name获取action对象
     *
     * @param name
     * @return
     */
    public UpdateAction getActionByName(String name) {
        for (UpdateActionModel action : updateActions) {
            if (ComparatorUtils.equals(name, action.getActionName()) && action.getAction().isEnabled()) {
                return action.getAction();
            }
        }
        return null;
    }

    /**
     * 处理action的搜索文本。
     * 缓存逻辑：
     * 1.首次索引或缓存失效的时候（更新版本会使缓存失效），会将索引缓存存到env.xml，
     * 下次直接加载。
     * 2.需要重新索引，则等待设计器初始化完毕之后单线程运行索引任务。
     * 3.集中索引结束之后，每次添加为增量索引。
     *
     * @param paneClass    面板类名
     * @param updateAction 待处理的updateAction
     */
    public void dealWithSearchText(String paneClass, UpdateAction updateAction) {
        Map<String, String> actionSearchTextCache = AlphaFineConfigManager.getInstance().getActionSearchTextCache();
        if (isCacheValid.getValue() && actionSearchTextCache.containsKey(paneClass)) {
            updateAction.setSearchText(actionSearchTextCache.get(paneClass));
        } else {
            if (afterStartup) {
                incrementIndexSearchTextTask(paneClass, updateAction);
            } else {
                if (!updateActionsIndexCache.containsKey(paneClass)) {
                    updateActionsIndexCache.put(paneClass, updateAction);
                }
                registerIndexSearchTextTask();
            }
        }
    }

    /**
     * 由于是UI线程不考虑并发问题
     */
    private void registerIndexSearchTextTask() {
        if (isRegisterIndexSearchTextTask) {
            return;
        }
        isRegisterIndexSearchTextTask = true;
        // 没有缓存或者缓存失效的时候，等待设计器启动之后开始索引任务
        EventDispatcher.listen(DesignerLaunchStatus.STARTUP_COMPLETE, new Listener<Null>() {
            @Override
            public void on(Event event, Null param) {
                EventDispatcher.stopListen(this);
                afterStartup = true;
                for (Map.Entry<String, UpdateAction> cache : updateActionsIndexCache.entrySet()) {
                    searchPool.execute(new IndexTask(cache.getKey(), cache.getValue()));
                }
                updateActionsIndexCache.clear();
                // 标记一下缓存版本
                AlphaFineConfigManager.getInstance().setCacheBuildNO(GeneralUtils.readBuildNO());
            }
        });
    }

    private void incrementIndexSearchTextTask(String key, UpdateAction action) {
        searchPool.execute(new IndexTask(key, action));
        // 标记一下缓存版本
        AlphaFineConfigManager.getInstance().setCacheBuildNO(GeneralUtils.readBuildNO());
    }

    /**
     * 索引任务
     */
    static class IndexTask implements Runnable {
        private String className;
        private UpdateAction updateAction;

        IndexTask(String className, UpdateAction updateAction) {
            this.className = className;
            this.updateAction = updateAction;
        }

        @Override
        public void run() {
            JPanel panel;
            try {
                panel = (JPanel) StableUtils.classForName(className).newInstance();
                if (panel instanceof LoadingBasicPane) {
                    panel = ((LoadingBasicPane) panel).getAllComponents();
                }
                String componentTexts = updateAction.getComponentTexts(panel, "_", new StringBuffer(), new StringBuffer(), new StringBuffer());
                updateAction.setSearchText(componentTexts);
                AlphaFineConfigManager.getInstance().setActionSearchTextCache(className, componentTexts);
            } catch (Exception e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
        }
    }
}