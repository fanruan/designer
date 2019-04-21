package com.fr.design.mainframe.toolbar;

import com.fr.third.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by XiaXiang on 2019/4/20.
 */
public class VcsContext extends AnnotationConfigApplicationContext {
    public static final VcsContext INSTANCE = new VcsContext();

    public static VcsContext get() {

        return INSTANCE;
    }


}
