package com.fr.design.mainframe.alphafine.preview;


import com.fr.design.gui.itextarea.UITextArea;
import com.fr.design.mainframe.alphafine.AlphaFineConstants;

import javax.swing.*;
import java.awt.*;

/**
 * Created by XiaXiang on 2017/5/2.
 */
public class DocumentPreviewPane extends JPanel {

    public DocumentPreviewPane(String title, String summary) {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(AlphaFineConstants.RIGHT_WIDTH, AlphaFineConstants.CONTENT_HEIGHT));
        UITextArea titleArea = new UITextArea(title);
        titleArea.setBorder(null);
        titleArea.setEditable(false);
        titleArea.setForeground(AlphaFineConstants.BLUE);
        titleArea.setFont(AlphaFineConstants.LARGE_FONT);
        add(titleArea, BorderLayout.NORTH);
        UITextArea contentArea = new UITextArea(summary);
        contentArea.setEditable(false);
        contentArea.setBorder(null);
        contentArea.setForeground(AlphaFineConstants.BLACK);
        contentArea.setFont(AlphaFineConstants.MEDIUM_FONT);
        add(contentArea, BorderLayout.CENTER);
    }
}
