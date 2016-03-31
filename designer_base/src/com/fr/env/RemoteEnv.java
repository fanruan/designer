package com.fr.env;

import com.fr.base.*;
import com.fr.base.remote.RemoteDeziConstants;
import com.fr.data.core.DataCoreUtils;
import com.fr.data.core.db.TableProcedure;
import com.fr.data.impl.Connection;
import com.fr.data.impl.EmbeddedTableData;
import com.fr.data.impl.storeproc.ProcedureDataModel;
import com.fr.data.impl.storeproc.StoreProcedure;
import com.fr.dav.DavXMLUtils;
import com.fr.dav.UserBaseEnv;
import com.fr.design.DesignerEnvManager;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.dialog.InformationWarnPane;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.fun.DesignerEnvProcessor;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrameFileDealerPane;
import com.fr.design.mainframe.loghandler.DesignerLogHandler;
import com.fr.file.CacheManager;
import com.fr.file.DatasourceManager;
import com.fr.file.DatasourceManagerProvider;
import com.fr.file.filetree.FileNode;
import com.fr.general.*;
import com.fr.general.http.HttpClient;
import com.fr.json.JSONArray;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.plugin.Plugin;
import com.fr.plugin.PluginLoader;
import com.fr.stable.*;
import com.fr.stable.file.XMLFileManagerProvider;
import com.fr.stable.project.ProjectConstants;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLTools;
import com.fr.stable.xml.XMLableReader;
import com.fr.web.ResourceConstants;

import javax.swing.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.logging.Level;
import java.util.regex.Pattern;

public class RemoteEnv implements Env {
    private static final int TIME_OUT = 30 * 1000;
    private static final int PLAIN_SOCKET_PORT = 80;
    private static final int SSL_PORT = 443;
    private static final int MAX_PER_ROUTE = 20;
    private static final int MAX_TOTAL = 100;
    private static final String REMOTE_PLUGIN = "remote_plugin.info";
    private static final String CERT_KEY = "javax.net.ssl.trustStore";
    private static final String PWD_KEY = "javax.net.ssl.trustStorePassword";
    private static final String HTTPS_PREFIX = "https:";
    private final static String[] FILE_TYPE = {"cpt", "frm", "form", "cht", "chart"};
    private String path;
    private String user;
    private String password;
    private Clock clock = null;
    private String userID;
    private Timer timer;
    private int licNotSupport = 0;
    private boolean isRoot = false;
    private Timer logTimer = null;
    private static ThreadLocal<String> threadLocal = new ThreadLocal<String>();
    private boolean isReadTimeOut = false;

    public RemoteEnv() {
        this.clock = new Clock(this);
    }

    public RemoteEnv(String path, String userName, String password) {
        this();
        this.path = path;
        this.user = userName;
        this.password = password;
    }

    /**
     * 返回env配置路径
     */
    @Override
    public String getPath() {
        return this.path;
    }

    public void setPath(String s) {
        this.path = s;
    }

