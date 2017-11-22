/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.help;

import com.fr.base.FRContext;
import com.fr.design.DesignerEnvManager;
import com.fr.design.gui.ilable.ActionLabel;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.ComparatorUtils;
import com.fr.general.GeneralUtils;
import com.fr.general.Inter;
import com.fr.general.SiteCenter;
import com.fr.stable.ProductConstants;
import com.fr.stable.StringUtils;
import com.fr.stable.bridge.StableFactory;
import com.fr.start.BBSGuestPaneProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.Locale;

public class AboutPane extends JPanel {
    private static final String FINEREPORT = "FineReport";
    private static final int DEFAULT_GAP = 12;
    private static final String COPYRIGHT_LABEL = "\u00A9 ";
    private static final String BUILD_PREFIX = "  Build #";

    public AboutPane() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        //center panel
        JPanel centerPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
        this.add(centerPane, BorderLayout.CENTER);

        JPanel contentPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane();
        centerPane.add(contentPane, BorderLayout.NORTH);

        BoxCenterAlignmentCopyablePane buildCopyPane = new BoxCenterAlignmentCopyablePane(
                getBuildTitle(),
                GeneralUtils.readFullBuildNO(),
                new String[]{
                        Inter.getLocText("FR-Designer-Basic_Copy_Build_NO"),
                        Inter.getLocText("FR-Designer-Basic_Copy_Build_NO_OK")
                });
        contentPane.add(buildCopyPane);

        BoxCenterAligmentPane boxCenterAlignmentPane = new BoxCenterAligmentPane(getCopyRight());
        contentPane.add(boxCenterAlignmentPane);

        boxCenterAlignmentPane = new BoxCenterAligmentPane(Inter.getLocText("About-All_Rights_Reserved"));
        contentPane.add(boxCenterAlignmentPane);

        contentPane.add(new BoxCenterAlignmentCopyablePane(
                Inter.getLocText("FR-Designer-Basic_Activation_Key"),
                DesignerEnvManager.getEnvManager().getActivationKey(),
                new String[]{
                        Inter.getLocText("FR-Designer-Basic_Copy_Activation_Key"),
                        Inter.getLocText("FR-Designer-Basic_Activation_Key_Copy_OK")
                }));

        if (shouldShowPhoneAndQQ()) {
            if (ComparatorUtils.equals(ProductConstants.APP_NAME, FINEREPORT)) {
                boxCenterAlignmentPane = new BoxCenterAligmentPane(Inter.getLocText("FR-Designer_Service_Phone") + ProductConstants.COMPARE_TELEPHONE);
                contentPane.add(boxCenterAlignmentPane);
            }
            boxCenterAlignmentPane = new BoxCenterAligmentPane("QQ:" + SiteCenter.getInstance().acquireUrlByKind("help.qq"));
            contentPane.add(boxCenterAlignmentPane);
        }

        BoxCenterAligmentPane actionLabel = getURLActionLabel(SiteCenter.getInstance().acquireUrlByKind("website." + FRContext.getLocale(), ProductConstants.WEBSITE_URL));
        BoxCenterAligmentPane emailLabel = getEmailActionLabel(SiteCenter.getInstance().acquireUrlByKind("support.email", ProductConstants.SUPPORT_EMAIL));

