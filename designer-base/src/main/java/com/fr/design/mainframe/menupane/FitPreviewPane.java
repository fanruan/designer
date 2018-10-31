package com.fr.design.mainframe.menupane;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.IOUtils;

import javax.swing.ImageIcon;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/5/0005.
 */
public class FitPreviewPane extends BasicPane {
    private static final String DEFAULT_FONT_TAG = "00";
    private static final String DEFAULT_TAG = "01";
    private static final String HORIZON_FONT_TAG = "10";
    private static final String HORIZON_TAG = "11";
    private static final String DOUBLE_FONT_TAG = "20";
    private static final String DOUBLE_TAG = "21";
    private static final String NOT_FONT_TAG = "30";
    private static final String NOT_TAG = "31";

    private UILabel imageLabel;
    private Map<String, ImageIcon> cachedPreviewImage = new HashMap<String, ImageIcon>();
    private Map<String, ImageIcon> globalCachedPreviewImage = new HashMap<String, ImageIcon>();

    public FitPreviewPane() {
        //初始化缓存图片, 有些无意义的组合.
        initCacheImage();
        //初始化组件
        initComponents();
    }

    //默认和不自适应时，字体自适应不起效，只有6张图
    private void initCacheImage() {
        globalCachedPreviewImage.put(DEFAULT_FONT_TAG, new ImageIcon(IOUtils.readImage("/com/fr/design/images/reportfit/preview/gray/" + DEFAULT_FONT_TAG + ".png")));
        globalCachedPreviewImage.put(DEFAULT_TAG, new ImageIcon(IOUtils.readImage("/com/fr/design/images/reportfit/preview/gray/" + DEFAULT_FONT_TAG + ".png")));
        globalCachedPreviewImage.put(HORIZON_FONT_TAG, new ImageIcon(IOUtils.readImage("/com/fr/design/images/reportfit/preview/gray/" + HORIZON_FONT_TAG + ".png")));
        globalCachedPreviewImage.put(HORIZON_TAG, new ImageIcon(IOUtils.readImage("/com/fr/design/images/reportfit/preview/gray/" + HORIZON_TAG + ".png")));
        globalCachedPreviewImage.put(DOUBLE_FONT_TAG, new ImageIcon(IOUtils.readImage("/com/fr/design/images/reportfit/preview/gray/" + DOUBLE_FONT_TAG + ".png")));
        globalCachedPreviewImage.put(DOUBLE_TAG, new ImageIcon(IOUtils.readImage("/com/fr/design/images/reportfit/preview/gray/" + DOUBLE_TAG + ".png")));
        globalCachedPreviewImage.put(NOT_FONT_TAG, new ImageIcon(IOUtils.readImage("/com/fr/design/images/reportfit/preview/gray/" + NOT_FONT_TAG + ".png")));
        globalCachedPreviewImage.put(NOT_TAG, new ImageIcon(IOUtils.readImage("/com/fr/design/images/reportfit/preview/gray/" + NOT_FONT_TAG + ".png")));
        cachedPreviewImage.put(DEFAULT_FONT_TAG, new ImageIcon(IOUtils.readImage("/com/fr/design/images/reportfit/preview/" + DEFAULT_FONT_TAG + ".png")));
        cachedPreviewImage.put(DEFAULT_TAG, new ImageIcon(IOUtils.readImage("/com/fr/design/images/reportfit/preview/" + DEFAULT_FONT_TAG + ".png")));
        cachedPreviewImage.put(HORIZON_FONT_TAG, new ImageIcon(IOUtils.readImage("/com/fr/design/images/reportfit/preview/" + HORIZON_FONT_TAG + ".png")));
        cachedPreviewImage.put(HORIZON_TAG, new ImageIcon(IOUtils.readImage("/com/fr/design/images/reportfit/preview/" + HORIZON_TAG + ".png")));
        cachedPreviewImage.put(DOUBLE_FONT_TAG, new ImageIcon(IOUtils.readImage("/com/fr/design/images/reportfit/preview/" + DOUBLE_FONT_TAG + ".png")));
        cachedPreviewImage.put(DOUBLE_TAG, new ImageIcon(IOUtils.readImage("/com/fr/design/images/reportfit/preview/" + DOUBLE_TAG + ".png")));
        cachedPreviewImage.put(NOT_FONT_TAG, new ImageIcon(IOUtils.readImage("/com/fr/design/images/reportfit/preview/" + NOT_FONT_TAG + ".png")));
        cachedPreviewImage.put(NOT_TAG, new ImageIcon(IOUtils.readImage("/com/fr/design/images/reportfit/preview/" + NOT_FONT_TAG + ".png")));
    }

    private void initComponents() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        imageLabel = new UILabel();
        imageLabel.setIcon(cachedPreviewImage.get(DEFAULT_TAG));
        this.add(imageLabel);
    }

    public void refreshPreview(String index, boolean isEditedable) {
        ImageIcon newImageIcon = isEditedable ? cachedPreviewImage.get(index) : globalCachedPreviewImage.get(index);
        imageLabel.setIcon(newImageIcon);
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("FR-Plugin_Preview");
    }


}
