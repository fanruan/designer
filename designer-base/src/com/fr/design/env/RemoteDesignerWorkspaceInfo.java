package com.fr.design.env;

import com.fr.security.SecurityToolbox;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;
import com.fr.workspace.connect.WorkspaceConnection;

public class RemoteDesignerWorkspaceInfo implements DesignerWorkspaceInfo {
    
    private String name;

    private WorkspaceConnection connection;

    public static RemoteDesignerWorkspaceInfo create(WorkspaceConnection connection) {
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
    public WorkspaceConnection getConnection() {

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
                String password = SecurityToolbox.defaultDecrypt(reader.getAttrAsString("password", StringUtils.EMPTY).replaceAll(" ","\r\n"));
                this.connection = new WorkspaceConnection(url, username, password);
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
            writer.end();
        }
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {

        RemoteDesignerWorkspaceInfo object = (RemoteDesignerWorkspaceInfo)super.clone();

        object.connection = (WorkspaceConnection)StableUtils.cloneObject(this.connection);
        return  object;
    }
}
