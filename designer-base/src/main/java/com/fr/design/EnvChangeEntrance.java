package com.fr.design;

import com.fr.design.data.DesignTableDataManager;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.env.DesignerWorkspaceGenerator;
import com.fr.design.env.DesignerWorkspaceInfo;
import com.fr.design.env.DesignerWorkspaceType;
import com.fr.design.env.RemoteWorkspace;
import com.fr.design.file.HistoryTemplateListCache;
import com.fr.design.file.TemplateTreePane;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.utils.DesignUtils;
import com.fr.design.write.submit.CheckServiceDialog;
import com.fr.env.EnvListPane;
import com.fr.exit.DesignerExiter;
import com.fr.general.GeneralContext;
import com.fr.general.GeneralUtils;
import com.fr.invoke.Reflect;
import com.fr.json.JSONArray;
import com.fr.license.exception.RegistEditionException;
import com.fr.locale.InterProviderFactory;
import com.fr.log.FineLoggerFactory;
import com.fr.rpc.Result;
import com.fr.stable.AssistUtils;
import com.fr.stable.EnvChangedListener;
import com.fr.start.server.ServerTray;
import com.fr.workspace.WorkContext;
import com.fr.workspace.WorkContextCallback;
import com.fr.workspace.Workspace;
import com.fr.workspace.base.WorkspaceAPI;
import com.fr.workspace.connect.WorkspaceConnectionInfo;
import com.fr.workspace.engine.base.FineObjectPool;
import com.fr.workspace.engine.channel.http.FunctionalHttpRequest;
import com.fr.workspace.engine.exception.WorkspaceAuthException;
import com.fr.workspace.engine.exception.WorkspaceConnectionException;
import com.fr.workspace.engine.rpc.WorkspaceProxyPool;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;

public class EnvChangeEntrance {

    private String currentEnvName = "";

    public static EnvChangeEntrance getInstance() {
        return HOLDER.singleton;
    }

    private static class HOLDER {
        private static EnvChangeEntrance singleton = new EnvChangeEntrance();
    }


    private EnvChangeEntrance() {
        currentEnvName = DesignerEnvManager.getEnvManager().getCurEnvName();
        GeneralContext.addEnvChangedListener(new EnvChangedListener() {
            @Override
            public void envChanged() {
                currentEnvName = DesignerEnvManager.getEnvManager().getCurEnvName();
            }
        });
    }

    private boolean envListOkAction(EnvListPane envListPane, PopTipStrategy strategy) {
        final String selectedName = envListPane.updateEnvManager();
        return switch2Env(selectedName, strategy);
    }


    /**
     * 切换到指定名称的工作目录
     *
     * @param envName 目标工作目录名称
     */
    public void switch2Env(final String envName) {
        switch2Env(envName, PopTipStrategy.LATER);
    }

