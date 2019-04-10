package com.fr.design.ui;

import com.fr.web.struct.browser.RequestClient;

/**
 * @author richie
 * @version 10.0
 * Created by richie on 2019-03-07
 */
public enum  ModernRequestClient implements RequestClient {

    KEY;

    @Override
    public boolean isIE() {
        return false;
    }

    @Override
    public boolean isLowIEVersion() {
        return false;
    }}
