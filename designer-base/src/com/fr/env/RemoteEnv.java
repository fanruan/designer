package com.fr.env;

import com.fr.base.AbstractEnv;
import com.fr.base.EnvException;
import com.fr.base.FRContext;
import com.fr.base.TableData;
import com.fr.base.Utils;
import com.fr.base.env.EnvContext;
import com.fr.base.env.resource.RemoteEnvConfig;
import com.fr.base.remote.RemoteDeziConstants;
import com.fr.core.env.EnvConstants;
import com.fr.data.TableDataSource;
import com.fr.data.impl.EmbeddedTableData;
import com.fr.dav.DavXMLUtils;
import com.fr.design.DesignerEnvManager;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.fun.DesignerEnvProcessor;
import com.fr.design.mainframe.DesignerContext;
import com.fr.file.CacheManager;
import com.fr.file.ConnectionConfig;
import com.fr.file.TableDataConfig;
import com.fr.general.ComparatorUtils;
import com.fr.general.IOUtils;
import com.fr.general.Inter;
import com.fr.general.http.HttpClient;
import com.fr.general.http.HttpToolbox;
import com.fr.io.utils.ResourceIOUtils;
import com.fr.json.JSONArray;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.log.FineLoggerFactory;
import com.fr.report.DesignAuthority;
import com.fr.share.ShareConstants;
import com.fr.stable.EncodeConstants;
import com.fr.stable.Filter;
import com.fr.stable.JavaCompileInfo;
import com.fr.stable.ProductConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.SvgProvider;
import com.fr.stable.file.XMLFileManagerProvider;
import com.fr.stable.project.ProjectConstants;
import com.fr.stable.xml.XMLTools;
import com.fr.third.guava.collect.ImmutableMap;
import com.fr.web.ResourceConstants;

import javax.swing.JOptionPane;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.fr.third.guava.base.Preconditions.checkArgument;

/**
 * @author null
 */
public class RemoteEnv extends AbstractEnv implements DesignAuthorityConfigurable {

    private static final String CERT_KEY = "javax.net.ssl.trustStore";
    private static final String PWD_KEY = "javax.net.ssl.trustStorePassword";
    private static final String HTTPS_PREFIX = "https:";
    private final static String[] FILE_TYPE = {"cptx", "cpt", "frm", "form", "cht", "chart"};
    private String buildFilePath;
    private RemoteEnvConfig env;


    public RemoteEnv(String path, String userName, String password) {
        env = new RemoteEnvConfig(path, userName, password);
    }

    /**
     * 返回env配置路径
     */
    @Override
    public String getPath() {
        return env.getPath();
    }

    @Override
    public String getUser() {
        return env.getUsername();
    }


    public String getPassword() {
        return env.getPassword();
    }

    /**
     * 所有与服务器端交互前,都要调用这个方法生成UserID
     */
    private String createUserID() {
        return EnvContext.currentToken();
    }

    private HttpClient createHttpMethod(HashMap<String, String> para) throws EnvException {
        return createHttpMethod(para, false);
    }

    /**
     * 根据nameValuePairs,也就是参数对,生成PostMethod
     */
    private HttpClient createHttpMethod(HashMap<String, String> para, boolean isSignIn) throws EnvException {
        String methodPath = getPath();
        if (!isSignIn) {
            methodPath = methodPath + "?id=" + createUserID();
        }
        HttpClient client = new HttpClient(methodPath, para);
         /*
         todo post 方法好象过去不了
         但是get方法也会有一些url参数问题，尤其是图表部分
         比如:
         op=fr_remote_design&cmd=design_get_plugin_service_data&serviceID=plugin.phantomjs&req=
         */
        client.asGet();
        return client;
    }

