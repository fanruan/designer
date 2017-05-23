package com.fr.design.mainframe.alphafine.preview;


import com.fr.design.gui.itextarea.UITextArea;
import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.design.utils.gui.GUICoreUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Created by XiaXiang on 2017/5/2.
 */
public class DocumentPreviewPane extends JPanel {

    public DocumentPreviewPane(String title, String summary) {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.white);
        UITextArea titleArea = new UITextArea(title);
        UITextArea contentArea = new UITextArea(summary);
        titleArea.setOpaque(false);
        contentArea.setOpaque(false);
        titleArea.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        titleArea.setForeground(AlphaFineConstants.BLUE);
        contentArea.setForeground(AlphaFineConstants.BLACK);
        titleArea.setPreferredSize(new Dimension(360, 30));
        titleArea.setFont(AlphaFineConstants.LARGE_FONT);
        contentArea.setFont(AlphaFineConstants.MEDIUM_FONT);
        add(titleArea, BorderLayout.NORTH);
        add(contentArea, BorderLayout.CENTER);
    }


    public static void main(String[] args) {
        JFrame jf = new JFrame("test");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel content = (JPanel) jf.getContentPane();
        content.setLayout(null);

        content.add(new DocumentPreviewPane("test", "ababababaabbababab"));
        GUICoreUtils.centerWindow(jf);
        jf.setSize(400, 400);
        jf.setVisible(true);
    }
}
