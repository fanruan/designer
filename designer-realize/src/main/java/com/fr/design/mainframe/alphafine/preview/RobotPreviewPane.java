package com.fr.design.mainframe.alphafine.preview;


import com.fr.design.gui.itextarea.UITextArea;
import com.fr.design.mainframe.alphafine.AlphaFineConstants;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;


/**
 * Created by alex.sung on 2018/8/3.
 */
public class RobotPreviewPane extends JPanel {

    public RobotPreviewPane(String title, String content) {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(AlphaFineConstants.RIGHT_WIDTH, AlphaFineConstants.CONTENT_HEIGHT));
        UITextArea titleArea = new UITextArea(title);
        titleArea.setBorder(null);
        titleArea.setEditable(false);
        titleArea.setForeground(AlphaFineConstants.BLUE);
        titleArea.setFont(AlphaFineConstants.LARGE_FONT);
        add(titleArea, BorderLayout.NORTH);

        JEditorPane editorPane = new JEditorPane();
        editorPane.setEditorKit(new HTMLEditorKit());
        editorPane.setText(content);
        editorPane.setEditable(false);
        JScrollPane jScrollPane = new JScrollPane(editorPane);
        jScrollPane.setBorder(BorderFactory.createMatteBorder(5, 10, 0, 10, Color.white));
        add(jScrollPane, BorderLayout.CENTER);
    }
}
