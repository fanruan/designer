package com.fr.design.write.submit.batch;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.fun.impl.AbstractSubmitProvider;
import com.fr.general.Inter;

/**
 * Created by loy on 16/8/13.
 */
public class BatchSubmitProvider extends AbstractSubmitProvider {
    private volatile static BatchSubmitProvider instance;

    public static BatchSubmitProvider getInstance() {
        if (instance == null) {
            synchronized (BatchSubmitProvider.class) {
                if (instance == null) {
                    instance = new BatchSubmitProvider();
                }
            }
        }
        return instance;
    }
    @Override
    public BasicBeanPane appearanceForSubmit() {
        return new SmartInsertBatchSubmitPane();
    }

    @Override
    public String dataForSubmit() {
        return Inter.getLocText("Performance-plugin_submitbatch_name");
    }

    @Override
    public String keyForSubmit() {
        return "submitbatch";
    }

    @Override
    public int currentAPILevel() {
        return 1;
    }
}
