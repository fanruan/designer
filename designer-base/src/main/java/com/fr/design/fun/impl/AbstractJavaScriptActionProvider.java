package com.fr.design.fun.impl;

import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.fun.JavaScriptActionProvider;
import com.fr.design.javascript.JavaScriptActionPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JTemplate;
import com.fr.js.JavaScript;
import com.fr.stable.fun.impl.AbstractProvider;
import com.fr.stable.fun.mark.API;

/**
 * Created by zack on 2015/8/20.
 */
@API(level = JavaScriptActionProvider.CURRENT_LEVEL)
public abstract class AbstractJavaScriptActionProvider extends AbstractProvider implements JavaScriptActionProvider {

    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public String mark4Provider() {
        return getClass().getName();
    }

    @Override
    public FurtherBasicBeanPane<? extends JavaScript> getJavaScriptActionPane(JavaScriptActionPane pane) {
        return getJavaScriptActionPane();
    }

    @Override
    public boolean accept(JTemplate template) {
        // 这里只是为了兼容原来的isSupportType方法
        boolean result = isSupportType();
        if (result) {
            return true;
        }
        return true;
    }

    @Override
    public boolean isSupportType(){
        return true;
    }
}