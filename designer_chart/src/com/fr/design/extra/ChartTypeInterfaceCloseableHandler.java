package com.fr.design.extra;

import com.fr.chart.charttypes.ChartTypeManager;
import com.fr.plugin.proxy.CloseableInvocationHandler;

/**
 * Created by juhaoyu on 2016/12/27.
 */
public class ChartTypeInterfaceCloseableHandler extends CloseableInvocationHandler {


    private final String plotID;

    public ChartTypeInterfaceCloseableHandler(String plotID) throws NoSuchMethodException {

        super();
        this.plotID = plotID;
    }

    @Override
    protected boolean invokeIsClosed() {

        //UI对应的chart如果关闭或者不存在，则UI关闭
        return super.invokeIsClosed() || !containsChart();
    }

    private boolean containsChart() {

        return ChartTypeManager.getInstance().containsPlot(plotID);
    }
}
