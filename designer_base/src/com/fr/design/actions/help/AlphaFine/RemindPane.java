package com.fr.design.actions.help.AlphaFine;

import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.general.IOUtils;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by XiaXiang on 2017/5/26.
 */
public class RemindPane extends JPanel {
    public static final Font MEDIUM_FONT = new Font("Song_TypeFace", 0, 12);
    public static final Font LARGE_FONT = new Font("Song_TypeFace", 0, 18);
    private UIButton openButton;
    private JPanel backgroundPane;
    private UILabel backgroundLabel;
    private UILabel checkLabel;
    private Icon checkIcon = IOUtils.readIcon("/com/fr/design/mainframe/alphafine/images/check.png");
    private Icon unCheckIcon = IOUtils.readIcon("/com/fr/design/mainframe/alphafine/images/uncheck.png");
    private Icon closeIcon = IOUtils.readIcon("/com/fr/design/mainframe/alphafine/images/remind_close.png");
    private Icon labelIcon = IOUtils.readIcon("/com/fr/design/mainframe/alphafine/images/remind.png");
    private Icon openIcon = IOUtils.readIcon("com/fr/design/mainframe/alphafine/images/open.png");
    public JComponent navigateButton = new JComponent() {
        protected void paintComponent(Graphics g) {
            closeIcon.paintIcon(this, g, 0, 0);
        }
    };

    public RemindPane(AlphafineConfigManager manager) {
        this.setPreferredSize(new Dimension(600, 400));
        initUI(manager);
        this.setLayout(getAbsoluteLayout());
    }

    private void initUI(final AlphafineConfigManager manager) {
        openButton = new UIButton() {
            @Override
            public void paintComponent(Graphics g) {
                g.setColor( Color.white );
                g.fillRect(0, 0, getSize().width, getSize().height);
                super.paintComponent(g);
            }
        };
        openButton.setContentAreaFilled(false);
        openButton.setForeground(new Color(0x3394F0));
        openButton.setIcon(openIcon);
        openButton.setFont(LARGE_FONT);
        openButton.set4ToolbarButton();
        openButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {


            }
        });
        backgroundLabel = new UILabel(Inter.getLocText("FR-Designer-Alphafine_No_Remind"));
        backgroundLabel.setFont(MEDIUM_FONT);
        backgroundLabel.setForeground(Color.white);
        checkLabel = new UILabel();
        checkLabel.setIcon(unCheckIcon);
        checkLabel.addMouseListener(new MouseAdapter() {
            private boolean isCheck = false;

            @Override
            public void mouseClicked(MouseEvent e) {
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
        add(navigateButton, 0);
        add(checkLabel, 1);
        add(openButton, 2);
        add(backgroundLabel, 3);
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
                navigateButton.setBounds((width - 30), 0, 30, 30);
                openButton.setBounds(30, 300, 150, 40);
                backgroundLabel.setBounds(95, 350, 100, 20);
                checkLabel.setBounds(70, 350, 20, 20);
                backgroundPane.setBounds(0, 0, width, height);
            }

            @Override
            public void addLayoutComponent(String name, Component comp) {
            }
        };
    }

}
