package com.fr.design.write.submit.batch;

import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.fun.impl.AbstractJavaScriptActionProvider;
import com.fr.design.javascript.JavaScriptActionPane;
import com.fr.js.JavaScript;

/**
 * Created by loy on 16/8/22.
 */
public class BatchJavaScriptActionProvider extends AbstractJavaScriptActionProvider {

    @Override
    public FurtherBasicBeanPane<? extends JavaScript> getJavaScriptActionPane() {
        return new BatchCommit2DBJavaScriptPane();
    }

    @Override
    public FurtherBasicBeanPane<? extends JavaScript> getJavaScriptActionPane(JavaScriptActionPane pane) {
        return new BatchCommit2DBJavaScriptPane(pane);
    }

}