    /**
     * 当前设计环境的用户名，用于远程设计
     */
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
        clearUserID();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        clearUserID();
    }

    public Clock getClock() {
        return this.clock;
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }

    private void clearUserID() {
        this.userID = null;
    }

    public void setThreadLocal(String value) {
        synchronized (this) {
            threadLocal.set(value);
        }

    }

    public String getThreadLocal() {
        return threadLocal.get();
    }

    /**
     * 所有与服务器端交互前,都要调用这个方法生成UserID
     */
    private String createUserID() throws EnvException {
        // 如果登录之后userID还是null
        if (this.userID == null) {
            if (!VT4FR.REMOTE_DESIGN.support() && licNotSupport <= 0) {
                licNotSupport++;
                JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Lic_does_not_Support_Remote"));
            }
            throw new EnvException(Inter.getLocText("Env-Invalid_User_and_Password"));
        }

        return this.userID;
    }

    private HttpClient createHttpMethod(HashMap<String, String> para) throws EnvException, UnsupportedEncodingException {
        return createHttpMethod(para, false);
    }

    /**
     * 根据nameValuePairs,也就是参数对,生成PostMethod
     */
    private HttpClient createHttpMethod(HashMap<String, String> para, boolean isSignIn) throws EnvException, UnsupportedEncodingException {
        String methodPath = this.path;
        if (!isSignIn) {
            methodPath = methodPath + "?id=" + createUserID();
        }
        return new HttpClient(methodPath, para);
    }

    /**
     * 根据nameValuePairs,也就是参数对,生成PostMethod,不同之处在于,参数拼在path后面,不是method.addParameters
     */
    private HttpClient createHttpMethod2(HashMap<String, String> para) throws EnvException {
        StringBuilder sb = new StringBuilder(path);

        sb.append('?');
        sb.append("id=").append(createUserID());

        return new HttpClient(sb.toString(), para, true);
    }


     /*
      * Read the response body.
	  * 拿出InputStream中所有的Byte,转换成ByteArrayInputStream的形式返回
      *
	  * 这样做的目的是确保method.releaseConnection
	  *
	  * TODO 但如果不做method.releaseConnection,有多大危害呢?不确定...
	  */

    /**
     * execute method之后,取返回的inputstream
     */
    private ByteArrayInputStream execute4InputStream(HttpClient client) throws Exception {
        setHttpsParas();
        try {
            int statusCode = client.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                //数据加载太多，屏蔽掉
                //doWithTimeOutException();
                throw new EnvException("Method failed: " + statusCode);
            }
        } catch (Exception e) {
            FRContext.getLogger().info("Connection reset ");
        }
        InputStream in = client.getResponseStream();
        if (in == null) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            Utils.copyBinaryTo(in, out);

            // 看一下传过来的byte[]是不是DesignProcessor.INVALID,如果是的话,就抛Exception
            byte[] bytes = out.toByteArray();
            // carl：格式一致传中文
            String message = new String(bytes, EncodeConstants.ENCODING_UTF_8);
            if (ComparatorUtils.equals(message, RemoteDeziConstants.NO_SUCH_RESOURCE)) {
                return null;
            } else if (ComparatorUtils.equals(message, RemoteDeziConstants.INVALID_USER)) {
                throw new EnvException(RemoteDeziConstants.INVALID_USER);
            } else if (ComparatorUtils.equals(message, RemoteDeziConstants.FILE_LOCKED)) {
                JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Remote_File_is_Locked"));
                return null;
            } else if (message.startsWith(RemoteDeziConstants.RUNTIME_ERROR_PREFIX)) {
            }
            return new ByteArrayInputStream(bytes);
        } finally {
            synchronized (this) {
                in.close();
                out.close();
                client.release();
            }
        }
    }

    private void doWithTimeOutException() {
        boolean isNotNeedTip = ComparatorUtils.equals(getThreadLocal(), "HEART_BEAT") || ComparatorUtils.equals(getThreadLocal(), "LOG_MESSAGE");
        if (!isReadTimeOut && !isNotNeedTip) {
            isReadTimeOut = true;
            JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), Inter.getLocText(new String[]{"Data", "read_time_out"}));
            isReadTimeOut = false;
        }
        FRContext.getLogger().info("Connection reset ");
    }


    /**
     * nameValuePairs,这个参数要接着this.path,拼成一个URL,否则服务器端req.getParameter是无法得到的
     *
     * @param bytes 数据
     * @return 是否成功提交
     * @throws Exception 异常
     */
    private boolean postBytes2Server(byte[] bytes, HashMap<String, String> para) throws Exception {
        HttpClient client = createHttpMethod2(para);
        client.setContent(bytes);
        execute4InputStream(client);

        return true;
    }

    /**
     * 把InputStream转成一段String
     *
     * @param in InputStream输入流
     * @return 转换后的字符串
     */
    public static String stream2String(InputStream in) {
        if (in == null) {
            return null;
        }
        BufferedReader br;
        try {
            br = new BufferedReader(new InputStreamReader(in, EncodeConstants.ENCODING_UTF_8));
        } catch (UnsupportedEncodingException e) {
            br = new BufferedReader(new InputStreamReader(in));
        }
        StringBuilder sb = new StringBuilder();
        String line;

        try {
            while ((line = br.readLine()) != null) {
                if (sb.length() > 0) {
                    sb.append('\n');
                }
                sb.append(line);
            }
        } catch (IOException e) {
            FRContext.getLogger().error(e.getMessage(), e);
        }

        return sb.toString();
    }

    /**
     * 测试连接服务器
     *
     * @return 测试连接成功返回true
     * @throws Exception 异常
     */
    public boolean testServerConnection() throws Exception {
        return testConnection(true, true, DesignerContext.getDesignerFrame());
    }

    /**
     * 测试当前配置是否正确
     *
     * @return 链接是否成功
     * @throws Exception 异常
     */
    public boolean testServerConnectionWithOutShowMessagePane() throws Exception {
        return testConnection(false, true, DesignerContext.getDesignerFrame());
    }

    /**
     * 主要用于在环境配置面板中的测试连接按钮时，不要注册进远程环境
     *
     * @param messageParentPane 弹框的依赖的面板
     * @return 是否测试连接成功
     * @throws Exception 异常
     */
    public boolean testConnectionWithOutRegisteServer(Component messageParentPane) throws Exception {
        return testConnection(true, false, messageParentPane);
    }


    private boolean testConnection(boolean needMessage, boolean isRegisteServer, Component parentComponent) throws Exception {
        extraChangeEnvPara();

        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "test_server_connection");
        para.put("user", user);
        para.put("password", password);

        if (path.startsWith("https") && (!DesignerEnvManager.getEnvManager().isHttps())) {
            return false;
        }

        HttpClient client = createHttpMethod(para, true);

        String res = stream2String(execute4InputStream(client));
        if (res == null) {
            if (needMessage) {
                JOptionPane.showMessageDialog(parentComponent, Inter.getLocText("Datasource-Connection_failed"));
            }
            return false;
        } else if (ComparatorUtils.equals(res, "true")) {
            if (!clock.connected && isRegisteServer) {
                //服务器中断又重新启动之后，重新向远程服务器注册
                register2Server();
            }
            return true;
        } else if (ComparatorUtils.equals(res, "invalid username or password.")) {
            JOptionPane.showMessageDialog(parentComponent,
                    Inter.getLocText(new String[]{"Datasource-Connection_failed", "Registration-User_Name", "Password", "Error"}, new String[]{",", "", "", "!"})
                    , Inter.getLocText("FR-Server-All_Error"), JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (res.indexOf("RegistEditionException") != -1) {
            if (needMessage) {
                JOptionPane.showMessageDialog(parentComponent, Inter.getLocText(new String[]{"Datasource-Connection_failed", "Version-does-not-support"}, new String[]{",", "!"}));
            } else {
                FRLogger.getLogger().info(Inter.getLocText(new String[]{"Datasource-Connection_failed", "Version-does-not-support"}, new String[]{",", "!"}));
            }
            return false;
        } else if (ComparatorUtils.equals(res, "war not support remote design.")) {
            if (needMessage) {
                JOptionPane.showMessageDialog(parentComponent, Inter.getLocText(new String[]{"Datasource-Connection_failed", "NS-war-remote"}, new String[]{",", "!"}));
            } else {
                FRLogger.getLogger().info(Inter.getLocText(new String[]{"Datasource-Connection_failed", "NS-war-remote"}, new String[]{",", "!"}));
            }
            return false;
        } else {
            throw new EnvException(res);
        }
    }

    private void extraChangeEnvPara() {
        //在env连接之前, 加载一下不依赖env的插件. 看看需不需要改变参数.
        PluginLoader.init();
        DesignerEnvProcessor envProcessor = ExtraDesignClassManager.getInstance().getEnvProcessor();
        if (envProcessor != null) {
            this.path = envProcessor.changeEnvPathBeforeConnect(user, password, path);
        }
    }

    private void setHttpsParas() {
        if (path.startsWith(HTTPS_PREFIX) && System.getProperty(CERT_KEY) == null) {
            DesignerEnvManager envManager = DesignerEnvManager.getEnvManager();
            System.setProperty(CERT_KEY, envManager.getCertificatePath());
            System.setProperty(PWD_KEY, envManager.getCertificatePass());
        }
    }

    private void register2Server() {
        try {
            SignIn.signIn(this);
        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage());
        }
    }

    /**
     * 心跳访问，用来更新当前用户的访问时间
     *
     * @throws Exception
     */
    public void heartBeatConnection() throws Exception {
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "heart_beat");
        para.put("user", user);
        para.put("userid", userID);

        HttpClient client = createHttpMethod(para, true);
        execute4InputStream(client);

        //这做法不好, 30秒刷一次, 刷新的时候会重新构建树, 构建完会把子节点都收缩起来, 效果太差.
        //为什么不保存刷新前树的伸缩状态, 因为刷新后的树和刷新前的树的结构未必是一致的.

        //服务器通知客户端更新左上角文件树面板
