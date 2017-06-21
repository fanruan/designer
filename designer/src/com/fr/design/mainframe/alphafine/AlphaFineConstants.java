package com.fr.design.mainframe.alphafine;

import com.fr.general.SiteCenter;

import java.awt.*;

/**
 * Created by XiaXiang on 2017/5/10.
 */
public class AlphaFineConstants {
    public static final String SAVE_FILE_NAME = "alpha.coco";

    public static final int SHOW_SIZE = 5;

    public static final int MAX_FILE_SIZE = 1000;

    public static final int LATEST_SHOW_SIZE = 3;

    public static final int HEIGHT = 680;

    public static final int WIDTH = 460;

    public static final int LEFT_WIDTH = 300;

    public static final int RIGHT_WIDTH = 380;

    public static final int FIELD_HEIGHT = 55;

    public static final int CONTENT_HEIGHT = 405;

    public static final int CELL_HEIGHT = 32;

    public static final int CELL_TITLE_HEIGHT = 24;


    public static final Dimension FULL_SIZE = new Dimension(680, 460);

    public static final Dimension CONTENT_SIZE = new Dimension(680, 405);

    public static final Dimension FIELD_SIZE = new Dimension(680, 55);

    public static final Dimension ICON_LABEL_SIZE = new Dimension(64, 64);

    public static final Dimension CLOSE_BUTTON_SIZE = new Dimension(40, 40);

    public static final Color WHITE = new Color(0xf9f9f9);

    public static final Color GRAY = new Color(0xd2d2d2);

    public static final Color LIGHT_GRAY = new Color(0xcccccc);

    public static final Color MEDIUM_GRAY = new Color(0x999999);

    public static final Color BLUE = new Color(0x3394f0);

    public static final Color BLACK = new Color(0x222222);

    public static final Color DARK_GRAY = new Color(0x666666);

    public static final Color RED = new Color(0xf46c4c);

    public static final Font SMALL_FONT = new Font("Song_TypeFace", 0, 10);

    public static final Font MEDIUM_FONT = new Font("Song_TypeFace", 0, 12);

    public static final Font LARGE_FONT = new Font("Song_TypeFace", 0, 18);

    public static final Font GREATER_FONT = new Font("Song_TypeFace", 0, 20);


    public static final String PLUGIN_SEARCH_URL = SiteCenter.getInstance().acquireUrlByKind("plugin.searchAPI");

    public static final String PLUGIN_URL = "http://shop.finereport.com/plugin/";

    public static final String REUSE_URL = "http://shop.finereport.com/reuse/";

    public static final String DOCUMENT_DOC_URL = "http://help.finereport.com/doc-view-";

    public static final String DOCUMENT_SEARCH_URL = "http://help.finereport.com/?api-search-title-";

    public static final String DOCUMENT_INFORMATION_URL = "http://help.finereport.com/?api-search-did-";

    public static final String PLUGIN_IMAGE_URL = "http://shopres.finereport.com/";

    public static final String SERVER_URL = "http://cloud.fanruan.com/api/monitor/record_of_afsearch/single";

    //TODO:先用本地服务器测试一下，避免影响云中心正常信息收集
    public static final String CLOUD_TEST_URL = "http://localhost:8080/monitor/monitor/record_of_afsearch/single";

}
