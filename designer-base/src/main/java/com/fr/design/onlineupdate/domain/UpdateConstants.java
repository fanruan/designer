package com.fr.design.onlineupdate.domain;

/**
 * Created by XINZAI on 2018/8/21.
 */

import com.fr.third.org.apache.commons.collections4.collection.UnmodifiableCollection;
import com.fr.third.org.apache.commons.collections4.list.UnmodifiableList;

import java.awt.Color;
import java.util.Arrays;

/**
 * 更新升级的常量
 */
public interface UpdateConstants {

    String APPS_FOLDER_NAME = "webapps";

    int CONNECTION_TIMEOUT = 1000 * 5;
    Color BAR_COLOR = new Color(0x3384F0);

    String CHANGELOG_X_START = "2018-07-11";

    String DEFAULT_APP_NAME = "FineReport";
    String DOWNLOAD_DIR = "update";
    String DESIGNER_BACKUP_DIR = "designerbackup";

    String UPDATE_CACHE_CONFIG_X = "updateCacheConfig10";
    String UPDATE_CACHE_INFO_X = "updateCacheInfo10";


    int BYTE = 153600;

    UnmodifiableList JARS_FOR_SERVER_X = new UnmodifiableList(Arrays.asList(new String[]{
            "fine-activator-10.0.jar",
            "fine-core-10.0.jar",
            "fine-report-engine-10.0.jar",
            "fine-decision-10.0.jar",
            "fine-decision-report-10.0.jar",
            "fine-schedule-10.0.jar",
            "fine-schedule-report-10.0.jar",
            "fine-swift-log-adaptor-10.0.jar",
            "fine-webui-10.0.jar",
            "fine-datasource-10.0.jar",
            "fine-third-10.0.jar",
            "fine-accumulator-10.0.jar"
    }));

    UnmodifiableList  JARS_FOR_DESIGNER_X = new UnmodifiableList(Arrays.asList(new String[]{
            "fine-report-designer-10.0.jar",
            "aspectjrt.jar"
    }));


    UnmodifiableList  LOG_TYPE = new UnmodifiableList(Arrays.asList(new String[]{
            "REPORT", "MOBILE", "CHART", "PFC", "BI"
    }));

}