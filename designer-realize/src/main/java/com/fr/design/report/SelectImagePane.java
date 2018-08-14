package com.fr.design.report;

import com.fr.base.BaseUtils;
import com.fr.base.Style;
import com.fr.base.frpx.pack.PictureCollection;
import com.fr.base.frpx.util.ImageIOHelper;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.style.background.image.ImageFileChooser;
import com.fr.design.style.background.image.ImagePreviewPane;
import com.fr.design.utils.gui.GUICoreUtils;

import com.fr.report.cell.Elem;
import com.fr.report.cell.cellattr.CellImage;
import com.fr.report.cell.painter.CellImagePainter;
import com.fr.stable.Constants;
import com.fr.stable.CoreGraphHelper;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * 这个类主要用于插入图片时的设置
 */
public class SelectImagePane extends BasicPane {
    private ImagePreviewPane previewPane = null;

    private ImageFileChooser imageFileChooser = null;
    private UIRadioButton defaultRadioButton = null;
    private UIRadioButton tiledRadioButton = null;
    private UIRadioButton extendRadioButton = null;
    private UIRadioButton adjustRadioButton = null;

    private Style imageStyle = null;

    private Image previewImage = null;

    /**
     * 默认格式
     */
    private String suffix = PictureCollection.DEFAULT_SUFFIX;

    private File imageFile;

    public SelectImagePane() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        // preview pane
        JPanel previewContainerPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        this.add(previewContainerPane, BorderLayout.CENTER);

        JPanel previewOwnerPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        previewContainerPane.add(previewOwnerPane, BorderLayout.CENTER);

        previewOwnerPane.setBorder(GUICoreUtils.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Preview"), null));

        previewPane = new ImagePreviewPane();
        previewOwnerPane.add(new JScrollPane(previewPane));

        JPanel selectFilePane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        previewContainerPane.add(selectFilePane, BorderLayout.EAST);
        selectFilePane.setBorder(BorderFactory
                .createEmptyBorder(8, 2, 4, 0));

        UIButton selectPictureButton = new UIButton(
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Image_Select_Picture"));
        selectFilePane.add(selectPictureButton, BorderLayout.NORTH);
        selectPictureButton.setMnemonic('S');
        selectPictureButton.addActionListener(selectPictureActionListener);

        JPanel layoutPane = FRGUIPaneFactory.createMediumHGapHighTopFlowInnerContainer_M_Pane();
        selectFilePane.add(layoutPane, BorderLayout.CENTER);

        //布局
        defaultRadioButton = new UIRadioButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Default"));
        tiledRadioButton = new UIRadioButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Image_Titled"));
        extendRadioButton = new UIRadioButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Image_Extend"));
        adjustRadioButton = new UIRadioButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Image_Adjust"));

        defaultRadioButton.addActionListener(layoutActionListener);
        tiledRadioButton.addActionListener(layoutActionListener);
        extendRadioButton.addActionListener(layoutActionListener);
        adjustRadioButton.addActionListener(layoutActionListener);

        JPanel jp = new JPanel(new GridLayout(4, 1, 15, 15));
        jp.add(defaultRadioButton);
        jp.add(tiledRadioButton);
        jp.add(extendRadioButton);
        jp.add(adjustRadioButton);
        layoutPane.add(jp);

        ButtonGroup layoutBG = new ButtonGroup();
        layoutBG.add(defaultRadioButton);
        layoutBG.add(tiledRadioButton);
        layoutBG.add(extendRadioButton);
        layoutBG.add(adjustRadioButton);

        defaultRadioButton.setSelected(true);

        // init image file chooser.
        imageFileChooser = new ImageFileChooser();
        imageFileChooser.setMultiSelectionEnabled(false);
    }

    /**
     * Select picture.
     */
    ActionListener selectPictureActionListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent evt) {
            int returnVal = imageFileChooser
                    .showOpenDialog(SelectImagePane.this);
            if (returnVal != JFileChooser.CANCEL_OPTION) {
                File selectedFile = imageFileChooser.getSelectedFile();

                if (selectedFile != null && selectedFile.isFile()) {
                    String filePath = selectedFile.getPath();
                    suffix = ImageIOHelper.getSuffix(filePath);
                    Image image = BaseUtils.readImage(filePath);
                    CoreGraphHelper.waitForImage(image);

                    imageFile = selectedFile;
                    setImageStyle();
                    previewPane.setImage(image);
                    previewPane.setImageStyle(imageStyle);
                    previewImage = image;
                } else {
                    previewPane.setImage(null);
                }
                previewPane.repaint();
            }
        }
    };

    // 调整图片样式，只有水平和垂直对齐以及拉伸。相对于背景，平铺不予考虑。
    private void changeImageStyle() {
        previewPane.setImageStyle(this.imageStyle);
        previewPane.repaint();
    }

    private void setImageStyle() {
        if (tiledRadioButton.isSelected()) {
            imageStyle = Style.DEFAULT_STYLE.deriveImageLayout(Constants.IMAGE_TILED);
        } else if (adjustRadioButton.isSelected()) {
            imageStyle = Style.DEFAULT_STYLE.deriveImageLayout(Constants.IMAGE_ADJUST);
        } else if (extendRadioButton.isSelected()) {
            imageStyle = Style.DEFAULT_STYLE.deriveImageLayout(Constants.IMAGE_EXTEND);
        } else {
            imageStyle = Style.DEFAULT_STYLE.deriveImageLayout(Constants.IMAGE_CENTER);
        }
    }

    ActionListener layoutActionListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent evt) {
            setImageStyle();
            changeImageStyle();
        }
    };

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Image");
    }

    public void populate(Elem cell) {
        Style style = null;
        if (cell != null) {
            Object value = cell.getValue();
            if (value instanceof Image) {
                setImage((Image) value);
            } else if (value instanceof CellImagePainter) {
                setImage(((CellImagePainter) value).getImage());
                suffix = ((CellImagePainter) value).getSuffix();
            }

            style = cell.getStyle();
        }
        if (style == null) {
            return;
        }

        if (style.getImageLayout() == Constants.IMAGE_TILED) {
            tiledRadioButton.setSelected(true);
        } else if (style.getImageLayout() == Constants.IMAGE_EXTEND) {
            extendRadioButton.setSelected(true);
        } else if (style.getImageLayout() == Constants.IMAGE_ADJUST) {
            adjustRadioButton.setSelected(true);
        } else {
            style.deriveImageLayout(Constants.IMAGE_CENTER);
            defaultRadioButton.setSelected(true);
        }
        this.imageStyle = style;
        changeImageStyle();
    }

    public void setImage(Image image) {
        previewPane.setImage(image);
        this.previewImage = image;
    }

    public CellImage update() {
        CellImage cellImage = new CellImage();
        cellImage.setImage(previewPane.getImage());
        cellImage.setStyle(this.imageStyle);
        if (suffix != null) {
            cellImage.setSuffix(suffix);
        }
        return cellImage;
    }

    public File getSelectedImage() {
        return imageFile;
    }
}

