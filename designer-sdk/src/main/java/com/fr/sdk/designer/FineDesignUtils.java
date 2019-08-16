//package com.fr.sdk.designer;
//
//import com.fr.config.activator.ConfigurationActivator;
//import com.fr.design.env.DesignerWorkspaceGenerator;
//import com.fr.design.env.RemoteDesignerWorkspaceInfo;
//import com.fr.log.FineLoggerFactory;
//import com.fr.module.Activator;
//import com.fr.module.Module;
//import com.fr.module.tool.ActivatorToolBox;
//import com.fr.report.ReportActivator;
//import com.fr.report.RestrictionActivator;
//import com.fr.report.module.ReportBaseActivator;
//import com.fr.scheduler.SchedulerActivator;
//import com.fr.sdk.server.shell.ModuleShell;
//import com.fr.serialization.SerializationActivator;
//import com.fr.stable.StringUtils;
//import com.fr.startup.WorkspaceRegister;
//import com.fr.store.StateServerActivator;
//import com.fr.workspace.WorkContext;
//import com.fr.workspace.connect.WorkspaceConnectionInfo;
//import com.fr.workspace.engine.WorkspaceActivator;
//import com.fr.workspace.server.ServerWorkspaceRegister;
//
///**
// * 设计器SDK模块工具类，用来放一些设计器相关插件开发过程中常用的工具函数
// */
//public class FineDesignUtils {
//
//    /**
//     * 创建一个连接远程服务器的模块
//     * @param remoteUrl 远程服务器地址
//     * @param username 用户名
//     * @param password 密码
//     * @return 模块代理对象 使用ModuleShell的start和stop控制模块启停
//     */
//    public static ModuleShell createRemoteServerModule(String remoteUrl, String username, String password) {
//        return createRemoteServerModule(remoteUrl, username, password, StringUtils.EMPTY, StringUtils.EMPTY);
//    }
//
//    /**
//     * 创建一个连接远程服务器的模块
//     * @param remoteUrl 远程服务器地址
//     * @param username 用户名
//     * @param password 密码
//     * @param certPath https证书路径
//     * @param certSecretKey 证书秘钥
//     * @return 模块代理对象 使用ModuleShell的start和stop控制模块启停
//     */
//    public static ModuleShell createRemoteServerModule(final String remoteUrl, final String username, final String password, final String certPath, final String certSecretKey) {
//        Module module = ActivatorToolBox.simpleLink(
//                new WorkspaceActivator(),
//                new SerializationActivator(),
//                new Activator() {
//                    @Override
//                    public void start() {
//                        WorkspaceConnectionInfo connectionInfo = new WorkspaceConnectionInfo(remoteUrl, username, password, certPath, certSecretKey);
//                        try {
//                            WorkContext.switchTo(DesignerWorkspaceGenerator.generate(RemoteDesignerWorkspaceInfo.create(connectionInfo)));
//                        } catch (Exception e) {
//                            FineLoggerFactory.getLogger().error(e.getMessage(),e);
//                        }
//                    }
//
//                    @Override
//                    public void stop() {
//
//                    }
//                },
//                new ConfigurationActivator(),
//                new StateServerActivator(),
//                new SchedulerActivator(),
//                new ReportBaseActivator(),
//                new RestrictionActivator(),
//                new ReportActivator(),
//                new WorkspaceRegister(),
//                new ServerWorkspaceRegister()
//        );
//        return new ModuleShell(module);
//    }
//}