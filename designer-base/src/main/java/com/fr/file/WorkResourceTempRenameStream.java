package com.fr.file;

import com.fr.stable.ArrayUtils;
import com.fr.stable.project.ProjectConstants;
import com.fr.workspace.WorkContext;
import com.fr.workspace.resource.WorkResource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 这个流会先输出临时文件到服务器.
 * 然后再操作服务器对文件进行重命名.
 * 用于设计器本地和远程修改模板.
 */
public class WorkResourceTempRenameStream extends ByteArrayOutputStream {

    private String path;

    public WorkResourceTempRenameStream(String path) {
        this.path = path;
    }

    @Override
    public void close() throws IOException {
        //写到给定的path上去
        byte[] content = super.toByteArray();
        if (ArrayUtils.isEmpty(content)) {
            return;
        }

        String tmpPath = path + ProjectConstants.TEMP_SUFFIX;
        WorkResource resource = WorkContext.getWorkResource();
        // 输出临时文件到服务器
        resource.write(tmpPath, content);
        // 重命名文件
        resource.rename(tmpPath, path);
    }
}
