package com.fr.design.env;

import com.fr.core.env.impl.AbstractEnvConfig;
import com.fr.general.Inter;
import com.fr.security.SecurityToolbox;
import com.fr.stable.AssistUtils;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;


public class RemoteEnvConfig extends AbstractEnvConfig {

    public static final int DEFAULT_RPC_PORT = 39999;

    private String host;
    private int port;
    private String username;
    private String password;

    public RemoteEnvConfig() {

    }

    public RemoteEnvConfig(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    @Override
    public String getPath() {
        return StableUtils.join(new Object[]{host, port}, ":");
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getDescription(String name) {
        return username + "@" + name + "[" + Inter.getLocText("Fine-Designer_Basic_Remote_Env") + "]";
    }

    @Override
    public void readXML(XMLableReader reader) {
        super.readXML(reader);
        if (reader.isChildNode()) {
            String tagName = reader.getTagName();
            if ("Attr".equals(tagName)) {
                this.host = reader.getAttrAsString("host", StringUtils.EMPTY);
                this.port = reader.getAttrAsInt("port", DEFAULT_RPC_PORT);
                this.username = reader.getAttrAsString("username", StringUtils.EMPTY);
                String password = reader.getAttrAsString("password", StringUtils.EMPTY);
                if (StringUtils.isNotEmpty(password)) {
                    this.password = SecurityToolbox.decrypt(password);
                }
            } else if ("Username".equals(tagName)) {
                this.username = reader.getElementValue();
            } else if ("Password".equals(tagName)) {
                String txt = reader.getElementValue();
                this.password = SecurityToolbox.decrypt(txt);
            }
        }
    }

    @Override
    public void writeXML(XMLPrintWriter writer) {
        super.writeXML(writer);
        writer.startTAG("Attr")
                .attr("host", host)
                .attr("port", port);
        writer.end();
        writer.startTAG("Username").textNode(username).end();
        if (StringUtils.isNotEmpty(password)) {
            writer.startTAG("Password").textNode(SecurityToolbox.encrypt(password)).end();
        }

    }

    @Override
    public boolean equals(Object o) {
        return o instanceof RemoteEnvConfig
                && AssistUtils.equals(((RemoteEnvConfig) o).host, host)
                && AssistUtils.equals(((RemoteEnvConfig) o).port, port)
                && AssistUtils.equals(((RemoteEnvConfig) o).username, username)
                && AssistUtils.equals(((RemoteEnvConfig) o).password, password);
    }

    @Override
    public int hashCode() {
        return AssistUtils.hashCode(host, port, username, password);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        RemoteEnvConfig cloned = (RemoteEnvConfig) super.clone();
        cloned.host = host;
        cloned.port = port;
        cloned.username = username;
        cloned.password = password;
        return cloned;
    }
}
