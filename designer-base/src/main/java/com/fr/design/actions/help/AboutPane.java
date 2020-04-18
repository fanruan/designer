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
import com.fr.general.CloudCenter;
import com.fr.general.GeneralContext;
import com.fr.general.GeneralUtils;
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
    private static final String BUILD_PREFIX = "   ";
    private static final String COMPANY_TELEPHONE = CloudCenter.getInstance().acquireUrlByKind("help.compNo");
    private static final String PRESIDENT_PHONE = CloudCenter.getInstance().acquireUrlByKind("help.PNo");

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
                        com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Copy_Build_NO"),
                        com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Copy_Build_NO_OK")
                });
        contentPane.add(buildCopyPane);

        BoxCenterAligmentPane boxCenterAlignmentPane = new BoxCenterAligmentPane(getCopyRight());
        contentPane.add(boxCenterAlignmentPane);

        boxCenterAlignmentPane = new BoxCenterAligmentPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_About_All_Rights_Reserved"));
        contentPane.add(boxCenterAlignmentPane);

        contentPane.add(new BoxCenterAlignmentCopyablePane(
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Activation_Key"),
                DesignerEnvManager.getEnvManager().getActivationKey(),
                new String[]{
                        com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Copy_Activation_Key"),
                        com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Activation_Key_Copy_OK")
                }));

        addPhoneAndQQPane(contentPane);

        // 官网
        JPanel urlActionPane = getURLActionPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Official_Website"), CloudCenter.getInstance().acquireConf("website." + GeneralContext.getLocale(), ProductConstants.WEBSITE_URL));

        // 支持邮箱
        String defaultEmail = CloudCenter.getInstance().acquireConf("support.email", ProductConstants.SUPPORT_EMAIL);
        JPanel emailPane = getEmailActionPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Support_Email"), CloudCenter.getInstance().acquireConf("support.email." + GeneralContext.getLocale(), defaultEmail));

        contentPane.add(urlActionPane);
        contentPane.add(emailPane);

        if (GeneralContext.getLocale().equals(Locale.CHINA) || GeneralContext.getLocale().equals(Locale.TAIWAN)) {
            contentPane.add(getRemarkPane());
        }

        if (shouldShowThanks()) {
            addThankPane(contentPane);
        }
    }

    private void addPhoneAndQQPane(JPanel contentPane) {
        BoxCenterAligmentPane boxCenterAlignmentPane;
        // 英文版不显示服务电话和QQ
        if (GeneralContext.getLocale().equals(Locale.US)) {
            return;
        }
        boxCenterAlignmentPane = new BoxCenterAligmentPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Service_Phone") + CloudCenter.getInstance().acquireConf("service.phone." + FRContext.getLocale(), COMPANY_TELEPHONE));
        contentPane.add(boxCenterAlignmentPane);
        // 繁体版不显示QQ
        if (GeneralContext.getLocale().equals(Locale.TAIWAN)) {
            return;
        }
        boxCenterAlignmentPane = new BoxCenterAligmentPane("QQ: " + CloudCenter.getInstance().acquireUrlByKind("help.qq"));
        contentPane.add(boxCenterAlignmentPane);
    }

    // 是否显示鸣谢面板
    private boolean shouldShowThanks() {
        Locale[] hideLocales = {Locale.US, Locale.KOREA, Locale.JAPAN};
        for (Locale loc : hideLocales) {
            if (GeneralContext.getLocale().equals(loc)) {
                return false;
            }
        }
        return true;
    }

    private JPanel getRemarkPane() {
        String remark = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_About_Remark_Info", PRESIDENT_PHONE);
        UILabel label = new UILabel();
        label.setSize(new Dimension(580, 30));

        //用THML标签进行拼接，以实现自动换行
        StringBuilder builder = new StringBuilder("<html>");
        char[] chars = remark.toCharArray();
        //获取字体计算大小
        FontMetrics fontMetrics = label.getFontMetrics(label.getFont());
        int start = 0;
        int len = 0;
        while (start + len < remark.length()) {
            while (true) {
                len++;
                if (start + len > remark.length())
                    break;
                if (fontMetrics.charsWidth(chars, start, len)
                        > label.getWidth()) {
                    break;
                }
            }
            builder.append(chars, start, len - 1).append("<br/>");
            start = start + len - 1;
            len = 0;
        }
        //拼接剩余部分
        builder.append(chars, start, remark.length() - start);
        builder.append("</html>");

        JPanel jPanel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        label.setText(builder.toString());
        jPanel.add(label);

        return jPanel;
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
        return append(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_About_CopyRight"), COPYRIGHT_LABEL,
                ProductConstants.HISTORY, StringUtils.BLANK, CloudCenter.getInstance().acquireConf("company.name", ProductConstants.COMPANY_NAME));
    }

    private String getBuildTitle() {
        return append(ProductConstants.APP_NAME, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_About_Version"),
                StringUtils.BLANK, ProductConstants.RELEASE_VERSION, BUILD_PREFIX);
    }

    private JPanel getEmailActionPane(final String desc, final String mailTo) {
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

        JPanel panel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        panel.add(new UILabel(desc));
        panel.add(emailLabel);
        return panel;
    }

    private JPanel getURLActionPane(final String desc, final String url) {
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

        JPanel panel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        panel.add(new UILabel(desc));
        panel.add(actionLabel);

        return panel;
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
