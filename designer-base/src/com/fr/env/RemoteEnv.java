package com.fr.env;

import com.fr.base.EnvException;
import com.fr.base.operator.common.CommonOperator;
import com.fr.base.operator.connect.ConnectOperator;
import com.fr.base.operator.file.FileOperator;
import com.fr.base.operator.org.OrganizationOperator;
import com.fr.base.remote.RemoteDeziConstants;
import com.fr.common.rpc.RemoteCallServerConfig;
import com.fr.common.rpc.netty.MessageSendExecutor;
import com.fr.common.rpc.netty.RemoteCallClient;
import com.fr.core.env.EnvConstants;
import com.fr.core.env.EnvContext;
import com.fr.data.impl.storeproc.StoreProcedure;
import com.fr.dav.AbstractEnv;
import com.fr.dav.DavXMLUtils;
import com.fr.design.DesignerEnvManager;
import com.fr.design.env.RemoteEnvConfig;
import com.fr.design.mainframe.DesignerContext;
import com.fr.file.CacheManager;
import com.fr.general.CommonIOUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.EnvProxyFactory;
import com.fr.general.IOUtils;
import com.fr.general.Inter;
import com.fr.general.http.HttpToolbox;
import com.fr.io.utils.ResourceIOUtils;
import com.fr.json.JSONArray;
import com.fr.json.JSONObject;
import com.fr.log.FineLoggerFactory;
import com.fr.report.DesignAuthority;
import com.fr.report.util.AuthorityXMLUtils;
import com.fr.share.ShareConstants;
import com.fr.stable.ArrayUtils;
import com.fr.stable.EncodeConstants;
import com.fr.stable.Filter;
import com.fr.stable.ProductConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.SvgProvider;
import com.fr.stable.project.ProjectConstants;
import com.fr.third.guava.base.Strings;
import com.fr.third.guava.collect.ImmutableMap;
import com.fr.web.ResourceConstants;

import javax.swing.*;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.fr.third.guava.base.Preconditions.checkArgument;

/**
 * @author null
 */
public class RemoteEnv extends AbstractEnv<RemoteEnvConfig> implements DesignAuthorityConfigurable {

    private static final String CERT_KEY = "javax.net.ssl.trustStore";
    private static final String PWD_KEY = "javax.net.ssl.trustStorePassword";
    private static final String HTTPS_PREFIX = "https:";
    private final static String[] FILE_TYPE = {"cptx", "cpt", "frm", "form", "cht", "chart"};
    private String buildFilePath;
    private RemoteEnvConfig config;

    public RemoteEnv(RemoteEnvConfig config) {
        this.config = config;
    }

    @Override
    public void connect() throws Exception {
        RemoteCallClient.getInstance().load(config.getHost(), config.getPort(), RemoteCallServerConfig.getInstance().getSerializeProtocol());
    }

    @Override
    public void connectOnce() throws Exception {
        RemoteCallClient.getInstance().loadOnce(config.getHost(), config.getPort(), RemoteCallServerConfig.getInstance().getSerializeProtocol(), 10 * 1000);
        ConnectOperator operator = RemoteCallClient.getInstance().execute(ConnectOperator.class);
        boolean result = false;
        try {
            result = operator.connect(config.getUsername(), config.getPassword());
        } catch (Exception ignore) {

        }
        if (!result) {
            throw new Exception("Cannot connect to the remote server!");
        }
    }

    @Override
    public boolean disconnect() {
        MessageSendExecutor.getInstance().stop();
        return true;
    }

