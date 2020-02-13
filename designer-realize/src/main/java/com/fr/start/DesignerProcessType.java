package com.fr.start;

import com.fr.process.FineProcessType;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2020/1/14
 */
public enum  DesignerProcessType implements FineProcessType {

    INSTANCE;

    @Override
    public String obtain() {
        return "designer";
    }
}
