package com.fr.design.mainframe.alphafine.preview;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.general.IOUtils;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

/**
 * Created by XiaXiang on 2017/8/14.
 */
public class DefaulPane extends JPanel {
    private static final Font NAME = new Font("Song_TypeFace", 0, 14);
    public DefaulPane() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(135, 80, 135, 80));
        UILabel image = new UILabel();
        image.setPreferredSize(new Dimension(150, 111));
        image.setHorizontalAlignment(SwingConstants.CENTER);
        image.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        image.setIcon(IOUtils.readIcon("/com/fr/design/mainframe/alphafine/images/no_result.png"));
        UILabel description = new UILabel(Inter.getLocText("FR-Designer-AlphaFine_NO_Result"));
        description.setForeground(AlphaFineConstants.MEDIUM_GRAY);
        description.setFont(NAME);
        description.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        description.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(image, BorderLayout.CENTER);
        this.add(description, BorderLayout.SOUTH);
        UILabel splitLine = new UILabel();
        splitLine.setBackground(AlphaFineConstants.GRAY);
        splitLine.setPreferredSize(new Dimension(10, AlphaFineConstants.CONTENT_HEIGHT));
        this.add(splitLine, BorderLayout.EAST);
    }
}
