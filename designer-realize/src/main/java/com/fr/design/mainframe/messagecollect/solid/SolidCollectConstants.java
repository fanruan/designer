package com.fr.design.mainframe.messagecollect.solid;

/**
 * Created by alex sung on 2019/9/5.
 */
public class SolidCollectConstants {
    private SolidCollectConstants(){}

    /**
     * 客户端请求subject
     */
    public static final String REQUEST_SUBJECT = "solid";

    /**
     * 客户端请求超时鉴权时间，默认1h失效
     */
    public static final long TIME_OUT = 60 * 60 * 1000;
}
