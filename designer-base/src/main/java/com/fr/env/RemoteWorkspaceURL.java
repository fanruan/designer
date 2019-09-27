package com.fr.env;

import com.fr.stable.AssistUtils;
import com.fr.stable.FCloneable;
import com.fr.stable.StringUtils;

/**
 * @author yaohwu
 */
public class RemoteWorkspaceURL implements FCloneable {

    /**
     * 默认 web app name
     */
    static final String DEFAULT_WEB_APP_NAME = "webroot";
    /**
     * 默认 servlet name
     */
    static final String DEFAULT_SERVLET_NAME = "decision";
    /**
     * 默认 hostname
     */
    private static final String DEFAULT_HOST_NAME = "${IP}";
    /**
     * 默认端口
     */
    private static final String DEFAULT_PORT = "8075";
    private static final String HTTPS = "https://";
    private static final String HTTP = "http://";

    public static final RemoteWorkspaceURL DEFAULT_URL =
            new RemoteWorkspaceURL(
                    false,
                    DEFAULT_HOST_NAME,
                    DEFAULT_PORT,
                    DEFAULT_WEB_APP_NAME,
                    DEFAULT_SERVLET_NAME);

    private boolean isHttps;
    private String host;
    private String port;
    private String web;
    private String servlet;
    private String url;


    /**
     * 解析 url 字符串 生成 RemoteWorkspaceURL 对象
     * url 字符串格式 (http(s)://)host(:port)/+web/+servlet/+(others)
     *
     * @param url x:x/x/x/x
     */
    public RemoteWorkspaceURL(String url) {
        this.url = url;
        // 没有写协议名称 默认 使用 http 协议
        if (!url.startsWith(HTTPS) && !url.startsWith(HTTP)) {
            url = HTTP + url;
        }
        // 第二次出现":"的地方,port位置起始点
        int portIndex = url.indexOf(":", url.indexOf(":") + 1);
        // 第三次出现"/"的地方
        int webIndex = url.indexOf("/", url.indexOf("://") + 3);

        isHttps = url.startsWith(HTTPS);

        if (portIndex > webIndex && webIndex != -1) {
            portIndex = -1;
        }

        if (portIndex == -1) {
            if (webIndex == -1) {
                host = isHttps ? url.substring(HTTPS.length()) : url.substring(HTTP.length());
                port = StringUtils.EMPTY;
                web = StringUtils.EMPTY;
                servlet = StringUtils.EMPTY;
            } else {
                host = isHttps ? url.substring(HTTPS.length(), webIndex) : url.substring(HTTP.length(), webIndex);
                port = StringUtils.EMPTY;
                web = StringUtils.EMPTY;
                servlet = StringUtils.EMPTY;
                String[] lefts = url.substring(webIndex + 1).split("/+");
                parserWebAndServlet(lefts);
            }
        } else {
            if (webIndex == -1) {
                host = isHttps ? url.substring(HTTPS.length(), portIndex) : url.substring(HTTP.length(), portIndex);
                port = url.substring(portIndex + 1);
                web = StringUtils.EMPTY;
                servlet = StringUtils.EMPTY;
            } else {
                host = isHttps ? url.substring(HTTPS.length(), portIndex) : url.substring(HTTP.length(), portIndex);
                port = url.substring(portIndex + 1, webIndex);
                web = StringUtils.EMPTY;
                servlet = StringUtils.EMPTY;
                String[] lefts = url.substring(webIndex + 1).split("/+");
                parserWebAndServlet(lefts);
            }
        }
    }

    public boolean hasDefaultHostName() {
        return DEFAULT_HOST_NAME.equals(host);
    }

    public static RemoteWorkspaceURL createDefaultURL() {
        return DEFAULT_URL.clone();
    }


    public RemoteWorkspaceURL(boolean isHttps, String host, String port, String web, String servlet) {
        this.isHttps = isHttps;
        this.host = host != null ? host.trim() : StringUtils.EMPTY;
        this.port = port != null ? port.trim() : StringUtils.EMPTY;
        this.web = web != null ? web.trim() : StringUtils.EMPTY;
        this.servlet = servlet != null ? servlet.trim() : StringUtils.EMPTY;
    }

    public String getURL() {
        if (this.url != null) {
            return url;
        }
        String prefix = isHttps ? HTTPS : HTTP;
        String portColon = StringUtils.isNotEmpty(port) ? ":" : StringUtils.EMPTY;
        String webAppNameSlash = StringUtils.isNotEmpty(web) ? "/" : StringUtils.EMPTY;
        String servletNameSlash = StringUtils.isNotEmpty(servlet) ? "/" : StringUtils.EMPTY;
        this.url = prefix + host + portColon + port + webAppNameSlash + web + servletNameSlash + servlet;
        return this.url;
    }


    public void setHttps(boolean https) {
        isHttps = https;
    }

    public void setHost(String host) {
        this.host = host != null ? host.trim() : StringUtils.EMPTY;
    }

    public void setPort(String port) {
        this.port = port != null ? port.trim() : StringUtils.EMPTY;
    }

    public void setWeb(String web) {
        this.web = web != null ? web.trim() : StringUtils.EMPTY;
    }

    public void setServlet(String servlet) {
        this.servlet = servlet != null ? servlet.trim() : StringUtils.EMPTY;
    }

    public boolean getHttps() {
        return isHttps;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getWeb() {
        return web;
    }

    public String getServlet() {
        return servlet;
    }

    public void resetUrl() {
        this.url = null;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof RemoteWorkspaceURL && AssistUtils.equals(isHttps, ((RemoteWorkspaceURL) o).isHttps)
                && AssistUtils.equals(host, ((RemoteWorkspaceURL) o).host)
                && AssistUtils.equals(port, ((RemoteWorkspaceURL) o).port)
                && AssistUtils.equals(web, ((RemoteWorkspaceURL) o).web)
                && AssistUtils.equals(servlet, ((RemoteWorkspaceURL) o).servlet)
                && AssistUtils.equals(url, ((RemoteWorkspaceURL) o).url);
    }

    @Override
    public int hashCode() {

        return AssistUtils.hashCode(isHttps, host, port, web, servlet, url);
    }

    @Override
    public String toString() {
        return "RemoteWorkspaceURL{" +
                "isHttps=" + isHttps +
                ", host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", web='" + web + '\'' +
                ", servlet='" + servlet + '\'' +
                '}';
    }

    @Override
    public RemoteWorkspaceURL clone() {
        RemoteWorkspaceURL cloned;
        try {
            cloned = (RemoteWorkspaceURL) super.clone();
            return cloned;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError(e.getMessage());
        }
    }

    private void parserWebAndServlet(String[] lefts) {
        int index;
        for (index = 0; index < lefts.length; index++) {
            if (StringUtils.isNotEmpty(lefts[index])) {
                web = lefts[index];
                index++;
                break;
            }
        }
        for (int servletIndex = index; servletIndex < lefts.length; servletIndex++) {
            if (StringUtils.isNotEmpty(lefts[servletIndex])) {
                servlet = lefts[servletIndex];
                break;
            }
        }
    }
}