package com.fr.design.mainframe.vcs.common;

import com.fr.base.io.XMLEncryptUtils;
import com.fr.file.FileNodeFILE;
import com.fr.file.filetree.FileNode;
import com.fr.general.ComparatorUtils;
import com.fr.stable.StableUtils;
import com.fr.workspace.WorkContext;
import com.fr.workspace.resource.WorkResource;
import com.fr.workspace.resource.WorkResourceOutputStream;

import java.io.InputStream;
import java.io.OutputStream;


public class VcsCacheFileNodeFile extends FileNodeFILE {

    private final FileNode node;

    public VcsCacheFileNodeFile(FileNode node) {
        super(node);
        this.node = node;
    }

    /**
     * 和FileNodeFILE中一样，只是去掉了必须以reportlets开头的限制，改为vcs开头
     *
     * @return
     * @throws Exception
     */
    @Override
    public InputStream asInputStream() {
        if (node == null) {
            return null;
        }

        String envPath = node.getEnvPath();
        // envPath必须以vcs开头
        if (!envPath.startsWith(VcsHelper.VCS_CACHE_DIR)) {
            return null;
        }

        InputStream in = WorkContext.getCurrent().get(WorkResource.class)
                .openStream(StableUtils.pathJoin(VcsHelper.VCS_CACHE_DIR, envPath.substring(VcsHelper.VCS_CACHE_DIR.length() + 1)));

        return envPath.endsWith(".cpt") || envPath.endsWith(".frm")
                ? XMLEncryptUtils.decodeInputStream(in) : in;
    }


    /**
     * 和FileNodeFILE中一样，只是去掉了必须以reportlets开头的限制，改为vcs开头
     *
     * @return
     * @throws Exception
     */
    @Override
    public OutputStream asOutputStream() {
        if (ComparatorUtils.equals(node, null)) {
            return null;
        }

        String envPath = node.getEnvPath();
        // envPath必须以reportLets开头
        if (!envPath.startsWith(VcsHelper.VCS_CACHE_DIR)) {
            return null;
        }

        return new WorkResourceOutputStream(StableUtils.pathJoin(VcsHelper.VCS_CACHE_DIR, envPath.substring(VcsHelper.VCS_CACHE_DIR.length() + 1)));
    }
}