    /**
     * 切换到新环境
     *
     * @param envName 新工作环境名称
     * @return 是否成功
     */
    private boolean switch2Env(final String envName, PopTipStrategy strategy) {
        DesignerEnvManager envManager = DesignerEnvManager.getEnvManager();
        DesignerWorkspaceInfo selectedEnv = envManager.getWorkspaceInfo(envName);
        WorkspaceConnectionInfo connectionInfo = selectedEnv.getConnection();

        try {
            Workspace workspace = DesignerWorkspaceGenerator.generate(selectedEnv);
            boolean checkValid = workspace != null && selectedEnv.checkValid();
            if (!checkValid) {
                strategy.showTip(new PopTip() {
                    @Override
                    public void show() {
                        FineJOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), Toolkit.i18nText("Fine-Design_Basic_Switch_Workspace_Failed"),
                                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Error"), ERROR_MESSAGE, UIManager.getIcon("OptionPane.errorIcon"));
                    }
                });
                return false;
            }

            WorkContext.switchTo(workspace, new WorkContextCallback() {
                @Override
                public void done() {
                    DesignerEnvManager.getEnvManager().setCurEnvName(envName);
                    DesignUtils.refreshDesignerFrame();
                    DesignTableDataManager.fireDSChanged(new HashMap<String, String>());
                    if (WorkContext.getCurrent().isLocal()) {
                        //初始化一下serverTray
                        ServerTray.init();
                    }
                }
            });
            //REPORT-13810如果只是添加了工作目录,没有切换,这里ToolArea也是要显示新建的工作目录
            JTemplate template = HistoryTemplateListCache.getInstance().getCurrentEditingTemplate();
            if (template != null) {
                template.refreshToolArea();
            }
            showServiceDialog(selectedEnv);
        } catch (WorkspaceAuthException | RegistEditionException e) {
            // String title = Toolkit.i18nText("Fine-Design_Basic_Remote_Connect_Auth_Failed");
            // String title = Toolkit.i18nText("Fine-Design_Basic_Lic_Does_Not_Support_Remote");
            strategy.showTip(new PopTip() {
                @Override
                public void show() {
                    FineJOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), Toolkit.i18nText("Fine-Design_Basic_Switch_Workspace_Failed"),
                            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Error"), ERROR_MESSAGE, UIManager.getIcon("OptionPane.errorIcon"));
                }
            });
            return false;
        } catch (Exception exception) {
            FineLoggerFactory.getLogger().error(exception.getMessage(), exception);
            strategy.showTip(new PopTip() {
                @Override
                public void show() {
                    FineJOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), Toolkit.i18nText("Fine-Design_Basic_Switch_Workspace_Failed"),
                            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Error"), ERROR_MESSAGE, UIManager.getIcon("OptionPane.errorIcon"));
                }
            });

            return false;
        }
        TemplateTreePane.getInstance().refreshDockingView();
        DesignModelAdapter<?, ?> model = DesignModelAdapter.getCurrentModelAdapter();
        if (model != null) {
            model.envChanged();
        }
        return true;
    }

    /**
     * 这个功能留着，可能会加回来，先做注释处理
     * 切换远程环境之前，进行版本检测，当版本不一致的时候，提示。
     * 当用户确认选择 ok 时，才继续。
     *
     * @param selectedEnv 选择的环境
     * @return 是否一致
     * 1. 非远程环境 ， 返回 true
     * 2. 远程环境
     * 2.1 不匹配，
     * 2.1.1 当选择 ok ， 返回 true
     * 2.1.2 当选择 no, 返回 false
     * 2.2 匹配， 返回 true
     * @throws Exception 异常
     */
    private boolean versionCheckAndConfirm(DesignerWorkspaceInfo selectedEnv) throws Exception {

        if (selectedEnv.getType() == DesignerWorkspaceType.Remote) {

            WorkspaceConnectionInfo info = selectedEnv.getConnection();
            String serverVersion = new FunctionalHttpRequest(info).getServerVersion();

            if (AssistUtils.equals(serverVersion, WorkContext.getVersion())) {
                return true;
            }

            final List<Integer> result = new ArrayList<>(1);
            PopTipStrategy.NOW.showTip(new PopTip() {
                @Override
                public void show() {
                    String[] option = {Toolkit.i18nText("Fine-Design_Report_Yes"), Toolkit.i18nText("Fine-Design_Report_No")};
                    int choice = JOptionPane.showOptionDialog(DesignerContext.getDesignerFrame(), Toolkit.i18nText("Fine-Design_Basic_Remote_Design_Version_Inconsistency"),
                            UIManager.getString("OptionPane.messageDialogTitle"), JOptionPane.YES_NO_OPTION, QUESTION_MESSAGE, UIManager.getIcon("OptionPane.warningIcon"), option, 1);
                    result.add(choice);
                }
            });

            // 只有选择 yes ， 这里的值才为 0， 返回 true
            // 否着返回 false, 将不进行下面的连接操作。
            return result.size() != 0 && result.get(0) == 0;
        }

        return true;
    }

  	/**
     * 对选择的环境做服务检测
     * @param selectedEnv 选择的工作环境
     */
    public void showServiceDialog(DesignerWorkspaceInfo selectedEnv) throws Exception {
        //是否需要做服务校验
        if(needCheckBranch(selectedEnv)) {
            String localBranch;
            String remoteBranch;
            WorkspaceConnectionInfo connectionInfo = selectedEnv.getConnection();
            localBranch = GeneralUtils.readFullBuildNO();
            try {
                remoteBranch = new FunctionalHttpRequest(connectionInfo).getServerBranch();
            } catch (WorkspaceConnectionException e) {
                remoteBranch = Toolkit.i18nText("Fine-Design_Basic_Remote_Design_Branch_Is_Old") + formatBranch(localBranch);
            }
            //通过是否包含#来避免当前版本为非安装版本（主要是内部开发版本）
            if (localBranch.contains("#") && localBranch.equals(remoteBranch)) {
                //说明版本一致，仅做日志记录
                FineLoggerFactory.getLogger().info("Remote Designer version consistency");
            } else {
                localBranch = formatBranch(localBranch);
                remoteBranch = formatBranch(remoteBranch);
                Set<Class> noExistServiceSet = getNoExistServiceSet(connectionInfo);
                StringBuilder textBuilder = new StringBuilder();
                for (Class clazz : noExistServiceSet) {
                    WorkspaceAPI workspaceAPI = (WorkspaceAPI) clazz.getAnnotation(WorkspaceAPI.class);
                    String descriptionOfCN = InterProviderFactory.getProvider().getLocText(workspaceAPI.description());
                    textBuilder.append(descriptionOfCN).append("\n");
                }
                String areaText = textBuilder.toString();
                CheckServiceDialog dialog = new CheckServiceDialog(DesignerContext.getDesignerFrame(), areaText, localBranch, remoteBranch);
                dialog.setVisible(true);
            }
        }
    }

    /**
     * 判断是否需要做版本验证，判断依据为
     * 1、选择的环境为远程环境
     * 2、一个月内不弹出是否勾选
     * @param selectedEnv 选择的环境
     * @return
     */
    private  boolean needCheckBranch(DesignerWorkspaceInfo selectedEnv){
        if(selectedEnv.getType() == DesignerWorkspaceType.Remote){
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();
                //获取记录的时间
                Date remindTime = format.parse(selectedEnv.getRemindTime());
                calendar.setTime(remindTime);
                //获取一个月后的时间
                calendar.add(Calendar.MONTH,1);
                //与当前时间作对比，然后判断是否提示
                if(new Date().after(calendar.getTime())){
                    return true;
                }
            } catch (ParseException e) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取不存在的服务列表
     * @param info 环境连接信息
     * @return 以Set形式返回不存在的服务
     */
    public Set<Class> getNoExistServiceSet(WorkspaceConnectionInfo info){
        Set<Class> noExistServiceSet = new HashSet<Class>();
        Set<Class> remoteServiceSet = new HashSet<Class>();
        Set<Class> localServiceSet = FineObjectPool.getInstance().getServerPool().keySet();

        try {
            JSONArray serviceArray = new FunctionalHttpRequest(info).getServiceList();
            for(int i = 0; i < serviceArray.size(); i++){
                try{
                    Class clazz = Class.forName((String) serviceArray.get(i));
                    remoteServiceSet.add(clazz);
                } catch (Exception e){
                    continue;
                }
            }
            noExistServiceSet.addAll(localServiceSet);
            noExistServiceSet.removeAll(remoteServiceSet);
            return noExistServiceSet;
        } catch (WorkspaceConnectionException e) {
            FineLoggerFactory.getLogger().info(e.getMessage());
            //根据本地的服务列表做逐一检测
            for(Class clazz : localServiceSet) {
                Method testMethod = Reflect.on(Method.class).create(clazz, "connectTest", new Class[0], String.class, new Class[0], 1025, 8, null, null, null, null).get();
                WorkspaceProxyPool proxyPool = (WorkspaceProxyPool) (((RemoteWorkspace) WorkContext.getCurrent()).getClient()).getPool();
                Result result = proxyPool.testInvoker(testMethod);
                Exception invokeException = (Exception) result.getException();
                if(invokeException != null){
                    Exception cause = (Exception) invokeException.getCause();
                    //获取被包装最底层的异常
                    while (cause != null) {
                        invokeException = cause;
                        cause = (Exception) invokeException.getCause();
                    }
                    //该异常表示服务不存在
                    if(invokeException instanceof ClassNotFoundException){
                        noExistServiceSet.add(clazz);
                    }
                }
            }
            return noExistServiceSet;
        } catch (Exception e){
            FineLoggerFactory.getLogger().error(e.getMessage(),e);
            return null;
        }
    }

    /**
     * 格式化分支版本号
     * @param branch 初始的分支版本号
     * @return 格式化后的版本号
     */
    private String formatBranch(String branch){
        if(branch.contains("#")){
            return branch.substring(branch.lastIndexOf("#") + 1, branch.length() - 13);
        }
        return branch;
    }


    /**
     * 编辑items
     *
     * @see EnvChangeEntrance#chooseEnv()
     * @deprecated use {@link EnvChangeEntrance#chooseEnv()}
     */
    @Deprecated
    public void editItems() {
        chooseEnv();
    }

    /**
     * 出现对话框，选择使用的工作环境
     */
    public void chooseEnv() {
        final EnvListPane envListPane = new EnvListPane();
        final BasicDialog envListDialog = envListPane.showWindow(SwingUtilities.getWindowAncestor(DesignerContext.getDesignerFrame()));

        envListPane.populateEnvManager(currentEnvName);
        envListDialog.addDialogActionListener(new DialogActionAdapter() {

            @Override
            public void doOk() {
                envListOkAction(envListPane, PopTipStrategy.LATER);
            }

            @Override
            public void doCancel() {
                envListDialog.dispose();
                // todo 断开了但是没选择新的环境，那么尝试重连旧环境，等接口
            }
        });
        envListDialog.setVisible(true);
    }

    /**
     * 处理异常
     */
    public void dealEvnExceptionWhenStartDesigner() {
        final EnvListPane envListPane = new EnvListPane();
        envListPane.populateEnvManager(currentEnvName);
        BasicDialog envListDialog = envListPane.showWindow(SwingUtilities.getWindowAncestor(DesignerContext.getDesignerFrame()));
        envListDialog.addDialogActionListener(new DialogActionAdapter() {

            @Override
            public void doOk() {
                if (!envListOkAction(envListPane, PopTipStrategy.NOW)) {
                    DesignerExiter.getInstance().execute();
                }
            }

            @Override
            public void doCancel() {
                DesignerExiter.getInstance().execute();            }
        });
        envListDialog.setVisible(true);
    }


    /**
     * 提示显示策略
     */
    enum PopTipStrategy {

        /**
         * 切换失败，就马上提示失败，不关闭选择列表对话框
         */
        NOW {
            @Override
            void showTip(PopTip tip) {
                tip.show();
            }
        },
        /**
         * 切换失败，自动关闭选择列表对话框，然后提示切换失败
         */
        LATER {
            @Override
            void showTip(final PopTip tip) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        tip.show();
                    }
                });
            }
        };

        abstract void showTip(PopTip tip);
    }

    interface PopTip {
        void show();
    }
}
