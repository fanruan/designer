package com.fr.design.style.background.image;

import com.fr.base.background.ImageFileBackground;
import com.fr.base.frpx.pack.PictureCollection;
import com.fr.base.frpx.util.ImageIOHelper;
import com.fr.design.gui.frpane.ImgChooseWrapper;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.style.AlphaPane;
import com.fr.design.style.background.BackgroundPane4BoxChange;
import com.fr.general.Background;

import com.fr.stable.Constants;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


/**
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2011-11-1 下午03:50:02
 * 类说明 : 背景图片的新选择界面 , UIComboBox切换的分支pane. bug原型图@5471
 */
public class ImageSelectPane extends BackgroundPane4BoxChange {
    private static final long serialVersionUID = -3938766570998917557L;
    private static String layoutCenter = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Default");
    private static String layoutTitled = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Image_Titled");

    private String suffix = PictureCollection.DEFAULT_SUFFIX;

    //产品设计说：只需要居中（默认的）和平铺两种方式
    private static final String[] layoutTypes = {
            layoutCenter,
            layoutTitled,
    };

    private UIComboBox layoutComboBox;

    private ImageFileChooser imageFileChooser = null;
    private UILabel imageSizeLabel = new UILabel();

    private AlphaPane alphaPane;

    private transient Image selectImage;

    public ImageSelectPane() {
        this.setLayout(new BorderLayout());

        JPanel pane = FRGUIPaneFactory.createYBoxEmptyBorderPane();
        this.add(pane, BorderLayout.CENTER);

        JPanel testPane1 = new JPanel();
        testPane1.setLayout(new BorderLayout());

        JPanel selectFilePane = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
        testPane1.add(selectFilePane, BorderLayout.CENTER);
        pane.add(testPane1);

        // 选择图片按钮
        UIButton selectPictureButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Image_Select_Picture"));
        selectFilePane.add(selectPictureButton);

        selectPictureButton.setPreferredSize(new Dimension(110, 20));

        imageFileChooser = new ImageFileChooser();
        imageFileChooser.setMultiSelectionEnabled(false);
        selectPictureButton.setMnemonic('S');
        selectPictureButton.addActionListener(selectPictureActionListener);

        //布局
        selectFilePane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Form_Layout") + ":"));
        layoutComboBox = new UIComboBox(layoutTypes);
        selectFilePane.add(layoutComboBox);

        layoutComboBox.setPreferredSize(new Dimension(60, 20));

        selectFilePane.add(alphaPane = new AlphaPane());
        // image size label.

        JPanel testPane = new JPanel();
        testPane.setLayout(new BorderLayout());
        JPanel southImagePane = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
        testPane.add(southImagePane, BorderLayout.CENTER);

        pane.add(testPane);

        imageSizeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        southImagePane.add(imageSizeLabel);
        imageSizeLabel.setPreferredSize(new Dimension(100, 20));
    }

    ActionListener selectPictureActionListener = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            int returnVal = imageFileChooser.showOpenDialog(ImageSelectPane.this);
            if (returnVal != JFileChooser.CANCEL_OPTION) {
                File selectedFile = imageFileChooser.getSelectedFile();
                String path = selectedFile.getPath();
                suffix = ImageIOHelper.getSuffix(path);
                ImgChooseWrapper.getInstance(selectImage, imageSizeLabel, imageFileChooser).dealWithImageFile(returnVal);
            }
        }
    };

    private void chechLabelText() {
        if (selectImage == null) {
            imageSizeLabel.setText("");
        } else {
            imageSizeLabel.setText(selectImage.getWidth(null) + "x"
                    + selectImage.getHeight(null) + com.fr.design.i18n.Toolkit.i18nText("px"));
        }
    }

    public void populate(Background background) {

        if (background instanceof ImageFileBackground) {
            ImageFileBackground imageBackground = (ImageFileBackground) background;
            selectImage = imageBackground.getImage();
            suffix = imageBackground.getUri();

            if (imageBackground.getLayout() == Constants.IMAGE_TILED) {
                layoutComboBox.setSelectedItem(layoutTitled);
            } else {
                layoutComboBox.setSelectedItem(layoutCenter);
            }
        }

        chechLabelText();
    }

    public void populateAlpha(int alpha) {
        alphaPane.populate(alpha);
    }

    public float updateAlpha() {
        return alphaPane.update();
    }

    public Background update() {
        ImageFileBackground imageBackground = new ImageFileBackground(selectImage, suffix);

        Object selectLayout = layoutComboBox.getSelectedItem();
        if (selectLayout.equals(layoutCenter)) {
            imageBackground.setLayout(Constants.IMAGE_CENTER);
        } else if (selectLayout.equals(layoutTitled)) {
            imageBackground.setLayout(Constants.IMAGE_TILED);
        }

        return imageBackground;
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Image_Select_Picture");
    }
}
