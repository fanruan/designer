package com.fr.design.mainframe.vcs.common;

import com.fr.base.io.XMLEncryptUtils;
import com.fr.file.FileNodeFILE;
import com.fr.file.filetree.FileNode;
import com.fr.general.ComparatorUtils;
import com.fr.stable.StableUtils;
import com.fr.workspace.WorkContext;
import com.fr.workspace.resource.WorkResourceOutputStream;
import com.fr.workspace.server.lock.TplOperator;
import com.fr.workspace.server.vcs.filesystem.VcsFileSystem;

import java.io.ByteArrayInputStream;
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
    public InputStream asInputStream() throws Exception {
        if (node == null) {
            return null;
        }
        String vcsCacheDir = VcsFileSystem.getInstance().getVcsCacheRelativePath();
        String envPath = node.getEnvPath();
        // envPath必须以VcsCacheRelativePath开头
        if (!envPath.startsWith(vcsCacheDir)) {
            return null;
        }
        InputStream in = new ByteArrayInputStream(
                WorkContext.getCurrent().get(TplOperator.class).readAndLockFile(
                        StableUtils.pathJoin(vcsCacheDir, envPath.substring(vcsCacheDir.length() + 1))
                )
        );

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
        String vcsCacheDir = VcsFileSystem.getInstance().getVcsCacheRelativePath();
        String envPath = node.getEnvPath();
        // envPath必须以VcsCacheRelativePath开头
        if (!envPath.startsWith(vcsCacheDir)) {
            return null;
        }

        return new WorkResourceOutputStream(StableUtils.pathJoin(vcsCacheDir, envPath.substring(vcsCacheDir.length() + 1)));
    }
}
