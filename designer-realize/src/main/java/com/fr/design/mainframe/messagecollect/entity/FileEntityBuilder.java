package com.fr.design.mainframe.messagecollect.entity;

import com.fr.config.MarketConfig;
import com.fr.design.DesignerEnvManager;
import com.fr.general.CloudClient;
import com.fr.general.IOUtils;
import com.fr.json.JSONArray;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.CommonUtils;
import com.fr.stable.CoreConstants;
import com.fr.stable.EncodeConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.third.jodd.datetime.JDateTime;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.zip.ZipOutputStream;

/**
 * @author alex sung
 * @date 2019/4/8
 */
public class FileEntityBuilder {

    private static final String FOCUS_POINT_FILE_ROOT_PATH = "FocusPoint";
    private static final String FILE_FROM = "design";

    /**
     * 文件夹路径
     */
    private String folderName;

    public FileEntityBuilder(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public File generateZipFile(String pathName) {
        if (pathName == null) {
            return null;
        }
        File zipFile = null;
        ZipOutputStream zipOut = null;
        try {
            zipFile = new File(pathName + ".zip");
            zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
            IOUtils.zip(zipOut, new File(pathName));
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        } finally {
            IOUtils.close(zipOut);
        }
        return zipFile;
    }

    public void generateFile(JSONArray jsonArray, String folderName) {
        if (jsonArray.size() == 0) {
            return;
        }
        FileOutputStream out = null;
        String content = jsonArray.toString();
        try (InputStream in = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8))) {
            String fileName = String.valueOf(UUID.randomUUID());
            File file = new File(folderName + File.separator + fileName + ".json");
            StableUtils.makesureFileExist(file);
            out = new FileOutputStream(file);
            IOUtils.copyBinaryTo(in, out);
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        } finally {
            IOUtils.close(out);
        }
    }

    public void deleteFileAndZipFile(File zipFile, String pathName) {
        File file = new File(pathName);
        CommonUtils.deleteFile(file);
        CommonUtils.deleteFile(zipFile);
    }

    /**
     * 上传文件到云中心
     *
     * @param file        待上传文件
     * @param keyFileName 目标文件
     * @throws IOException
     */
    public static void uploadFile(File file, String keyFileName) throws IOException {
        CloudClient client = CloudClient.getInstance();
        String today = new JDateTime().toString("YYYY-MM-DD");
        String filePath = FOCUS_POINT_FILE_ROOT_PATH + CoreConstants.SEPARATOR + today + CoreConstants.SEPARATOR + keyFileName;
        String bbsUserName = MarketConfig.getInstance().getBbsUsername();
        String uuid = DesignerEnvManager.getEnvManager().getUUID();
        String name = StringUtils.isEmpty(bbsUserName) ? uuid : bbsUserName;

        client.uploadFile(file, filePath, URLEncoder.encode(name, EncodeConstants.ENCODING_UTF_8), FILE_FROM);
    }
}
