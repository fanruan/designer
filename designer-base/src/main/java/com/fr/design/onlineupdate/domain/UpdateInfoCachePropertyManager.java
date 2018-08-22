package com.fr.design.onlineupdate.domain;

import com.fr.log.FineLoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Created by XINZAI on 2018/8/21.
 */
public class UpdateInfoCachePropertyManager {
    private Properties prop;
    private String filePath;

    public UpdateInfoCachePropertyManager(String filePath) {
        this.filePath = filePath;
        prop = new Properties();
        try {
            prop.load(new FileInputStream(filePath));
        } catch (Exception ignored) {

        }
    }

    public void updateProperty(String keyName, String keyValue) {
        try {
            OutputStream fos = new FileOutputStream(filePath);
            prop.setProperty(keyName, keyValue);
            prop.store(fos, null);
        } catch (IOException e) {
            FineLoggerFactory.getLogger().error(e.getMessage());
        }
    }

    public String readProperty(String keyName) {
        return prop.getProperty(keyName);
    }
}