    @Override
    public EnvProxyFactory getProxyFactory() {

        return new EnvProxyFactory() {

            @Override
            public <T> T get(Class<T> clazz, T obj) {

                assert clazz != null;
                try {
                    return MessageSendExecutor.getInstance().execute(clazz);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    @Override
    public FileOperator getFileOperator() throws Exception {

        return MessageSendExecutor.getInstance().execute(FileOperator.class);
    }

    @Override
    public CommonOperator getCommonOperator() throws Exception {
        return MessageSendExecutor.getInstance().execute(CommonOperator.class);
    }

    @Override
    public OrganizationOperator getOrganizationOperator() throws Exception {
        return MessageSendExecutor.getInstance().execute(OrganizationOperator.class);
    }

    @Override
    public RemoteEnvConfig getEnvConfig() {
        return config;
    }

    @Override
    public String getPath() {
        return config.getPath();
    }

    @Override
    public String getUser() {
        return config.getUsername();
    }

    public String getPassword() {
        return config.getPassword();
    }

    @Override
    public String getUserID() {
        return EnvContext.currentToken();
    }

    @Override
    public boolean isLocalEnv() {
        return false;
    }

    /**
     * execute method之后,取返回的 InputStream
     */
    private InputStream filterInputStream(InputStream in) throws Exception {
        if (in == null) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            IOUtils.copyBinaryTo(in, out);
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
        filterInputStream(RemoteEnvUtils.simulateRPCByHttpPost(bytes, para, false, this));
        return true;
    }

    /**
     * 把InputStream转成一段String
     *
     * @param in InputStream输入流
     * @return 转换后的字符串
     */
    private static String stream2String(InputStream in) {
        if (in == null) {
            return null;
        }
        String result = null;
        try {
            result = CommonIOUtils.inputStream2String(in, EncodeConstants.ENCODING_UTF_8);
        } catch (UnsupportedEncodingException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 测试连接服务器
     *
     * @return 测试连接成功返回true
     * @throws Exception 异常
     */
    private boolean testServerConnection() throws Exception {
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
        if (Strings.isNullOrEmpty(res)) {
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
                        JOptionPane.showMessageDialog(component, Inter.getLocText("Datasource-Connection_failed"));
                    } else {
                        FineLoggerFactory.getLogger().info(Inter.getLocText(new String[]{"Datasource-Connection_failed", "Version-does-not-support"}, new String[]{",", "!"}));
                    }
                    return false;
                }
            }
        }
    }


    private void refreshHttpSProperty() {
        if (getPath().startsWith(HTTPS_PREFIX) && System.getProperty(CERT_KEY) == null) {
            DesignerEnvManager envManager = DesignerEnvManager.getEnvManager();
            System.setProperty(CERT_KEY, envManager.getCertificatePath());
            System.setProperty(PWD_KEY, envManager.getCertificatePass());
        }
    }


    /**
     * 心跳访问，用来更新当前用户的访问时间
     *
     * @throws Exception e
     */

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
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
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
        refreshHttpSProperty();
        String res = null;
        try {
            InputStream inputStream = RemoteEnvUtils.updateAuthorities(authorities, this);
            inputStream = filterInputStream(inputStream);
            res = IOUtils.inputStream2String(inputStream, EncodeConstants.ENCODING_UTF_8);
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage());
        }
        return StringUtils.isNotEmpty(res) && Boolean.valueOf(res);
    }

    @Override
    public DesignAuthority[] getAuthorities() {
        refreshHttpSProperty();
        DesignAuthority[] authorities = new DesignAuthority[0];
        try {
            InputStream inputStream = RemoteEnvUtils.getAuthorities(this);
            inputStream = filterInputStream(inputStream);
            authorities = AuthorityXMLUtils.readDesignAuthoritiesXML(inputStream);
            return authorities;
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage());
        }
        return authorities;
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
        refreshHttpSProperty();
        HashMap<String, String> para = new HashMap<>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "write_privilege_map");
        para.put("current_user", getUser());
        para.put("current_password", getPassword());
        para.put("key", key);
        para.put("value", value);

        //jim ：加上user，远程设计点击预览时传递用户角色信息
        InputStream input = filterInputStream(
                RemoteEnvUtils.simulateRPCByHttpGet(para, false, this)
        );

        if (input == null) {
            return false;
        }

