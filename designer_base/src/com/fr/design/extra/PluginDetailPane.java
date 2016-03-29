package com.fr.design.extra;

import com.fr.design.dialog.BasicPane;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.plugin.Plugin;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author richie
 * @date 2015-03-10
 * @since 8.0
 */
public class PluginDetailPane extends BasicPane {

    private JEditorPane textPane;

    public PluginDetailPane() {
        setLayout(new BorderLayout());
        PluginDescriptionLabel label = new PluginDescriptionLabel();
        label.setText(Inter.getLocText("FR-Designer-Plugin_Plugin_Description"));
        add(label, BorderLayout.NORTH);

        textPane = new JEditorPane();
        textPane.setContentType("text/html");
        textPane.setEditable(false);
        textPane.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() != HyperlinkEvent.EventType.ACTIVATED) {
                    return;
                }

                URL linkUrl = e.getURL();
                if (linkUrl != null) {
                    try {
                        Desktop.getDesktop().browse(linkUrl.toURI());
                    } catch (IOException | URISyntaxException e1) {
                        FRLogger.getLogger().error(e1.getMessage());
                    }
                }
            }

        });


        add(textPane, BorderLayout.CENTER);

    }

    public void populate(Plugin plugin) {
        textPane.setText(plugin.toHTML());
    }

    public void reset() {
        textPane.setText(StringUtils.EMPTY);
    }

    @Override
    protected String title4PopupWindow() {
        return "Detail";
    }
}