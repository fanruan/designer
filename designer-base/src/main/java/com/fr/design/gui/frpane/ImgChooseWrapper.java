package com.fr.design.gui.frpane;

import com.fr.base.BaseUtils;
import com.fr.base.Style;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.style.background.image.ImageFileChooser;
import com.fr.design.style.background.image.ImagePreviewer;
import com.fr.design.utils.ImageUtils;
import com.fr.general.ImageWithSuffix;
import com.fr.stable.CoreGraphHelper;
import com.fr.stable.StringUtils;

import javax.swing.JFileChooser;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * 图片选择框包装类
 * Created by zack on 2018/3/9.
 */
public class ImgChooseWrapper {
    private ImagePreviewer previewPane = null;

    private ImageFileChooser imageFileChooser = null;

    private Style imageStyle = null;

    private SwingWorker<Void, Void> imageWorker;
    private ChangeListener changeListener;

    private transient Image selectImage;
    private UILabel imageSizeLabel;

    public static ImgChooseWrapper getInstance(ImagePreviewer previewPane, ImageFileChooser imageFileChooser, Style imageStyle) {
        return getInstance(previewPane, imageFileChooser, imageStyle, null);
    }

    public static ImgChooseWrapper getInstance(ImagePreviewer previewPane, ImageFileChooser imageFileChooser, Style imageStyle, ChangeListener changeListener) {
        return new ImgChooseWrapper(previewPane, imageFileChooser, imageStyle, changeListener, null, null);
    }

    public static ImgChooseWrapper getInstance(Image selectImage, UILabel imageSizeLabel, ImageFileChooser imageFileChooser) {
        return new ImgChooseWrapper(null, imageFileChooser, null, null, selectImage, imageSizeLabel);
    }

    private ImgChooseWrapper(ImagePreviewer previewPane, ImageFileChooser imageFileChooser, Style imageStyle, ChangeListener changeListener, Image selectImage, UILabel imageSizeLabel) {
        this.previewPane = previewPane;
        this.imageFileChooser = imageFileChooser;
        this.imageStyle = imageStyle;
        this.changeListener = changeListener;
        this.selectImage = selectImage;
        this.imageSizeLabel = imageSizeLabel;
    }


    public void setPreviewPane(ImagePreviewer previewPane) {
        this.previewPane = previewPane;
    }

    public ImageFileChooser getImageFileChooser() {
        return imageFileChooser;
    }

    public void setImageFileChooser(ImageFileChooser imageFileChooser) {
        this.imageFileChooser = imageFileChooser;
    }

    public Style getImageStyle() {
        return imageStyle;
    }

    public void setImageStyle(Style imageStyle) {
        this.imageStyle = imageStyle;
    }

    public SwingWorker<Void, Void> getImageWorker() {
        return imageWorker;
    }

    public void setImageWorker(SwingWorker<Void, Void> imageWorker) {
        this.imageWorker = imageWorker;
    }

    public void dealWithImageFile(int returnVal) {
        if (returnVal != JFileChooser.CANCEL_OPTION) {
            final File selectedFile = imageFileChooser.getSelectedFile();

            if (selectedFile != null && selectedFile.isFile()) {
                if (previewPane != null) {
                    previewPane.showLoading();
                }
                if (imageWorker != null && !imageWorker.isDone()) {
                    imageWorker = null;
                }
                imageWorker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        ImageWithSuffix imageWithSuffix = null;
                        if (imageFileChooser.isCheckSelected()) {
                            imageWithSuffix = ImageUtils.defaultImageCompWithSuff(selectedFile);
                        } else {
                            BufferedImage image = BaseUtils.readImage(selectedFile.getPath());
                            String type = ImageUtils.getImageType(selectedFile);
                            imageWithSuffix = new ImageWithSuffix(image, type);
                        }

                        CoreGraphHelper.waitForImage(imageWithSuffix);

                        if (previewPane != null) {
                            previewPane.setImageStyle(imageStyle);
                            previewPane.setImageWithSuffix(imageWithSuffix);
                            previewPane.repaint();
                        }
                        checkLabelText();
                        fireChangeListener();
                        return null;
                    }
                };
                imageWorker.execute();
            } else {
                if (previewPane != null) {
                    previewPane.setImage(null);
                }

            }
            if (previewPane != null) {
                previewPane.repaint();
            }
        }
    }

    private void fireChangeListener() {
        if (this.changeListener != null) {
            ChangeEvent evt = new ChangeEvent(this);
            this.changeListener.stateChanged(evt);
        }
    }

    private void checkLabelText() {
        if (imageSizeLabel == null) {
            return;
        }
        if (selectImage == null) {
            imageSizeLabel.setText(StringUtils.EMPTY);
        } else {
            imageSizeLabel.setText(selectImage.getWidth(null) + "x"
                    + selectImage.getHeight(null) + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Px"));
        }
    }
}