    /**
     * 根据nameValuePairs,也就是参数对,生成PostMethod,不同之处在于,参数拼在path后面,不是method.addParameters
     */
    private HttpClient createHttpMethod2(HashMap<String, String> para) throws EnvException, UnsupportedEncodingException {
        String methodPath = getPath() + '?' + "id=" + createUserID();
        return new HttpClient(methodPath);
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
    @Override
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
        checkArgument(parentComponent instanceof Component, "parentComponent should be a java.awt.component");
        Component component = parentComponent;
        String url = String.format("%s/connection", EnvConstants.toDecisionPath(getPath()));
        ImmutableMap<String, Object> params = ImmutableMap.of(
                "version", (Object) ProductConstants.DESIGNER_VERSION
        );
        ImmutableMap<String, String> headers = ImmutableMap.of(
                EnvConstants.USERNAME, getUser(),
                EnvConstants.PWD, getPassword());
        String res = HttpToolbox.post(url, params, headers);
        if (res == null) {
            if (needMessage) {
                JOptionPane.showMessageDialog(component, Inter.getLocText("Datasource-Connection_failed"));
            }
            return false;
        } else if (ComparatorUtils.equals(res, "true")) {
            return true;
        } else {
            if (ComparatorUtils.equals(res, EnvConstants.AUTH_ERROR)) {
                JOptionPane.showMessageDialog(component,
                        Inter.getLocText(new String[]{"Datasource-Connection_failed", "Registration-User_Name", "Password", "Error"}, new String[]{",", "", "", "!"})
                        , Inter.getLocText("FR-Server-All_Error"), JOptionPane.ERROR_MESSAGE);
                return false;
            } else {
                if (ComparatorUtils.equals(res, EnvConstants.WAR_ERROR)) {
                    if (needMessage) {
                        JOptionPane.showMessageDialog(component, Inter.getLocText(new String[]{"Datasource-Connection_failed", "NS-war-remote"}, new String[]{",", "!"}));
                    } else {
                        FineLoggerFactory.getLogger().info(Inter.getLocText(new String[]{"Datasource-Connection_failed", "NS-war-remote"}, new String[]{",", "!"}));
                    }
                    return false;
                } else {
                    if (needMessage) {
                        JOptionPane.showMessageDialog(component, Inter.getLocText(new String[]{"Datasource-Connection_failed", "Version-does-not-support"}, new String[]{",", "!"}));
                    } else {
                        FineLoggerFactory.getLogger().info(Inter.getLocText(new String[]{"Datasource-Connection_failed", "Version-does-not-support"}, new String[]{",", "!"}));
                    }
                    return false;
                }
            }
        }
    }



    private void setHttpsParas() {
        if (getPath().startsWith(HTTPS_PREFIX) && System.getProperty(CERT_KEY) == null) {
            DesignerEnvManager envManager = DesignerEnvManager.getEnvManager();
            System.setProperty(CERT_KEY, envManager.getCertificatePath());
            System.setProperty(PWD_KEY, envManager.getCertificatePass());
        }
    }




    /**
     * 返回描述该运行环境的名字
     *
     * @return 描述环境名字的字符串
     */
    @Override
    public String getEnvDescription() {
        return Inter.getLocText("Env-Remote_Server");
    }

    /**
     * 登录,返回userID
     */
    @Override
    public void signIn() throws Exception {
        throw new UnsupportedOperationException("unsupport now");
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
         * @throws IOException e
         */
        @Override
        public void flush() throws IOException {
            super.flush();
            post2Server();
        }

        /**
         * 将指定字节写入输入流数组
         *
         * @param b 写入的字节
         */
        @Override
        public void write(int b) {
            out.write(b);

        }
    }


    @Override
    public boolean updateAuthorities(DesignAuthority[] authorities) {
        return RemoteEnvUtils.updateAuthorities(authorities, this);
    }

    @Override
    public DesignAuthority[] getAuthorities() {

        return RemoteEnvUtils.getAuthorities(this);

    }


