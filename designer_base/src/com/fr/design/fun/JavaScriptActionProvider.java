package com.fr.design.fun;

import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.js.JavaScript;
import com.fr.stable.fun.Level;

/**
 * Created by zack on 2015/8/14.
 */
public interface JavaScriptActionProvider extends Level{

    String XML_TAG = "JavaScriptActionProvider";

    int CURRENT_LEVEL = 1;


    FurtherBasicBeanPane<? extends JavaScript> getJavaScriptActionPane();
}