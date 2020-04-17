package com.fr.design.os.impl;

import com.fr.design.actions.help.AboutDialog;
import com.fr.design.actions.help.AboutPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.exit.DesignerExiter;
import com.fr.general.ComparatorUtils;
import com.fr.invoke.Reflect;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.os.support.OSBasedAction;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2020/3/13
 */
public class MacOsAddListenerAction implements OSBasedAction {

    @Override
    public void execute(final Object... objects) {
        try {
            Class app = Class.forName("com.apple.eawt.Application");
            Class quitHandler = Class.forName("com.apple.eawt.QuitHandler");
            Object quitInstance = getProxy(quitHandler, "handleQuitRequestWith", new QuitAction());
            Class aboutHandler = Class.forName("com.apple.eawt.AboutHandler");
            Object aboutInstance = getProxy(aboutHandler, "", new AboutAction());
            Reflect.on(Reflect.on(app).call("getApplication").get()).call("setQuitHandler", quitInstance)
                                                                    .call("setAboutHandler", aboutInstance);
        } catch (ClassNotFoundException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }


    private Object  getProxy(Class clazz, final String methodName, final Action action) {
        return Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz},
                                                 new InvocationHandler() {
                                                     @Override
                                                     public Object invoke(Object proxy, Method method,
                                                                          Object[] args) throws Throwable {
                                                         if (ComparatorUtils.equals(methodName, method.getName())) {
                                                             action.execute();
                                                         }
                                                         return null;
                                                     }
                                                 });
    }

    interface Action {
        void execute();
    }

    private class QuitAction implements Action {

        @Override
        public void execute() {
            if (DesignerContext.getDesignerFrame() != null && DesignerContext.getDesignerFrame().isShowing()) {
                DesignerContext.getDesignerFrame().exit();
            } else {
                DesignerExiter.getInstance().execute();
            }
        }
    }

    private class AboutAction implements Action {

        @Override
        public void execute() {
            AboutPane aboutPane = new AboutPane();
            AboutDialog aboutDialog = new AboutDialog(DesignerContext.getDesignerFrame(), aboutPane);
            aboutDialog.setVisible(true);
        }
    }
}