        return Boolean.valueOf(IOUtils.inputStream2String(input, EncodeConstants.ENCODING_UTF_8));
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
        refreshHttpSProperty();
        return filterInputStream(RemoteEnvUtils.simulateRPCByHttpPost(bytes, para, false, this));
    }


    /**
     * 读取路径下的svg文件
     *
     * @param path 制定路径,是基于报表目录下resource文件夹路径
     * @return 读到的文件
     */
    @Override
    public String[] readPathSvgFiles(String path) {
        refreshHttpSProperty();
        String cataloguePath = StableUtils.pathJoin(CacheManager.getProviderInstance().getCacheDirectory().getPath(), SvgProvider.SERVER, path);

        ArrayList<String> fileArray = new ArrayList<>();
        try {
            HashMap<String, String> para = new HashMap<>();
            para.put("op", "fr_remote_design");
            para.put("cmd", "design_read_svgfile");
            para.put("resourcePath", path);
            para.put("current_uid", this.getUserID());
            para.put("currentUsername", this.getUser());

            InputStream input = filterInputStream(
                    RemoteEnvUtils.simulateRPCByHttpGet(para, false, this)
            );
            JSONArray ja = new JSONArray(stream2String(input));
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jsonObject = (JSONObject) ja.get(i);
                String svgFileName = (String) jsonObject.get("svgfileName");
                String svgFileContent = (String) jsonObject.get("svgfileContent");

                String file = StableUtils.pathJoin(cataloguePath, svgFileName);
                InputStream in = new ByteArrayInputStream(svgFileContent.getBytes(EncodeConstants.ENCODING_UTF_8));
                ResourceIOUtils.write(file, in);
                fileArray.add(file);
            }
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage());
        }

        return fileArray.toArray(new String[0]);
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
        para.put("current_uid", this.getUserID());
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
                FineLoggerFactory.getLogger().error(ex.getMessage());
            }

        } catch (TransformerConfigurationException ex) {
            FineLoggerFactory.getLogger().error(ex.getMessage());
            return false;
        }

        try {
            String res = stream2String(filterInputStream(
                    RemoteEnvUtils.simulateRPCByHttpPost(out.getOut().toByteArray(), out.nameValuePairs, false, this)
            ));
            if (StringUtils.isNotEmpty(res)) {
                JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Already_exist") + res);
                return false;
            }
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage());
            return false;
        }

        return true;
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

        return StableUtils.pathJoin(getPath(), getAppName());
    }

    public String[] getProcedureColumns(StoreProcedure storeProcedure, Map parameterMap) throws Exception {
        refreshHttpSProperty();
        String[] columns;
        HashMap<String, String> para = new HashMap<>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "list_sp");
        try {
            InputStream input = filterInputStream(
                    RemoteEnvUtils.simulateRPCByHttpGet(para, false, this)
            );

            if (input == null) {
                return ArrayUtils.EMPTY_STRING_ARRAY;
            }

            columns = DavXMLUtils.readXMLSPColumns(input);
            return columns;
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage());
        }

        return new String[0];
    }

    public String[] getProcedureColumns(String name) throws Exception {
        refreshHttpSProperty();
        String[] columns;
        HashMap<String, String> para = new HashMap<>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "list_sp_columns_name");
        para.put("name", name);
        try {
            InputStream input = filterInputStream(
                    RemoteEnvUtils.simulateRPCByHttpGet(para, false, this)
            );
            if (input == null) {
                return ArrayUtils.EMPTY_STRING_ARRAY;
            }
            columns = DavXMLUtils.readXMLSPColumns(input);
            return columns;
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage());
        }
        return new String[0];

    }

    @Override
    public String[] getSupportedTypes() {
        return FILE_TYPE;
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

    @Override
    public String[] loadREUFile() {
        refreshHttpSProperty();
        ResourceIOUtils.delete(StableUtils.pathJoin(
                CacheManager.getProviderInstance().getCacheDirectory().getAbsolutePath(),
                ShareConstants.DIR_SHARE_CACHE));

        String zipFilePath = null;
        try {
            HashMap<String, String> para = new HashMap<>();
            para.put("op", "fr_remote_design");
            para.put("cmd", "design_read_reufile");
            para.put("current_uid", this.getUserID());
            para.put("currentUsername", this.getUser());

            //拿到服务端传过来的整个共享文件夹的压缩文件的文件流
            InputStream input = RemoteEnvUtils.simulateRPCByHttpGet(para, false, this);

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
            FineLoggerFactory.getLogger().error(e.getMessage());
        } finally {

            if (zipFilePath != null) {
                ResourceIOUtils.delete(zipFilePath);
            }
        }

        return new String[0];
    }

    @Override
    public boolean installREUFile(File reuFile) {
        refreshHttpSProperty();
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
            para.put("current_uid", this.getUserID());
            para.put("currentUsername", this.getUser());
            para.put("reuFileName", reuFile.getName());

            InputStream input = filterInputStream(
                    RemoteEnvUtils.simulateRPCByHttpPost(
                            IOUtils.inputStream2Bytes(
                                    new FileInputStream(new File(shareXMLName))
                            )
                            , para,
                            false,
                            this)
            );
            para.put("isComplete", "true");
            InputStream input1 = filterInputStream(RemoteEnvUtils.simulateRPCByHttpPost(
                    IOUtils.inputStream2Bytes(
                            new FileInputStream(new File(helpXMLName))
                    )
                    , para,
                    false,
                    this));
            return ComparatorUtils.equals(stream2String(input), "true") && ComparatorUtils.equals(stream2String(input1), "true");
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean removeREUFilesByName(String fileName) {
        refreshHttpSProperty();
        if (StringUtils.isEmpty(fileName)) {
            return true;
        }
        try {
            HashMap<String, String> para = new HashMap<>();
            para.put("op", "fr_remote_design");
            para.put("cmd", "design_remove_reufile");
            para.put("current_uid", this.getUserID());
            para.put("currentUsername", this.getUser());
            para.put("reuFileName", fileName);

            InputStream input = filterInputStream(
                    RemoteEnvUtils.simulateRPCByHttpGet(para, false, this)
            );
            return ComparatorUtils.equals(stream2String(input), "true");
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getSharePath() {
        refreshHttpSProperty();
        try {
            HashMap<String, String> para = new HashMap<>();
            para.put("op", "fr_remote_design");
            para.put("cmd", "design_get_share_path");
            para.put("current_uid", this.getUserID());
            para.put("currentUsername", this.getUser());

            InputStream input = filterInputStream(
                    RemoteEnvUtils.simulateRPCByHttpGet(para, false, this)
            );
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