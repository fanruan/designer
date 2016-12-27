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

        //找不到Plugin对象时，默认是关闭的
        return super.invokeIsClosed() && containsChart();
    }

    //UI对应的chart如果没有关闭或者不存在，则UI关闭
    private boolean containsChart() {

        return ChartTypeManager.getInstance().containsPlot(plotID);
    }
}
