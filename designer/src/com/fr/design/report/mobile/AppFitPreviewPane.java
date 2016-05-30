package com.fr.design.report.mobile;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.IOUtils;
import com.fr.general.Inter;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by 夏翔 on 2016/5/28.
 */
public class AppFitPreviewPane extends BasicPane{

    private UILabel horizontalImageLabel;

    private UILabel verticalImagelabel;

    private ArrayList<ImageIcon> cachedVerticalPreviewImage = new ArrayList<ImageIcon>();

    private ArrayList<ImageIcon> cachedHorizonPreviewImage = new ArrayList<ImageIcon>();


    public AppFitPreviewPane() {
        //初始化缓存图片
        initCacheImage();
        //初始化组件
        initComponents();
    }

    private void initCacheImage() {
        cachedVerticalPreviewImage.add(new ImageIcon(IOUtils.readImage("/com/fr/base/images/dialog/appfit/" + "V0.png")));
        cachedVerticalPreviewImage.add(new ImageIcon(IOUtils.readImage("/com/fr/base/images/dialog/appfit/" + "V1.png")));
        cachedVerticalPreviewImage.add(new ImageIcon(IOUtils.readImage("/com/fr/base/images/dialog/appfit/" + "V2.png")));
        cachedVerticalPreviewImage.add(new ImageIcon(IOUtils.readImage("/com/fr/base/images/dialog/appfit/" + "V3.png")));
        cachedHorizonPreviewImage.add(new ImageIcon(IOUtils.readImage("/com/fr/base/images/dialog/appfit/" + "H0.png")));
        cachedHorizonPreviewImage.add(new ImageIcon(IOUtils.readImage("/com/fr/base/images/dialog/appfit/" + "H1.png")));
        cachedHorizonPreviewImage.add(new ImageIcon(IOUtils.readImage("/com/fr/base/images/dialog/appfit/" + "H2.png")));
        cachedHorizonPreviewImage.add(new ImageIcon(IOUtils.readImage("/com/fr/base/images/dialog/appfit/" + "H3.png")));
    }

    private void initComponents() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        JPanel outnorthPane = FRGUIPaneFactory.createTitledBorderPane(this.title4PopupWindow());
        this.add(outnorthPane);

        horizontalImageLabel = new UILabel();
        horizontalImageLabel.setIcon(cachedHorizonPreviewImage.get(1));
        outnorthPane.add(horizontalImageLabel);

        verticalImagelabel = new UILabel();
        verticalImagelabel.setIcon(cachedVerticalPreviewImage.get(0));
        outnorthPane.add(verticalImagelabel);
    }

    public void refreshPreview(int[] index) {
        ImageIcon newHorizonImageIcon =  cachedHorizonPreviewImage.get(index[0]) ;
        ImageIcon newVerticalImageIcon = cachedVerticalPreviewImage.get(index[1]);
        horizontalImageLabel.setIcon(newHorizonImageIcon);
        verticalImagelabel.setIcon(newVerticalImageIcon);

    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("FR-Plugin_Preview");
    }
}
