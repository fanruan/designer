package com.fr.start.server;

import com.fr.design.mainframe.DesignerContext;
import com.fr.event.Event;
import com.fr.event.EventDispatcher;
import com.fr.event.Listener;
import com.fr.event.Null;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 内置服务器启动监视器
 * Created by zack on 2018/8/21.
 */
public class FineEmbedServerMonitor {
    private int progress;
    private static final int COMPLETE = 100;//启动完成
    private static final int STEP = 5;//随便设置一个假的进度条
    private static final int STEP_HEARTBEAT = 2000;//2秒更新进度
    private static volatile FineEmbedServerMonitor monitor;

    private FineEmbedServerMonitor() {
    }

    static {
        EventDispatcher.listen(EmbedServerEvent.AfterStop, new Listener<Null>() {
            @Override
            public void on(Event event, Null aNull) {
                DesignerContext.getDesignerFrame().disposeProgressDialog();
            }
        });
    }

    public static FineEmbedServerMonitor getInstance() {
        if (monitor == null) {
            synchronized (FineEmbedServerMonitor.class) {
                if (monitor == null) {
                    monitor = new FineEmbedServerMonitor();
                }
            }
        }
        return monitor;
    }

    public int getProgress() {
        if (progress == COMPLETE) {
            return progress;
        } else {
            progress += STEP;
            return progress;
        }
    }

    public void setComplete() {
        this.progress = COMPLETE;
    }

    public void reset() {
        this.progress = 0;
    }

    public boolean isComplete() {
        return this.progress == COMPLETE;
    }

    public void monitor() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {

            @Override
            public void run() {
                while (!isComplete()) {
                    if (!DesignerContext.getDesignerFrame().getProgressDialog().isVisible()) {
                        DesignerContext.getDesignerFrame().showProgressDialog();
                        DesignerContext.getDesignerFrame().getProgressDialog().updateLoadingText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Loading_Embed_Server"));
                    }
                    DesignerContext.getDesignerFrame().updateProgress(getProgress());
                    try {
                        Thread.sleep(STEP_HEARTBEAT);
                    } catch (InterruptedException ignore) {
                    }
                }
                DesignerContext.getDesignerFrame().disposeProgressDialog();
            }
        });
        service.shutdown();
    }
}
