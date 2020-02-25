package com.fr.design.actions.help.alphafine;

import com.fr.design.dialog.UIDialog;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.general.IOUtils;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by XiaXiang on 2017/5/26.
 */
public class RemindPane extends JPanel {

    private UIButton openButton;
    private JPanel backgroundPane;
    private UILabel noRemindLabel;
    private UILabel checkLabel;
    private Icon checkIcon = IOUtils.readIcon("/com/fr/design/mainframe/alphafine/images/check.png");
    private Icon unCheckIcon = IOUtils.readIcon("/com/fr/design/mainframe/alphafine/images/uncheck.png");
    private Icon closeIcon = IOUtils.readIcon("/com/fr/design/mainframe/alphafine/images/remind_close.png");
    private Icon labelIcon = IOUtils.readIcon("/com/fr/design/mainframe/alphafine/images/remind.png");
    private Icon openIcon = IOUtils.readIcon("com/fr/design/mainframe/alphafine/images/open.png");

    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static final int CLOSE = 30;

    private static final Rectangle OPEN = new Rectangle(30, 300, 150, 40);
    private static final Rectangle REMIND = new Rectangle(95, 350, 100, 20);
    private static final Rectangle CHECK = new Rectangle(70, 350, 20, 20);
    public static final Font MEDIUM_FONT = new Font("Song_TypeFace", 0, 12);
    public static final Font LARGE_FONT = new Font("Song_TypeFace", 0, 18);

    public JComponent closeButton = new JComponent() {
        protected void paintComponent(Graphics g) {
            closeIcon.paintIcon(this, g, 0, 0);
        }
    };

    public RemindPane(AlphaFineConfigManager manager, UIDialog remindDialog) {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        initUI(manager, remindDialog);
        this.setLayout(getAbsoluteLayout());
    }

    /**
     * 初始化面板
     * @param manager
     * @param dialog
     */
    private void initUI(final AlphaFineConfigManager manager, final UIDialog dialog) {

        openButton = new UIButton();
        openButton.setIcon(openIcon);
        openButton.set4ToolbarButton();
        openButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                manager.setOperateCount(0);
                dialog.dispose();
                AlphaFineContext.fireAlphaFineShowDialog();

            }
        });
        noRemindLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Alphafine_No_Remind"));
        noRemindLabel.setFont(MEDIUM_FONT);
        noRemindLabel.setForeground(Color.WHITE);
        checkLabel = new UILabel();
        checkLabel.setIcon(unCheckIcon);
        checkLabel.addMouseListener(new MouseAdapter() {
            private boolean isCheck = false;

            @Override
            public void mousePressed(MouseEvent e) {
                if (isCheck) {
                    checkLabel.setIcon(unCheckIcon);
                    manager.setNeedRemind(true);
                    isCheck = false;
                } else {
                    checkLabel.setIcon(checkIcon);
                    manager.setNeedRemind(false);
                    isCheck = true;
                }
            }
        });
        backgroundPane = new JPanel(new BorderLayout());
        backgroundPane.add(new UILabel(labelIcon), BorderLayout.CENTER);
        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                manager.setOperateCount(0);
                dialog.dispose();

            }
        });
        add(closeButton, 0);
        add(checkLabel, 1);
        add(openButton, 2);
        add(noRemindLabel, 3);
        add(backgroundPane, 4);
    }

    /**
     * 控件排列用绝对布局
     * @return
     */
    protected LayoutManager getAbsoluteLayout() {
        return new LayoutManager() {

            @Override
            public void removeLayoutComponent(Component comp) {
                // Do nothing
            }

            @Override
            public Dimension preferredLayoutSize(Container parent) {
                return parent.getPreferredSize();
            }

            @Override
            public Dimension minimumLayoutSize(Container parent) {
                return null;
            }

            @Override
            public void layoutContainer(Container parent) {
                int width = parent.getWidth();
                int height = parent.getHeight();
                closeButton.setBounds((width - CLOSE), 0, CLOSE, CLOSE);
                openButton.setBounds(OPEN);
                noRemindLabel.setBounds(REMIND);
                checkLabel.setBounds(CHECK);
                backgroundPane.setBounds(0, 0, width, height);
            }

            @Override
            public void addLayoutComponent(String name, Component comp) {
                // Do nothing
            }
        };
    }

}
