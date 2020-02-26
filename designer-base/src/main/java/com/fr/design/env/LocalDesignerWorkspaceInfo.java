package com.fr.design.env;

import com.fr.general.ComparatorUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;
import com.fr.workspace.connect.WorkspaceConnectionInfo;

import java.io.File;

/**
 * Created by juhaoyu on 2018/6/15.
 */
public class LocalDesignerWorkspaceInfo implements DesignerWorkspaceInfo {
    
    private String name;

    private String path;

    public static LocalDesignerWorkspaceInfo create(String name, String path) {

        LocalDesignerWorkspaceInfo info = new LocalDesignerWorkspaceInfo();
        info.name = name;
        info.path = path;
        return info;
    }

    @Override
    public DesignerWorkspaceType getType() {

        return DesignerWorkspaceType.Local;
    }

    @Override
    public String getName() {

        return name;
    }

    @Override
    public String getPath() {

        return path;
    }

    @Override
    public WorkspaceConnectionInfo getConnection() {
        return null;
    }

    @Override
    public void readXML(XMLableReader reader) {

        if (reader.isAttr()) {
            this.name = reader.getAttrAsString("name", StringUtils.EMPTY);
            this.path = reader.getAttrAsString("path", StringUtils.EMPTY);
        }
    }

    @Override
    public void writeXML(XMLPrintWriter writer) {

        writer.attr("name", name);
        writer.attr("path", path);
    }


    @Override
    @SuppressWarnings("squid:S2975")
    public Object clone() throws CloneNotSupportedException {
        LocalDesignerWorkspaceInfo object = (LocalDesignerWorkspaceInfo)super.clone();

        return  object;
    }

    @Override
    public boolean checkValid(){
        File file = new File(this.path);
        //判断不是文件夹/路径不在WEB-INF下/代码启动三种情况
        if(!file.isDirectory() || !ComparatorUtils.equals(file.getName(), "WEB-INF") || this.path.startsWith(".")) {
            return false;
        }

        return true;
    }
}
