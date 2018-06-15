package com.fr.design.env;

import com.fr.security.SecurityToolbox;
import com.fr.stable.StringUtils;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLable;
import com.fr.stable.xml.XMLableReader;
import com.fr.workspace.connect.WorkspaceConnection;

/**
 * Created by juhaoyu on 2018/6/15.
 */
public class DesignerWorkspaceInfo implements XMLable {
    
    private static final int DEFAULT_RPC_PORT = 39999;
    
    public static final String XML_TAG = "DesignerWorkspace";
    
    private DesignerWorkspaceType type;
    
    private String name;
    
    private String path;
    
    private WorkspaceConnection connection;
    
    public static DesignerWorkspaceInfo createLocal(String name, String path) {
        
        DesignerWorkspaceInfo info = new DesignerWorkspaceInfo();
        info.connection = null;
        info.name = name;
        info.path = path;
        info.type = DesignerWorkspaceType.Local;
        return info;
    }
    
    public void setName(String name) {
        
        this.name = name;
    }
    
    public DesignerWorkspaceType getType() {
        
        return type;
    }
    
    public String getName() {
        
        return name;
    }
    
    public String getPath() {
        
        return path;
    }
    
    public WorkspaceConnection getConnection() {
        
        return connection;
    }
    
    public void setType(DesignerWorkspaceType type) {
        
        this.type = type;
    }
    
    public void setPath(String path) {
        
        this.path = path;
    }
    
    public void setConnection(WorkspaceConnection connection) {
        
        this.connection = connection;
    }
    
    @Override
    public void readXML(XMLableReader reader) {
        
        if (reader.isAttr()) {
            this.name = reader.getAttrAsString("name", StringUtils.EMPTY);
            this.type = DesignerWorkspaceType.valueOf(reader.getAttrAsString("name", "Local"));
            this.path = reader.getAttrAsString("path", StringUtils.EMPTY);
        }
        if (reader.isChildNode()) {
            String tagName = reader.getTagName();
            if ("Connection".equals(tagName)) {
                String ip = reader.getAttrAsString("ip", StringUtils.EMPTY);
                int port = reader.getAttrAsInt("port", DEFAULT_RPC_PORT);
                String username = reader.getAttrAsString("username", StringUtils.EMPTY);
                //密码解密
                String password = SecurityToolbox.decrypt(reader.getAttrAsString("password", StringUtils.EMPTY));
                this.connection = new WorkspaceConnection(ip, port, username, password);
            }
        }
    }
    
    @Override
    public void writeXML(XMLPrintWriter writer) {
        
        writer.attr("name", name);
        writer.attr("path", path);
        writer.attr("type", type.toString());
        if (this.connection != null) {
            writer.startTAG("Connection");
            writer.attr("ip", connection.getIp());
            writer.attr("port", connection.getPort());
            writer.attr("username", connection.getUserName());
            writer.attr("password", SecurityToolbox.encrypt(connection.getPassword()));
            writer.end();
        }
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        
        return null;
    }
}