    /**
     * 远程设计器设计时，假如开了权限就不可预览了。这边放一个全局的map来开后门
     *
     * @param key   键值
     * @param value 值
     * @return 如果写入成功，返回true
     * @throws Exception e
     */
    public boolean writePrivilegeMap(String key, String value) throws Exception {
        HashMap<String, String> para = new HashMap<>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "write_privilege_map");
        para.put("current_user", getUser());
        para.put("current_password", getPassword());
        para.put("key", key);
        para.put("value", value);

        //jim ：加上user，远程设计点击预览时传递用户角色信息
        HttpClient client = createHttpMethod(para);
        InputStream input = execute4InputStream(client);

        if (input == null) {
            return false;
        }

        return Boolean.valueOf(IOUtils.inputStream2String(input, EncodeConstants.ENCODING_UTF_8));
    }

    /**
     * DataSource中去除当前角色没有权限访问的数据源
     */
    @Override
    public void removeNoPrivilegeConnection() {
        TableDataConfig dm = TableDataConfig.getInstance();

        try {
            HashMap<String, String> para = new HashMap<>();
            para.put("op", "fs_remote_design");
            para.put("cmd", "env_get_role");
            para.put("currentUsername", this.getUser());
            para.put("currentPwd", this.getPassword());

            HttpClient client = createHttpMethod(para);
            InputStream input = execute4InputStream(client);
            JSONArray ja = new JSONArray(stream2String(input));
            ArrayList<String> toBeRemoveTDName = new ArrayList<>();
            for (int i = 0; i < ja.length(); i++) {
                String toBeRemoveConnName = (String) ((JSONObject) ja.get(i)).get("name");
                ConnectionConfig.getInstance().removeConnection(toBeRemoveConnName);
                Iterator it = dm.getTableDatas().keySet().iterator();
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


    @Override
    public EmbeddedTableData previewTableData(Object tableData, Map parameterMap, int rowCount) throws Exception {
        return previewTableData(null, tableData, parameterMap, rowCount);
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
    @Override
    public EmbeddedTableData previewTableData(TableDataSource dataSource, Object tableData, java.util.Map parameterMap, int rowCount) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // 把tableData写成xml文件到out
        DavXMLUtils.writeXMLFileTableDataAndSource((TableData) tableData, out);

        // 把parameterMap转成JSON格式的字符串
        JSONObject jo = new JSONObject(parameterMap);
        String jsonParameter = jo.toString();
        HashMap<String, String> para = new HashMap<>();
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
    @Override
    public Object previewTableData(Object tableData, java.util.Map parameterMap, int start, int end, String[] cols, int[] colIdx) throws Exception {
        return previewTableData(tableData, parameterMap, -1);
    }

    @Override
    public Object previewTableData(TableDataSource dataSource, Object tableData, Map parameterMap, int start, int end, String[] cols, int[] colIdx) throws Exception {
        return previewTableData(dataSource, tableData, parameterMap, -1);
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
        /*
         todo post 方法好象过去不了
         但是get方法也会有一些url参数问题，尤其是图表部分
         比如:
         op=fr_remote_design&cmd=design_get_plugin_service_data&serviceID=plugin.phantomjs&req=
         */
//        client.asGet();
        client.setContent(bytes);
        return execute4InputStream(client);
    }


    /**
     * 读取路径下的svg文件
     *
     * @param path 制定路径,是基于报表目录下resource文件夹路径
     * @return 读到的文件
     */
    @Override
    public String[] readPathSvgFiles(String path) {
        String cataloguePath = StableUtils.pathJoin(CacheManager.getProviderInstance().getCacheDirectory().getPath(), SvgProvider.SERVER, path);


        ArrayList<String> fileArray = new ArrayList<>();
        try {
            HashMap<String, String> para = new HashMap<>();
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

                String file = StableUtils.pathJoin(cataloguePath, svgFileName);
                InputStream in = new ByteArrayInputStream(svgfileContent.getBytes(EncodeConstants.ENCODING_UTF_8));
                ResourceIOUtils.write(file, in);
                fileArray.add(file);
            }
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage());
        }

        return fileArray.toArray(new String[fileArray.size()]);
    }


    /**
     * 写svg文件
     *
     * @param svgFile svg文件
     * @return 是否写入成功
     * @throws Exception 异常
     */
    @Override
    public boolean writeSvgFile(SvgProvider svgFile) throws Exception {
        testServerConnection();

        HashMap<String, String> para = new HashMap<>();
        para.put("op", "svgrelate");
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
        testServerConnection();

        HashMap<String, String> para = new HashMap<>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_save_resource");
        para.put("resource", mgr.fileName());
        para.put("class_name", mgr.getClass().getName());
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
    @Override
    public InputStream readBean(String beanPath, String prefix)
            throws Exception {
        HashMap<String, String> para = new HashMap<>();
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
    @Override
    public OutputStream writeBean(String beanPath, String prefix) {
        HashMap<String, String> para = new HashMap<>();
        para.put("op", "fs_remote_design");
        para.put("cmd", "design_save_report");
        para.put(RemoteDeziConstants.PREFXI, prefix);
        para.put(RemoteDeziConstants.TEMPLATE_PATH, beanPath);

        return new Bytes2ServerOutputStream(para);
    }


    /**
     * 返回模板文件路径
     */
    @Override
    public String getWebReportPath() {
        return getPath().substring(0, getPath().lastIndexOf("/"));
    }

    /**
     * 输出日志信息
     *
     * @throws Exception
     */
    @Override
    public void printLogMessage() throws Exception {
        throw new UnsupportedOperationException("unsupport now");
    }

    @Override
    public String getUserID() {
        return EnvContext.currentToken();
    }


    @Override
    public String[] getSupportedTypes() {
        return FILE_TYPE;
    }


    /**
     * 判断是否有文件夹权限
     *
     * @param path 路径
     * @return 有权限则返回true
     */
    @Override
    public boolean hasFileFolderAllow(String path) {
        HttpClient client = null;
        try {
            HashMap<String, String> para = new HashMap<>();
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
            FRContext.getLogger().error(e.getMessage());
            return false;
        }

    }


    @Override
    public InputStream getDataSourceInputStream(String filePath) throws Exception {
        return readBean(filePath, "datasource");
    }


    @Override
    public ArrayList getAllRole4Privilege(boolean isFS) {
        ArrayList allRoleList = new ArrayList();
        try {
            HashMap<String, String> para = new HashMap<>();
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


    /**
     * 获取当前env的build文件路径
     */
    @Override
    public String getBuildFilePath() {
        return StringUtils.isEmpty(buildFilePath) ? ResourceConstants.BUILD_PATH : buildFilePath;
    }

    /**
     * 设置当前env的build文件路径
     */
    @Override
    public void setBuildFilePath(String buildFilePath) {
        this.buildFilePath = buildFilePath;
    }

    /**
     * 编译Java源代码，方便二次开发的进行
     *
     * @param sourceText 源代码
     * @return 编译信息，有可能是成功信息，也有可能是出错或者警告信息
     */
    @Override
    public JavaCompileInfo compilerSourceCode(String sourceText) throws Exception {
        HashMap<String, String> para = new HashMap<>();
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


    @Override
    public String pluginServiceAction(String serviceID, String req) throws Exception {
        HashMap<String, String> para = new HashMap<>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_get_plugin_service_data");
        para.put("serviceID", serviceID);
        para.put("req", req);
        //jim ：加上user，远程设计点击预览时传递用户角色信息
        HttpClient client = createHttpMethod(para);
        InputStream inputStream = execute4InputStream(client);
        return IOUtils.inputStream2String(inputStream);
    }

    /**
     * 远程不启动，使用虚拟服务
     * <p>
     *
     * @param serviceID serviceID
     */
    @Override
    public void pluginServiceStart(String serviceID) {
    }

    @Override
    public String[] loadREUFile() {
        ResourceIOUtils.delete(StableUtils.pathJoin(
                CacheManager.getProviderInstance().getCacheDirectory().getAbsolutePath(),
                ShareConstants.DIR_SHARE_CACHE));

        String zipFilePath = null;
        try {
            HashMap<String, String> para = new HashMap<>();
            para.put("op", "fr_remote_design");
            para.put("cmd", "design_read_reufile");
            para.put("current_uid", this.createUserID());
            para.put("currentUsername", this.getUser());

            HttpClient client = createHttpMethod(para);
            //拿到服务端传过来的整个共享文件夹的压缩文件的文件流
            InputStream input = client.getResponseStream();

            zipFilePath = StableUtils.pathJoin(CacheManager.getProviderInstance().getCacheDirectory().getAbsolutePath(), "share.zip");
            String cacheDir = StableUtils.pathJoin(CacheManager.getProviderInstance().getCacheDirectory().getAbsolutePath(), ShareConstants.DIR_SHARE_CACHE);

            ResourceIOUtils.write(zipFilePath, input);
            ResourceIOUtils.unzip(zipFilePath, cacheDir, EncodeConstants.ENCODING_GBK);


            return ResourceIOUtils.listWithFullPath(cacheDir, new Filter<String>() {
                @Override
                public boolean accept(String s) {
                    return s.endsWith(ProjectConstants.REU);
                }
            });

        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage());
        } finally {

            if (zipFilePath != null) {
                ResourceIOUtils.delete(zipFilePath);
            }
        }

        return new String[0];
    }

    @Override
    public boolean installREUFile(File reuFile) {
        if (reuFile == null) {
            return false;
        }
        File tempFile = new File(CacheManager.getProviderInstance().getCacheDirectory(), "temp_remote");
        IOUtils.unzip(reuFile, tempFile.getAbsolutePath());
        String shareXMLName = StableUtils.pathJoin(tempFile.getAbsolutePath(), ShareConstants.NAME_XML_MODULE);
        String helpXMLName = StableUtils.pathJoin(tempFile.getAbsolutePath(), ShareConstants.NAME_XML_HELP);
        try {
            HashMap<String, String> para = new HashMap<>();
            para.put("op", "fr_remote_design");
            para.put("cmd", "design_install_reufile");
            para.put("current_uid", this.createUserID());
            para.put("currentUsername", this.getUser());
            para.put("reuFileName", reuFile.getName());

            HttpClient client = createHttpMethod(para);
            client.setContent(IOUtils.inputStream2Bytes(new FileInputStream(new File(shareXMLName))));
            InputStream input = execute4InputStream(client);
            client.release();
            para.put("isComplete", "true");
            HttpClient client1 = createHttpMethod(para);
            client1.setContent(IOUtils.inputStream2Bytes(new FileInputStream(new File(helpXMLName))));
            InputStream input1 = execute4InputStream(client1);
            return ComparatorUtils.equals(stream2String(input), "true") && ComparatorUtils.equals(stream2String(input1), "true");
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean removeREUFilesByName(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            return true;
        }
        try {
            HashMap<String, String> para = new HashMap<>();
            para.put("op", "fr_remote_design");
            para.put("cmd", "design_remove_reufile");
            para.put("current_uid", this.createUserID());
            para.put("currentUsername", this.getUser());
            para.put("reuFileName", fileName);

            HttpClient client = createHttpMethod(para);
            InputStream input = execute4InputStream(client);
            return ComparatorUtils.equals(stream2String(input), "true");
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getSharePath() {
        try {
            HashMap<String, String> para = new HashMap<>();
            para.put("op", "fr_remote_design");
            para.put("cmd", "design_get_share_path");
            para.put("current_uid", this.createUserID());
            para.put("currentUsername", this.getUser());

            HttpClient client = createHttpMethod(para);
            InputStream input = execute4InputStream(client);
            return stream2String(input);
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
    }


    @Override
    public boolean hasPluginServiceStarted(String key) {

        return true;
    }

}