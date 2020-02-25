package com.fr.design.env;

import com.fr.log.FineLoggerFactory;
import com.fr.security.SecurityToolbox;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;
import com.fr.workspace.WorkContext;
import com.fr.workspace.connect.WorkspaceConnectionInfo;

public class RemoteDesignerWorkspaceInfo implements DesignerWorkspaceInfo {

    private String name;

    private WorkspaceConnectionInfo connection;

    public static RemoteDesignerWorkspaceInfo create(WorkspaceConnectionInfo connection) {
        RemoteDesignerWorkspaceInfo info = new RemoteDesignerWorkspaceInfo();
        info.connection = connection;
        return info;
    }

    @Override
    public DesignerWorkspaceType getType() {

        return DesignerWorkspaceType.Remote;
    }

    @Override
    public String getName() {

        return name;
    }

    @Override
    public String getPath() {

        return null;
    }

    @Override
    public WorkspaceConnectionInfo getConnection() {

        return connection;
    }

    @Override
    public void readXML(XMLableReader reader) {

        if (reader.isAttr()) {
            this.name = reader.getAttrAsString("name", StringUtils.EMPTY);
        }
        if (reader.isChildNode()) {
            String tagName = reader.getTagName();
            if ("Connection".equals(tagName)) {
                String url = reader.getAttrAsString("url", StringUtils.EMPTY);
                String username = reader.getAttrAsString("username", StringUtils.EMPTY);
                //密码解密
                String password = SecurityToolbox.defaultDecrypt(reader.getAttrAsString("password", StringUtils.EMPTY).replaceAll(" ", "\r\n"));
                String certPath = reader.getAttrAsString("certPath", StringUtils.EMPTY);
                String certSecretKey = reader.getAttrAsString("certSecretKey", StringUtils.EMPTY);
                this.connection = new WorkspaceConnectionInfo(url, username, password, certPath, certSecretKey);
            }
        }
    }

    @Override
    public void writeXML(XMLPrintWriter writer) {

        writer.attr("name", name);
        if (this.connection != null) {
            writer.startTAG("Connection");
            writer.attr("url", connection.getUrl());
            writer.attr("username", connection.getUserName());
            writer.attr("password", SecurityToolbox.defaultEncrypt(connection.getPassword()));
            writer.attr("certPath", connection.getCertPath());
            writer.attr("certSecretKey", connection.getCertSecretKey());
            writer.end();
        }
    }

    @Override
    @SuppressWarnings("squid:S2975")
    public Object clone() throws CloneNotSupportedException {

        RemoteDesignerWorkspaceInfo object = (RemoteDesignerWorkspaceInfo) super.clone();

        object.connection = (WorkspaceConnectionInfo) StableUtils.cloneObject(this.connection);
        return object;
    }


    @Override
    public boolean checkValid() {
        boolean result = false;
        try {
            result = WorkContext.getConnector().testConnection(connection);
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
            return result;
        }
        return result;
    }
}
