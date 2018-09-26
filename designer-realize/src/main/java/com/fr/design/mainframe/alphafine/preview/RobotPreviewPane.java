package com.fr.design.mainframe.alphafine.preview;


import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.itextarea.UITextArea;
import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.design.mainframe.alphafine.cell.model.AlphaCellModel;
import com.fr.design.mainframe.alphafine.cell.model.RobotModel;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.StringUtils;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;


/**
 * Created by alex.sung on 2018/8/3.
 */
public class RobotPreviewPane extends JPanel {

    private static final int TITLE_AREA_HEIGHT = 15;
    private static final int HOT_TITLE_AREA_HEIGHT = 30;
    private static final String STRING_ENCODER = "UTF-8";

    public RobotPreviewPane(AlphaCellModel model, String content) {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(AlphaFineConstants.RIGHT_WIDTH, AlphaFineConstants.CONTENT_HEIGHT));
        UITextArea titleArea = new UITextArea(model.getName());
        titleArea.setBorder(null);
        titleArea.setEditable(false);
        titleArea.setForeground(AlphaFineConstants.BLUE);
        titleArea.setFont(AlphaFineConstants.LARGE_FONT);
        add(titleArea, BorderLayout.NORTH);

        final JEditorPane editorPane = new JEditorPane();
        editorPane.setEditorKit(new HTMLEditorKit());
        editorPane.setText(content + "<br><br><br>");
        editorPane.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() != HyperlinkEvent.EventType.ACTIVATED) {
                    return;
                }
                if (e.getDescription().startsWith(AlphaFineConstants.JAVASCRIPT_PREFIX)) {
                    String s = e.getDescription().replaceAll(AlphaFineConstants.JAVASCRIPT_PREFIX, StringUtils.EMPTY)
                            .replaceAll("\\('", StringUtils.EMPTY)
                            .replaceAll("'\\)", StringUtils.EMPTY);
                    try {
                        Desktop.getDesktop().browse(new URI(AlphaFineConstants.ALPHA_PREVIEW + URLEncoder.encode(s, STRING_ENCODER)));
                    } catch (IOException e1) {
                        FineLoggerFactory.getLogger().error(e1.getMessage());
                    } catch (URISyntaxException e1) {
                        FineLoggerFactory.getLogger().error(e1.getMessage());
                    }
                }
                URL linkUrl = e.getURL();
                if (linkUrl != null) {
                    try {
                        Desktop.getDesktop().browse(e.getURL().toURI());
                    } catch (IOException | URISyntaxException e1) {
                        FineLoggerFactory.getLogger().error(" Jump to webpage error: " + e1.getMessage());
                    }
                }
            }
        });
        editorPane.setEditable(false);
        UIScrollPane jScrollPane = new UIScrollPane(editorPane);
        jScrollPane.getVerticalScrollBar().setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
        jScrollPane.setBorder(BorderFactory.createMatteBorder(5, 10, 0, 10, Color.white));
        if (((RobotModel) model).isHotItemModel()) {
            this.setPreferredSize(new Dimension(AlphaFineConstants.RIGHT_WIDTH, AlphaFineConstants.CONTENT_HEIGHT - HOT_TITLE_AREA_HEIGHT));
        } else {
            this.setPreferredSize(new Dimension(AlphaFineConstants.RIGHT_WIDTH, AlphaFineConstants.CONTENT_HEIGHT - TITLE_AREA_HEIGHT));
        }
        add(jScrollPane, BorderLayout.CENTER);
    }
}
