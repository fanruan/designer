package com.fr.design.style.background.impl;

import com.fr.base.Style;
import com.fr.base.background.ImageBackground;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.general.Background;
import com.fr.general.Inter;
import com.fr.stable.Constants;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ibm on 2017/1/5.
 */
public class ImageButtonBackgroundPane extends ImageBackgroundPane {
    private UIButton chooseButton;
    private UIButton clearButton;

    public ImageButtonBackgroundPane(){
        super();
        Style imageStyle = Style.DEFAULT_STYLE.deriveImageLayout(Constants.IMAGE_CENTER);
        previewPane.setImageStyle(imageStyle);
    }

    public JPanel initSelectFilePane(){

        JPanel choosePane = new JPanel(new BorderLayout(0, 10));
        choosePane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        JPanel choosePane1 = new JPanel(new BorderLayout(0, 10));
        initButton();

        choosePane.add(chooseButton, BorderLayout.NORTH);

        choosePane1.add(clearButton,BorderLayout.NORTH);
        choosePane.add(choosePane1,BorderLayout.CENTER);

        imageSizeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        choosePane1.add(imageSizeLabel,BorderLayout.CENTER);
        this.add(choosePane,BorderLayout.EAST);

        return choosePane;
    }

    private void initButton() {
        chooseButton = new UIButton(Inter.getLocText("FR-Designer_Background_Image_Select"));
        chooseButton.addActionListener(selectPictureActionListener);
        clearButton = new UIButton(Inter.getLocText("FR-Designer_Background_Clear"));
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                previewPane.setImage(null);
                previewPane.repaint();
            }
        });
    }

    public void imageStyleRepaint(){

    }


    public void populate(Background background) {
        if(background != null && background instanceof ImageBackground){
            ImageBackground imageBackground = (ImageBackground) background;
            if(imageBackground.getImage() != null) {
                previewPane.setImage(imageBackground.getImage());
            }
        }

    }

    public Background update() {
        if(previewPane.getImage() == null) {
            return null;
        }
        return new ImageBackground(previewPane.getImage());
    }
}
