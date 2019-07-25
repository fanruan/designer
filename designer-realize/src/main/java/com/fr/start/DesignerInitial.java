package com.fr.start;

/**
 * Created by juhaoyu on 2019-06-14.
 * 设计器上下文
 */
public class DesignerInitial {
    
    private static volatile Designer designer;
    
    public synchronized static void init(String... args) {
        
        designer = new Designer(args);
    }
    
    public synchronized static void show() {
        
        if (designer != null) {
            designer.show();
        }
        //启动画面结束
        SplashContext.getInstance().hide();
    }
}
