package com.fr.design.style.background.impl;

import com.fr.base.Style;
import com.fr.base.background.ImageBackground;
import com.fr.base.background.ImageFileBackground;
import com.fr.design.gui.frpane.ImgChooseWrapper;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.style.background.BackgroundDetailPane;
import com.fr.design.style.background.image.ImageFileChooser;
import com.fr.design.style.background.image.ImagePreviewPane;
import com.fr.general.Background;

import com.fr.stable.Constants;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Image background pane.
 */
public class ImageBackgroundPane extends BackgroundDetailPane {

    protected ImagePreviewPane previewPane = null;
    private Style imageStyle = null;
    private ChangeListener changeListener = null;
    private ImageFileChooser imageFileChooser = null;
    protected UILabel imageSizeLabel = new UILabel();

    protected UIRadioButton defaultRadioButton = null;
    protected UIRadioButton tiledRadioButton = null;
    private UIRadioButton extendRadioButton = null;
    private UIRadioButton adjustRadioButton = null;

    public ImageBackgroundPane() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        // preview pane
        JPanel previewContainerPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
        this.add(previewContainerPane, BorderLayout.CENTER);

        JPanel previewOwnerPane = FRGUIPaneFactory.createTitledBorderPane(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Preview"));
        previewOwnerPane.setLayout(new BorderLayout());
        previewContainerPane.add(previewOwnerPane, BorderLayout.CENTER);
        previewContainerPane.add(initSelectFilePane(), BorderLayout.EAST);
        previewPane = new ImagePreviewPane();
        previewOwnerPane.add(new JScrollPane(previewPane));
        previewPane.addChangeListener(imageSizeChangeListener);


        // init image file chooser.
        imageFileChooser = new ImageFileChooser();
        imageFileChooser.setMultiSelectionEnabled(false);
    }

    public JPanel initSelectFilePane() {
        JPanel selectFilePane = FRGUIPaneFactory.createBorderLayout_L_Pane();

        selectFilePane.setBorder(BorderFactory.createEmptyBorder(8, 2, 4, 0));

        UIButton selectPictureButton = new UIButton(
                com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Background_Image_Select"));
        selectFilePane.add(selectPictureButton, BorderLayout.NORTH);
        selectPictureButton.setMnemonic('S');
        selectPictureButton.addActionListener(selectPictureActionListener);
        JPanel layoutPane = FRGUIPaneFactory.createMediumHGapHighTopFlowInnerContainer_M_Pane();
        selectFilePane.add(layoutPane, BorderLayout.CENTER);

        //布局
        defaultRadioButton = new UIRadioButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Alignment_Layout_Default"));
        tiledRadioButton = new UIRadioButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Alignment_Layout_Image_Titled"));
        extendRadioButton = new UIRadioButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Alignment_Layout_Image_Extend"));
        adjustRadioButton = new UIRadioButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Alignment_Layout_Image_Adjust"));

        defaultRadioButton.addActionListener(layoutActionListener);
        tiledRadioButton.addActionListener(layoutActionListener);
        extendRadioButton.addActionListener(layoutActionListener);
        adjustRadioButton.addActionListener(layoutActionListener);

        JPanel jp = new JPanel(new GridLayout(4, 1, 15, 15));
        for (UIRadioButton button : imageLayoutButtons()) {
            jp.add(button);
        }
        layoutPane.add(jp);

        ButtonGroup layoutBG = new ButtonGroup();
        layoutBG.add(defaultRadioButton);
        layoutBG.add(tiledRadioButton);
        layoutBG.add(extendRadioButton);
        layoutBG.add(adjustRadioButton);

        defaultRadioButton.setSelected(true);
        return selectFilePane;
    }

    protected UIRadioButton[] imageLayoutButtons() {
        return new UIRadioButton[]{
                defaultRadioButton,
                tiledRadioButton,
                extendRadioButton,
                adjustRadioButton
        };
    }

    /**
     * Select picture.
     */
    ActionListener selectPictureActionListener = new ActionListener() {

        public void actionPerformed(ActionEvent evt) {
            int returnVal = imageFileChooser.showOpenDialog(ImageBackgroundPane.this);
            setImageStyle();
            ImgChooseWrapper.getInstance(previewPane, imageFileChooser, imageStyle, changeListener).dealWithImageFile(returnVal);
        }
    };

    protected void setImageStyle() {
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

        private void changeImageStyle() {
            previewPane.setImageStyle(ImageBackgroundPane.this.imageStyle);
            previewPane.repaint();
        }
    };

    @Override
    public void populate(Background background) {

        if (background instanceof ImageBackground) {
            ImageBackground imageBackground = (ImageBackground) background;

            if (imageBackground.getLayout() == Constants.IMAGE_CENTER) {
                defaultRadioButton.setSelected(true);
                imageStyle = Style.DEFAULT_STYLE.deriveImageLayout(Constants.IMAGE_CENTER);
            } else if (imageBackground.getLayout() == Constants.IMAGE_EXTEND) {
                extendRadioButton.setSelected(true);
                imageStyle = Style.DEFAULT_STYLE.deriveImageLayout(Constants.IMAGE_EXTEND);
            } else if (imageBackground.getLayout() == Constants.IMAGE_ADJUST) {
                adjustRadioButton.setSelected(true);
                imageStyle = Style.DEFAULT_STYLE.deriveImageLayout(Constants.IMAGE_ADJUST);
            } else {
                tiledRadioButton.setSelected(true);
                imageStyle = Style.DEFAULT_STYLE.deriveImageLayout(Constants.IMAGE_TILED);
            }

            previewPane.setImageStyle(ImageBackgroundPane.this.imageStyle);
            if (imageBackground.getImage() != null) {
                previewPane.setImageWithSuffix(imageBackground.getImageWithSuffix());
                imageSizeLabel.setText(previewPane.getImage().getWidth(null)
                        + " X " + previewPane.getImage().getHeight(null));
            }

            if (imageBackground.getImage() != null) {
                previewPane.setImage(imageBackground.getImage());
            }
        } else {
            previewPane.setImage(null);
            tiledRadioButton.setSelected(true);

            imageSizeLabel.setText("");
        }

        fireChagneListener();
    }

    @Override
    public Background update() throws Exception {
        ImageBackground imageBackground = new ImageFileBackground(previewPane.getImageWithSuffix());
        setImageStyle();
        imageBackground.setLayout(imageStyle.getImageLayout());
        return imageBackground;
    }

    @Override
    public void addChangeListener(ChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    private void fireChagneListener() {
        if (this.changeListener != null) {
            ChangeEvent evt = new ChangeEvent(this);
            this.changeListener.stateChanged(evt);
        }
    }

    ChangeListener imageSizeChangeListener = new ChangeListener() {

        @Override
        public void stateChanged(ChangeEvent evt) {
            Image image = ((ImagePreviewPane) evt.getSource()).getImage();

            if (image == null) {
                imageSizeLabel.setText("");
            } else {
                imageSizeLabel.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Size_Detail", image.getWidth(null) + "x" + image.getHeight(null)));
            }
        }
    };
}
