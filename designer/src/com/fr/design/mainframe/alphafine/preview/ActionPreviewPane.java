package com.fr.design.mainframe.alphafine.preview;


import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.general.IOUtils;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;


/**
 * Created by XiaXiang on 2017/5/5.
 */
public class ActionPreviewPane extends JPanel {
    private static final Font NAME = new Font("Song_TypeFace", 0, 14);

    public ActionPreviewPane() {
        setLayout(new BorderLayout());
        setBackground(null);
        setBorder(BorderFactory.createEmptyBorder(135, 0, 0, 0));
        UILabel image = new UILabel();
        image.setPreferredSize(new Dimension(150, 111));
        image.setHorizontalAlignment(SwingConstants.CENTER);
        image.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        image.setIcon(IOUtils.readIcon("/com/fr/design/mainframe/alphafine/images/noresult.png"));
        UILabel description = new UILabel(Inter.getLocText("FR-Designer_NoResult"));
        description.setForeground(AlphaFineConstants.MEDIUM_GRAY);
        description.setFont(NAME);
        description.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        description.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(image, BorderLayout.CENTER);
        this.add(description, BorderLayout.SOUTH);
    }

}
