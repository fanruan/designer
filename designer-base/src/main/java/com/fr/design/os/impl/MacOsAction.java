package com.fr.design.os.impl;

import com.fr.design.mainframe.DesignerFrame;
import com.fr.invoke.Reflect;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.os.OperatingSystem;
import com.fr.stable.os.support.OSBasedAction;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2020/3/13
 */
public class MacOsAction implements OSBasedAction {

    @Override
    public void execute(final Object... objects) {
        if (OperatingSystem.isMacos()) {
            try {
                Class app = Class.forName("com.apple.eawt.Application");
                Class handler = Class.forName("com.apple.eawt.QuitHandler");
                Object instance = Proxy.newProxyInstance(handler.getClassLoader(), new Class[]{handler},
                                                         new InvocationHandler() {
                                                             @Override
                                                             public Object invoke(Object proxy, Method method,
                                                                                  Object[] args) throws Throwable {
                                                                 if ("handleQuitRequestWith".equals(method.getName())) {
                                                                     DesignerFrame designerFrame = (DesignerFrame) objects[0];
                                                                     designerFrame.exit();
                                                                 }
                                                                 return null;
                                                             }
                                                         });
                Reflect.on(Reflect.on(app).call("getApplication").get()).call("setQuitHandler", instance);
            } catch (ClassNotFoundException e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
        }
    }
}
