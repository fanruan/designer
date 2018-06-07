package com.fr.design.mainframe.toolbar;

import com.fr.design.actions.UpdateAction;
import com.fr.design.gui.frpane.LoadingBasicPane;
import com.fr.design.utils.concurrent.ThreadFactoryBuilder;
import com.fr.general.ComparatorUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.StableUtils;

import javax.swing.JPanel;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by XiaXiang on 2017/4/13.
 */
public class UpdateActionManager {
    private static UpdateActionManager updateActionManager = null;
    private List<UpdateActionModel> updateActions;
    private ExecutorService threadPoolExecutor = new ThreadPoolExecutor(
            1, 1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(),
            new ThreadFactoryBuilder().setNameFormat("alphafine-thread-%s").build());//目前测下来一个线程慢慢做处理总共大概也只要两秒,暂时就这样

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

    public synchronized void dealWithSearchText(final String paneClass, final UpdateAction updateAction) {
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                JPanel panel = null;
                try {
                    panel = (JPanel) StableUtils.classForName(paneClass).newInstance();
                    if (panel instanceof LoadingBasicPane) {
                        panel = ((LoadingBasicPane) panel).getAllComponents();
                    }
                    updateAction.setSearchText(updateAction.getComponentTexts(panel, "_", new StringBuffer(), new StringBuffer(), new StringBuffer()));
                } catch (Exception e) {
                    FineLoggerFactory.getLogger().error(e.getMessage(), e);
                }
            }
        });
    }
}