//        try {
//            if (ComparatorUtils.equals(stream2String(execute4InputStream(method)), "true")) {
//                DesignerFrameFileDealerPane.getInstance().refresh();
//            }
//        } catch (Exception e) {
//            FRLogger.getLogger().error(e.getMessage());
//        }
    }

    /**
     * 返回描述该运行环境的名字
     *
     * @return 描述环境名字的字符串
     */
    public String getEnvDescription() {
        return Inter.getLocText("Env-Remote_Server");
    }

    /**
     * 登录,返回userID
     */
    public void signIn() throws Exception {
        if (clock != null && clock.connected) {
            return;
        }
        String remoteVersion = getDesignerVersion();
        if (StringUtils.isBlank(remoteVersion) || ComparatorUtils.compare(remoteVersion, ProductConstants.DESIGNER_VERSION) < 0) {
            throw new Exception("version not match");
        }
        clearUserID();
        startLogTimer();
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "r_sign_in");
        para.put("user", user);
        para.put("password", password);

        simulaRPC(para, true);

        //neil:调用Clock方法，10秒向服务器发送一个字节，确保没掉线
        if (clock == null) {
            Clock clock = new Clock(this);
            setClock(clock);
        }
        clock.start();

        // 远程登录的心跳访问, 防止设计器强制关闭而没有Logout
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                try {
                    RemoteEnv.this.setThreadLocal("HEART_BEAT");
                    RemoteEnv.this.heartBeatConnection();
                } catch (Exception e) {
                    FRContext.getLogger().error("Server may be disconnected.", e);
                }
            }
        }, RemoteDeziConstants.HEARTBEAT_DELAY, RemoteDeziConstants.HEARTBEAT_DELAY);
    }


    private void startLogTimer() {
        if (logTimer != null) {
            logTimer.cancel();
        }

        logTimer = new Timer();
        logTimer.schedule(new TimerTask() {
            public void run() {
                try {
                    RemoteEnv.this.setThreadLocal("LOG_MESSAGE");
                    FRContext.getCurrentEnv().printLogMessage();
                } catch (Exception e) {
                    FRLogger.getLogger().info(e.getMessage());
                }
            }
        }, 10000, 10000);
    }

    private void stopLogTimer() {
        logTimer.cancel();
        logTimer = null;
    }

    /**
     * 根据userID sign out
     *
     * @return 成功签出返回true
     * @throws Exception
     */
    public boolean signOut() throws Exception {
        if (userID == null) {
            return true;
        }
        stopLogTimer();
        // richer:登出的时候就把定时发送的时钟停掉
        clock.stop();
        // richer:把轮训使用的定时器也去掉
        timer.cancel();

        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "r_sign_out");
        para.put("id", userID);

        return simulaRPC(para, false
        );
    }

    protected boolean simulaRPC(HashMap<String, String> para, boolean isSignIn) throws Exception {
        HttpClient client = createHttpMethod(para, isSignIn);

        // execute method取到input stream再转成String
        String resJSON = null;
        try {
            resJSON = stream2String(execute4InputStream(client));
        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage());
        }

        if (resJSON == null) {
            return false;
        }
        if (resJSON.indexOf("RegistEditionException") != -1) {
            JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Lic_does_not_Support_Remote"));
            return false;
        }
        try {
            JSONObject jo = new JSONObject(resJSON);

            if (isSignIn) {
                if (jo.has("id")) {
                    userID = jo.getString("id");
                }
                if (jo.has("isRoot")) {
                    isRoot = jo.getBoolean("isRoot");
                }

                if (userID != null) {
                    return true;
                }
            } else {
                if (jo.has("res")) {
                    return jo.getBoolean("res");
                }
            }
            String exception = jo.getString("exp");
            if (exception != null) {
                throw new EnvException(exception);
            }
        } catch (JSONException je) {
            // 返回的resJSON不是JSON格式的,那就直接返回resJSON作为userID
            return true;
        }

        return true;
    }

    protected boolean doLockOperation(String[] filePathes, String cmd) throws Exception {
        if (filePathes == null || filePathes.length == 0) {
            return true;
        }

        JSONArray ja = new JSONArray(filePathes);
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", cmd);
        para.put("pathes", ja.toString());

        return simulaRPC(para, false);
    }

    /**
     * 取路径filePath下面文件的lock
     * <p/>
     * 处于同一个原子操作,要么拿到所有的锁,要么一个锁也没有拿到
     */
    public boolean getLock(String[] filePathes) throws Exception {
        return doLockOperation(filePathes, "design_get_lock");
    }

    /**
     * 解锁文件
     *
     * @param filePathes 文件路径
     * @return 成功解锁返回true
     * @throws Exception
     */
    public boolean releaseLock(String[] filePathes) throws Exception {
        return doLockOperation(filePathes, "design_release_lock");
    }

    /**
     * 当前Env下,tplPath目录下是否存在模板
     *
     * @param reportPath 路径
     * @return 是否存在
     */
    @Override
    public boolean isTemplateExist(String reportPath) throws Exception {
        if (reportPath == null) {
            return false;
        }

        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_report_exist");
        para.put("report_path", reportPath);

        HttpClient client = createHttpMethod(para);
        InputStream input = execute4InputStream(client);

        return ComparatorUtils.equals(stream2String(input), "true");
    }

    /**
     * 解锁当前模板，用于远程设计。当远程设计某张模板 时，在解锁之前改模板处于锁定状态
     *
     * @param tplPath 路径
     * @throws Exception
     */
    @Override
    public void unlockTemplate(String tplPath) throws Exception {
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_close_report");
        para.put(RemoteDeziConstants.TEMPLATE_PATH, tplPath);
        HttpClient client = createHttpMethod(para);

        InputStream input = execute4InputStream(client);
        String info = Utils.inputStream2String(input, EncodeConstants.ENCODING_UTF_8);

        FRContext.getLogger().error(info);
    }

    public class Bytes2ServerOutputStream extends OutputStream {
        private ByteArrayOutputStream out = new ByteArrayOutputStream();
        private HashMap<String, String> nameValuePairs;

        public Bytes2ServerOutputStream(HashMap<String, String> nameValuePairs) {
            this.nameValuePairs = nameValuePairs;
        }

        public HashMap<String, String> getNameValuePairs() {
            return nameValuePairs;
        }

        public ByteArrayOutputStream getOut() {
            return out;
        }

        public OutputStream getZipOutputStream() throws Exception {
            return IOUtils.toZipOut(out);
        }

        /**
         * post ro Server 提交到服务器
         *
         * @return 是否提交成功
         */
        public boolean post2Server() {
            try {
                return postBytes2Server(out.toByteArray(), nameValuePairs);
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage(), e);
                return false;
            }
        }

        /**
         * 刷新数出流，并提交
         *
         * @throws IOException
         */
        public void flush() throws IOException {
            super.flush();
            post2Server();
        }

        /**
         * 将指定字节写入输入流数组
         *
         * @param b 写入的字节
         * @throws IOException
         */
        @Override
        public void write(int b) throws IOException {
            out.write(b);

        }
    }

    /**
     * 测试数据连接是否能够正确的连接上
     *
     * @param database 数据连接
     * @return 如果能正确的连接到数据库则返回true
     * @throws Exception 无法正确连接到数据库则抛出此异常
     *                   TODO alex_ENV 个人以为,这里应该是测试所有Connection的连接,所以Connection与TableData接口的关联需要思考
     */
    @Override
    public boolean testConnection(com.fr.data.impl.Connection database) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // 把database写成xml文件到out
        DavXMLUtils.writeXMLFileDatabaseConnection(database, out);

        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_test_con");

        InputStream input = postBytes2ServerB(out.toByteArray(), para);

        if (input == null) {
            return false;
        }

        return Boolean.valueOf(IOUtils.inputStream2String(input, EncodeConstants.ENCODING_UTF_8));
    }

    /**
     * ben:取schema
     */
    @Override
    public String[] getTableSchema(com.fr.data.impl.Connection database) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        DavXMLUtils.writeXMLFileDatabaseConnection(database, out);
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_get_schema");
        InputStream input = postBytes2ServerB(out.toByteArray(), para);
        if (input == null) {
            return null;
        }
        return DavXMLUtils.readXMLFileSchema(input);
    }

    /**
     * b:分别取Table,View,Procedure,实际应用时更有意义
     */
    @Override
    public TableProcedure[] getTableProcedure(com.fr.data.impl.Connection database, String type, String schema) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DavXMLUtils.writeXMLFileDatabaseConnection(database, out);
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_get_tables");
        para.put("__type__", type);
        para.put("__dbschema__", schema);
        InputStream input = postBytes2ServerB(out.toByteArray(), para);
        if (input == null) {
            return new TableProcedure[0];
        }
        return DavXMLUtils.readXMLSQLTables(input);
    }

    public List getProcedures(com.fr.data.impl.Connection datasource, String[] schemas, boolean isOracle, boolean isOracleSysSpace) throws Exception {
        HashMap schemaTableProcedureMap = new HashMap();
        List sqlTableObjs = new ArrayList();
        TableProcedure[] sqlTableObj = null;
        int len = schemas.length;
        if (len > 0) {
            for (int i = 0; i < len; i++) {
                String schema = schemas[i];
                sqlTableObj = this.getTableProcedure(datasource, TableProcedure.PROCEDURE, schema);
                if (sqlTableObj == null) {
                    sqlTableObj = new TableProcedure[0];
                }
                sqlTableObjs.add(sqlTableObj);
                schemaTableProcedureMap.put(schema, sqlTableObj);
            }
        } else {
            sqlTableObj = this.getTableProcedure(datasource, TableProcedure.PROCEDURE, null);
            if (sqlTableObj == null) {
                sqlTableObj = new TableProcedure[0];
            }
            sqlTableObjs.add(sqlTableObj);
            schemaTableProcedureMap.put(null, sqlTableObj);
        }
        DataCoreUtils.putProcedureMap(datasource, schemaTableProcedureMap);
        return sqlTableObjs;
    }

    /**
     * 在当前路径下新建文件夹
     *
     * @param folderPath 文件名
     * @return 成功创建返回true
     * @throws Exception
     */
    @Override
    public boolean createFolder(String folderPath) throws Exception {
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_create_folder");
        para.put("folder_path", folderPath);

        HttpClient client = createHttpMethod(para);
        InputStream input = execute4InputStream(client);

        if (input == null) {
            return false;
        }

        return Boolean.valueOf(IOUtils.inputStream2String(input, EncodeConstants.ENCODING_UTF_8));
    }

    /**
     * 新建一个文件
     *
     * @param filePath ：目标文件相对路径
     * @return 成功新建返回true
     * @throws Exception
     */
    @Override
    public boolean createFile(String filePath) throws Exception {
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_create_file");
        para.put("file_path", filePath);

        HttpClient client = createHttpMethod(para);
        InputStream input = execute4InputStream(client);

        if (input == null) {
            return false;
        }

        return Boolean.valueOf(IOUtils.inputStream2String(input, EncodeConstants.ENCODING_UTF_8));
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath ：目标文件相对路径
     * @return 文件是否存在
     * @throws Exception
     */
    @Override
    public boolean fileExists(String filePath) throws Exception {
        if (filePath == null) {
            return false;
        }

        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_file_exists");
        para.put("file_path", filePath);

        HttpClient client = createHttpMethod(para);
        InputStream input = execute4InputStream(client);

        if (input == null) {
            return false;
        }

        return Boolean.valueOf(IOUtils.inputStream2String(input, EncodeConstants.ENCODING_UTF_8));
    }

    /**
     * 判断文件是否锁住
     *
     * @param filePath 文件路径
     * @return 文件被锁住了，返回true
     * @throws Exception
     */
    public boolean fileLocked(String filePath) throws Exception {
        if (filePath == null) {
            return false;
        }

        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_file_locked");
        para.put("file_path", filePath);

        HttpClient client = createHttpMethod(para);
        InputStream input = execute4InputStream(client);

        if (input == null) {
            return false;
        }

        return Boolean.valueOf(IOUtils.inputStream2String(input, EncodeConstants.ENCODING_UTF_8));
    }


    /**
     * 注册环境，用于检测是否启动定时器，主要用于本地环境来监测远程
     *
     * @param env 用户环境
     */
    public void registerUserEnv(UserBaseEnv env) {
    }

    /**
     * 用于检测用户环境
     * ，启动定时器
     */
    public void startUserCheckTimer() {
    }


    /**
     * 停止定时器
     */
    public void stopUserCheckTimer() {
    }


    /**
     * 删除文件
     *
     * @param filePath 文件地址
     * @return 删除成功返回true
     */
    public boolean deleteFile(String filePath) {
        if (filePath == null) {
            return false;
        }
        try {
            HashMap<String, String> para = new HashMap<String, String>();
            para.put("op", "fr_remote_design");
            para.put("cmd", "delete_file");
            para.put("file_path", filePath);

            HttpClient client = createHttpMethod(para);
            InputStream input = execute4InputStream(client);

            if (input == null) {
                return false;
            }

            return Boolean.valueOf(IOUtils.inputStream2String(input, EncodeConstants.ENCODING_UTF_8));
        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage());
        }
        return false;
    }

    /**
     * 远程设计器设计时，假如开了权限就不可预览了。这边放一个全局的map来开后门
     *
     * @param key   键值
     * @param value 值
     * @return 如果写入成功，返回true
     * @throws Exception
     */
    public boolean writePrivilegeMap(String key, String value) throws Exception {
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "write_privilege_map");
        para.put("current_user", this.user);
        para.put("current_password", this.password);
        para.put("key", key);
        para.put("value", value);

        HttpClient client = createHttpMethod(para); //jim ：加上user，远程设计点击预览时传递用户角色信息
        InputStream input = execute4InputStream(client);

        if (input == null) {
            return false;
        }

        return Boolean.valueOf(IOUtils.inputStream2String(input, EncodeConstants.ENCODING_UTF_8));
    }

    /**
     * DataSource中去除当前角色没有权限访问的数据源
     */
    public void removeNoPrivilegeConnection() {
        DatasourceManagerProvider dm = DatasourceManager.getProviderInstance();

        try {
            HashMap<String, String> para = new HashMap<String, String>();
            para.put("op", "fs_remote_design");
            para.put("cmd", "env_get_role");
            para.put("currentUsername", this.getUser());
            para.put("currentPwd", this.getPassword());

            HttpClient client = createHttpMethod(para);
            InputStream input = execute4InputStream(client);
            JSONArray ja = new JSONArray(stream2String(input));
            ArrayList<String> toBeRemoveTDName = new ArrayList<String>();
            for (int i = 0; i < ja.length(); i++) {
                String toBeRemoveConnName = (String) ((JSONObject) ja.get(i)).get("name");
                dm.removeConnection(toBeRemoveConnName);
                Iterator it = dm.getTableDataNameIterator();
                while (it.hasNext()) {
                    String tdName = (String) it.next();
                    TableData td = dm.getTableData(tdName);
                    td.registerNoPrivilege(toBeRemoveTDName, toBeRemoveConnName, tdName);
                }
            }

            for (int i = 0; i < toBeRemoveTDName.size(); i++) {
                dm.removeTableData(toBeRemoveTDName.get(i));
            }
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage());
        }
    }

    /**
     * 列出WEB-INF目录下指定路径的文件夹与文件
     *
     * @param rootFilePath 指定目录
     * @return WEB-INF目录下指定路径的文件夹与文件
     * @throws Exception
     */
    @Override
    public FileNode[] listFile(String rootFilePath) throws Exception {
        return listFile(rootFilePath, false);
    }

    /**
     * 列出WEB-INF上层目录下指定路径的文件夹与文件
     *
     * @param rootFilePath 指定目录
     * @return WEB-INF上层目录下指定路径的文件夹与文件
     * @throws Exception
     */
    @Override
    public FileNode[] listReportPathFile(String rootFilePath) throws Exception {
        return listFile(rootFilePath, true);
    }

    private FileNode[] listFile(String rootFilePath, boolean isWebReport) throws Exception {
        FileNode[] fileNodes;

        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fs_remote_design");
        para.put("cmd", "design_list_file");
        para.put("file_path", rootFilePath);
        para.put("currentUserName", this.getUser());
        para.put("currentUserId", this.createUserID());
        para.put("isWebReport", isWebReport ? "true" : "false");

        HttpClient client = createHttpMethod(para);
        InputStream input = execute4InputStream(client);

        if (input == null) {
            return new FileNode[0];
        }

        // 远程环境下左侧目录树暂不需要打开xlsx，xls文件
        fileNodes = DavXMLUtils.readXMLFileNodes(input);
        ArrayList<FileNode> al = new ArrayList<FileNode>();
        for (int i = 0; i < fileNodes.length; i++) {
            al.add(fileNodes[i]);
        }

        FileNode[] fileNodes2 = new FileNode[al.size()];
        for (int i = 0; i < al.size(); i++) {
            fileNodes2[i] = al.get(i);
        }

        return fileNodes2;
    }


    /**
     * 列出目标目录下所有cpt文件或文件夹
     *
     * @param rootFilePath 指定目录
     * @return 列出目标目录下所有cpt文件或文件夹
     * @throws Exception
     */
    public FileNode[] listCpt(String rootFilePath) throws Exception {
        return listCpt(rootFilePath, false);
    }

    /**
     * 列出目标目录下所有cpt文件或文件夹
     *
     * @param rootFilePath 指定目录
     * @param recurse      是否递归查找其子目录
     * @return 列出目标目录下所有cpt文件或文件夹
     * @throws Exception
     */
    public FileNode[] listCpt(String rootFilePath, boolean recurse) {
        List<FileNode> fileNodeList = new ArrayList<FileNode>();
        try {
            listAll(rootFilePath, fileNodeList, new String[]{"cpt"}, recurse);
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage(), e);
        }
        return fileNodeList.toArray(new FileNode[fileNodeList.size()]);
    }

    private void listAll(String rootFilePath, List<FileNode> nodeList, String[] fileTypes, boolean recurse) throws Exception {
        FileNode[] fns = listFile(rootFilePath);
        for (FileNode fileNode : fns) {
            if (isAcceptFileType(fileNode, fileTypes)) {
                nodeList.add(fileNode);
            } else if (fileNode.isDirectory()) {
                if (recurse) {
                    listAll(rootFilePath + File.separator + fileNode.getName(), nodeList, fileTypes, true);
                } else {
                    nodeList.add(fileNode);
                }
            }
        }
    }

    private boolean isAcceptFileType(FileNode fileNode, String[] fileTypes) {
        for (String fileType : fileTypes) {
            if (fileNode.isFileType(fileType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取指定数据集的参数
     *
     * @param tableData 数据集
     * @return 数据集的参数
     * @throws Exception 获取参数失败则抛出此异常
     */
    public Parameter[] getTableDataParameters(TableData tableData) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

//        把tableData写成xml文件到out
        DavXMLUtils.writeXMLFileTableData(tableData, out);

        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_td_pars");
        InputStream input = postBytes2ServerB(out.toByteArray(), para);

        if (input == null) {
            return new Parameter[0];
        }
        return DavXMLUtils.readXMLParameters(input);
    }


    /**
     * 获取存储过程中的参数
     *
     * @param storeProcedure 存储过程
     * @return 返回存储过程中的所有参数组成的数组
     * @throws Exception 如果获取参数失败则抛出此异常
     */
    public Parameter[] getStoreProcedureParameters(StoreProcedure storeProcedure) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // 把tableData写成xml文件到out
        DavXMLUtils.writeXMLFileStoreProcedureAndSource(storeProcedure, out);
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_sp_pars");
        InputStream input = postBytes2ServerB(out.toByteArray(), para);

        if (input == null) {
            return new Parameter[0];
        }
        return DavXMLUtils.readXMLParameters(input);
    }

    /**
     * 根据指定的参数生成一个实际可预览的数据集
     *
     * @param tableData    带参数的数据集
     * @param parameterMap 参数键值对
     * @param rowCount     需要获取的行数
     * @return 实际的二维数据集
     * @throws Exception 如果生成数据失败则抛出此异常
     */
    public EmbeddedTableData previewTableData(Object tableData, java.util.Map parameterMap, int rowCount) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // 把tableData写成xml文件到out
        DavXMLUtils.writeXMLFileTableDataAndSource((TableData) tableData, out);

        // 把parameterMap转成JSON格式的字符串
        JSONObject jo = new JSONObject(parameterMap);
        String jsonParameter = jo.toString();
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_preview_td");
        para.put("pars", jsonParameter);
        para.put("rowcount", String.valueOf(rowCount));
        InputStream input = postBytes2ServerB(out.toByteArray(), para);

        if (input == null) {
            return null;
        }

        return (EmbeddedTableData) DavXMLUtils.readXMLTableData(input);
    }

    /**
     * 根据指定的参数生成一个实际可预览的数据集
     *
     * @param tableData    带参数的数据集
     * @param parameterMap 参数键值对
     * @param start        开始
     * @param end          结尾
     * @param cols         列名
     * @param colIdx       列序号
     * @return 实际的二位数据条
     * @throws Exception 异常
     */
    public Object previewTableData(Object tableData, java.util.Map parameterMap, int start, int end, String[] cols, int[] colIdx) throws Exception {
        return previewTableData(tableData, parameterMap, -1);
    }

    /**
     * nameValuePairs,这个参数要接着this.path,拼成一个URL,否则服务器端req.getParameter是无法得到的
     *
     * @param bytes 数据
     * @param para  参数
     * @return 从服务器端得到InputStream
     * @throws Exception 异常
     */
    public InputStream postBytes2ServerB(byte[] bytes, HashMap<String, String> para) throws Exception {
        HttpClient client = createHttpMethod2(para);
        client.setContent(bytes);
        return execute4InputStream(client);
    }

    /**
     * Read XML.<br>
     * The method will be invoked when read data from XML file.<br>
     * May override the method to read the data that you saved.
     */
    public void readXML(XMLableReader reader) {
        if (reader.isChildNode()) {
            String tmpVal;
            if ("DIR".equals(reader.getTagName())) {
                if ((tmpVal = reader.getAttrAsString("path", null)) != null) {
                    this.path = tmpVal;
                }
                if ((tmpVal = reader.getAttrAsString("user", null)) != null) {
                    this.user = tmpVal;
                }
                if ((tmpVal = reader.getAttrAsString("password", null)) != null) {
                    this.password = tmpVal;
                }
            }
        }
    }

    /**
     * Write XML.<br>
     * The method will be invoked when save data to XML file.<br>
     * May override the method to save your own data.
     *
     * @param writer the PrintWriter.
     */
    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG("DIR").attr("path", this.path).attr("user", this.user).attr("password", this.password).end();
    }


    public static class Clock {

        private static final long CONNECT_INTERVAL = 3000L;
        private boolean connected = false;

        private RemoteEnv remoteEnv;

        public Clock(RemoteEnv remoteEnv) {
            this.remoteEnv = remoteEnv;
        }

        /**
         * 开始连接
         */
        public void start() {
            if (connected) {
                return;
            }
            connected = true;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    // richie:连续三次尝试连接都没有响应才判定为丢失连接
                    while (connected) {
                        try {
                            attemptConnect();
                        } catch (Exception ex) {
                            try {
                                attemptConnect();
                            } catch (Exception ee) {
                                try {
                                    attemptConnect();
                                } catch (Exception exc) {
                                    stop();
                                    if (exc instanceof NoRouteToHostException) {
                                        //网络问题导致的连接中断
                                        if (JOptionPane.showConfirmDialog(null, Inter.getLocText("FR-Remote_Connect2Server_Again"), UIManager.getString("OptionPane.titleText"), JOptionPane.YES_NO_OPTION)
                                                == JOptionPane.OK_OPTION) {
                                            //调用重新连接服务器的方法
                                            connectedAgain();
                                        }
                                    } else {
                                        //服务器关闭引起的连接中断
                                        if (JOptionPane.showConfirmDialog(null, Inter.getLocText("FR-Remote_Re_Connect_to_Server"), UIManager.getString("OptionPane.titleText"), JOptionPane.YES_NO_OPTION)
                                                == JOptionPane.OK_OPTION) {
                                            //调用重新连接服务器的方法
                                            connectedAgain();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }).start();
        }

        /**
         * 服务器连接中断后重新连接
         */
        private void connectedAgain() {
            try {
                if (!remoteEnv.testServerConnectionWithOutShowMessagePane()) {
                    JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), Inter.getLocText(new String[]{"Datasource-Connection_failed", "check_communication"},
                            new String[]{",", "!"}));
                    DesignerFrameFileDealerPane.getInstance().refresh();
                    return;
                }
                String remoteVersion = remoteEnv.getDesignerVersion();
                if (StringUtils.isBlank(remoteVersion) || ComparatorUtils.compare(remoteVersion, ProductConstants.DESIGNER_VERSION) < 0) {
                    String infor = Inter.getLocText("FR-Server_Version_Tip");
                    String moreInfo = Inter.getLocText("FR-Server_Version_Tip_MoreInfo");
                    FRLogger.getLogger().log(Level.WARNING, infor);
                    new InformationWarnPane(infor, moreInfo, Inter.getLocText("FR-Designer_Tooltips")).show();
                    return;
                }
                SignIn.signIn(remoteEnv);
                LicUtils.resetBytes();
                HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().refreshToolArea();
            } catch (Exception em) {
                FRContext.getLogger().error(em.getMessage(), em);
            }
        }

        /**
         * 停止连接
         */
        public void stop() {
            connected = false;
        }

        private void attemptConnect() throws Exception {
            Thread.sleep(CONNECT_INTERVAL);
            Pattern pattern = Pattern.compile("[/:]+");
            String[] strs = pattern.split(remoteEnv.path);

            String shost = strs[1];//host,如：192.168.100.195
            int sport = Integer.parseInt(strs[2]);//端口,如：8080

            Socket socket = new Socket(shost, sport);
            //OOBBINLINE：是否支持发送一个字节的TCP紧急数据,false表示服务器不用处理这个数据
            socket.setOOBInline(false);
            socket.sendUrgentData(0xFF);
            socket.close();
        }
    }

    /**
     * 读报表运行环境所需的配置文件,如datasource.xml, config.xml,这些文件都保存在WEB-INF/resources目录下面
     *
     * @param resourceName 配置文件的名字，如datasource.xml
     * @return 输入流
     * @throws Exception
     */
    @Override
    public InputStream readResource(String resourceName) throws Exception {
        return readBean(resourceName, ProjectConstants.RESOURCES_NAME);
    }


    /**
     * 读取路径下的svg文件
     *
     * @param path 制定路径,是基于报表目录下resource文件夹路径
     * @return 读到的文件
     */
    public File[] readPathSvgFiles(String path) {
        String cataloguePath = StableUtils.pathJoin(new String[]{CacheManager.getProviderInstance().getCacheDirectory().getPath(), SvgProvider.SERVER, path});

        //检查缓存文件保存的目录下serversvgs文件夹是否存在 ，先用来暂存服务器读过来的svg文件
        File catalogue = new File(cataloguePath);
        if (!catalogue.exists()) {
            catalogue.mkdirs();
        }

        ArrayList<File> fileArray = new ArrayList<>();
        try {
            HashMap<String, String> para = new HashMap<String, String>();
            para.put("op", "fr_remote_design");
            para.put("cmd", "design_read_svgfile");
            para.put("resourcePath", path);
            para.put("current_uid", this.createUserID());
            para.put("currentUsername", this.getUser());

            HttpClient client = createHttpMethod(para);
            InputStream input = execute4InputStream(client);
            JSONArray ja = new JSONArray(stream2String(input));
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jsonObject = (JSONObject) ja.get(i);
                String svgFileName = (String) jsonObject.get("svgfileName");
                String svgfileContent = (String) jsonObject.get("svgfileContent");
                File file = new File(StableUtils.pathJoin(new String[]{cataloguePath, svgFileName}));
                InputStream in = new ByteArrayInputStream(svgfileContent.getBytes(EncodeConstants.ENCODING_UTF_8));
                FileOutputStream out = new FileOutputStream(file);
                IOUtils.copyBinaryTo(in, out);
                fileArray.add(file);
            }
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage());
        }

        return fileArray.toArray(new File[fileArray.size()]);
    }


    /**
     * 写svg文件
     *
     * @param svgFile svg文件
     * @return 是否写入成功
     * @throws Exception 异常
     */
    public boolean writeSvgFile(SvgProvider svgFile) throws Exception {
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_save_svg");
        para.put("filePath", svgFile.getFilePath());
        para.put("current_uid", this.createUserID());
        para.put("currentUsername", this.getUser());

        // 通过ByteArrayOutputStream将svg写成字节流
        Bytes2ServerOutputStream out = new Bytes2ServerOutputStream(para);
        OutputStreamWriter outWriter = new OutputStreamWriter(out, "UTF-8");
        StreamResult result = new StreamResult(outWriter);

        Source source = new DOMSource(svgFile.getSvgDocument());
        try {
            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            try {
                xformer.transform(source, result);
            } catch (TransformerException ex) {
                FRContext.getLogger().error(ex.getMessage());
            }

        } catch (TransformerConfigurationException ex) {
            FRContext.getLogger().error(ex.getMessage());
            return false;
        }

        try {
            HttpClient client = createHttpMethod2(out.getNameValuePairs());
            client.setContent(out.getOut().toByteArray());
            String res = stream2String(execute4InputStream(client));
            if (StringUtils.isNotEmpty(res)) {
                JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Already_exist") + res);
                return false;
            }
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * 写报表运行环境所需的配置文件
     *
     * @param mgr 管理各个资源文件的管理器
     * @return 写入xml成功返回true
     * @throws Exception 写入xml错误则抛出此异常
     */
    @Override
    public boolean writeResource(XMLFileManagerProvider mgr) throws Exception {
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_save_resource");
        para.put("resource", mgr.fileName());
        para.put("current_uid", this.createUserID());
        para.put("currentUsername", this.getUser());

        // alex:通过ByteArrayOutputStream将mgr写成字节流
        Bytes2ServerOutputStream out = new Bytes2ServerOutputStream(para);
        XMLTools.writeOutputStreamXML(mgr, out);

        try {
            HttpClient client = createHttpMethod2(out.getNameValuePairs());
            client.setContent(out.getOut().toByteArray());
            String res = stream2String(execute4InputStream(client));
            if (StringUtils.isNotEmpty(res)) {
                JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Already_exist") + res);
                return false;
            }
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * 读取文件
     *
     * @param beanPath 文件名
     * @param prefix   当前Env下得工程分类，如reportlets，lib等
     * @return InputStream  输入流
     */
    public InputStream readBean(String beanPath, String prefix)
            throws Exception {
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fs_remote_design");
        para.put("cmd", "design_open");
        para.put(RemoteDeziConstants.PREFXI, prefix);
        para.put("resource", beanPath);

        HttpClient client = createHttpMethod(para);
        //        return Utils.toZipIn(execute4InputStream(method));
        //Utils.toZipIn这边有bug，远程连接的时候datasource.xml不能读取，先还原了
        return execute4InputStream(client);
    }

    /**
     * 写文件
     *
     * @param beanPath 文件名
     * @param prefix   当前Env下得工程分类，如reportlets，lib等
     * @return OutputStream  输出流
     */
    public OutputStream writeBean(String beanPath, String prefix)
            throws Exception {
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fs_remote_design");
        para.put("cmd", "design_save_report");
        para.put(RemoteDeziConstants.PREFXI, prefix);
        para.put(RemoteDeziConstants.TEMPLATE_PATH, beanPath);

        return new Bytes2ServerOutputStream(para);
    }

    /**
     * 返回数据库表的列名
     *
     * @param selectedName 所选择数据库名
     * @param schema       数据库模式，用于存储过程
     * @param tableName    所选择数据库名
     */
    @Override
    public String[] getColumns(String selectedName, String schema, String tableName) throws Exception {
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_columns");
        para.put("dsName", selectedName);
        para.put("schema", schema);
        para.put("tableName", tableName);

        HttpClient client = createHttpMethod2(para);
        InputStream input = execute4InputStream(client);

        if (input == null) {
            return null;
        }

        String colums = stream2String(input);
        if (StringUtils.isEmpty(colums)) {
            return null;
        }
        return colums.split("\\.");
    }

    /**
     * 返回模板文件路径
     */
    @Override
    public String getWebReportPath() {
        return getPath().substring(0, getPath().lastIndexOf("/"));
    }

    @Override
    public String getProcedureText(String connectionName, String databaseName) throws Exception {
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_get_procedure_text");
        para.put("procedure_name", databaseName);
        para.put("connectionName", connectionName);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream input = postBytes2ServerB(out.toByteArray(), para);
        if (input == null) {
            return StringUtils.EMPTY;
        }
        return DavXMLUtils.readXMLProcedureText(input);
    }

    @Override
    public StoreProcedureParameter[] getStoreProcedureDeclarationParameters(String connectionName, String databaseName, String parameterDefaultValue) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_get_sp_parameters");
        para.put("__name__", databaseName);
        para.put("__default_value__", parameterDefaultValue);
        para.put("connectionName", connectionName);

        InputStream input = postBytes2ServerB(out.toByteArray(), para);
        if (input == null) {
            return new StoreProcedureParameter[0];
        }
        return DavXMLUtils.readXMLStoreProcedureParameters(input);
    }

    /**
     * 获取datasource.xml文件的修改表
     */
    public ModifiedTable getDataSourceModifiedTables(String type) {
        try {
            HashMap<String, String> para = new HashMap<String, String>();
            para.put("op", "fr_remote_design");
            para.put("cmd", "get_datasource_modified_tables");
            para.put("type", type);

            HttpClient client = createHttpMethod(para);
            InputStream input = execute4InputStream(client);
            if (input == null) {
                return new ModifiedTable();
            }
            return DavXMLUtils.readXMLModifiedTables(input);
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage());
        }
        return new ModifiedTable();
    }


    /**
     * 写修改表
     *
     * @param modifiedTable 修改表
     * @param type          操作类型，是数据连接还是服务器数据集
     * @return 写入成功返回true
     */
    public boolean writeDataSourceModifiedTables(ModifiedTable modifiedTable, String type) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // 把tableData写成xml文件到out
        DavXMLUtils.writeXMLModifiedTables(modifiedTable, out);
        try {
            HashMap<String, String> para = new HashMap<String, String>();
            para.put("op", "fr_remote_design");
            para.put("cmd", "update_modifytable_to_server");
            para.put("type", type);

            InputStream input = postBytes2ServerB(out.toByteArray(), para);

            if (input == null) {
                return false;
            }

            return Boolean.valueOf(IOUtils.inputStream2String(input, EncodeConstants.ENCODING_UTF_8));
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage());
        }
        return false;
    }

    public String[] getProcedureColumns(StoreProcedure storeProcedure, java.util.Map parameterMap) throws Exception {
        String[] columns;
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "list_sp");
        HttpClient client = createHttpMethod(para);
        try {
            InputStream input = execute4InputStream(client);

            if (input == null) {
                return ArrayUtils.EMPTY_STRING_ARRAY;
            }

            columns = DavXMLUtils.readXMLSPColumns(input);
            return columns;
        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage());
        }

        return new String[0];
    }

    ;

    public String[] getProcedureColumns(String name) throws Exception {
        String[] columns;
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "list_sp_columns_name");
        para.put("name", name);
        HttpClient client = createHttpMethod(para);
        try {
            InputStream input = execute4InputStream(client);
            if (input == null) {
                return ArrayUtils.EMPTY_STRING_ARRAY;
            }
            columns = DavXMLUtils.readXMLSPColumns(input);
            return columns;
        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage());
        }
        return new String[0];

    }

    /**
     * 输出日志信息
     *
     * @throws Exception
     */
    public void printLogMessage() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "get_log_message");

        InputStream input = postBytes2ServerB(out.toByteArray(), para);
        if (input == null) {
            return;
        }
        LogRecordTime[] records = DavXMLUtils.readXMLLogRecords(input);
        for (LogRecordTime logRecordTime : records) {
            DesignerLogHandler.getInstance().printRemoteLog(logRecordTime);

        }
    }

    public String getUserID() {
        return userID;
    }


    //TODO:

    /**
     * 预览存储过程
     *
     * @param storeProcedure 存储过程
     * @param parameterMap   参数map
     * @param rowCount       行数
     * @return 返回取到的存储过程
     */
    @Override
    public ProcedureDataModel[] previewProcedureDataModel(StoreProcedure storeProcedure, Map parameterMap, int rowCount) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // 把tableData写成xml文件到out
        DavXMLUtils.writeXMLFileStoreProcedureAndSource(storeProcedure, out);

        // 把parameterMap转成JSON格式的字符串
        JSONObject jo = new JSONObject(parameterMap);
        String jsonParameter = jo.toString();

        try {
            HashMap<String, String> para = new HashMap<String, String>();
            para.put("op", "fr_remote_design");
            para.put("cmd", "list_sp");
            para.put("pars", jsonParameter);

            InputStream input = postBytes2ServerB(out.toByteArray(), para);
            if (input == null) {
                return null;
            }

            TableData[] tableDatas = DavXMLUtils.readXMLTableDataArray(input);
            if (tableDatas == null || tableDatas.length == 0) {
                return new ProcedureDataModel[0];
            }
            ProcedureDataModel[] procedureDataModels = new ProcedureDataModel[tableDatas.length];
            for (int i = 0; i < tableDatas.length; i++) {
                if (tableDatas[i] instanceof EmbeddedTableData) {
                    procedureDataModels[i] = ((EmbeddedTableData) tableDatas[i]).trans2ProcedureDataModel();
                }
            }
            return procedureDataModels;


        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage());
        }
        return new ProcedureDataModel[0];
    }


    public String getAppName() {
        return "WebReport";
    }

    /**
     * 是否为Oracle数据连接
     *
     * @param database 数据连接
     * @return 是返回true
     * @throws Exception
     */
    public boolean isOracle(Connection database) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DavXMLUtils.writeXMLFileDatabaseConnection(database, out);
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_get_isOracle");
        InputStream input = postBytes2ServerB(out.toByteArray(), para);
        if (input == null) {
            return true;
        }
        return DavXMLUtils.readXMLBoolean(input);
    }

    public String[] getSupportedTypes() {
        return FILE_TYPE;
    }

    /**
     * 在模板面板中是否支持增加打开所在文件夹、重命名、删除三个工具栏选项
     *
     * @return 不支持返回false
     */
    public boolean isSupportLocalFileOperate() {
        return false;
    }

    /**
     * 判断是否有文件夹权限
     *
     * @param path 路径
     * @return 有权限则返回true
     */
    public boolean hasFileFolderAllow(String path) {
        HttpClient client = null;
        try {
            HashMap<String, String> para = new HashMap<String, String>();
            para.put("op", "fs_remote_design");
            para.put("cmd", "design_filefolder_allow");
            para.put("current_uid", this.createUserID());
            para.put(RemoteDeziConstants.TEMPLATE_PATH, path);

            client = createHttpMethod(para);
            InputStream input = execute4InputStream(client);

            if (input == null) {
                return false;
            }
            return Boolean.valueOf(IOUtils.inputStream2String(input, EncodeConstants.ENCODING_UTF_8));
        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage());
            return false;
        }

    }

    /**
     * 是否是管理员身份
     *
     * @return 是则返回true
     */
    public boolean isRoot() {
        return isRoot;
    }

    /**
     * 是否为压缩包部署
     *
     * @return 是则返回true
     */
    @Override
    public boolean isPackDeploy() {
        return false;
    }

    @Override
    public String getDesignerVersion() throws Exception {
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_get_designer_version");
        para.put("user", user);
        para.put("password", password);

        HttpClient client = createHttpMethod(para, true);
        try {
            return stream2String(execute4InputStream(client));
        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage());
        }
        return null;
    }

    public InputStream getDataSourceInputStream(String filePath) throws Exception {
        return readBean(filePath, "datasource");
    }


    @Override
    public ArrayList getAllRole4Privilege(boolean isFS) {
        ArrayList allRoleList = new ArrayList();
        try {
            HashMap<String, String> para = new HashMap<String, String>();
            para.put("op", "fr_remote_design");
            para.put("cmd", "get_all_role");
            para.put("isFS", String.valueOf(isFS));

            HttpClient client = createHttpMethod(para);
            InputStream input = execute4InputStream(client);
            JSONArray ja = new JSONArray(stream2String(input));
            for (int i = 0; i < ja.length(); i++) {
                String roleName = (String) ((JSONObject) ja.get(i)).get("name");
                allRoleList.add(roleName);
            }
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage());
        }
        return allRoleList;
    }

    @Override
    public String getLicName() {
        return LicUtils.FILE_NAME;
    }

    @Override
    public void setLicName(String licName) {
        //do nth
    }

    /**
     * 获取当前env的build文件路径
     */
    public String getBuildFilePath() {
        return ResourceConstants.BUILD_PATH;
    }

    /**
     * 设置当前env的build文件路径
     */
    public void setBuildFilePath(String buildFilePath) {
    }

    /**
     * 编译Java源代码，方便二次开发的进行
     *
     * @param sourceText 源代码
     * @return 编译信息，有可能是成功信息，也有可能是出错或者警告信息
     */
    public JavaCompileInfo compilerSourceCode(String sourceText) throws Exception {
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_compile_source_code");
        InputStream in = postBytes2ServerB(sourceText.getBytes(EncodeConstants.ENCODING_UTF_8), para);
        BufferedReader br = new BufferedReader(new InputStreamReader(in, EncodeConstants.ENCODING_UTF_8));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        JSONObject jo = new JSONObject(sb.toString());
        JavaCompileInfo info = new JavaCompileInfo();
        info.parseJSON(jo);
        return info;
    }

    /**
     * 将文件拷贝到插件目录
     *
     * @param dir    要拷贝的文件
     * @param plugin 插件
     */
    public void copyFilesToPluginAndLibFolder(File dir, Plugin plugin) throws Exception {

    }

    /**
     * 将文件添加到指定目录或者删除指定目录的文件
     *
     * @param file   解压插件的临时目录
     * @param plugin 当前处理的插件
     */
    public void movePluginEmbFile(File file, Plugin plugin) throws Exception {

    }

    /**
     * 将文件从插件目录删除
     *
     * @param plugin 要删除插件
     * @return 同上
     */
    public String[] deleteFileFromPluginAndLibFolder(Plugin plugin) {
        return new String[0];
    }

    /**
     * 保存插件的配置文件
     *
     * @param plugin 插件
     */
    public void writePlugin(Plugin plugin) throws Exception {

    }

    public InputStream readPluginConfig() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_get_plugin_info");

        return postBytes2ServerB(out.toByteArray(), para);
    }

    /**
     * 远程设计先不需要检测MD5
     *
     * @return 是否正确
     * @throws Exception MD5算法异常
     */
    @Override
    public boolean isTruePluginMD5(Plugin plugin, File file) throws Exception {
        return true;
    }


}