        contentPane.add(actionLabel);
        contentPane.add(emailLabel);
        if (shouldShowThanks()) {
            addThankPane(contentPane);
        }
    }

    // 是否显示服务电话和 qq
    private boolean shouldShowPhoneAndQQ() {
        return !FRContext.getLocale().equals(Locale.US);
    }

    // 是否显示鸣谢面板
    private boolean shouldShowThanks() {
        Locale[] hideLocales = {Locale.US, Locale.KOREA, Locale.JAPAN};
        for (Locale loc : hideLocales) {
            if (FRContext.getLocale().equals(loc)) {
                return false;
            }
        }
        return true;
    }

    //添加鸣谢面板
    private void addThankPane(JPanel contentPane) {
        BBSGuestPaneProvider pane = StableFactory.getMarkedInstanceObjectFromClass(BBSGuestPaneProvider.XML_TAG, BBSGuestPaneProvider.class);
        if (pane == null) {
            return;
        }

        contentPane.add(Box.createVerticalStrut(DEFAULT_GAP));
        contentPane.add((Component) pane);
    }

    private String append(String... strs) {
        StringBuilder sb = new StringBuilder();
        for (String str : strs) {
            sb.append(str);
        }

        return sb.toString();
    }

    private String getCopyRight() {
        return append(Inter.getLocText("FR-Designer_About_CopyRight"), COPYRIGHT_LABEL,
                ProductConstants.HISTORY, StringUtils.BLANK, SiteCenter.getInstance().acquireUrlByKind("company.name", ProductConstants.COMPANY_NAME));
    }

    private String getBuildTitle() {
        return append(ProductConstants.APP_NAME, Inter.getLocText("FR-Designer_About_Version"),
                StringUtils.BLANK, ProductConstants.RELEASE_VERSION, BUILD_PREFIX);
    }

    private BoxCenterAligmentPane getEmailActionLabel(final String mailTo) {
        ActionLabel emailLabel = new ActionLabel(mailTo);

        emailLabel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().mail(new URI(mailTo));
                } catch (Exception ignore) {

                }
            }
        });

        return new BoxCenterAligmentPane(emailLabel);
    }

    private BoxCenterAligmentPane getURLActionLabel(final String url) {
        ActionLabel actionLabel = new ActionLabel(url);
        actionLabel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (Exception exp) {

                }
            }
        });

        return new BoxCenterAligmentPane(actionLabel);
    }

    class UserLabel extends BoldFontTextLabel {
        public UserLabel(String text) {
            super(text);

            if (text != null && text.length() > 30) {
                ToolTipManager manager = ToolTipManager.sharedInstance();
                manager.registerComponent(this);
            }
        }

        public String getToolTipText(MouseEvent e) {
            return this.getText();
        }

        public Point getToolTipLocation(MouseEvent e) {
            return new Point(0, 0);
        }
    }

    class BoxCenterAligmentPane extends JPanel {

        private UILabel textLabel;

        public BoxCenterAligmentPane(String text) {
            this(new UILabel(text));
        }

        public BoxCenterAligmentPane(UILabel label) {
            this.setLayout(FRGUIPaneFactory.createBorderLayout());

            JPanel centerPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            this.add(centerPane, BorderLayout.CENTER);

            this.textLabel = label;
            centerPane.add(textLabel);
        }

        public void setFont(Font font) {
            super.setFont(font);

            if (textLabel != null) {
                textLabel.setFont(font);
            }
        }
    }

    class BoxCenterAlignmentCopyablePane extends JPanel {

        private UILabel label;
        private UILabel textField;
        private UILabel lastLabel;

        public BoxCenterAlignmentCopyablePane(String title, String copyText, final String[] descriptions) {
            setLayout(FRGUIPaneFactory.createBorderLayout());
            JPanel centerPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            this.label = new UILabel(title);
            this.textField = new UILabel(copyText);
            textField.setBackground(null);
            textField.setBorder(null);

            centerPane.add(label);
            centerPane.add(textField);
            textField.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        StringSelection selection = new StringSelection(textField.getText());
                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                        clipboard.setContents(selection, selection);
                        lastLabel.setText(descriptions[1]);
                        lastLabel.setForeground(Color.RED);
                    }
                }
            });

            this.lastLabel = new UILabel(descriptions[0]);
            lastLabel.setForeground(Color.lightGray);
            centerPane.add(lastLabel);

            add(centerPane, BorderLayout.CENTER);
        }

        public void setFont(Font font) {
            super.setFont(font);

            if (textField != null) {
                textField.setFont(font);
            }
            if (label != null) {
                label.setFont(font);
            }
        }
    }
}