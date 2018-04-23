package com.fr.design.fun;

import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.javascript.JavaScriptActionPane;
import com.fr.js.JavaScript;
import com.fr.stable.fun.mark.Mutable;

/**
 * Created by zack on 2015/8/14.
 */
public interface JavaScriptActionProvider extends Mutable{

    String XML_TAG = "JavaScriptActionProvider";

    int CURRENT_LEVEL = 1;

    FurtherBasicBeanPane<? extends JavaScript> getJavaScriptActionPane();

    FurtherBasicBeanPane<? extends JavaScript> getJavaScriptActionPane(JavaScriptActionPane pane);

}