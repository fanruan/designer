package com.fr.design.fun.impl;

import com.fr.design.javascript.JavaScriptActionPane;

/**
 * Created by loy on 16/8/23.
 */
public abstract class AbstractJavaScriptActionExtensionProvider extends AbstractJavaScriptActionProvider {

    /**
     * 传给插件额外参数
     * @param pane
     */
    public abstract void setJavaScriptActionPane(JavaScriptActionPane pane);
}
